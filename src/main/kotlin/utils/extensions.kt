@file:Suppress("unused", "UnusedReceiverParameter")

package utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

var defaultTimeout: Duration = Duration.ofSeconds(60L)

private val logger = KotlinLogging.logger {}

fun WebDriver.waitUntilAllPresentBy(
    by: By, duration: Duration = defaultTimeout
): List<WebElement> = WebDriverWait(this, duration).until(ExpectedConditions.presenceOfAllElementsLocatedBy(by))

infix fun WebDriver.isDisplayed(by: By): Boolean = findElement(by).isDisplayed

infix fun WebDriver.click(element: WebElement): WebElement {
    Actions(this).moveToElement(element).click().perform()
    return element
}

infix fun WebDriver.click(by: By): WebElement {
    val element: WebElement = findElement(by)
    Actions(this).moveToElement(element).click().perform()
    return element
}

infix fun WebDriver.clickOptional(element: WebElement): WebElement {
    return try {
        click(element)
        element
    } catch (t: Throwable) {
        logger.debug(t.localizedMessage)
        element
    }
}

infix fun WebDriver.clickOptional(by: By): Result<WebElement> {
    return try {
        Result.success(click(by))
    } catch (t: Throwable) {
        logger.debug(t.localizedMessage)
        Result.failure(t)
    }
}

fun WebDriver.delayedClick(element: WebElement, timeMillis: Long = 1000): WebElement {
    waitMillis(timeMillis)
    waitThenClick(element)
    return element
}

fun WebDriver.delayedClick(by: By, timeMillis: Long = 1000): WebElement {
    waitMillis(timeMillis)
    return waitThenClick(by)
}

infix fun WebDriver.rightClick(element: WebElement): WebElement {
    Actions(this).moveToElement(element).contextClick().perform()
    return element
}

infix fun WebDriver.rightClick(by: By): WebElement {
    val element: WebElement = findElement(by)
    Actions(this).moveToElement(element).contextClick().perform()
    return element
}

infix fun WebDriver.doubleClick(element: WebElement): WebElement {
    Actions(this).moveToElement(element).doubleClick().perform()
    return element
}

infix fun WebDriver.doubleClick(by: By): WebElement {
    val element: WebElement = findElement(by)
    Actions(this).moveToElement(element).doubleClick().perform()
    return element
}

infix fun WebDriver.clickJS(element: WebElement): WebElement {
    (this as JavascriptExecutor).executeScript("arguments[0].click();", element)
    return element
}

infix fun WebDriver.clickJS(by: By): WebElement {
    return clickJS(findElement(by) as WebElement)
}

fun WebDriver.input(webElement: WebElement, string: CharSequence) = webElement.apply { sendKeys(string) }

fun WebDriver.input(by: By, string: CharSequence): WebElement = input(findElement(by) as WebElement, string)

infix fun WebDriver.hover(webElement: WebElement): WebElement = webElement.apply {
    Actions(this@hover).moveToElement(this).moveByOffset(1, 1).perform()
}

infix fun WebDriver.hover(by: By): WebElement = hover(findElement(by) as WebElement)

fun WebDriver.drag(element: WebElement, offset: Pair<Int, Int>): WebElement {
    Actions(this).moveToElement(element).clickAndHold().moveByOffset(offset.first, offset.second).release().perform()
    return element
}

fun WebDriver.drag(by: By, offset: Pair<Int, Int>): WebElement {
    return drag(findElement(by) as WebElement, offset)
}

fun WebDriver.waitThenClick(by: By, duration: Duration = defaultTimeout): WebElement =
    waitUntilDisplayed(by, duration).apply(::click)

fun WebDriver.waitThenClick(element: WebElement, duration: Duration = defaultTimeout): WebElement =
    waitUntilDisplayed(element, duration).apply(::click)

fun WebDriver.waitThenInput(by: By, string: CharSequence, duration: Duration = defaultTimeout): WebElement =
    waitUntilDisplayed(by, duration).apply { sendKeys(string) }

fun WebDriver.waitUntilDisplayed(by: By, duration: Duration = defaultTimeout): WebElement {
    waitUntil(duration) {
        this.isDisplayed(by)
    }
    return findElement(by)
}

inline fun WebDriver.waitUntil(duration: Duration = defaultTimeout, crossinline action: WebDriver.() -> Boolean) {
    WebDriverWait(this, duration).until {
        action()
    }
}

