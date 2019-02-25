package dk.eleknet.tattoodemo.base

import androidx.annotation.StringRes

abstract class BaseState(
    @StringRes var error: Int = -1
)