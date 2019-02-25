package dk.eleknet.tattoodemo.providers

import io.reactivex.Scheduler

interface ISchedulerProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun mainThread(): Scheduler
    fun single(): Scheduler
}