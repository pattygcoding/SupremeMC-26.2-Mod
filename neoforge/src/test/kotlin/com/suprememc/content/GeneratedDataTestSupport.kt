package com.suprememc.content

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertTrue

abstract class GeneratedDataTestSupport {
    protected fun readJson(relativePath: String): JsonObject {
        val path = generatedResources.resolve(relativePath)
        assertTrue("Missing generated resource: $relativePath") { Files.isRegularFile(path) }
        return JsonParser.parseString(Files.readString(path)).asJsonObject
    }

    protected fun assertResourceExists(relativePath: String) {
        assertTrue("Missing generated resource: $relativePath") { Files.isRegularFile(generatedResources.resolve(relativePath)) }
    }

    protected fun assertTagContains(relativePath: String, vararg expectedValues: String) {
        val values = readJson(relativePath).getAsJsonArray("values").toString()
        expectedValues.forEach { assertTrue("Missing tag value $it in $relativePath") { it in values } }
    }

    protected companion object {
        val generatedResources: Path = Path.of("src", "generated", "resources")
    }
}