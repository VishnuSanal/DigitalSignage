import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val settings = Settings()

    private val _announcements = MutableStateFlow<List<Announcement>>(
        listOf(
            Announcement(
                title = "Loading..."
            )
        )
    )
    val announcements: StateFlow<List<Announcement>> = _announcements

    init {
        logger.info("init")
        fetch()
    }

    fun fetch() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = firebaseDatabaseAPI.getAnnouncements()

                if (response.isSuccessful) {
                    if (response.body() != null) {

                        _announcements.value = response.body()!!

                        logger.info("Announcements fetched: " + _announcements.value.toList())

                        settings.putString(
                            Constants.ANNOUNCEMENT_LIST_KEY,
                            Gson().toJson(_announcements.value.toList())
                        )
                    } else {
                        _announcements.value = listOf(
                            Announcement(
                                title = "No Announcements"
                            )
                        )

                        logger.info("Announcements empty")

                        settings.putString(
                            Constants.ANNOUNCEMENT_LIST_KEY,
                            Gson().toJson(_announcements.value.toList())
                        )
                    }
                }

            } catch (e: Exception) {

                logger.error("Network fetch failed", e)

                if (settings.hasKey(Constants.ANNOUNCEMENT_LIST_KEY)) {
                    _announcements.value =
                        Gson().fromJson(
                            settings.getString(Constants.ANNOUNCEMENT_LIST_KEY, ""),
                            object : TypeToken<ArrayList<Announcement>>() {}.type
                        )

                    logger.info("Using local cache: " + _announcements.value.toList())
                } else {
                    logger.info("No connection, no cache")

                    _announcements.value = listOf(
                        Announcement(
                            title = "No connection",
                            message = "Please connect to the internet."
                        )
                    )
                }
            }
        }
    }
}
