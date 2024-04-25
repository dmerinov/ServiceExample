package com.example.serviceexample.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import com.example.serviceexample.handlers.NotificationHandler


class NotificationService : Service() {

    private val binder = NotificationBinder();

    override fun onBind(intent: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val handler = NotificationHandler(this)
        handler.showSimpleNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    inner class NotificationBinder : Binder() {
        fun getService(): NotificationBinder = this@NotificationBinder
    }
}