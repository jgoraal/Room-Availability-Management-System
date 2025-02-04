package com.example.apptemplates.utils.validation

import com.example.apptemplates.data.firebase.database.result.Result

class UIStateValidator : Validator {
    override fun validate(value: String): Result<String> {
        return Result.Success
    }
}