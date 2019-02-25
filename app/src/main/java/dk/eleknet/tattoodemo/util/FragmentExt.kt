package dk.eleknet.tattoodemo.util

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showMessage(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
    this.view?.let { view ->
        Snackbar.make(view, message, duration).show()
    }
}