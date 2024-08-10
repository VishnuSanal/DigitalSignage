import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val firebaseDatabaseAPI: FirebaseDatabaseAPI =
    Retrofit.Builder().baseUrl(Constants.DB_BASE_URL).client(
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(Constants.LOGLEVEL)
        ).build()
    ).addConverterFactory(
        GsonConverterFactory.create(
            GsonBuilder().setLenient().serializeNulls().create()
        )
    ).build().create(FirebaseDatabaseAPI::class.java);

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
