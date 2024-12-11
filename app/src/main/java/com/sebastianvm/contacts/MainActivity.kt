package com.sebastianvm.contacts

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sebastianvm.contacts.data.ContactsRepository
import com.sebastianvm.contacts.features.birthdaylist.BirthdayListUi
import com.sebastianvm.contacts.model.ContactWithBirthday
import com.sebastianvm.contacts.ui.theme.ContactsTheme
import com.sebastianvm.contacts.ui.util.permissions.rememberPermissionHandler
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    createNotificationChannel()
    enableEdgeToEdge()
    setContent {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionHandler(Manifest.permission.POST_NOTIFICATIONS) {
          setBirthdayNotifications()
        }
      }

      ContactsTheme { BirthdayListUi(modifier = Modifier.fillMaxSize()) }
    }
  }

  private fun createNotificationChannel() {
    val name = "Default"
    val descriptionText = "Description"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel =
        NotificationChannel("your_channel_id", name, importance).apply {
          description = descriptionText
        }
    // Register the channel with the system.
    val notificationManager: NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
  }

  private fun setBirthdayNotifications() {
    val constraints =
        androidx.work.Constraints.Builder()
            .setRequiresCharging(false) // Allow running even if not charging
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED) // Allow running without network
            .build()

    val birthdayReminderRequest =
        PeriodicWorkRequestBuilder<BirthdayReminderWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

    WorkManager.getInstance(this)
        .enqueueUniquePeriodicWork(
            "birthdayReminder", ExistingPeriodicWorkPolicy.KEEP, birthdayReminderRequest)
  }
}

class BirthdayReminderWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

  private val contactsRepository = ContactsRepository(context)

  override fun doWork(): Result {
    // Get birthdays from contacts
    val birthdays = contactsRepository.getBirthdays()
      val today = LocalDate.now()

    // Send notifications for today's birthdays
      birthdays.filter {
          it.birthday.withYear(today.year).minusDays(1) == today
      }.forEach { birthday ->
          sendBirthdayNotification(birthday)
      }

    return Result.success()
  }

  private fun sendBirthdayNotification(contact: ContactWithBirthday) {

    val notificationId = contact.hashCode()
    val builder =
        NotificationCompat.Builder(context, "your_channel_id")
            .setContentTitle("Today is ${contact.name}'s birthday!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MAX)

    with(NotificationManagerCompat.from(context)) {
      if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
          PackageManager.PERMISSION_GRANTED) {
        return
      }
      notify(notificationId, builder.build())
    }
  }
}
