package rakshith.com.doctalk.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import rakshith.com.doctalk.services.RepoService
import rakshith.com.doctalk.services.models.GitResponse

/**
 * Created DocTalk by rakshith on 6/4/18.
 */


public class RepoViewModel(application: Application) : AndroidViewModel(application) {
    private var projectListObservable: LiveData<GitResponse>? = null

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    fun getProjectListObservable(): LiveData<GitResponse>? {
        return projectListObservable
    }

    fun setSearchTerm(searchTerm: String?) {
        projectListObservable = RepoService?.getInstance().getSearchResult(searchTerm as String)
    }
}