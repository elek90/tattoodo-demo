package dk.eleknet.tattoodemo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.eleknet.tattoodemo.TattoodoApp
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val disposeBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as TattoodoApp)
            .appComponent
            .inject(this)
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposeBag.isDisposed) {
            disposeBag.dispose()
        }
    }

    open fun inject() {}
}