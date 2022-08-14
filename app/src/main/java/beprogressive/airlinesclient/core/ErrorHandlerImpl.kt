package beprogressive.airlinesclient.core

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gmail.beprogressive.it.airlinesclient.R
import beprogressive.airlinesclient.core.MessageReceiver.Companion.EVENT_ERROR
import beprogressive.airlinesclient.core.MessageReceiver.Companion.EXTRA_ERROR_MESSAGE
import beprogressive.airlinesclient.utils.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


class ErrorHandlerImpl @Inject constructor(@ApplicationContext private val context: Context):
    ErrorHandler {

    override fun onException(throwable: Throwable): Boolean {
        throwable.printStackTrace()
        when (throwable) {
            is UnknownHostException -> {
                if (!context.isNetworkAvailable())
                    onNoConnection()
                else
                    onNetworkError()
                return true
            }
            is CancellationException -> {}
            else -> onError(throwable.message ?: throwable.stackTraceToString())
        }
        return false
    }

    private fun onError(message: String) {
        sendBroadcast(message)
    }

    private fun onNetworkError() {
        sendBroadcast(context.getString(R.string.server_error))
    }

    private fun onNoConnection() {
        sendBroadcast(context.getString(R.string.no_internet_connection))
    }

    private fun sendBroadcast(message: String) {
        if (message.isEmpty()) return

        val it = Intent(EVENT_ERROR)
        it.putExtra(EXTRA_ERROR_MESSAGE, message)

        LocalBroadcastManager.getInstance(context).sendBroadcast(it)
    }
}