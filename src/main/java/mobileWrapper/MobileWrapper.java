package mobileWrapper;

import static io.appium.java_client.touch.offset.PointOption.point;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.StartsActivity;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.android.connection.HasNetworkConnection;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.PressesKey;
import io.appium.java_client.touch.WaitOptions;
import utils.Reporter;

public class MobileWrapper  extends Reporter{

	public static AppiumDriver<WebElement> driver;

	public boolean launchApp(String appPackage, String appActivity, String deviceName,String udid) {
		try {
			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setCapability("appPackage", appPackage);
			dc.setCapability("appActivity", appActivity);
			dc.setCapability("deviceName", deviceName);
			dc.setCapability("automationName", "UiAutomator2");
			dc.setCapability("noReset", true);
			dc.setCapability("udid", udid);


			driver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), dc);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
		return true;
	}

	public boolean launchBrowser(String browserName, String deviceName, String URL) {
		try {
			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setCapability("browserName", browserName);
			dc.setCapability("deviceName", deviceName);
			driver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), dc);
			driver.get(URL);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean loadURL(String URL) {
		driver.get(URL);
		return true;
	}

	public boolean verifyAndInstallApp(String appPackage, String appPath) {
		boolean bInstallSuccess = false;

		if (driver.isAppInstalled(appPackage))
			driver.removeApp(appPackage);

		driver.installApp(appPath);
		bInstallSuccess = true;

		return bInstallSuccess;
	}

	// Only for Android
	public boolean switchBetweenAppsInAndroid(String appPackage, String appActivity) {
		try {
			Activity activity = new Activity(appPackage, appActivity);
			((StartsActivity) driver).startActivity(activity);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void printContext() {
		try {
			Set<String> contexts = driver.getContextHandles();
			for (String string : contexts) {
				System.out.println(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean switchContext(String Context) {
		try {
			driver.context(Context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean switchWebview(){
		try {		
			Set<String> contextNames = driver.getContextHandles();
			for (String contextName : contextNames) {
				if (contextName.contains("WEBVIEW"))
					driver.context(contextName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean switchNativeview(){
		try {		
			Set<String> contextNames = driver.getContextHandles();
			for (String contextName : contextNames) {
				if (contextName.contains("NATIVE"))
					driver.context(contextName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean verifyText(String Expected, String locator, String locValue) {
		boolean val = false;
		WebElement ele = getWebElement(locator, locValue);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOf(ele));
			String name = ele.getText();
			if (name.contains(Expected)) {
				val = true;
			} else
				val = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public void showNotificationMenu() {
		((AndroidDriver<WebElement>) driver).openNotifications();
	}

	public void getBatteryInfoInAndroid() {
		Map<String, Object> args = new HashMap<>();
		args.put("command", "dumpsys");
		args.put("args", "battery");
		Object executeScript = driver.executeScript("mobile:shell", args);
		System.out.println(executeScript.toString());
	}

	public void sleep(int mSec) {
		try {
			Thread.sleep(mSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean pressEnter() {
		((PressesKey) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		return true;
	}

	public boolean pressBack() {
		((PressesKey) driver).pressKey(new KeyEvent(AndroidKey.BACK));
		return true;
	}

	public long takeScreenShot() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
		try {
			File srcFiler = driver.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFiler, new File("./reports/images/" + number + ".png"));
		} catch (WebDriverException e) {
			e.printStackTrace();
			System.out.println("The browser has been closed.");
		} catch (IOException e) {
			System.out.println("The snapshot could not be taken");
		}
		return number;
	}

	private WebElement getWebElement(String locator, String locValue) {
		switch (locator) {
		case "id":
			return driver.findElementById(locValue);
		case "name":
			return driver.findElementByName(locValue);
		case "className":
			return driver.findElementByClassName(locValue);
		case "link":
			return driver.findElementByLinkText(locValue);
		case "partialLink":
			return driver.findElementByPartialLinkText(locValue);
		case "tag":
			return driver.findElementByTagName(locValue);
		case "css":
			return driver.findElementByCssSelector(locValue);
		case "xpath":
			return driver.findElementByXPath(locValue);
		case "accessibilityId":
			return driver.findElementByAccessibilityId(locValue);
		case "image":
			return driver.findElementByImage(locValue);
		}
		return null;
	}

	public boolean verifyTextString(String Expected, String locator, String locValue) {

		boolean val = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOf(getWebElement(locator, locValue)));
			String name = getWebElement(locator, locValue).getText();
			if (name.contains(Expected)) {
				val = true;
			} else
				val = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	// Applicable only for Mobile Web/Browser
	public boolean scrollDownInBrowser(int val) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0," + val + "\")", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void scrollDownInBrowser() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("direction", "down");
		js.executeScript("mobile: scroll", scrollObject);
	}

	// Applicable only for Mobile Web/Browser
	public boolean navigateBackInBrowser() {
		try {
			driver.navigate().back();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean scrollFromDownToUpinAppUsingTouchActions() {
		try {
			Dimension size = driver.manage().window().getSize();
			int x1 = (int) (size.getWidth() * 0.5);
			int y1 = (int) (size.getHeight() * 0.8);
			int x0 = (int) (size.getWidth() * 0.5);
			int y0 = (int) (size.getHeight() * 0.2);
			MultiTouchAction touch = new MultiTouchAction(driver);
			touch.add(new TouchAction<>(driver)
					.press(point(x1, y1))
					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
					.moveTo(point(x0, y0)).release())
			.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void scrollFromDownToUpinAppUsingPointerInput() {
		Dimension size = driver.manage().window().getSize();
		int x1 = (int) (size.getWidth() * 0.5);
		int y1 = (int) (size.getHeight() * 0.8);
		int x0 = (int) (size.getWidth() * 0.5);
		int y0 = (int) (size.getHeight() * 0.2);
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1);
		sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x1, y1));
		sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
		sequence.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x0, y0));
		sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
		driver.perform(Arrays.asList(sequence));
	}

	public boolean scrollFromUpToDowninAppUsingTouchActions() {
		try {
			Dimension size = driver.manage().window().getSize();
			int x1 = (int) (size.getWidth() * 0.5);
			int y1 = (int) (size.getHeight() * 0.2);
			int x0 = (int) (size.getWidth() * 0.5);
			int y0 = (int) (size.getHeight() * 0.8);
			MultiTouchAction touch = new MultiTouchAction(driver);
			touch.add(new TouchAction<>(driver).press(point(x1, y1))
					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
					.moveTo(point(x0, y0))
					.release())
			.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void scrollFromUpToDowninAppUsingPointerInput() {
		Dimension size = driver.manage().window().getSize();
		int x1 = (int) (size.getWidth() * 0.5);
		int y1 = (int) (size.getHeight() * 0.8);
		int x0 = (int) (size.getWidth() * 0.5);
		int y0 = (int) (size.getHeight() * 0.2);
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1);
		sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x0, y0));
		sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
		sequence.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x1, y1));
		sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
		driver.perform(Arrays.asList(sequence));
	}

	public boolean scrollFromRightToLeftInAppUsingTouchActions() {
		try {
			Dimension size = driver.manage().window().getSize();
			int x1 = (int) (size.getWidth() * 0.8);
			int y1 = (int) (size.getHeight() * 0.5);
			int x0 = (int) (size.getWidth() * 0.2);
			int y0 = (int) (size.getHeight() * 0.5);
			MultiTouchAction touch = new MultiTouchAction(driver);
			touch.add(new TouchAction<>(driver).press(point(x1, y1))
					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
					.moveTo(point(x0, y0))
					.release())
			.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void scrollFromRightToLeftInAppUsingPointerInput() {
		Dimension size = driver.manage().window().getSize();
		int x1 = (int) (size.getWidth() * 0.8);
		int y1 = (int) (size.getHeight() * 0.5);
		int x0 = (int) (size.getWidth() * 0.2);
		int y0 = (int) (size.getHeight() * 0.5);
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1);
		sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x1, y1));
		sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
		sequence.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x0, y0));
		sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
		driver.perform(Arrays.asList(sequence));
	}

	public boolean scrollFromLeftToRightInAppUsingTouchActions() {
		try {
			Dimension size = driver.manage().window().getSize();
			int x1 = (int) (size.getWidth() * 0.8);
			int y1 = (int) (size.getHeight() * 0.5);
			int x0 = (int) (size.getWidth() * 0.2);
			int y0 = (int) (size.getHeight() * 0.5);
			MultiTouchAction touch = new MultiTouchAction(driver);
			touch.add(new TouchAction<>(driver).press(point(x0, y0))
					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
					.moveTo(point(x1, y1))
					.release())
			.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void scrollFromLeftToRightInAppUsingPointerInput() {
		Dimension size = driver.manage().window().getSize();
		int x1 = (int) (size.getWidth() * 0.8);
		int y1 = (int) (size.getHeight() * 0.5);
		int x0 = (int) (size.getWidth() * 0.2);
		int y0 = (int) (size.getHeight() * 0.5);
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1);
		sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x0, y0));
		sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
		sequence.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x1, y1));
		sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
		driver.perform(Arrays.asList(sequence));
	}

	public void pinchUsingPointerInput() {

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

		Dimension size = driver.manage().window().getSize();
		Point source = new Point(size.getWidth(), size.getHeight());

		Sequence pinchAndZoom1 = new Sequence(finger, 0);
		pinchAndZoom1.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
				source.x / 3, source.y / 3));
		pinchAndZoom1.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		pinchAndZoom1.addAction(new Pause(finger, Duration.ofMillis(100)));
		pinchAndZoom1.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(),
				source.x / 2, source.y / 2));
		pinchAndZoom1.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		Sequence pinchAndZoom2 = new Sequence(finger2, 1);
		pinchAndZoom2.addAction(finger2.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
				source.x * 3 / 4, source.y * 3 / 4));
		pinchAndZoom2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		pinchAndZoom2.addAction(new Pause(finger, Duration.ofMillis(100)));
		pinchAndZoom2.addAction(finger2.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(),
				source.x / 2, source.y / 2));
		pinchAndZoom2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		driver.perform(Arrays.asList(pinchAndZoom1, pinchAndZoom2));
	}

	// Pinch using Touch Action will not work. Known Bug.
	public void pinchUsingTouchActions() {
		Dimension size = driver.manage().window().getSize();
		Point source = new Point(size.getWidth(), size.getHeight());
		MultiTouchAction multiTouch = new MultiTouchAction(driver);
		TouchAction<?> tAction0 = new TouchAction<>(driver);
		TouchAction<?> tAction1 = new TouchAction<>(driver);
		tAction0
		.press(point(source.x / 3, source.y / 3))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
		.moveTo(point(source.x / 2, source.y / 2))
		.release();
		tAction1
		.press(point(source.x * 3 / 4, source.y * 3 / 4))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
		.moveTo(point(source.x / 2, source.y / 2))
		.release();

		multiTouch.add(tAction0).add(tAction1);
		multiTouch.perform();
	}

	public void ZoomUsingPointerInput() {

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

		Dimension size = driver.manage().window().getSize();
		Point source = new Point(size.getWidth(), size.getHeight());

		Sequence pinchAndZoom1 = new Sequence(finger, 0);
		pinchAndZoom1.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
				source.x / 2, source.y / 2));
		pinchAndZoom1.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		pinchAndZoom1.addAction(new Pause(finger, Duration.ofMillis(100)));
		pinchAndZoom1.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(),
				source.x / 3, source.y / 3));
		pinchAndZoom1.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		Sequence pinchAndZoom2 = new Sequence(finger2, 0);
		pinchAndZoom2.addAction(finger2.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
				source.x / 2, source.y / 2));
		pinchAndZoom2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		pinchAndZoom2.addAction(new Pause(finger, Duration.ofMillis(100)));
		pinchAndZoom2.addAction(finger2.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(),
				source.x * 3 / 4, source.y * 3 / 4));
		pinchAndZoom2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		driver.perform(Arrays.asList(pinchAndZoom1, pinchAndZoom2));
	}

	public void zoomUsingTouchActions() {
		Dimension size = driver.manage().window().getSize();
		Point source = new Point(size.getWidth(), size.getHeight());
		MultiTouchAction multiTouch = new MultiTouchAction(driver);
		TouchAction<?> tAction0 = new TouchAction<>(driver);
		TouchAction<?> tAction1 = new TouchAction<>(driver);
		tAction0
		.press(point(source.x / 2, source.y / 2))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
		.moveTo(point(source.x / 3, source.y / 3))
		.release();
		tAction1
		.press(point(source.x * 2, source.y * 2))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
		.moveTo(point(source.x * 3 / 4, source.y * 3 / 4))
		.release();

		multiTouch.add(tAction0).add(tAction1);
		multiTouch.perform();
	}

	private boolean eleIsDisplayed(WebElement ele) {
		try {
			if (ele.isDisplayed())
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;

	}

	public boolean scrollFromUpToDowninAppWithWebElementUsingPointerInput(String locator, String locValue) {
		try {
			WebElement ele = getWebElement(locator, locValue);
			// int startX = ele.getLocation().getX();
			int startY = ele.getLocation().getY();

			int centerX = ((MobileElement) ele).getCenter().getX();
			int centerY = ((MobileElement) ele).getCenter().getY();
			// int endX = (centerX*2)-startX;
			int endY = (centerY * 2) - startY;

			int x0 = (int) centerX;
			int y0 = (int) ((endY - startY) * 0.2) + startY;
			int x1 = (int) centerX;
			int y1 = (int) ((endY - startY) * 0.8) + startY;

			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence sequence = new Sequence(finger, 1);
			sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x0, y0));
			sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
			sequence.addAction(
					finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x1, y1));
			sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
			driver.perform(Arrays.asList(sequence));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean scrollFromDownToUpinAppWithWebElementUsingPointerInput(String locator, String locValue) {
		try {
			WebElement ele = getWebElement(locator, locValue);
			// int startX = ele.getLocation().getX();
			int startY = ele.getLocation().getY();

			int centerX = ((MobileElement) ele).getCenter().getX();
			int centerY = ((MobileElement) ele).getCenter().getY();
			// int endX = (centerX*2)-startX;
			int endY = (centerY * 2) - startY;

			int x0 = (int) centerX;
			int y0 = (int) ((endY - startY) * 0.8) + startY;
			int x1 = (int) centerX;
			int y1 = (int) ((endY - startY) * 0.2) + startY;

			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence sequence = new Sequence(finger, 1);
			sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x0, y0));
			sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
			sequence.addAction(
					finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x1, y1));
			sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
			driver.perform(Arrays.asList(sequence));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	public boolean scrollFromLeftToRightinAppWithWebElementUsingPointerInput(String locator, String locValue) {
		try {
			WebElement ele = getWebElement(locator, locValue);
			int startX = ele.getLocation().getX();
			// int startY = ele.getLocation().getY();

			int centerX = ((MobileElement) ele).getCenter().getX();
			int centerY = ((MobileElement) ele).getCenter().getY();
			int endX = (centerX * 2) - startX;
			// int endY = (centerY*2)-startY;

			int x0 = (int) ((endX - startX) * 0.2) + startX;
			int y0 = (int) centerY;
			int x1 = (int) ((endX - startX) * 0.8) + startX;
			int y1 = (int) centerY;

			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence sequence = new Sequence(finger, 1);
			sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x0, y0));
			sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
			sequence.addAction(
					finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x1, y1));
			sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
			driver.perform(Arrays.asList(sequence));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean scrollFromRightToLeftinAppWithWebElementUsingPointerInput(String locator, String locValue) {
		try {
			WebElement ele = getWebElement(locator, locValue);
			int startX = ele.getLocation().getX();
			// int startY = ele.getLocation().getY();

			int centerX = ((MobileElement) ele).getCenter().getX();
			int centerY = ((MobileElement) ele).getCenter().getY();
			int endX = (centerX * 2) - startX;
			// int endY = (centerY*2)-startY;

			int x0 = (int) ((endX - startX) * 0.8) + startX;
			int y0 = (int) centerY;
			int x1 = (int) ((endX - startX) * 0.2) + startX;
			int y1 = (int) centerY;

			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence sequence = new Sequence(finger, 1);
			sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x0, y0));
			sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
			sequence.addAction(
					finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x1, y1));
			sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
			driver.perform(Arrays.asList(sequence));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean scrollFromDownToUpinAppUsingPointerInputUntilElementIsVisible(String locator, String locValue) {
		while (!eleIsDisplayed(getWebElement(locator, locValue))) {
			scrollFromDownToUpinAppUsingPointerInput();
		}
		return true;
	}

	public boolean scrollFromUpToDowninAppUsingPointerInputUntilElementIsVisible(String locator, String locValue) {
		while (!eleIsDisplayed(getWebElement(locator, locValue))) {
			scrollFromUpToDowninAppUsingPointerInput();
		}
		return true;
	}

	public boolean clickInCoOrdinatesOfApp(int x0, int y0) {
		try {
			TouchAction<?> touch = new TouchAction<>(driver);
			touch.
			press(point(x0, y0))
			.release()
			.perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void resetApp() {
		driver.resetApp();
	}

	public void closeApp() {
		if (driver != null) {
			try {
				driver.closeApp();
			} catch (Exception e) {
			}
		}

	}

	// Only for Android
	public boolean WiFiOffInAndorid() {
		((HasNetworkConnection) driver).setConnection(new ConnectionStateBuilder().withAirplaneModeEnabled().build());
		return true;
	}

	// Only for Android
	public boolean WiFiOnInAndroid() {
		((HasNetworkConnection) driver).setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
		return true;
	}

	public boolean setPortraitOrientation() {
		driver.rotate(ScreenOrientation.PORTRAIT);
		return true;
	}

	public boolean setLanscapeOrientation() {
		driver.rotate(ScreenOrientation.LANDSCAPE);
		return true;
	}

	public void hideKeyboard() {
		try {
			driver.hideKeyboard();
		} catch (Exception e) {}
	}

	private String getOrientation() {
		return driver.getOrientation().toString();
	}

	public boolean enterValue(String data, String locator, String locValue ) {
		WebElement ele = getWebElement(locator, locValue);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(ele));
		ele.clear();
		ele.sendKeys(data);
		return true;
	}

	public boolean click(String locator, String locValue) {
		try {
			WebElement ele = getWebElement(locator, locValue);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			ele.click();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String getText(String locator, String locValue) {
		WebElement ele = getWebElement(locator, locValue);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOf(ele));
		return ele.getText();
	}

}
