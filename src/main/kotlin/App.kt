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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val fontFamily = FontFamily(Font(resource = "poppins.ttf"))

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {

    val coroutineScope = rememberCoroutineScope()

    val contentList = remember { mutableStateListOf<String>("Loading...") }

    var pagerState: PagerState = rememberPagerState(pageCount = { contentList.size })

    var currentPage = 0;

    coroutineScope.launch {
        while (true) {

            if (currentPage == 0) {
                coroutineScope.launch(Dispatchers.Default) {
                    val response = firebaseDatabaseAPI.getNotifications()

                    if (response.isSuccessful && response.body() != null) {
                        contentList.clear()
                        contentList.addAll(response.body()!!)
                    }
                }
            }

            pagerState.animateScrollToPage(currentPage)
            delay(Constants.SCROLL_DELAY)
            currentPage = (currentPage + 1) % contentList.size;
        }
    }.start()

    pagerState = rememberPagerState(pageCount = { contentList.size })

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
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize(0.3f)
                            .padding(8.dp)
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
                                text = contentList.get(page),
                                fontFamily = fontFamily,
                                fontSize = 44.sp,
                                fontWeight = FontWeight.W200,
                                lineHeight = 64.sp,
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