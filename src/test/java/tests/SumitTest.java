package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


public class SumitTest {
	@Test
	public void show() {
	WebDriver driver = new ChromeDriver();
	
	try {
        // 2. Navigate to a URL
        driver.get("https://www.google.com");

        // 3. Maximize the window
        driver.manage().window().maximize();

        // 4. Find an element (The search box)
        // Google's search box usually has the name attribute "q"
        WebElement searchBox = driver.findElement(By.name("q"));

        // 5. Perform an action
        searchBox.sendKeys("Selenium Java Tutorial");
        searchBox.submit();

        // 6. Verify the page title
        System.out.println("Page title is: " + driver.getTitle());

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // 7. Close the browser
        driver.quit();
    }
}}

	
	
