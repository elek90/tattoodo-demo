package dk.eleknet.tattoodemo.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel<T : BaseState>(protected val state: T) {
    val stateSubject: BehaviorSubject<T> = BehaviorSubject.create()
    protected val disposeBag = CompositeDisposable()
}