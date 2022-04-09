package com.mispran.outlet_order.common.app

import android.app.Application
import android.app.NotificationManager
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import com.mispran.outlet_order.BuildConfig
import com.mispran.outlet_order.common.di.appModule
import com.mispran.outlet_order.common.utils.CustomDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import timber.log.Timber
import java.io.File


@KoinExperimentalAPI
@ExperimentalCoilApi
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        initializeLoggers()
        configureCoilImageLoader()
        initializeDependencyInjections()
        initializeNotificationChannels()



    }




    private fun initializeDependencyInjections() {
        startKoin {
            androidContext(this@App)
            androidLogger()
            fragmentFactory()
            modules(appModule)
        }
    }

    private fun configureCoilImageLoader() {
        val loader = ImageLoader.Builder(this).apply {
            diskCache {
                DiskCache.Builder(this@App)
                    .maxSizeBytes(100000000L)
                    .directory(File(filesDir, "coil-caches"))
                    .build()
            }
            respectCacheHeaders(false)
        }.build()
        Coil.setImageLoader(loader)
    }

    private fun initializeLoggers() {
        if (BuildConfig.DEBUG) {
            Timber.plant(CustomDebugTree())
        }
    }

    private fun initializeNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*
            * Currently Empty Will be added later
            * */
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf())
        }
    }
}