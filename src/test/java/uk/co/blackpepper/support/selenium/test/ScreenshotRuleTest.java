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

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import uk.co.blackpepper.support.logback.test.LogbackRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.runner.Description.createTestDescription;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScreenshotRuleTest {

	private interface TakesScreenshotWebDriver extends WebDriver, TakesScreenshot {
		// composite interface for tests
	}
	
	private LogbackRule logbackRule = new LogbackRule();
	
	private ExpectedException thrown = ExpectedException.none();
	
	@Rule
	public LogbackRule getLogbackRule() {
		return logbackRule;
	}
	
	@Rule
	public ExpectedException getThrown() {
		return thrown;
	}
	
	@Test
	public void constructorWithNullDriverThrowsException() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("driver");
		
		new ScreenshotRule(null);
	}
	
	@Test
	public void failedStoresScreenshot() throws IOException {
		TakesScreenshotWebDriver driver = mock(TakesScreenshotWebDriver.class);
		when(driver.getScreenshotAs(OutputType.FILE)).thenReturn(File.createTempFile("screenshot", "png"));
		ScreenshotRule rule = new ScreenshotRule(driver);
		
		rule.failed(new Exception(), createTestDescription("x", "y"));
		
		assertThat(new File("target/test-screenshots/x.y.png").exists(), is(true));
	}
	
	@Test
	public void failedWhenNotTakesScreenshotDoesNotStoreScreenshot() {
		ScreenshotRule rule = new ScreenshotRule(mock(WebDriver.class));
		logbackRule.off(ScreenshotRule.class);

		rule.failed(new Exception(), createTestDescription("x", "z"));
		
		assertThat(new File("target/test-screenshots/x.z.png").exists(), is(false));
	}
}
