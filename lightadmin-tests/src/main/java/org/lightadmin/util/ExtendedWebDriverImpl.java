package org.lightadmin.util;

import com.google.common.base.Predicate;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

public class ExtendedWebDriverImpl implements ExtendedWebDriver {

    private RemoteWebDriver webDriver;
    private long webDriverTimeout;

    public ExtendedWebDriverImpl(final RemoteWebDriver webDriver, final long webDriverTimeout) {
        this.webDriver = webDriver;
        this.webDriverTimeout = webDriverTimeout;
    }

    @Override
    public boolean isElementPresent(final WebElement webElement) {
        try {
            return webElement.isDisplayed();
        } catch (NoSuchElementException ex) {
            return false;
        }
        //todo: iko: handle stale element properly by trying to find it on a page again
        catch (StaleElementReferenceException e) {
            return false;
        }
    }

    @Override
    public boolean isElementPresent(By by) {
        try {
            webDriver.findElement(by);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isElementValuePresent(final String elementName, final String expectedValue) {
        return webDriver.findElement(By.name(elementName)).getAttribute("value").equals(expectedValue);
    }

    @Override
    public void waitForElementVisible(final WebElement element) throws TimeoutException {
        waitForElementVisible(element, webDriverTimeout);
    }

    @Override
    public void waitForElementVisible(final WebElement element, long timeout) {
        new WebDriverWait(webDriver, timeout).until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return isElementPresent(element);
            }
        });
    }

    @Override
    public void waitForElementInvisible(final WebElement element) {
        new WebDriverWait(webDriver, webDriverTimeout).until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return !isElementPresent(element);
            }
        });
    }

    @Override
    public void clearAndType(final WebElement field, final String text) {
        field.clear();
        field.sendKeys(text);
        field.sendKeys(Keys.TAB);
    }

    @Override
    public void clear(final WebElement field) {
        field.click();
        field.clear();
        field.sendKeys(Keys.TAB);
    }

    @Override
    public void forceFocusOnCurrentWindow() {
        webDriver.switchTo().window(webDriver.getWindowHandle());
    }

    @Override
    public void scrollTo(int pixels) {
        webDriver.executeScript("window.scrollBy(0, " + pixels +")");
    }

    @Override
    public void get(final String url) {
        webDriver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return webDriver.getTitle();
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return webDriver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return webDriver.getPageSource();
    }

    @Override
    public void close() {
        webDriver.close();
    }

    @Override
    public void quit() {
        webDriver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return webDriver.navigate();
    }

    @Override
    public Options manage() {
        return webDriver.manage();
    }
}
