package pages;

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
	
	public HomePage clickLogin() {
		if(click("xpath", "//span[text()='Login']"))
			reportStep("Login button clicked Successfully", "PASS");
		else
			reportStep("Login button click failed", "FAIL");
		return new HomePage();
	}
	

}
