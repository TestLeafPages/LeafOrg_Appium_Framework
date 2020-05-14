package runner;

import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.gherkin.model.Scenario;

import cucumber.api.CucumberOptions;
import mobileWrapper.AppSpecificMethods;

@CucumberOptions(features = "src/test/java/features",
				glue = "pages")
				//snippets = SnippetType.CAMELCASE)
public class RunTest extends AppSpecificMethods{
	
	@BeforeTest
	public void setData() {
		testCaseName = "TC001_Login";
		testDescription = "Login in LeafOrg app";
		testNodes = "TC001";
		authors = "Gopinath Jayakumar";
		category = "Smoke";
	}

}
