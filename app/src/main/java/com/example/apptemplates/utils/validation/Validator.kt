package com.example.apptemplates.utils.validation

import com.example.apptemplates.data.firebase.database.result.Result

fun interface Validator {
    fun validate(value: String): Result<*>
}