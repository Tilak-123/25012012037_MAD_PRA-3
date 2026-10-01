package com.example.a25012012037_mad_pra_3

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var cardListAlarm: MaterialCardView
    lateinit var btnCreateAlarm: MaterialButton
    lateinit var btnCancleAlarm: MaterialButton
    lateinit var textAlaramTime: TextView
    lateinit var textAlarmTimeSelected: TextView

    private val REQUEST_NOTIFICATION_PERMISSION = 2001
    private val REQUEST_TIME_PICKER = 3001
    private val PENDING_INTENT_REQUEST_CODE = 234324243

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        cardListAlarm = findViewById(R.id.card2)
        btnCreateAlarm = findViewById(R.id.create_alarm)
        btnCancleAlarm = findViewById(R.id.cancle_alarm)
        textAlaramTime = findViewById(R.id.datetime)
        textAlarmTimeSelected = findViewById(R.id.datetime1)

        cardListAlarm.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }

        btnCreateAlarm.setOnClickListener {
            showTimeDialog()
        }

        btnCancleAlarm.setOnClickListener {
            cancelAlarm()
            cardListAlarm.visibility = View.GONE
        }
    }

    private fun showTimeDialog() {

        val cldr: Calendar = Calendar.getInstance()

        val hour = cldr.get(Calendar.HOUR_OF_DAY)
        val minutes = cldr.get(Calendar.MINUTE)

        val picker = TimePickerDialog(
            this,
            { _, sHour, sMinute ->
                sendDialogDateToActivity(sHour, sMinute)
            },
            hour,
            minutes,
            false
        )

        picker.show()
    }

    private fun sendDialogDateToActivity(hour: Int, minute: Int) {

        val alarmCalendar: Calendar = Calendar.getInstance()

        val year: Int = alarmCalendar.get(Calendar.YEAR)
        val month: Int = alarmCalendar.get(Calendar.MONTH)
        val day: Int = alarmCalendar.get(Calendar.DATE)

        alarmCalendar.set(Calendar.YEAR, year)
        alarmCalendar.set(Calendar.MONTH, month)
        alarmCalendar.set(Calendar.DAY_OF_MONTH, day)
        alarmCalendar.set(Calendar.HOUR_OF_DAY, hour)
        alarmCalendar.set(Calendar.MINUTE, minute)
        alarmCalendar.set(Calendar.SECOND, 0)
        alarmCalendar.set(Calendar.MILLISECOND, 0)

        if (alarmCalendar.timeInMillis <= System.currentTimeMillis()) {
            alarmCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val sdf = SimpleDateFormat("hh:mm:ss a MMM, dd yyyy", Locale.getDefault())
        textAlarmTimeSelected.text = sdf.format(alarmCalendar.time)

        cardListAlarm.visibility = View.VISIBLE

        setAlarm(
            alarmCalendar.timeInMillis,
            "Start"
        )
    }

    private fun setAlarm(millisTime: Long, str: String) {

        val intent = Intent(
            this,
            AlarmBroadcastReceiver::class.java
        )

        intent.putExtra("Service1", str)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            PENDING_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            getSystemService(ALARM_SERVICE) as AlarmManager

        if (str == "Start") {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                if (!alarmManager.canScheduleExactAlarms()) {

                    Toast.makeText(
                        this,
                        "Please allow scheduling exact alarms in settings.",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    )

                    return
                }
            }

            // Use setAlarmClock for guaranteed exact delivery on all Android versions
            val showIntent = Intent(this, MainActivity::class.java)
            val showPendingIntent = PendingIntent.getActivity(
                this,
                0,
                showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val alarmClockInfo = AlarmManager.AlarmClockInfo(millisTime, showPendingIntent)
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)

            val timeFormatted = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(millisTime)
            Toast.makeText(
                this,
                "Alarm set for $timeFormatted",
                Toast.LENGTH_LONG
            ).show()

        } else if (str == "Stop") {

            alarmManager.cancel(pendingIntent)

            val stopIntent = Intent(
                this,
                AlarmBroadcastReceiver::class.java
            )

            stopIntent.putExtra(
                "Service1",
                "Stop"
            )

            sendBroadcast(stopIntent)

            Toast.makeText(
                this,
                "Alarm Stopped",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun cancelAlarm() {
        setAlarm(
            System.currentTimeMillis(),
            "Stop"
        )
    }
}