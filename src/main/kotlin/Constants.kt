import androidx.compose.ui.graphics.Color
import okhttp3.logging.HttpLoggingInterceptor

object Constants {

    val COLOR_BG = Color(0xFF202020)
    val COLOR_CARD = Color(0xFF121214)
    val COLOR_TEXT = Color.White

    val LOGLEVEL: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC

    const val DB_BASE_URL = "https://digital-signage-gec-pkd-default-rtdb.firebaseio.com/"

    const val SCROLL_DELAY: Long = 2_500
}