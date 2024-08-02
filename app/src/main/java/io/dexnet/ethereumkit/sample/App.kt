package io.dexnet.ethereumkit.sample

import android.app.Application
import com.facebook.stetho.Stetho
import io.dexnet.ethereumkit.core.EthereumKit
import io.reactivex.plugins.RxJavaPlugins
import java.util.logging.Logger

class App : Application() {

    private val logger = Logger.getLogger("App")

    override fun onCreate() {
        super.onCreate()
        io.dexnet.ethereumkit.sample.App.Companion.instance = this

        RxJavaPlugins.setErrorHandler { e: Throwable? ->
            logger.warning("RxJava ErrorHandler: ${e?.message}")
        }

        // Enable debug bridge
        Stetho.initializeWithDefaults(this)
        EthereumKit.init()
    }

    companion object {
        lateinit var instance: io.dexnet.ethereumkit.sample.App
            private set
    }

}
