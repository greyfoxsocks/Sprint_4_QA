package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    private final By nameField = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroField = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");
    private final By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriod = By.xpath("//div[contains(@class, 'Dropdown-placeholder') and text()='* Срок аренды']");
    private final By blackCheckbox = By.id("black");
    private final By greyCheckbox = By.id("grey");
    private final By confirmButton = By.xpath("//button[contains(., 'Да')]");
    private final By successModal = By.xpath("//div[contains(text(), 'Заказ оформлен')]");
    private final By submitOrderButton = By.xpath("//div[contains(@class, 'Order_Buttons')]//button[text()='Заказать']");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void fillOrderForm(String name, String surname, String address, String metro, String phone, String date, String period, boolean isBlack) {
        fillFirstPage(name, surname, address, metro, phone);
        fillSecondPage(date, period, isBlack);
    }

    private void fillFirstPage(String name, String surname, String address, String metro, String phone) {
        fillField(nameField, name);
        fillField(surnameField, surname);
        fillField(addressField, address);
        selectMetro(metro);
        fillField(phoneField, phone);
        clickNextButton();
    }

    private void fillSecondPage(String date, String period, boolean isBlack) {
        setDate(date);
        selectRentalPeriod(period);
        selectColor(isBlack);
        submitOrder();
        confirmOrder();
    }

    private void fillField(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private void selectMetro(String metro) {
        fillField(metroField, metro);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='" + metro + "']"))).click();
    }

    private void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    private void setDate(String date) {
        fillField(dateField, date);
        driver.findElement(By.tagName("body")).click();
    }

    private void selectRentalPeriod(String period) {
        // Клик по полю выбора срока
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(rentalPeriod));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
        dropdown.click();

        // Ожидание появления меню
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("Dropdown-menu")));

        // Поиск нужного варианта по частичному совпадению текста
        List<WebElement> options = driver.findElements(
                By.xpath("//div[contains(@class, 'Dropdown-option')]"));

        for (WebElement option : options) {
            if (option.getText().toLowerCase().contains(period.toLowerCase())) {
                ((JavascriptExecutor)driver).executeScript(
                        "arguments[0].scrollIntoView(true); arguments[0].click();", option);
                return;
            }
        }
        throw new NoSuchElementException("Не найден вариант срока аренды: " + period);
    }

    private void selectColor(boolean isBlack) {
        By colorLocator = isBlack ? blackCheckbox : greyCheckbox;
        WebElement color = wait.until(ExpectedConditions.elementToBeClickable(colorLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", color);
    }

    private void submitOrder() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitOrderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    private void confirmOrder() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        button.click();
    }

    public boolean isOrderSuccess() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successModal)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}