package utils;

import java.io.IOException;

import org.testng.reporters.XMLReporterConfig.StackTraceLevels;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import cucumber.api.testng.AbstractTestNGCucumberTests;

public abstract class Reporter extends AbstractTestNGCucumberTests{

	public ExtentHtmlReporter html;
	public ExtentReports extent;
	public static ExtentTest test, suiteTest;
	public String testCaseName, testNodes, testDescription, category, authors;

	// File Level - Before Suite
	public void startResult() {
		html = new ExtentHtmlReporter("./reports/result.html");
		html.config().setChartVisibilityOnOpen(false);
		html.config().setDocumentTitle("MobileFramework");
		html.setAppendExisting(true);		
		extent = new ExtentReports();	
		extent.attachReporter(html);	
	}

	// testCase level - Before class
	public void startTestModule(String testCaseName, String testDescription) {
		suiteTest = extent.createTest(testCaseName, testDescription);
	}


	// testData level - Before method
	public ExtentTest startTestCase(String testNodes) {
		test = 	suiteTest.createNode(testNodes);
		test.assignAuthor(authors);
		test.assignCategory(category);
		return test;
	}

	public abstract long takeScreenShot();

	// step level
	public void reportStep(String desc, String status, boolean bSnap)  {

		MediaEntityModelProvider img = null;
		if(bSnap && !status.equalsIgnoreCase("INFO")){

			long snapNumber = 100000L;
			snapNumber = takeScreenShot();
			try {
				img = MediaEntityBuilder.createScreenCaptureFromPath
						("./../reports/images/"+snapNumber+".png").build();
			} catch (IOException e) {
				
			}
		}
		if(status.equalsIgnoreCase("PASS")) {
			test.pass(desc, img);			
		}else if (status.equalsIgnoreCase("FAIL")) {
			test.fail(desc, img);
			throw new RuntimeException();
		}else if (status.equalsIgnoreCase("WARNING")) {
			test.warning(desc, img);
		}else if (status.equalsIgnoreCase("INFO")) {
			test.info(desc);
		}							
	}


	public void reportStep(String desc, String status) {
		reportStep(desc, status, true);
	}



	// execute code - After suite
	public void endResult() {
		extent.flush();
	}	

}