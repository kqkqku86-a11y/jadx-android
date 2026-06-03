package com.jadx.app

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.jadx.app.service.DecompilationService
import com.jadx.app.ui.theme.JadxAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			JadxAppTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					JadxApp()
				}
			}
		}
	}

	@Composable
	private fun JadxApp() {
		var selectedFile by remember { mutableStateOf<String?>(null) }
		var decompilationProgress by remember { mutableStateOf(0) }
		var isDecompiling by remember { mutableStateOf(false) }
		var decompilationError by remember { mutableStateOf<String?>(null) }
		var decompilationResult by remember { mutableStateOf<String?>(null) }

		val filePickerLauncher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.OpenDocument()
		) { uri: Uri? ->
			uri?.let {
				selectedFile = getFileNameFromUri(it)
				startDecompilation(it)
			}
		}

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
				.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			// Header
			Text(
				text = "JADX Decompiler",
				style = MaterialTheme.typography.headlineLarge
			)

			Text(
				text = "Android Dex to Java Decompiler",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)

			Divider()

			// File Selection
			Card(
				modifier = Modifier.fillMaxWidth()
			) {
				Column(
					modifier = Modifier.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					Text(
						text = "Select File",
						style = MaterialTheme.typography.titleMedium
					)

					Button(
						onClick = {
							filePickerLauncher.launch(
								arrayOf("application/vnd.android.package-archive", "application/x-dex")
							)
						},
						modifier = Modifier
							.fillMaxWidth()
							.height(48.dp),
						enabled = !isDecompiling
					) {
						Icon(
							imageVector = Icons.Default.OpenInNew,
							contentDescription = null,
							modifier = Modifier.size(20.dp)
						)
						Spacer(modifier = Modifier.width(8.dp))
						Text("Choose APK or DEX File")
					}

					if (selectedFile != null) {
						Text(
							text = "Selected: $selectedFile",
							style = MaterialTheme.typography.bodySmall,
							modifier = Modifier.fillMaxWidth()
						)
					}
				}
			}

			// Decompilation Progress
			if (isDecompiling) {
				Card(
					modifier = Modifier.fillMaxWidth()
				) {
					Column(
						modifier = Modifier
							.padding(16.dp)
							.fillMaxWidth(),
						verticalArrangement = Arrangement.spacedBy(12.dp)
					) {
						Text(
							text = "Decompiling...",
							style = MaterialTheme.typography.titleMedium
						)
						LinearProgressIndicator(
							progress = { decompilationProgress / 100f },
							modifier = Modifier.fillMaxWidth()
						)
						Text(
							text = "$decompilationProgress%",
							style = MaterialTheme.typography.bodySmall
						)
					}
				}
			}

			// Error Display
			if (decompilationError != null) {
				Card(
					modifier = Modifier
						.fillMaxWidth(),
					colors = CardDefaults.cardColors(
						containerColor = MaterialTheme.colorScheme.errorContainer
					)
				) {
					Column(
						modifier = Modifier.padding(16.dp),
						verticalArrangement = Arrangement.spacedBy(8.dp)
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.spacedBy(8.dp)
						) {
							Icon(
								imageVector = Icons.Default.Info,
								contentDescription = null,
								tint = MaterialTheme.colorScheme.error
							)
							Text(
								text = "Error",
								style = MaterialTheme.typography.titleMedium,
								color = MaterialTheme.colorScheme.error
							)
						}
						Text(
							text = decompilationError ?: "",
							style = MaterialTheme.typography.bodySmall,
							color = MaterialTheme.colorScheme.onErrorContainer
						)
						Button(
							onClick = { decompilationError = null },
							modifier = Modifier.align(Alignment.End)
						) {
							Text("Dismiss")
						}
					}
				}
			}

			// Result Display
			if (decompilationResult != null && !isDecompiling) {
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.weight(1f)
				) {
					Column(
						modifier = Modifier
							.padding(16.dp)
							.fillMaxSize(),
						verticalArrangement = Arrangement.spacedBy(8.dp)
					) {
						Text(
							text = "Decompiled Code",
							style = MaterialTheme.typography.titleMedium
						)
						Text(
							text = decompilationResult ?: "",
							style = MaterialTheme.typography.bodySmall.copy(
								fontFamily = FontFamily.Monospace
							),
							modifier = Modifier
								.fillMaxWidth()
								.weight(1f)
								.verticalScroll(rememberScrollState())
						)
					}
				}
			}

			// Info Footer
			Text(
				text = "Supported formats: APK, DEX, AAB, XAPK, APKM",
				style = MaterialTheme.typography.labelSmall,
				color = MaterialTheme.colorScheme.outline,
				modifier = Modifier.align(Alignment.CenterHorizontally)
			)
		}
	}

	private fun startDecompilation(fileUri: Uri) {
		lifecycleScope.launch {
			val intent = Intent(this@MainActivity, DecompilationService::class.java).apply {
				data = fileUri
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(intent)
			} else {
				startService(intent)
			}
		}
	}

	private fun getFileNameFromUri(uri: Uri): String {
		return uri.lastPathSegment ?: "Unknown File"
	}
}
