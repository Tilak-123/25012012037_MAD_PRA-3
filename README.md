# Android Alarm Application (MAD Practical)

An Android application built with Kotlin that allows users to create, schedule, and cancel alarms using `AlarmManager`, `BroadcastReceiver`, and a Foreground `Service`.

---

## 📱 Features

- **Digital Clock**: Real-time 12-hour digital clock display.
- **Time Picker**: Interactive time selection dialog (`TimePickerDialog`).
- **Exact Alarm Scheduling**: Uses `AlarmManager.setAlarmClock()` to guarantee exact alarm triggering even in Doze Mode or Deep Sleep.
- **Background Sound Playback**: Uses a Foreground `Service` with `MediaPlayer` to play custom alarm audio in the background.
- **Notification Action**: Displays a high-priority heads-up notification with a **STOP ALARM** action button to easily dismiss the alarm.
- **Alarm Cancellation**: In-app option to stop active alarms and cancel pending intents.
- **Material Design UI**: Formatted using Material 3 cards, smooth scrolling with `NestedScrollView`, and responsive constraints.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 / 35 (Android 14+)
- **UI Architecture**: XML ConstraintLayout, MaterialCardView, MaterialButton, NestedScrollView
- **Android System Components**:
  - `AlarmManager`
  - `BroadcastReceiver` (`AlarmBroadcastReceiver`)
  - `Foreground Service` (`AlarmService`)
  - `MediaPlayer`
  - `NotificationManager`

---

## 📂 Project Structure

```text
app/src/main/
├── java/com/example/a25012012037_mad_pra_3/
│   ├── MainActivity.kt              # Main UI & Alarm Manager Logic
│   ├── AlarmBroadcastReceiver.kt    # Receiver for Alarm Triggers
│   └── AlarmService.kt              # Foreground Service for Playing Audio
├── res/
│   ├── layout/activity_main.xml     # Responsive XML UI Layout
│   ├── raw/alarm_sound.mp3          # Custom Alarm Audio Resource
│   └── values/                      # Themes, Colors & Strings
└── AndroidManifest.xml              # Manifest Declarations & Permissions
```

---

## 🔒 Permissions Used

- `SCHEDULE_EXACT_ALARM` & `USE_EXACT_ALARM`: For exact alarm scheduling.
- `POST_NOTIFICATIONS`: For displaying heads-up alarm notifications (Android 13+).
- `FOREGROUND_SERVICE` & `FOREGROUND_SERVICE_MEDIA_PLAYBACK`: To run the audio playback service seamlessly in the background.

---

## 👤 Student Details

- **Name**: Tilak Pandya
- **Enrollment**: 25012012037
- **Class**: CE-H
