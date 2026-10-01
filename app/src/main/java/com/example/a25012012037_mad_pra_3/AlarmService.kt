package com.example.a25012012037_mad_pra_3


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.Toast

import androidx.core.app.NotificationCompat

class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    private val CHANNEL_ID = "alarm_channel"
    private val NOTIFICATION_ID = 1001

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        val action = intent?.getStringExtra("Service1")

        when (action) {

            "Start" -> {
                startAsForeground()
                startAlarmSound()
                return START_STICKY
            }

            "Stop" -> {
                stopAlarmSound()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()

                return START_NOT_STICKY
            }

            else -> {
                stopAlarmSound()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()

                return START_NOT_STICKY
            }
        }
    }

    private fun startAsForeground() {

        createNotificationChannelIfNeeded()

        val stopIntent = Intent(this, AlarmBroadcastReceiver::class.java).apply {
            putExtra("Service1", "Stop")
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            234324244,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm Ringing")
                .setContentText("Your scheduled alarm is ringing!")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .addAction(
                    android.R.drawable.ic_menu_close_clear_cancel,
                    "STOP ALARM",
                    stopPendingIntent
                )
                .build()

        startForeground(
            NOTIFICATION_ID,
            notification
        )
    }

    private fun createNotificationChannelIfNeeded() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val nm =
                getSystemService(NotificationManager::class.java)

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarms",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.setSound(
                null,
                null
            )

            nm.createNotificationChannel(channel)
        }
    }

    private fun startAlarmSound() {
        if (mediaPlayer?.isPlaying == true) return

        try {
            val assetFileDescriptor = resources.openRawResourceFd(R.raw.alarm_sound)
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                assetFileDescriptor.close()
                isLooping = true
                prepare()
                start()
            }
            Toast.makeText(this, "Playing Alarm Sound...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error playing sound: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun stopAlarmSound() {

        try {

            mediaPlayer?.let {

                if (it.isPlaying) {
                    it.stop()
                }

                it.reset()
                it.release()
            }

        } catch (e: Exception) {

            e.printStackTrace()

        } finally {

            mediaPlayer = null
        }

        Toast.makeText(
            this,
            "Alarm Cancelled!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {

        stopAlarmSound()

        super.onDestroy()
    }
}