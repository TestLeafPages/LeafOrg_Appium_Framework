package pages;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import mobileWrapper.AppSpecificMethods;

public class LoginPage extends AppSpecificMethods{
	
	
	public LoginPage enterEmailAddress(String email) {
		if(enterValue(email, "xpath", "//input[@placeholder='Email']"))
			reportStep("Email address "+email+" entered successfully", "PASS");
		else
			reportStep("Email address couldn't enter", "FAIL");
		return this;
	}
	
	public LoginPage enterPassword(String pwd) {
		if(enterValue(pwd, "xpath", "//input[@placeholder='Password']"))
			reportStep("Email address "+pwd+" entered successfully", "PASS");
		else
			reportStep("Email address couldn't enter", "FAIL");
		return this;
	}
	
	@When("the user clicked login which is designed under the password test field")
	public HomePage clickLogin() {
		if(click("xpath", "//span[text()='Login']"))
			reportStep("Login button clicked Successfully", "PASS");
		else
			reportStep("Login button click failed", "FAIL");
		return new HomePage();
	}
	
	@Given("the user enter valied credential username as (.*) and password as (.*)")
	public void enterValidCredential(String email, String pwd) {
		enterEmailAddress(email);
		enterPassword(pwd);
	}
	
	

}
