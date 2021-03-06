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

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ScreenshotRule extends TestWatcher {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScreenshotRule.class);
	
	private final WebDriver driver;

	@Autowired
	public ScreenshotRule(WebDriver driver) {
		this.driver = checkNotNull(driver, "driver");
	}
	
	@Override
	public void failed(Throwable exception, Description description) {
		if (driver instanceof TakesScreenshot) {
			screenshot((TakesScreenshot) driver, getOutputFile(description));
		}
		else {
			LOG.error("WebDriver cannot take screenshot: {}", driver);
		}
	}
	
	private static File getOutputFile(Description description) {
		String pathname = String.format("target/test-screenshots/%s.%s.png", description.getClassName(),
			description.getMethodName());
		
		return new File(pathname);
	}
	
	private static void screenshot(TakesScreenshot driver, File outputFile) {
		File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
		
		if (!outputFile.getParentFile().mkdirs() && !outputFile.getParentFile().exists()) {
			LOG.error("Unable to create directory for screenshot: {}", outputFile.getParentFile().getAbsolutePath());
		}
		
		try {
			Files.move(screenshotFile, outputFile);
		}
		catch (IOException exception) {
			LOG.error("Cannot move screenshot", exception);
		}
	}
}
