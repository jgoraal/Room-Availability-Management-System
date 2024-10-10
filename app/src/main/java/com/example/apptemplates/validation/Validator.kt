package com.example.apptemplates.validation

import com.example.apptemplates.result.Result

fun interface Validator {
    fun validate(value: String): Result<*>
}