import java.net.InetAddress
import java.net.UnknownHostException

class Utils {
    companion object {

        @JvmStatic
        // https://stackoverflow.com/a/75562156/9652621
        fun isInternetAvailable(): Boolean {
            return try {
                val address = InetAddress.getByName("www.google.com")
                address != null && !address.equals("")
            } catch (e: UnknownHostException) {
                false
            }
        }
    }
}