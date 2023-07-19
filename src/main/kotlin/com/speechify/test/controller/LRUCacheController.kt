package com.speechify.test.controller

import com.speechify.test.exceptions.RedisException
import com.speechify.test.model.Client
import com.speechify.test.service.RedisServices
import com.speechify.test.util.X_AUTHENTICATED_USER_HEADER
import com.speechify.test.util.X_B3_TRACE_ID_HEADER_NAME
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/speechify/redis")
class LRUCacheController(private val redisServices: RedisServices) {

    @PostMapping("/save/cache")
    fun saveRedis(@RequestHeader(name = X_AUTHENTICATED_USER_HEADER, required = true) xAuthenticatedUser: String,
                  @RequestHeader(name = X_B3_TRACE_ID_HEADER_NAME, required = true) xApplicationId: String,
                  @RequestBody client: Client
    ){
        if(xAuthenticatedUser==null || xApplicationId==null){
            throw RedisException.ErrorRedis("server","499","Error when trying to search the data")
        }
        redisServices.saveRedisClient(client)
    }

    @GetMapping("/client/{key}")
    fun getClient(@PathVariable key: String):Any?{
        return redisServices.getRedisClient(key)
    }
    @GetMapping("/parse")
    fun getClient():Any?{
        return redisServices.readSSMLFromFile()?.let { redisServices.castParseSSML(it) }
    }

}