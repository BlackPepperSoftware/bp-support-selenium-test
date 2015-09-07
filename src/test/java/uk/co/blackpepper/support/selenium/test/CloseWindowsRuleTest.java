/*
 * Copyright 2014 Black Pepper Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
