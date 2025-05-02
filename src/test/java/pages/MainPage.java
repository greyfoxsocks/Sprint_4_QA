package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class MainPage {
    public static final String URL = "https://qa-scooter.praktikum-services.ru/";
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cookieButton = By.id("rcc-confirm-button");
    private final By upperOrderButton = By.xpath("//button[text()='Заказать' and not(contains(@class, 'Button_Middle'))]");
    private final By lowerOrderButton = By.xpath("(//div[contains(@class, 'Home_FinishButton')]//button[text()='Заказать'])[last()]");
    private final By accordionItem = By.className("accordion__item");
    private final By accordionAnswer = By.xpath(".//div[@class='accordion__panel']");

    public MainPage(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "WebDriver не может быть null");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(URL);
    }

    public void acceptCookies() {
        try {
            driver.findElement(cookieButton).click();
        } catch (Exception e) {
            System.out.println("Cookie banner not found");
        }
    }

    public void clickOrderButton(boolean isUpper) {
        By locator = isUpper ? upperOrderButton : lowerOrderButton;
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        button.click();
    }

    public void clickAccordionItem(int index) {
        List<WebElement> items = driver.findElements(accordionItem);
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
        WebElement item = items.get(index);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", item);
        item.click();
    }

    public String getAccordionAnswerText(int index) {
        List<WebElement> answers = driver.findElements(accordionAnswer);
        if (index < 0 || index >= answers.size()) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
        return wait.until(ExpectedConditions.visibilityOf(answers.get(index))).getText();
    }
}