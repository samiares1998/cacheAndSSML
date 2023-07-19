package com.speechify.test.exceptions

sealed class RedisException(val httpStatus: String? = null, val code: String? =  null, override val message: String? = null) : RuntimeException() {
    class ErrorRedis(status: String, code:String, message: String) : RedisException(status, code, message)

}