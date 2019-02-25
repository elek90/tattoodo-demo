package dk.eleknet.tattoodemo

import android.app.Application
import dk.eleknet.tattoodemo.di.components.AppComponent
import dk.eleknet.tattoodemo.di.components.DaggerAppComponent
import dk.eleknet.tattoodemo.di.modules.AppModule

class TattoodoApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // AppEnv.init(this)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        appComponent.inject(this)
    }

}