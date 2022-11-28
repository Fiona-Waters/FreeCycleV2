package ie.wit.myapplication.main

import android.app.Application
import ie.wit.myapplication.models.FreecycleStore
import timber.log.Timber


class MainApp : Application() {
  //  lateinit var listingsStore: FreecycleStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //listingsStore = FreecycleManager()
        Timber.i("Freecycle Application Started")

    }
}