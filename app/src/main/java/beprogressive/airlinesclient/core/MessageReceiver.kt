package beprogressive.airlinesclient.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MessageReceiver(val callback: MessageInterface) : BroadcastReceiver() {

    companion object {
        const val EVENT_ERROR = "EVENT_ERROR"
        const val EXTRA_ERROR_MESSAGE = "errorMessage"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getStringExtra(EXTRA_ERROR_MESSAGE)?.let {
            callback.onErrorReceived(it)
        }
    }
}

interface MessageInterface {
    fun onErrorReceived(message: String)
}