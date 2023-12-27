package com.exercise.summarysix.Resource

sealed class Resources<T> (
    val loading:Boolean = false,
    val data: T? = null,
    val message:String? = null
){

    class SUCCESS<T>(data: T): Resources<T>(data = data)

    class ERROR<T>(data: T? = null, message: String): Resources<T>(message = message)

    class LOADING<T>(loading: Boolean) : Resources<T>(loading = loading)
}