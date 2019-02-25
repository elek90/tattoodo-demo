package dk.eleknet.tattoodemo.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import dk.eleknet.tattoodemo.di.scopes.ApplicationScope
import dk.eleknet.tattoodemo.providers.FileProvider
import dk.eleknet.tattoodemo.providers.IFileProvider

@Module(includes = [])
class FileModule {

    @ApplicationScope
    @Provides
    fun provideFileProvider(application: Application): IFileProvider {
        return FileProvider(application)
    }
}