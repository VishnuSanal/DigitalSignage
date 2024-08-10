import androidx.compose.ui.graphics.Color
import okhttp3.logging.HttpLoggingInterceptor

object Constants {

    val COLOR_BG = Color.Black
    val COLOR_CARD = Color.DarkGray
    val COLOR_TEXT = Color.White

    val LOGLEVEL: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

    const val DB_BASE_URL = "https://digital-signage-gec-pkd-default-rtdb.firebaseio.com/"

    const val SCROLL_DELAY: Long = 1_000
}