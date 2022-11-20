package ie.wit.myapplication.main

import android.app.Application
import ie.wit.myapplication.models.FreecycleStore
import ie.wit.myapplication.models.FreecycleManager



class MainApp : Application() {
    lateinit var listingsStore: FreecycleStore

    override fun onCreate() {
        super.onCreate()

        //listingsStore = FreecycleManager

    }
}