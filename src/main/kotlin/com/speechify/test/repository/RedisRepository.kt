package com.speechify.test.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisRepository(private val redisTemplate: RedisTemplate<String, Any>) {
    private val valueOperations: ValueOperations<String, Any> = redisTemplate.opsForValue()

    fun save(key: String, entity: Any) {
        evictLeastRecentlyUsed()
        valueOperations.set(key, entity,30000L, TimeUnit.SECONDS)
    }
    fun getAll(key: String): Any? {
        return redisTemplate.keys("*")
    }

    fun remove(key: String) {
        redisTemplate.delete(key)
    }
    fun evictLeastRecentlyUsed() {
        val allKeys = redisTemplate.keys("*")

        // Obtener el tiempo de expiración actual de cada clave
        val expirationTimes = allKeys.map { key ->
            redisTemplate.getExpire(key)
        }

        // Encontrar el índice del tiempo de expiración más pequeño (es decir, el elemento menos recientemente utilizado)
        val minExpirationIndex = expirationTimes.indexOf(expirationTimes.minOrNull())

        // Obtener la clave correspondiente al elemento menos recientemente utilizado
        // Convertir el Set<String> a un array de String
        val keysToEvict = allKeys.toTypedArray()

        // Eliminar la clave del elemento menos recientemente utilizado de la caché
        redisTemplate.delete(keysToEvict.get(minExpirationIndex))
    }
}