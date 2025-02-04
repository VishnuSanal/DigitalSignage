import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val fontFamily = FontFamily(Font(resource = "poppins.ttf"))

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    val mainViewModel: MainViewModel = viewModel(
        key = "MainViewModel",
        modelClass = MainViewModel::class,
        factory = viewModelFactory { initializer { MainViewModel() } }
    )

    val coroutineScope = rememberCoroutineScope()

    val contentList by mainViewModel.announcements.collectAsState()

    var pagerState: PagerState = rememberPagerState(pageCount = { contentList.size })
    key(contentList) {
        pagerState = rememberPagerState(pageCount = { contentList.size })
    }

    var currentPage = 0;
    coroutineScope.launch {
        while (true) {
            pagerState.animateScrollToPage(currentPage)
            delay(Constants.SCROLL_DELAY)
            currentPage = (currentPage + 1) % contentList.size
        }
    }.start()

    LaunchedEffect(pagerState) {
        if (pagerState.currentPage == 0) {
            mainViewModel.fetch()
        }
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
                            .fillMaxSize(0.9f),
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
                                fontSize = 128.sp,
                                fontWeight = FontWeight.W200,
                                lineHeight = 144.sp,
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
                                    fontSize = 60.sp,
                                    fontWeight = FontWeight.W200,
                                    lineHeight = 72.sp,
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