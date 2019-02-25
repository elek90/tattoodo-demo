package dk.eleknet.tattoodemo.app.grid

import dagger.Provides
import dagger.Subcomponent
import dk.eleknet.tattoodemo.di.scopes.ActivityScope
import dk.eleknet.tattoodemo.network.TattoodoApi
import dk.eleknet.tattoodemo.providers.ISchedulerProvider

@ActivityScope
@Subcomponent(modules = [GridComponent.Module::class])
interface GridComponent{

    fun inject(fragment: GridFragment)

    @dagger.Module
    class Module {
        @Provides
        fun provideViewModel(
            tattoodoApi: TattoodoApi,
            schedulerProvider: ISchedulerProvider
        ) = GridViewModel(GridState(), tattoodoApi, schedulerProvider)
    }
}