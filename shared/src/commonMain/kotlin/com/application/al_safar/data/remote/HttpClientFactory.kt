package com.application.al_safar.data.remote

import com.application.al_safar.core.ApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    /**
     * @param engineFactory platform engine (OkHttp on Android). Passed from app DI
     * so this factory can stay in commonMain.
     */
    fun create(engineFactory: HttpClientEngineFactory<*>): HttpClient {
        return HttpClient(engineFactory) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = false
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.BODY
            }

            defaultRequest {
                url(ApiConfig.BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }
}
