# Alarm App Implementation Plan

Improve the alarm creation flow by fixing UI IDs, correcting typos, and implementing the state transition between "Create Alarm" and "Alarm Active" states.

## Proposed Changes

### UI Layer

#### [MODIFY] [activity_main.xml](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRA3/app/src/main/res/layout/activity_main.xml)
- Correct typos in IDs and text ("alram" -> "alarm", "cancle" -> "cancel").
- Change `datetime1` from `TextClock` to `TextView` to display the selected alarm time.
- Update IDs for better clarity.

### Logic Layer

#### [MODIFY] [MainActivity.kt](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRA3/app/src/main/java/com/example/a25012012037_mad_pra_3/MainActivity.kt)
- Update view binding to use corrected IDs.
- Fix the logic to show `card2` and hide `card1` (or keep both as per user's current flow but fix where the time is displayed). Based on the prompt "after selecting time second image pop-ups", I will ensure `card2` appears with the correct time.
- Update the date format to match the user's visual requirement.

### Clean up

#### [MODIFY] [AlaramBroadcastReceiver.kt](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRA3/app/src/main/java/com/example/a25012012037_mad_pra_3/AlaramBroadcastReceiver.kt)
- Rename file and class if necessary for consistency (though Kotlin allows mismatch, it's better to fix). *Actually, I'll stick to minimum necessary changes to "make perfect code changes" without over-engineering.* I'll fix the class name if it doesn't match the file, but here it's `AlarmBroadcastReceiver` in `AlaramBroadcastReceiver.kt`. I'll rename the file.

## Verification Plan

### Manual Verification
- Deploy the app.
- Click "Create Alarm".
- Verify `TimePickerDialog` appears.
- Select a time.
- Verify the second card appears with the selected time and a "Cancel Alarm" button.
- Click "Cancel Alarm" and verify the second card disappears.
