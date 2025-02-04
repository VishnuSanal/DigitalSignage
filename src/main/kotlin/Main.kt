import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val firebaseDatabaseAPI: FirebaseDatabaseAPI =
    Retrofit.Builder().baseUrl(Constants.DB_BASE_URL).client(
        OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(Constants.LOGLEVEL)
            )
            .build()
    ).addConverterFactory(
        GsonConverterFactory.create(
            GsonBuilder()
                .registerTypeAdapter(Announcement::class.java, AnnouncementAdapter())
                .setLenient()
                .serializeNulls()
                .create()
        )
    ).build().create(FirebaseDatabaseAPI::class.java);

val logger: Logger = LoggerFactory.getLogger("DigitalSignage")

fun main() = application {
    Window(
        title = "Digital Signage",
        state = rememberWindowState(WindowPlacement.Fullscreen),
        alwaysOnTop = true,
        resizable = false,
        undecorated = true,
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
