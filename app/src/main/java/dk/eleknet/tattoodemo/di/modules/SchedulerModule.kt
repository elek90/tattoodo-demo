package dk.eleknet.tattoodemo.di.modules

import dagger.Module
import dagger.Provides
import dk.eleknet.tattoodemo.providers.ISchedulerProvider
import dk.eleknet.tattoodemo.providers.SchedulerProvider

@Module(includes = [])
class SchedulerModule {

    @Provides
    fun provideScheduler(): ISchedulerProvider {
        return SchedulerProvider()
    }
}