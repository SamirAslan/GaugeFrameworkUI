package step;

import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.Step;
import helper.ElementHelper;
import helper.StoreHelper;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.Driver;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class StepImplementation extends Driver {

    WebDriverWait wait = new WebDriverWait(driver, 10);

    @Step("<url> sitesine gidilir.")
    public void navigateToUrl(String url) {
        driver.navigate().to(url);
    }

    @Step("<second> saniye kadar bekle")
    public void waitWithSecond(int key) throws InterruptedException {
        Gauge.captureScreenshot();
        Gauge.writeMessage("Saniye bekleniyor");
        Thread.sleep(key * 1000L);
    }

    @Step("<key> li elementlerden texti <text> olana tıkla")
    public void clickTextOfElements(String key, String text) {
        String elementText = null;
        boolean flag = true;
        int count = 1;
        while (flag) {
            try {
                By byElement = ElementHelper.getElementInfoToBy(key);
                wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
                List<WebElement> elements = driver.findElements(byElement);
                for (WebElement element : elements) {
                    swipeToElement(element);
                    if ((text.startsWith("Value_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                        if (!element.isDisplayed())
                            wait.until(ExpectedConditions.visibilityOfElementLocated(byElement));
                        elementText = element.getText();
                        element.click();
                        break;
                    }
                }
                flag = false;
                break;
            } catch (StaleElementReferenceException s) {
                count = count + 1;
                scrollDown();
                if (text.equals(elementText)) {
                    break;
                }
            }
        }
    }
    public void scrollDown() {
        try {
            int i = 0;
            for (; i <= 30; i++) {
                ((JavascriptExecutor) driver).executeScript(("window.scrollBy(0," + i + ")"), "");
            }
            for (; i > 0; i--) {
                ((JavascriptExecutor) driver).executeScript(("window.scrollBy(0," + i + ")"), "");
            }
        } catch (WebDriverException wde) {
        } catch (Exception e) {
        }
    }
    public void swipeToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 0; i < 25; i++) {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            js.executeScript("arguments[0].scrollIntoView({behavior: \"auto\", block: \"center\", inline: \"center\"});", element);
        }
    }

    @Step("<key> li elemente tıkla")
    public void click(String key) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
        driver.findElement(byElement).click();
    }

    @Step({"<key> li elemente <text> degerini yaz"})
    public void sendKey(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        element.clear();
        if (text.startsWith("Value_")) {
            element.sendKeys(StoreHelper.getValue(text));
        } else {
            element.sendKeys(text);
        }
    }

    @Step("Açılan popup da <text> var mı?")
    public void alertMessageControl(String text) {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            String alertText = driver.switchTo().alert().getText();
            System.out.println(alertText);
            if ((text.startsWith("Value_") && alertText.contains(StoreHelper.getValue(text)))) {
                driver.switchTo().alert().accept();
            } else {
                Assert.fail();
            }
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Step("<key> li elementin text değeri <text>'ye eşit mi?")
    public void equalText(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        String elementText = element.getText();
        try {
            System.out.println(elementText);
            if ((text.startsWith("Value_") && elementText.equals(StoreHelper.getValue(text)))) {
            } else {
                Assert.fail();
            }
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Step("<key> li elementlerden birinin text değeri <text>'ye eşit mi?")
    public void equalTexts(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements){
            String elementText = element.getText();
            if ((text.startsWith("Value_") && elementText.equals(StoreHelper.getValue(text)))) {
                System.out.println(elementText);
            } else {
                Assert.fail();
            }
        }
    }
    @Step("<key> li elementin text değeri <text>'yi içeriyor mu?")
    public void containText(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        String elementText = element.getText();
        try {
            System.out.println(elementText);
            if ((text.startsWith("Value_") && elementText.contains(StoreHelper.getValue(text)))) {
            } else {
                Assert.fail();
            }
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Step("<key> li elementin price değeri <text>'yi içeriyor mu?")
    public void containTexts(String key, String text) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
        WebElement element = driver.findElement(byElement);
        String elementText = element.getText();
        try {
            System.out.println(elementText);
            if ((text.startsWith("Value_") && elementText.contains(StoreHelper.getValue(text).replaceFirst("\\$","")))) {
            } else {
                Assert.fail();
            }
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Step("Sayfa aşağı scroll yapılır.")
    public void scrollToElement() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

    }

    @Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementine tıkla"})
    public void findElementWithTextsAndClickNextTo(String key, String text, String key2) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            if ((text.startsWith("Value_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                WebElement elementsNextTo = element.findElement(ElementHelper.getElementInfoToBy(key2));
                elementsNextTo.click();
                break;
            }
        }
    }

    @Step({"<key> li elementlerden <text> değerine eşit olanın yanındaki <key2> elementine <text2> değerini yaz"})
    public void findElementWithTextsAndSendKeyNextTo(String key, String text, String key2, String text2) {
        By byElement = ElementHelper.getElementInfoToBy(key);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
        List<WebElement> elements = driver.findElements(byElement);
        for (WebElement element : elements) {
            if ((text.startsWith("Value_") && element.getText().contains(StoreHelper.getValue(text))) || element.getText().equals(text)) {
                WebElement elementNextTo = element.findElement(ElementHelper.getElementInfoToBy(key2));
                elementNextTo.clear();
                if (text2.startsWith("Value_")) {
                    elementNextTo.sendKeys(StoreHelper.getValue(text2));
                } else {
                    Actions action = new Actions(driver);
                    action.click(elementNextTo).perform();
                    action.moveByOffset(0, 0).sendKeys(text2).perform();
                }
                break;
            }
        }
    }

}
