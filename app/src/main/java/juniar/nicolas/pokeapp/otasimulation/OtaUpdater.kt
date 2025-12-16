package juniar.nicolas.pokeapp.otasimulation

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File

class OtaUpdater(private val context: Context) {

    fun downloadApk(
        apkUrl: String,
        fileName: String
    ) {

        val request = DownloadManager.Request(apkUrl.toUri())
            .setTitle("Downloading update")
            .setDescription("Downloading new version")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
            .setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )

        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadId = downloadManager.enqueue(request)

        Thread {
            var downloading = true
            while (downloading) {

                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = downloadManager.query(query)

                if (cursor != null && cursor.moveToFirst()) {

                    val status = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            DownloadManager.COLUMN_STATUS
                        )
                    )

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                        Handler(Looper.getMainLooper()).post {
                            installApk(fileName)
                        }
                    }

                    if (status == DownloadManager.STATUS_FAILED) {
                        downloading = false
                    }
                }

                cursor?.close()
                Thread.sleep(300)
            }
        }.start()
    }

    private fun installApk(fileName: String) {

        if (!context.packageManager.canRequestPackageInstalls()) return

        val apkFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )

        if (!apkFile.exists()) return

        val apkUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            apkFile
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(
                apkUri,
                "application/vnd.android.package-archive"
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        context.startActivity(intent)
    }
}
