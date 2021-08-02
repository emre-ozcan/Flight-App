package com.emreozcan.flightapp.util

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Settings.Global.getString
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.flightapp.BuildConfig
import com.emreozcan.flightapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import iamutkarshtiwari.github.io.ananas.editimage.ImageEditorIntentBuilder
import java.io.File


@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannelForO(notificationManager: NotificationManager,channelId: String) {
    val channelName = "Flight App"
    val channel = NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
        enableVibration(true)
        enableLights(true)
        description = "Flight Application"
    }

    notificationManager.createNotificationChannel(channel)
}


fun <T : RecyclerView.ViewHolder?> RecyclerView.setupRecyclerView(
    adapter: RecyclerView.Adapter<T>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
        this.context
    )
) {
    this.adapter = adapter
    this.layoutManager = layoutManager
}

fun Fragment.findNavControllerSafely(): NavController? {
    return if (isAdded) {
        findNavController()
    } else {
        null
    }
}

fun createPermissionDeniedAlertDialog(context: Context){
    MaterialAlertDialogBuilder(context).setTitle(context.getString(R.string.alert_call_title))
        .setMessage(context.getString(R.string.alert_call_permission_message))
        .setNegativeButton(context.getString(R.string.alert_decline)){ _, _ ->

        }.setPositiveButton(context.getString(R.string.alert_okay)){ _, _ ->
            context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)))

        }.show()
}

fun ImageEditorIntentBuilder.Companion.createEditorIntent(activity: Activity, imageUri: Uri?): Intent{
    val outputFile = generateEditFile()

    val intent = ImageEditorIntentBuilder(
        activity,
        getRealPathFromURI(imageUri!!,activity),
        outputFile?.absolutePath
    )
        .withAddText()
        .withPaintFeature()
        .withFilterFeature()
        .withRotateFeature()
        .withCropFeature()
        .withBrightnessFeature()
        .withSaturationFeature()
        .forcePortrait(true)
        .setSupportActionBarVisibility(false)
        .build()

    return intent
}




private fun generateEditFile(): File? {
    return getEmptyFile(
        "flight_app_edited"
                + System.currentTimeMillis() + ".jpg"
    )
}

private fun getEmptyFile(name: String?): File? {
    val folder: File? = createFolders()
    if (folder != null) {
        if (folder.exists()) {
            return File(folder, name)
        }
    }
    return null
}
private fun createFolders(): File? {
    val baseDir: File = Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        ?: return Environment.getExternalStorageDirectory()
    val cacheFolder = File(baseDir, "Flight_App")
    if (cacheFolder.exists()) return cacheFolder
    if (cacheFolder.isFile) cacheFolder.delete()
    return if (cacheFolder.mkdirs()) cacheFolder else Environment.getExternalStorageDirectory()
}

private fun getRealPathFromURI(uri: Uri,activity: Activity): String? {
    val cursor: Cursor =
        activity.getContentResolver().query(uri, null, null, null, null)!!
    cursor.moveToFirst()
    val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
    return cursor.getString(idx)
}






