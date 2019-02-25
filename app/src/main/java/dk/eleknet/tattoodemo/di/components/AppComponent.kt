package dk.eleknet.tattoodemo.di.components

import dagger.Component
import dk.eleknet.tattoodemo.TattoodoApp
import dk.eleknet.tattoodemo.app.details.DetailsComponent
import dk.eleknet.tattoodemo.app.grid.GridComponent
import dk.eleknet.tattoodemo.base.BaseActivity
import dk.eleknet.tattoodemo.di.modules.*
import dk.eleknet.tattoodemo.di.scopes.ApplicationScope

@ApplicationScope
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    SchedulerModule::class,
    FileModule::class
])
interface
AppComponent {
    fun inject(app: TattoodoApp)
    fun inject(activity: BaseActivity)

    fun with(module: GridComponent.Module): GridComponent
    fun with(module: DetailsComponent.Module): DetailsComponent
}