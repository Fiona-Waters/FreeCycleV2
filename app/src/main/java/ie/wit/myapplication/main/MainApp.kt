package ie.wit.myapplication.main

import android.app.Application
import ie.wit.myapplication.models.FreecycleStore
import timber.log.Timber


class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Freecycle Application Started")

    }
}