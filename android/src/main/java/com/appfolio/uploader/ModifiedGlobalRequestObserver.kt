package com.appfolio.uploader

import android.app.Application
import net.gotev.uploadservice.UploadServiceConfig
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.extensions.registerReceiverCompat
import net.gotev.uploadservice.observer.request.BaseRequestObserver
import net.gotev.uploadservice.observer.request.RequestObserverDelegate

class ModifiedGlobalRequestObserver  @JvmOverloads constructor(
        private val application: Application,
        delegate: RequestObserverDelegate,
        shouldAcceptEventsFrom: (uploadInfo: UploadInfo) -> Boolean = { true }
) : BaseRequestObserver(application, delegate, shouldAcceptEventsFrom) {
    init {
        register()
    }

    override fun register() {
        application.registerReceiverCompat(this, UploadServiceConfig.broadcastStatusIntentFilter)
    }
}
