package uk.co.blackpepper.support.selenium.test;

import java.util.Set;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.google.common.collect.Sets.newHashSet;

public class CloseWindowsRuleTest {

	@Test
	public void afterClosesOtherWindow() {
		WebDriver driver1 = mockWebDriverWithWindowHandles("x", "y");
		WebDriver driver2 = mock(WebDriver.class);
		when(driver1.switchTo().window("y")).thenReturn(driver2);
		
		new CloseWindowsRule(driver1).after();
		
		verify(driver2).close();
	}
	
	@Test
	public void afterClosesOtherWindows() {
		WebDriver driver1 = mockWebDriverWithWindowHandles("x", "y", "z");
		WebDriver driver2 = mock(WebDriver.class);
		when(driver1.switchTo().window("y")).thenReturn(driver2);
		WebDriver driver3 = mock(WebDriver.class);
		when(driver1.switchTo().window("z")).thenReturn(driver3);
		
		new CloseWindowsRule(driver1).after();
		
		verify(driver2).close();
		verify(driver3).close();
	}

	@Test
	public void afterSwitchesToOriginalWindow() {
		WebDriver driver = mockWebDriverWithWindowHandles("x");
		
		new CloseWindowsRule(driver).after();
		
		verify(driver.switchTo()).window("x");
	}
	
	private static WebDriver mockWebDriver() {
		WebDriver webDriver = mock(WebDriver.class);
		when(webDriver.switchTo()).thenReturn(mock(TargetLocator.class));
		return webDriver;
	}

	private static WebDriver mockWebDriverWithWindowHandles(String windowHandle, String... otherWindowHandles) {
		Set<String> windowHandles = newHashSet(otherWindowHandles);
		windowHandles.add(windowHandle);
		
		WebDriver webDriver = mockWebDriver();
		when(webDriver.getWindowHandle()).thenReturn(windowHandle);
		when(webDriver.getWindowHandles()).thenReturn(windowHandles);
		return webDriver;
	}
}
