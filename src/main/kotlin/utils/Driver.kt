package utils

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.nio.file.Path

enum class Driver(val property: String) {
    CHROME("webdriver.chrome.driver") {
        override fun get(language: String, headless: Boolean): RemoteWebDriver {
            val options = ChromeOptions().addArguments(
                "--lang=$language",
                "--disable-default-apps",
                "--start-maximized",
                "--ignore-certificate-errors",
                "--disable-popup-blocking",
                "--use-fake-ui-for-media-stream",
                "--use-fake-device-for-media-stream",
                "--allow-file-access-from-files",
            )
            if (headless) options.addArguments("--headless", "--disable-gpu")
            return ChromeDriver(options)
        }
    };

    abstract fun get(language: String, headless: Boolean = false): RemoteWebDriver
}