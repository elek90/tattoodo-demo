package dk.eleknet.tattoodemo.app.details

import dagger.Provides
import dagger.Subcomponent
import dk.eleknet.tattoodemo.di.scopes.ActivityScope
import dk.eleknet.tattoodemo.network.TattoodoApi
import dk.eleknet.tattoodemo.providers.ISchedulerProvider

@ActivityScope
@Subcomponent(modules = [DetailsComponent.Module::class])
interface DetailsComponent{

    fun inject(fragment: DetailsFragment)

    @dagger.Module
    class Module {
        @Provides
        fun provideViewModel(
            tattoodoApi: TattoodoApi,
            schedulerProvider: ISchedulerProvider
        ) = DetailsViewModel(DetailsState(), tattoodoApi, schedulerProvider)
    }
}