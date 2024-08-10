import retrofit2.Response
import retrofit2.http.GET

interface FirebaseDatabaseAPI {
    @GET("/notifications.json/")
    suspend fun getNotifications(): Response<List<String>>
}