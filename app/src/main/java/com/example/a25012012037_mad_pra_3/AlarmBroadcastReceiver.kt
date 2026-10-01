package com.example.a25012012037_mad_pra_3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import androidx.core.content.ContextCompat

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val str1 = intent.getStringExtra("Service1")

        if (str1 == "Start" || str1 == "Stop") {

            val intentServices = Intent(
                context,
                AlarmService::class.java
            )

            intentServices.putExtra(
                "Service1",
                str1
            )

            if (str1 == "Start") {

                ContextCompat.startForegroundService(
                    context,
                    intentServices
                )

            } else {

                context.stopService(intentServices)
            }
        }
    }
}