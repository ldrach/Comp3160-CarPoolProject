package com.example.carpoolapp;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class Alarm extends JobService {

//    public void setAlarm(Context context) {
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, Alarm.class);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
//    }
//
//    public void cancelAlarm(Context context) {
//        Intent intent = new Intent(context, Alarm.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }

    @Override
    public boolean onStartJob(JobParameters jobParameters ) {
        String userID = jobParameters.getExtras().getString("userID");
        UpdateDriveCount updateDriveCount = new UpdateDriveCount();
        updateDriveCount.updateDriveCount(this, userID);


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
