package dk.eleknet.tattoodemo.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import dk.eleknet.tattoodemo.di.scopes.ApplicationScope

/**
 * Created by Alex on 7/4/18
 */
@Module(includes = [])
class AppModule(private val application: Application) {
    @Provides
    @ApplicationScope
    fun provideApplication(): Application = application
}
