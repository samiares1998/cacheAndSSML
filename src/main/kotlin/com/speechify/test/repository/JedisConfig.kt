package com.speechify.test.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.speechify.test.model.Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

@Configuration
class JedisConfig(private val objectMapper: ObjectMapper) {


    private var redisHost: String="localhost"

    private var redisPort: Int = 6379

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        val config = RedisStandaloneConfiguration()
        config.hostName = "localhost" // Cambiar a la dirección del servidor de Redis
        config.port = 6379 // Cambiar al puerto correcto de Redis

        return JedisConnectionFactory(config)
    }

    @Bean
    fun redisTemplate(jedisConnectionFactory: JedisConnectionFactory): RedisTemplate<String, Any> {
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Any::class.java)
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper)

        val redisTemplate = RedisTemplate<String, Any>().apply {
            setConnectionFactory(jedisConnectionFactory)
            keySerializer = StringRedisSerializer()
            valueSerializer = jackson2JsonRedisSerializer
            afterPropertiesSet()

        }

        return redisTemplate
    }

}