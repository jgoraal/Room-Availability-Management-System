package com.example.apptemplates.form.validation

import com.example.apptemplates.result.Result

class UIStateValidator : Validator {
    override fun validate(value: String): Result<String> {
        return Result.Success
    }
}