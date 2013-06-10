package net.thucydidesfragments;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Predicate;

public class CheckBox extends Fragment {

	public void set(final boolean state){
		publishEvent(state ? "checking" : "unchecking");
		
		new FluentWait<WebElement>(this)
			.pollingEvery(500, TimeUnit.MILLISECONDS)
			.withTimeout(FragmentSettings.getInstance().getImplicitWait(), TimeUnit.MILLISECONDS)
			.ignoring(WebDriverException.class)
			.until(new Predicate<WebElement>(){
				public boolean apply(WebElement element){
					if(state == element.isSelected()){ return true; }
					
					element.click();
					
					return state == element.isSelected();
				}
			});
	}
}
