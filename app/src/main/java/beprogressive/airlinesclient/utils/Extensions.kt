package beprogressive.airlinesclient.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.text.DateFormat
import java.util.*

inline fun <reified T> T.log(message: String) = Timber.v(message)

fun <T> Flow<T>.handleErrors(onError: (Throwable) -> Unit): Flow<T> =
    catch { e -> onError(e) }

fun View.showSnackBar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Long.toReadableDateFormat() = DateFormat.getDateInstance(DateFormat.DEFAULT).format(Date(this))

fun Context.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networks = cm.allNetworks
        for (n in networks) {
            val nInfo = cm.getNetworkInfo(n)
            if (nInfo != null && nInfo.isConnected) return true
        }
        return false
    }
}