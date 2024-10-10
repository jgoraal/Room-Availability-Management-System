package com.example.apptemplates.validation

import com.example.apptemplates.result.Result

class UiStateValidator : Validator {
    override fun validate(value: String): Result<String> {
        return Result.Success
    }
}