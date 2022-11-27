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
            driver(true) {
                // write your code
            }
        }
    }
}