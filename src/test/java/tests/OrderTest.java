package tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pages.MainPage;
import pages.OrderPage;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest extends BaseTest {
    @Parameterized.Parameter public boolean isUpperButton;
    @Parameterized.Parameter(1) public String name;
    @Parameterized.Parameter(2) public String surname;
    @Parameterized.Parameter(3) public String address;
    @Parameterized.Parameter(4) public String metro;
    @Parameterized.Parameter(5) public String phone;
    @Parameterized.Parameter(6) public String date;
    @Parameterized.Parameter(7) public String period;
    @Parameterized.Parameter(8) public boolean isBlack;

    private MainPage mainPage;
    private OrderPage orderPage;

    @Parameterized.Parameters(name = "Тест через {0} кнопку")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {true, "Иван", "Иванов", "ул. Ленина, 1", "Черкизовская", "+79991112233", "01.06.2024", "сутки", true},
                {false, "Петр", "Петров", "ул. Пушкина, 10", "Сокольники", "+79994445566", "02.06.2024", "двое суток", false}
        });
    }

    @Before
    public void setup() {
        super.setup(); // Вызов родительского метода для инициализации driver
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
        mainPage.acceptCookies();
    }

    @Test
    public void testOrderCreation() {
        mainPage.clickOrderButton(isUpperButton);
        orderPage.fillOrderForm(name, surname, address, metro, phone, date, period, isBlack);
        assertTrue("Окно подтверждения не отображено", orderPage.isOrderSuccess());
    }
}