import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Test
import utils.*
import kotlin.io.path.Path

class TestAutomationEnvironment {
    init {
        WebDriverManager.chromedriver().setup()
    }

    val config = TestConfiguration(
        browserLanguage = "en",
        driver = Driver.CHROME,
        filesRootLocation = Path("C:/temp/test-web-test")
    )

    @Test
    fun runTest() {
        WebTest(config).apply {
            driver {
                val didYouKnowLink = "xpath"
                val pageTitle = "xpath"

                WebTest(config).apply {
                    driver(false) {
                        get("https://en.wikipedia.org/wiki/Main_Page")
                        waitUntilPageIsLoaded()
                        waitThenClick(didYouKnowLink)
                        waitMillis(100)
                        waitUntilPageIsLoaded()
                        waitUntilDisplayed(pageTitle)

                        val title = getText(pageTitle)
                            .filter { it.isWhitespace() or it.isLetterOrDigit() }
                            .replace(' ', '_')

                        takeScreenshot(title)
                    }
                }
            }
        }
    }
}