fun WebDriver.waitUntilDisplayed(element: WebElement, duration: Duration = defaultTimeout): WebElement {
    waitUntil(duration) {
        element.isDisplayed
    }
    return element
}

fun waitMillis(millis: Long) {
    runBlocking { delay(millis) }
}

fun WebDriver.scrollIntoView(element: WebElement, waitMillis: Long = 500): WebElement {
    (this as JavascriptExecutor).executeScript("arguments[0].scrollIntoView(true);", element)
    runBlocking { delay(waitMillis) }
    return element
}

fun WebDriver.scrollBy(x: Int, y: Int) {
    (this as JavascriptExecutor).executeScript("window.scrollBy($x, $y);")
}

fun WebDriver.scrollTo(x: Int, y: Int) {
    (this as JavascriptExecutor).executeScript("window.scrollTo($x, $y);")
}

fun WebDriver.getWindowInnerDimensions(): Dimension = (this as JavascriptExecutor).run {
    val height = (executeScript("return window.innerHeight") as Long).toInt()
    val width = (executeScript("return window.innerWidth") as Long).toInt()
    Dimension(width, height)
}

fun WebDriver.getDocumentScrollDimensions(): Dimension = (this as JavascriptExecutor).run {
    val height = (executeScript("return document.documentElement.scrollHeight") as Long).toInt()
    val width = (executeScript("return document.documentElement.scrollWidth") as Long).toInt()
    Dimension(width, height)
}

fun WebDriver.isElementVisibleInViewport(element: WebElement): Boolean =
    (this as JavascriptExecutor).executeScript(
        "let elem = arguments[0],                 "
            + "  box = elem.getBoundingClientRect(),    "
            + "  cx = box.left + box.width / 2,         "
            + "  cy = box.top + box.height / 2,         "
            + "  e = document.elementFromPoint(cx, cy); "
            + "for (; e; e = e.parentElement) {         "
            + "  if (e === elem)                        "
            + "    return true;                         "
            + "}                                        "
            + "return false;                            ", element
    ) as Boolean

fun WebDriver.waitUntilPageIsLoaded(duration: Duration = defaultTimeout) {
    waitUntil(duration) {
        (this as JavascriptExecutor).executeScript("return document.readyState") == "complete"
    }
}

fun WebDriver.getText(element: WebElement): String =
    (this as JavascriptExecutor).executeScript("return arguments[0].innerText;", element) as String

fun WebDriver.getText(by: By): String = getText(findElement(by))

typealias XPath = String

infix fun WebDriver.findElement(xpath: XPath): WebElement = findElement(By.xpath(xpath))

infix fun WebDriver.click(xpath: XPath): WebElement = click(By.xpath(xpath))

infix fun WebDriver.clickOptional(xpath: XPath): Result<WebElement> = clickOptional(By.xpath(xpath))

infix fun WebDriver.doubleClick(xpath: XPath): WebElement = doubleClick(By.xpath(xpath))

infix fun WebDriver.delayedClick(xpath: XPath): WebElement = delayedClick(By.xpath(xpath))

infix fun WebDriver.clickJS(xpath: XPath): WebElement = clickJS(By.xpath(xpath))

infix fun WebDriver.isDisplayed(xpath: XPath): Boolean = isDisplayed(By.xpath(xpath))

fun WebDriver.input(xpath: XPath, string: CharSequence): WebElement = input(By.xpath(xpath), string)

fun WebDriver.drag(xpath: XPath, offset: Pair<Int, Int>): WebElement = drag(By.xpath(xpath), offset)

fun WebDriver.waitThenClick(xpath: XPath, duration: Duration = defaultTimeout): WebElement =
    waitThenClick(By.xpath(xpath), duration)

fun WebDriver.waitThenInput(xpath: XPath, string: CharSequence, duration: Duration = defaultTimeout): WebElement =
    waitThenInput(By.xpath(xpath), string, duration)

fun WebDriver.waitUntilDisplayed(xpath: XPath, duration: Duration = defaultTimeout): WebElement =
    waitUntilDisplayed(By.xpath(xpath), duration)

fun WebDriver.waitUntilAllPresent(xpath: XPath, duration: Duration = defaultTimeout): WebElement =
    waitUntilAllPresentBy(By.xpath(xpath), duration).first()

fun WebDriver.getText(xpath: XPath): String = getText(By.xpath(xpath))