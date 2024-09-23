package com.appfolio.extensions

import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Build.VERSION.SDK_INT

import com.appfolio.work.TaskCompletionNotifier
import com.appfolio.work.UploadManager
import net.gotev.uploadservice.UploadTask
import net.gotev.uploadservice.data.UploadNotificationConfig
import net.gotev.uploadservice.data.UploadTaskParameters
import net.gotev.uploadservice.logger.UploadServiceLogger
import net.gotev.uploadservice.observer.task.BroadcastEmitter

data class UploadTaskCreationParameters(
    val params: UploadTaskParameters,
    val notificationConfig: UploadNotificationConfig
)

/**
 * Creates a new task instance based on the requested task class in the intent.
 * @param creationParameters task creation params
 * @return task instance or null if the task class is not supported or invalid
 */
@Suppress("UNCHECKED_CAST")
fun Context.getUploadTask(
    creationParameters: UploadTaskCreationParameters,
    notificationId: Int
): UploadTask? {
    return try {
        val observers = arrayOf(
            BroadcastEmitter(this),
            TaskCompletionNotifier()
        )

        val taskClass = Class.forName(creationParameters.params.taskClass) as Class<out UploadTask>
        val uploadTask = taskClass.newInstance().apply {
            init(
                context = this@getUploadTask,
                taskParams = creationParameters.params,
                notificationConfig = creationParameters.notificationConfig,
                notificationId = notificationId,
                taskObservers = observers
            )
        }

        UploadServiceLogger.debug(
            component = UploadManager.TAG,
            uploadId = creationParameters.params.id,
            message = {
                "Successfully created new task with class: ${taskClass.name}"
            }
        )
        uploadTask
    } catch (exc: Throwable) {
        UploadServiceLogger.error(
            component = UploadManager.TAG,
            uploadId = "",
            exception = exc,
            message = {
                "Error while instantiating new task"
            }
        )
        null
    }
}

@SuppressLint("UnspecifiedRegisterReceiverFlag")
fun Context.registerReceiverCompat(receiver: BroadcastReceiver, filter: IntentFilter) {
    // Apps targeting SDK 34+: Runtime-registered broadcasts receivers must specify export behavior
    // @link: https://developer.android.com/about/versions/14/behavior-changes-14#runtime-receivers-exported
    if (SDK_INT >= 34) {
        registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
    } else {
        registerReceiver(receiver, filter)
    }
}
