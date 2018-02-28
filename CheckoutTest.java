package com.sugaring;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutTest {
	private WebDriver driver;
	/*
	 * Test Scenario: Guest placing order with same shipping & payment data 
	 * Each @test method represents a test case for the scenario.
	 */
	@Before 
	public void setUp() throws Exception {
		//System.setProperty("webdriver.gecko.driver", "C:\\Users\\gaurang.chitroda\\Selenium\\drivers\\geckodriver-v0.19.1-win64\\geckodriver.exe");
		//driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\gaurang.chitroda\\Selenium\\drivers\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("https://test.sugaringfactory.com/");
		//add item into the cart
		driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[2]/div[1]/ul/li[1]/div[2]/div[3]/div/div[2]/div[1]/a")).click();
		//click on checkout from the top menu bar
		driver.findElement(By.xpath("//*[@id=\"header\"]/div[1]/div/div/div[2]/ul/li[5]/a")).click();
		//click on continue checkout to get to Checkout page
		driver.findElement(By.xpath("//*[@id=\"header\"]/div[1]/div/div/div[2]/ul/li[5]/a")).click();
		System.out.println(driver.getTitle());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
	//to verify that amazon pay(3rd party payment) is working [positive test case]
	@Test 
	public void testAmazon() throws InterruptedException {
		//store the parent window handle      
        String MainWindow=driver.getWindowHandle();	
        System.out.println(MainWindow);
        //perform click to open new window
        driver.findElement(By.xpath("//*[@id=\"OffAmazonPaymentsWidgets0\"]")).click(); 
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        
        // To handle all opened window.				
        Set<String> s1=driver.getWindowHandles();	
        Iterator<String> i1=s1.iterator();	
        
        i1.next();// will give parent handle
        //to get child handle
        String ChildWindow2=i1.next().toString();			
        System.out.println("Child 2 is: "+ChildWindow2);
        //to wait till page is loaded
        //driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        //using thread for FF, FF doesn't work with timeout
        //Thread.sleep(30);
	    //driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"amazon-proxy-https-api_cdn_amazon_com\"]")));
        //switch to child window
        driver.switchTo().window(ChildWindow2);
        System.out.println(driver.getTitle());
        //compare title of child window
        assertEquals("Amazon.com Sign In", driver.getTitle());		  
        // Closing the Child Window.
        driver.close();				
        // Switching to Parent window i.e Main Window.
        driver.switchTo().window(MainWindow);
	}
	//verify with valid data & credit card option [positive test case]
	@Test
	public void testValidData() throws InterruptedException {
		//type in valid data for required fields
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[1]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[1]/input")).sendKeys("Urja");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[2]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[2]/input")).sendKeys("Devmurari");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[4]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[4]/input")).sendKeys("urjadevmurari@gmail.com");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[5]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[5]/input")).sendKeys("1234567890");
		driver.findElement(By.xpath("//*[@id=\"zone_id\"]")).sendKeys("California");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[8]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[8]/input")).sendKeys("Los Altos");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[9]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[9]/input")).sendKeys("4970 El Camino Real");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[11]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[11]/input")).sendKeys("12345");
		//check the same shipping & payment checkbox
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[12]/label/input[2]")).click();
		//wait till credit card option becomes visible
		WebDriverWait wait = new WebDriverWait(driver, 35);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"authorizenet_aim\"]")));
		//Thread.sleep(30);
		//terms & condition checkbox
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[14]/label/input")).click();
		
		//confirm Terms & Condition Checkbox
        driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
        //using thread for FF, FF doesn't work with timeout
        //Thread.sleep(30);
        //click on accept
		driver.findElement(By.xpath("//*[@id=\"fancybox-content\"]/div[2]/div/a[2]")).click();
		driver.manage().timeouts().pageLoadTimeout(3000, TimeUnit.SECONDS);
		Thread.sleep(3000);
		System.out.println(driver.getTitle());
		
		//select credit card option
		driver.findElement(By.xpath("//*[@id=\"authorizenet_aim\"]")).click();
		driver.manage().timeouts().pageLoadTimeout(3000, TimeUnit.SECONDS);
		//confirm checkout
		driver.findElement(By.xpath("//*[@id=\"checkout-submit-button\"]")).click(); 
		driver.manage().timeouts().pageLoadTimeout(3000, TimeUnit.SECONDS);
		Thread.sleep(3000);
		//finally comparing the page title
		System.out.println(driver.getTitle());
		assertEquals("Payment", driver.getTitle());
	}
	//Verify if system rejects the order if all fields left blank [negative test case]
	@Test
	public void blankFeilds() {
		//click on 'confirm order' button
		driver.findElement(By.xpath("//*[@id=\"checkout-submit-button\"]")).click(); 
		//compare the page title
		assertEquals("Checkout", driver.getTitle());
	}
	//Verify if system rejects First name with > 32 characters
	@Test
	public void invalidFirstName() throws InterruptedException {
		//enter first name 33 characters long
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[1]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[1]/input")).sendKeys("This is a free online calculator.");
		//rest fields will have valid data
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[2]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[2]/input")).sendKeys("Devmurari");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[4]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[4]/input")).sendKeys("urjadevmurari@gmail.com");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[5]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[5]/input")).sendKeys("1234567890");
		driver.findElement(By.xpath("//*[@id=\"zone_id\"]")).sendKeys("California");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[8]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[8]/input")).sendKeys("Los Altos");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[9]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[9]/input")).sendKeys("4970 El Camino Real");
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[11]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[11]/input")).sendKeys("12345");
		//check the same shipping & payment checkbox
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[12]/label/input[2]")).click();
		//wait till credit card option becomes visible
		WebDriverWait wait = new WebDriverWait(driver, 35);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"authorizenet_aim\"]")));
		//Thread.sleep(30);
		//terms & condition checkbox
		driver.findElement(By.xpath("//*[@id=\"checkout-form\"]/div[1]/div[1]/div[14]/label/input")).click();
		
		//confirm Terms & Condition Checkbox
        //driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
        //using thread for FF, FF doesn't work with timeout
        //Thread.sleep(30);
        //click on accept
		driver.findElement(By.xpath("//*[@id=\"fancybox-content\"]/div[2]/div/a[2]")).click();
		//driver.manage().timeouts().pageLoadTimeout(3000, TimeUnit.SECONDS);
		Thread.sleep(3000);
		System.out.println(driver.getTitle());
		
		//select credit card option
		driver.findElement(By.xpath("//*[@id=\"authorizenet_aim\"]")).click();
		//driver.manage().timeouts().pageLoadTimeout(3000, TimeUnit.SECONDS);
		//confirm checkout
		driver.findElement(By.xpath("//*[@id=\"checkout-submit-button\"]")).click(); 
		//driver.manage().timeouts().pageLoadTimeout(3000, TimeUnit.SECONDS);
		//Thread.sleep(30);
		//comparing the page title
		System.out.println(driver.getTitle());
		assertEquals("Checkout", driver.getTitle());
	}
}
