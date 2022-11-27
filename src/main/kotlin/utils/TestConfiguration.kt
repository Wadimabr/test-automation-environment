package utils

import org.openqa.selenium.remote.RemoteWebDriver
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

data class TestConfiguration(
    val browserLanguage: String,
    val driver: Driver,
    val filesRootLocation: Path = FileSystems.getDefault().getPath("", "web test")
) {
    init {
        Files.createDirectories(filesRootLocation)
    }

    fun newDriver(headless: Boolean = false): RemoteWebDriver {
        return driver.get(language = browserLanguage, headless = headless)
    }
}
