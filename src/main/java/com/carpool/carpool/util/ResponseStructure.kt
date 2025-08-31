package com.carpool.carpool.util

class ResponseStructure<T> {
    var statusCode: Int = 0
    var message: String? = null
    var data: T? = null
    override fun toString(): String {
        return "ResponseStructure(statusCode=$statusCode, message=$message, data=$data)"
    }

}
