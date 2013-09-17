package com.astound.fragments.elements;

import com.astound.fragments.context.FragmentContext;
import com.astound.fragments.events.Publish;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Select extends Fragment {

    public Select(FragmentContext fragmentContext) {
        super(fragmentContext);
    }

    private org.openqa.selenium.support.ui.Select delegate() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement());
    }

    @Publish(format = "deselecting all options")
    public void deselectAll() {
        delegate().deselectAll();
    }

    @Publish(format = "deselecting option with index [{1}]")
    public void deselectByIndex(int index) {
        delegate().selectByIndex(index);
    }

    @Publish(format = "deselecting option with value [{1}]")
    public void deselectByValue(String value) {
        delegate().deselectByValue(value);
    }

    @Publish(format = "deselecting option with visible text [{1}]")
    public void deselectByVisibleText(String text) {
        delegate().deselectByVisibleText(text);
    }

    @Publish(format = "all selected options are [{return}]")
    public <E extends Fragment> Collection<E> getAllSelectedOptions(Class<E> eClass) {
        return findFragments(new By() {
            @Override public List<WebElement> findElements(SearchContext searchContext) {
                return delegate().getAllSelectedOptions();
            }
        }, eClass, getName() + "-selected-option-");
    }

    @Publish(format = "first selected option is [{return}]")
    public <E extends Fragment> E getFirstSelectedOption(Class<E> aClass) {
        return (E) findFragment(new By() {
            @Override public WebElement findElement(SearchContext context) {
                return delegate().getFirstSelectedOption();
            }

            @Override public List<WebElement> findElements(SearchContext searchContext) {
                return Arrays.asList(delegate().getFirstSelectedOption());
            }
        }, aClass, getName() + "-selected-option-1");
    }

    @Publish(format = "selected options text is [{return}]")
    public List<String> getSelectedOptionsText() {
        List<WebElement> elements = delegate().getAllSelectedOptions();

        List<String> options = new ArrayList<>();

        for (WebElement element : elements) {
            options.add(element.getText());
        }

        return options;
    }

    @Publish(format = "options text is [{return}]")
    public List<String> getOptionsText() {
        List<WebElement> elements = delegate().getOptions();

        List<String> options = new ArrayList<>();

        for (WebElement element : elements) {
            options.add(element.getText());
        }

        return options;
    }

    @Publish(format = "options are [{return}]")
    public <E extends Fragment> List<E> getOptions(Class<E> aClass) {
        return findFragments(new By() {
            @Override public List<WebElement> findElements(SearchContext searchContext) {
                return delegate().getOptions();
            }
        }, aClass, getName() + "-option");
    }

    @Publish(format = "select is multiple == [{return}]")
    public boolean isMultiple() {
        return delegate().isMultiple();
    }

    @Publish(format = "selecting option with index [{1}]")
    public void selectByIndex(int index) {
        delegate().selectByIndex(index);
    }

    @Publish(format = "selecting option with value [{1}]")
    public void selectByValue(String value) {
        delegate().selectByValue(value);
    }

    @Publish(format = "selecting element with visible text [{1}]")
    public void selectByVisibleText(String text) {
        delegate().selectByVisibleText(text);
    }

    @Publish(format = "selecting element which contains [{1}]")
    public void selectByPartialText(String text) {
        findFragment(By.xpath(String.format("./option[contains(text(), '%s')]", text))).jsClick();
    }
}
