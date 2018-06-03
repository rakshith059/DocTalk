package rakshith.com.doctalk.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import rakshith.com.doctalk.R
import rakshith.com.doctalk.adapters.SearchAdapter
import rakshith.com.doctalk.models.GitResponse
import rakshith.com.doctalk.util.Constants
import rakshith.com.doctalk.util.NetworkVolleyRequest
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        var TAG = MainActivity::class.java.simpleName
    }

    private var gson: Gson? = null
    var searchAdapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var gsonBuilder: GsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("M/d/yy hh:mm a")
        gson = gsonBuilder?.create()

        var searchTerm = activity_main_tet_enter_name?.text.toString()

        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(e: ObservableEmitter<String>) {
                activity_main_tet_enter_name?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        Unit
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        Unit
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        e.onNext(s.toString())
                    }
                })
            }
        }).debounce(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ searchTerm ->
                    getSearchResult(searchTerm)
                    activity_main_ll_progress_bar?.visibility = View.VISIBLE
                })
    }

    private fun getSearchResult(searchTerm: String) {
        var headerMap = HashMap<String, String>()
        var paramsMap = HashMap<String, String>()
        paramsMap.put(Constants.SEARCH_PARAM_KEY, searchTerm)

        NetworkVolleyRequest(NetworkVolleyRequest.RequestMethod.GET,
                Constants.gitHubSearchUserUrl,
                String::class.java,
                headerMap,
                paramsMap,
                object : NetworkVolleyRequest.Callback<Any> {
                    override fun onSuccess(response: Any) {
                        activity_main_ll_progress_bar?.visibility = View.GONE

                        var gitResponse: GitResponse? = gson?.fromJson(response.toString(), GitResponse::class.java)
                        Log.d("Rakshith", "response is " + gitResponse?.items?.size)

                        if (gitResponse?.items?.size as Int > 0) {
                            activity_main_rv_search_list?.visibility = View.VISIBLE
                            activity_main_tv_no_data_found?.visibility = View.GONE
                            searchAdapter = SearchAdapter(gitResponse)
                            activity_main_rv_search_list?.adapter = searchAdapter
                        } else {
                            activity_main_rv_search_list?.visibility = View.GONE
                            activity_main_tv_no_data_found?.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(errorCode: Int, errorMessage: String) {
                        Log.d(TAG, "error code is $errorCode and error message is $errorMessage")
                        activity_main_ll_progress_bar?.visibility = View.GONE
                    }
                }, NetworkVolleyRequest.ContentType.JSON
        ).execute()
    }
}
