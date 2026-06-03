package com.jadx.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import java.io.File

class DecompilationService : Service() {

	private val serviceJob = Job()
	private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

	companion object {
		private const val NOTIFICATION_ID = 1
		private const val CHANNEL_ID = "jadx_decompilation"
	}

	override fun onCreate() {
		super.onCreate()
		createNotificationChannel()
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		intent?.data?.let { fileUri ->
			val file = File(cacheDir, "temp.apk") // Convert URI to file
			serviceScope.launch {
				try {
					updateNotification("Preparing decompilation...")
					// TODO: Implement actual decompilation logic
					decompileFile(file)
					updateNotification("Decompilation complete")
				} catch (e: Exception) {
					updateNotification("Decompilation failed: ${e.message}")
				}
			}
		}
		return START_STICKY
	}

	private suspend fun decompileFile(file: File) {
		// TODO: Integrate JADX decompilation logic here
		withContext(Dispatchers.Main) {
			// Update UI with results
		}
	}

	private fun updateNotification(message: String) {
		val notification = NotificationCompat.Builder(this, CHANNEL_ID)
			.setContentTitle("JADX Decompilation")
			.setContentText(message)
			.setSmallIcon(android.R.drawable.ic_dialog_info)
			.setProgress(100, 0, true)
			.build()

		startForeground(NOTIFICATION_ID, notification)
	}

	private fun createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				CHANNEL_ID,
				"JADX Decompilation",
				NotificationManager.IMPORTANCE_LOW
			)
			val manager = getSystemService(NotificationManager::class.java)
			manager?.createNotificationChannel(channel)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		serviceJob.cancel()
	}

	override fun onBind(intent: Intent?): IBinder? = null
}
