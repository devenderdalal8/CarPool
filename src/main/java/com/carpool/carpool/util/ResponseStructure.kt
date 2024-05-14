package com.carpool.carpool.util

class ResponseStructure<T> {
    var statusCode: Int = 0
    var message: String? = null
    var data: T? = null
}
