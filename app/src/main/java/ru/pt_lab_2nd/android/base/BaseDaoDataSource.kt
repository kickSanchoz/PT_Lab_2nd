package ru.pt_lab_2nd.android.base

import android.database.sqlite.SQLiteException
import ru.pt_lab_2nd.android.utils.Resource

abstract class BaseDaoDataSource {

    data class ErrorBody(
        val message: String,
        val errors: Map<String, Array<String>>? = null,
        var httpCode: Int? = null
    )

    protected suspend fun <T> getResultAsync(call: suspend () -> T): Resource<T> {
        try {
            val response = call()
            return Resource.success(response)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            return Resource.error("$e")
        }
    }

    protected fun <T> getResult(call: () -> T): T {
        try {
            val response = call()
            return response
        } catch (e: SQLiteException) {
            e.printStackTrace()
            throw SQLiteException(e.message ?: e.toString())
        }
    }
}