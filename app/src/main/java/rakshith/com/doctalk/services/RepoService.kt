package rakshith.com.doctalk.services

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import rakshith.com.doctalk.services.models.GitResponse
import rakshith.com.doctalk.util.Constants
import rakshith.com.doctalk.util.NetworkVolleyRequest
import rakshith.com.doctalk.view.ui.activity.MainActivity

/**
 * Created DocTalk by rakshith on 6/4/18.
 */

open class RepoService {
    private var gson: Gson? = null

    companion object {
        var repoService: RepoService? = null

        @Synchronized
        fun getInstance(): RepoService {
            //TODO No need to implement this singleton in Part #2 since Dagger will handle it ...
            if (repoService == null) {
                if (repoService == null) {
                    repoService = RepoService()
                }
            }
            return repoService as RepoService
        }
    }

    open fun getSearchResult(searchTerm: String): LiveData<GitResponse> {
        var searchData: MutableLiveData<GitResponse> = MutableLiveData()

        var gsonBuilder: GsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("M/d/yy hh:mm a")
        gson = gsonBuilder?.create()

        var headerMap = HashMap<String, String>()
        var paramsMap = HashMap<String, String>()
        paramsMap.put(Constants.SEARCH_PARAM_KEY, searchTerm)
        paramsMap.put(Constants.SEARCH_SORT_KEY, Constants.SEARCH_FOLLOWERS)

        NetworkVolleyRequest(NetworkVolleyRequest.RequestMethod.GET,
                Constants.gitHubSearchUserUrl,
                String::class.java,
                headerMap,
                paramsMap,
                object : NetworkVolleyRequest.Callback<Any> {
                    override fun onSuccess(response: Any) {
                        var gitResponse: GitResponse? = gson?.fromJson(response.toString(), GitResponse::class.java)

                        searchData.value = gitResponse
                    }

                    override fun onError(errorCode: Int, errorMessage: String) {
                        Log.d(MainActivity.TAG, "error code is $errorCode and error message is $errorMessage")
                    }
                }, NetworkVolleyRequest.ContentType.JSON
        ).execute()

        return searchData
    }

}