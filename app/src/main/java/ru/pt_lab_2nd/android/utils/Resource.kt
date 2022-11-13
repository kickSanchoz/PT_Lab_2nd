package ru.pt_lab_2nd.android.utils

import ru.pt_lab_2nd.android.base.BaseDaoDataSource.ErrorBody

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val errorBody: ErrorBody?,
    val actionMessage: String? = null
) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR,
        NETWORK_ERROR;
    }

    /**
     * Флаг наличия [Status.ERROR] или [Status.NETWORK_ERROR]
     * */
    val hasError: Boolean
        get() = status == Status.ERROR || status == Status.NETWORK_ERROR

    fun <R> map(block: (T?) -> R?): Resource<R> {
        return Resource(status, block(data), errorBody, actionMessage)
    }

    companion object {
        fun <T> success(
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return Resource(Status.SUCCESS, data, null, actionMessage)
        }

        fun <T> error(
            errorBody: ErrorBody,
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return Resource(Status.ERROR, data, errorBody, actionMessage)
        }

        fun <T> error(
            message: String,
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return error(ErrorBody(message), data, actionMessage)
        }

        fun <T> loading(
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return Resource(Status.LOADING, data, null, actionMessage)
        }

        fun <T> networkError(
            errorBody: ErrorBody,
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return Resource(Status.NETWORK_ERROR, null, errorBody, actionMessage)
        }

    }
}