import retrofit2.Response
import retrofit2.http.GET

interface FirebaseDatabaseAPI {
    @GET("/announcements.json/")
    suspend fun getAnnouncements(): Response<List<Announcement>>
}