import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val fontFamily = FontFamily(Font(resource = "poppins.ttf"))

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {

    val coroutineScope = rememberCoroutineScope()
    val settings = Settings()

    var contentList = remember { mutableStateListOf<Announcement>(Announcement("Loading...")) }

    var pagerState: PagerState = rememberPagerState(pageCount = { contentList.size })

    var currentPage = 0;

    coroutineScope.launch {
        while (true) {
            if (currentPage == 0) {
                coroutineScope.launch(Dispatchers.Default) {

                    try {
                        val response = firebaseDatabaseAPI.getAnnouncements()

                        if (response.isSuccessful && response.body() != null) {
                            contentList.clear()
                            contentList.addAll(response.body()!!)

                            settings.putString(
                                Constants.ANNOUNCEMENT_LIST_KEY,
                                Gson().toJson(
                                    contentList
                                )
                            )
                        }

                    } catch (_: Exception) {

                        System.err.println("Network fetch failed")

                        if (settings.hasKey(Constants.ANNOUNCEMENT_LIST_KEY)) {
                            contentList.clear()
                            contentList.addAll(
                                Gson().fromJson(
                                    settings.getString(Constants.ANNOUNCEMENT_LIST_KEY, ""),
                                    object : TypeToken<ArrayList<Announcement>>() {}.type
                                )
                            )
                        } else {
                            contentList.clear()
                            contentList.add(
                                Announcement(
                                    title = "No connection",
                                    message = "Please connect to the internet."
                                )
                            )
                        }
                    }
                }
            }

            pagerState.animateScrollToPage(currentPage)
            delay(Constants.SCROLL_DELAY)
            currentPage = (currentPage + 1) % contentList.size;
        }
    }.start()

    key(contentList) {
        pagerState = rememberPagerState(pageCount = { contentList.size })
    }

    MaterialTheme {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .background(Constants.COLOR_BG),
                state = pagerState
            ) { page ->

                val announcement = contentList.get(page)

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize(1.0f)
                            .padding(0.dp)
                            .aspectRatio(1f),
                        backgroundColor = Constants.COLOR_CARD,
                        elevation = 36.dp,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                text = announcement.title,
                                fontFamily = fontFamily,
                                fontSize = 60.sp,
                                fontWeight = FontWeight.W200,
                                lineHeight = 64.sp,
                                textAlign = TextAlign.Center,
                                color = Constants.COLOR_TEXT
                            )

                            if (announcement.message != null)
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    text = announcement.message,
                                    fontFamily = fontFamily,
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.W200,
                                    lineHeight = 48.sp,
                                    textAlign = TextAlign.Center,
                                    color = Constants.COLOR_TEXT
                                )
                        }
                    }
                }
            }
        }
    }
}