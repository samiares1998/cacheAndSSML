package com.speechify.test.service

import com.speechify.test.model.Client
import com.speechify.test.model.SSMLText
import com.speechify.test.repository.RedisRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

@Service
class RedisServices(@Autowired private val redisRepository: RedisRepository) {

    fun saveRedisClient(client:Client){
        kotlin.runCatching {
            redisRepository.save(client.name,client)
        }.getOrThrow()

    }

    fun getRedisClient(key:String):Any?{
        return redisRepository.getAll(key)
    }
    fun readSSMLFromFile(): String? {
        val projectPath = System.getProperty("user.dir")
        val file = File("$projectPath/src/main/resources/static/test.XML")
        val stringBuilder = StringBuilder()

        try {
            val bufferedReader = BufferedReader(FileReader(file))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            bufferedReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return stringBuilder.toString()
    }

    fun castParseSSML(ssml: String):List<SSMLText>{
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val inputStream = ByteArrayInputStream(ssml.toByteArray(StandardCharsets.UTF_8))
        val document: Document = documentBuilder.parse(inputStream)

        val ssmlTextList = mutableListOf<SSMLText>()

        val textNodes = document.getElementsByTagName("speak").item(0).childNodes
        for (i in 0 until textNodes.length) {
            val node = textNodes.item(i)
            if (node.nodeName == "#text") {
                val text = node.textContent.trim()
                if (text.isNotEmpty()) {
                    ssmlTextList.add(SSMLText(text))
                }
            } else if (node.nodeName == "break") {
                val timeAttribute = node.attributes.getNamedItem("time")
                val time = timeAttribute?.nodeValue?.toDoubleOrNull() ?: 0.0
                // Assuming a break is indicated by a non-zero time attribute
                if (time > 0) {
                    ssmlTextList.add(SSMLText("", true))
                }
            }
        }

        return ssmlTextList
    }

}