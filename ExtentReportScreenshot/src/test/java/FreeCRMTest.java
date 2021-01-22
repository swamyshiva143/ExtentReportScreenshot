import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.tools.ant.util.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class FreeCRMTest {

	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;
	
	
	@BeforeTest
	public void setExtent() {
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/ExtentReport.html", true);
		extent.addSystemInfo("Host Name", "Swamyshiva");
		extent.addSystemInfo("User Name", "SwamyAkula");
		extent.addSystemInfo("Environment", "QA");
	}
	
	@AfterTest
	public void endReport() {
		extent.flush();
		extent.close();
	}
	
	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts= (TakesScreenshot) driver;
		File source= ts.getScreenshotAs(OutputType.FILE);
		//after execution you could see a folder "FailedTestsScreenshots"
		//under src folder
		
		String destination = System.getProperty("user.dir")+ "/FailedTestsScreenshots/"+ screenshotName+ dateName+ ".png";
		File finalDestination = new File(destination);
	//	FileUtils.copyFile(source, finalDestination);
		return destination;
		
		
		//return destination;
		
		
		  
	}
	
	
	
	
	@BeforeMethod
	public void setUp() {
		
		System.setProperty("webdriver.chrome.driver", "D:\\Swamyshiva\\swamyshiva\\chromedriver\\chromedriver.exe");
		driver =  new ChromeDriver();	//launch browser
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		driver.get("https://freecrm.co.in/");
	}
	
	
	
	@Test
	public void freeCRMTitleTest() {
		extentTest= extent.startTest("freeCRMTitleTest");
		String title =driver.getTitle();
		System.out.println(title);
		Assert.assertEquals(title, "Free CRM #1 cloud software for any business large or small123");
	}
	
	
	@AfterMethod
	public void tearDown(ITestResult result) throws Exception {
		if(result.getStatus()==ITestResult.FAILURE) {
		extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS  "+ result.getName()); 	//to add name in extent report
		extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS  "+ result.getThrowable());		// to add exception/error in extent report
		
	String screenshotPath= FreeCRMTest.getScreenshot(driver, result.getName());
	extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath));	//to add screenshot in extent report
	//extentTest.log(LogStatus.FAIL, extentTest.addScreencast(screenshotPath));		// to add screencast/video in extent report
	
		}
		else if(result.getStatus()==ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP, "TEST CASE SKIPPED IS  "+ result.getName() );
		}
		else if(result.getStatus()==ITestResult.SUCCESS) {
			extentTest.log(LogStatus.PASS, "TEST CASE PASSED IS  "+ result.getName());
		}
		
		extent.endTest(extentTest);
		driver.quit();
	}


	
	
	
	
}
