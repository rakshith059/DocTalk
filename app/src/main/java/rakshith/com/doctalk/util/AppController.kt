package rakshith.com.doctalk.util

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

/**
 * Created AppController by rakshith on 10/1/18.
 */

class AppController : Application() {

    private var mRequestQueue: RequestQueue? = null
    private var mImageLoader: ImageLoader? = null
    var cookieManager: CookieManager? = null
        private set

    val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                cookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)
                CookieHandler.setDefault(cookieManager)
                mRequestQueue = Volley.newRequestQueue(applicationContext)
            }

            return mRequestQueue as RequestQueue
        }

    val imageLoader: ImageLoader?
        get() {
            requestQueue
            if (mImageLoader == null) {
                mImageLoader = ImageLoader(this.mRequestQueue,
                        LruBitmapCache(this))
            }
            return this.mImageLoader
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        requestQueue.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        val TAG = AppController::class.java
                .simpleName

        @get:Synchronized
        var instance: AppController? = null
            private set
    }
}
