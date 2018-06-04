package rakshith.com.doctalk.view.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import rakshith.com.doctalk.R
import rakshith.com.doctalk.view.adapters.SearchAdapter
import rakshith.com.doctalk.viewmodels.RepoViewModel
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
        }).debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ searchTerm ->

                    var repoViewModel = ViewModelProviders.of(this@MainActivity).get(RepoViewModel::class.java)
                    repoViewModel.setSearchTerm(searchTerm)
                    observeViewModel(repoViewModel)

                    activity_main_ll_progress_bar?.visibility = View.VISIBLE
                })
    }

    private fun observeViewModel(repoViewModel: RepoViewModel) {
        repoViewModel.getProjectListObservable()?.observe(this, Observer { gitResponse ->
            run {
                if (gitResponse?.items?.size as Int > 0) {
                    activity_main_rv_search_list?.visibility = View.VISIBLE
                    activity_main_tv_no_data_found?.visibility = View.GONE
                    activity_main_ll_progress_bar?.visibility = View.GONE
                    searchAdapter = SearchAdapter(gitResponse)
                    activity_main_rv_search_list?.adapter = searchAdapter
                } else {
                    activity_main_ll_progress_bar?.visibility = View.GONE
                    activity_main_rv_search_list?.visibility = View.GONE
                    activity_main_tv_no_data_found?.visibility = View.VISIBLE
                }
            }
        })
    }
}
