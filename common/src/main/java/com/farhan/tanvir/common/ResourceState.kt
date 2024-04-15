
package com.farhan.tanvir.common

sealed class ResourceState<out T>(val data: T? = null, val message: String? = null) {
    class Success<out T>(data: T?) : ResourceState<T>(data)

    class Error<out T>(message: String, data: T? = null) : ResourceState<T>(data, message)
}
