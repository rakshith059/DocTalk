package rakshith.com.doctalk.util

/**
 * Created DocTalk by rakshith on 6/3/18.
 */

class Constants {
    companion object {
        val githubBaseUrl = "https://api.github.com/"
        val gitHubSearchUrl = githubBaseUrl + "search/"
        val gitHubSearchUserUrl = gitHubSearchUrl + "users"
        val SEARCH_PARAM_KEY: String = "q"
    }
}