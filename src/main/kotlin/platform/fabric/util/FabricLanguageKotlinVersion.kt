package com.demonwav.mcdev.platform.fabric.util

import com.demonwav.mcdev.creator.selectProxy
import com.demonwav.mcdev.update.PluginUtil
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.requests.suspendable
import com.google.gson.JsonSyntaxException
import com.intellij.openapi.diagnostic.Logger
import java.io.IOException

private val LOGGER = Logger.getInstance("FabricLanguageKotlinVersion")

suspend fun getLatestFabricLanguageKotlin(): String? {
    try {
        val url = "https://maven.fabricmc.net/net/fabricmc/fabric-language-kotlin/maven-metadata.xml"
        val manager = FuelManager()
        manager.proxy = selectProxy(url)

        val response = manager.get(url)
            .header("User-Agent", PluginUtil.useragent)
            .suspendable()
            .await()

        val release = response.body().toByteArray().decodeToString()
            .substringAfter("<release>")
            .substringBefore("</release>")
        return release.ifEmpty { null }
    } catch (e: IOException) {
        LOGGER.error(e)
    } catch (e: JsonSyntaxException) {
        LOGGER.error(e)
    } catch (e: IllegalStateException) {
        LOGGER.error(e)
    } catch (e: UnsupportedOperationException) {
        LOGGER.error(e)
    }
    return null
}
