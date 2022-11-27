package utils

import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.File

open class WebTest(val config: TestConfiguration) {
    var driver: RemoteWebDriver? = null

    var driverInit: (RemoteWebDriver.() -> Unit)? = null

    var screenshotFolder: String = config.browserLanguage

    fun <T> driver(quitOnFinish: Boolean = false, function: RemoteWebDriver.() -> T): T {
        if (driver == null || driver?.sessionId == null) {
            driver = config.newDriver()
            driver!!.apply {
                driverInit?.invoke(this)
            }
        }
        try {
            return function.invoke(driver!!)
        } finally {
            if (quitOnFinish) {
                driver?.quit()
            }
        }
    }

    fun WebDriver.takeScreenshot(name: String) {
        when (this) {
            is RemoteWebDriver -> getScreenshotAs(OutputType.FILE)
                .copyTo(File("${config.filesRootLocation.toAbsolutePath()}\\$screenshotFolder", "$name.png"), true)
        }
    }

    fun WebDriver.delayedScreenshot(name: String, millis: Long) {
        waitMillis(millis)
        takeScreenshot(name)
    }
}