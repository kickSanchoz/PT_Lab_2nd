package ru.pt_lab_2nd.android.base

import android.database.sqlite.SQLiteException

abstract class BaseDaoDataSource {

    data class ErrorBody(
        val message: String,
        val errors: Map<String, Array<String>>? = null,
        var httpCode: Int? = null
    )

    protected suspend fun <T> getResultAsync(call: suspend () -> T): T {
        try {
            val response = call()
            return response
        } catch (e: SQLiteException) {
            e.printStackTrace()
            throw SQLiteException(e.message ?: e.toString())
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