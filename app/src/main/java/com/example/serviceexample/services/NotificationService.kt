package com.example.serviceexample.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.SystemClock
import com.example.serviceexample.handlers.NotificationHandler
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class NotificationService : Service() {

    private val binder = NotificationBinder();

    override fun onBind(intent: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val handler = NotificationHandler(this)
        handler.showSimpleNotification()
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, NotificationService::class.java).also {
            it.setPackage(packageName)
        };
        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_IMMUTABLE);
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent)
        alarmService.setRepeating(AlarmManager.ELAPSED_REALTIME,1000,1000,restartServicePendingIntent)
    }

    inner class NotificationBinder : Binder() {
        fun getService(): NotificationBinder = this@NotificationBinder
    }
}