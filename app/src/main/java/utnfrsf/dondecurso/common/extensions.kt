package utnfrsf.dondecurso.common

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun <T> Activity.findView(id: Int): T {
    return this.findViewById<View>(id) as T
}

fun <T> View.findView(id: Int): T {
    return this.findViewById<View>(id) as T
}

fun Activity.launchActivity(activity: Activity) {
    val i = Intent(this, activity::class.java)
    startActivity(i)
}

fun View.launchActivity(activity: Activity) {
    val i = Intent(this.context, activity::class.java)
    this.context.startActivity(i)
}

fun <T> Call<T>.enqueue(onResponse: (call: Call<T>?, response: Response<T>?) -> Any, onFailure: (call: Call<T>?, t: Throwable?) -> Any) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            onResponse(call, response)
        }

        override fun onFailure(call: Call<T>?, t: Throwable?) {
            onFailure(call, t)
        }
    })
}

fun async(callback: () -> Unit) {
    AsyncTask.execute { callback() }
}

fun onUI(callback: () -> Unit) {
    Handler(Looper.getMainLooper()).post { callback() }
}