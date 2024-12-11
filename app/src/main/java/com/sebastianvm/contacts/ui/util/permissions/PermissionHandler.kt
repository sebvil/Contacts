package com.sebastianvm.contacts.ui.util.permissions

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

data class PermissionHandler(
    val hasPermission: Boolean,
    val requestPermission: () -> Unit,
)

@Composable
fun rememberPermissionHandler(
    permission: String,
    onPermissionGranted: () -> Unit
): PermissionHandler {
  var hasPermission by remember { mutableStateOf(false) }

  val permissionLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        hasPermission = isGranted
      }
  val context = LocalContext.current

  LaunchedEffect(hasPermission) {
    if (hasPermission) {
      onPermissionGranted()
      return@LaunchedEffect
    }

    val permissionState = ContextCompat.checkSelfPermission(context, permission)

    if (permissionState == PackageManager.PERMISSION_GRANTED) {
      hasPermission = true
      return@LaunchedEffect
    }
    permissionLauncher.launch(permission)
  }

  return remember(hasPermission, permissionLauncher) {
    PermissionHandler(hasPermission) { permissionLauncher.launch(permission) }
  }
}
