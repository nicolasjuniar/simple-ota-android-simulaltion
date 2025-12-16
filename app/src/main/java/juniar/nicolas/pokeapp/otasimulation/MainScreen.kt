package juniar.nicolas.pokeapp.otasimulation

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@Composable
fun MainScreen() {

    val context = LocalContext.current

    val apkUrl =
        "https://github.com/nicolasjuniar/simple-ota-android-simulaltion/releases/download/v1.1/ota-simulation-v1.1.apk"
    val apkName = "ota-simulation-v1.1.apk"

    fun ensureInstallPermission(onGranted: () -> Unit) {
        if (context.packageManager.canRequestPackageInstalls()) {
            onGranted()
        } else {
            val intent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                "package:${context.packageName}".toUri()
            )
            context.startActivity(intent)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Good News! There's New Update",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    ensureInstallPermission {
                        OtaUpdater(context).downloadApk(
                            apkUrl = apkUrl,
                            fileName = apkName
                        )
                    }
                }
            ) {
                Text("Update App")
            }
        }

        Text(
            text = "v1.0",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

