package uk.co.blackpepper.support.selenium.test;

import java.util.Set;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class CloseWindowsRule extends ExternalResource {
	
	private final WebDriver driver;

	@Autowired
	public CloseWindowsRule(WebDriver driver) {
		this.driver = driver;
	}
	
	@Override
	protected void after() {
		String originalWindowHandle = driver.getWindowHandle();
		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(originalWindowHandle);
		
		for (String windowHandle : windowHandles) {
			driver.switchTo().window(windowHandle).close();
		}
		
		driver.switchTo().window(originalWindowHandle);
	}
}
