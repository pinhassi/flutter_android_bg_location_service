package com.example.flutter_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class BootCompletedBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = BootCompletedBroadcastReceiver::class.java.simpleName

    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i(TAG,"ddd")
        }
    }
}