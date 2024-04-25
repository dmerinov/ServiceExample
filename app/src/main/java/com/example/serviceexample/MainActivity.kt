package com.example.serviceexample

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.serviceexample.handlers.NotificationHandler
import com.example.serviceexample.services.NotificationService
import com.example.serviceexample.ui.theme.ServiceExampleTheme
import java.util.Calendar
import java.util.Date
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiceExampleTheme {
                val intent = Intent(this, NotificationService::class.java)
                val context = LocalContext.current
                MainScreen(context = context)
                val scheduler = Executors.newScheduledThreadPool(1)
                scheduler.scheduleAtFixedRate({
                    startService(intent)
                }, 2, 10, TimeUnit.SECONDS)

            }
        }
    }
}

@Composable
fun MainScreen(context: Context) {
    val notificationHandler = NotificationHandler(context)

    Column {
        Button(onClick = {
            notificationHandler.showSimpleNotification()
        }) { Text(text = "Simple notification") }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun SetAlarm(context: Context, intent: Intent) {
    val calendar = Calendar.getInstance()
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    val pendingIntent =
        PendingIntent.getBroadcast(context, Date().seconds, intent, PendingIntent.FLAG_IMMUTABLE)
    alarmManager?.setRepeating(
        AlarmManager.ELAPSED_REALTIME_WAKEUP,
        calendar.timeInMillis,
        6000,
        pendingIntent
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ServiceExampleTheme {
        Greeting("Android")
    }
}