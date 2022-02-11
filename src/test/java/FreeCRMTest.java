import com.automation.remarks.video.annotations.Video;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.getProperty;
import static java.lang.System.out;


public class FreeCRMTest {
	
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;

	@BeforeTest
	public void setExtent(){
		extent = new ExtentReports(getProperty("user.dir")+"/ExtentReport.html", true);
		extent.addSystemInfo("Host Name", "Saurav Sharma");
		extent.addSystemInfo("User Name", "Saurav");
		extent.addSystemInfo("Environment", "QA");
	}
	
	@AfterTest
	public void endReport(){
		extent.flush();
		extent.close();
	}
	
	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException{
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = getProperty("user.dir") + screenshotName + dateName
				+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	@BeforeMethod
	public void setup(){

		WebDriverManager.chromedriver().setup();
		//System.setProperty("webdriver.chrome.driver", "C:\\Users\\SauravSharma\\Drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get("https://www.freecrm.com/");
		
	}
	
	@Test
	public void freeCrmTitleTest(){
		extentTest = extent.startTest("freeCrmTitleTest");
		String title = driver.getTitle();
		out.println(title);
		Assert.assertEquals(title,"#1 Free CRM for Any Business: Online Customer Relationship Software123");
	}
	
	@Test
	public void freemCRMLogoTest(){
		extentTest = extent.startTest("freemCRMLogoTest");
		boolean b = driver.findElement(By.xpath("//img[@class='img-responsive111']")).isDisplayed();
		Assert.assertTrue(b);
	}

	@AfterMethod
	@Video
	public void tearDown(ITestResult result) throws IOException, InterruptedException {
		
		if(result.getStatus()==ITestResult.FAILURE){
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getName()); //to add name in extent report
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getThrowable()); //to add error/exception in extent report
			
			String screenshotPath = getScreenshot(driver, result.getName());
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath));
			Thread.sleep(20000);//to add screenshot in extent report

			extentTest.log(LogStatus.FAIL, extentTest.addScreencast(screenshotPath));
			Thread.sleep(20000);//to add screencast/video in extent report
		}
		else if(result.getStatus()==ITestResult.SKIP){
			extentTest.log(LogStatus.SKIP, "Test Case SKIPPED IS " + result.getName());
		}
		else if(result.getStatus()==ITestResult.SUCCESS){
			extentTest.log(LogStatus.PASS, "Test Case PASSED IS " + result.getName());

		}
		
		
		extent.endTest(extentTest); //ending test and ends the current test and prepare to create html report
		driver.quit();
	}
}
