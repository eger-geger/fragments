package com.astound.fragments.tests;

import com.astound.fragments.HtmlUnitTests;
import com.astound.fragments.elements.Fragment;
import com.astound.fragments.pages.WikiPage;
import com.astound.fragments.parts.CollapsibleMenu;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class PageContextTests extends HtmlUnitTests {

    private WikiPage wikiPage;

    @BeforeClass public void openWikiPage() throws Exception {
        navigateToWiki();
        wikiPage = createPage(WikiPage.class);
    }

    @Test public void shouldFindWebElementOnPage() {
        WebElement webElement = wikiPage.findElement(By.tagName("a"));

        assertThat("Failed to find web element", webElement, notNullValue());
    }

    @Test public void shouldFindFragmentOnPage() {
        Fragment fragment = wikiPage.findFragment(By.tagName("div"));

        assertThat("Failed to find fragment", fragment, notNullValue());
    }

    @Test public void shouldInitCustomFragment() {
        CollapsibleMenu menu = wikiPage.findFragment(By.tagName("div"), CollapsibleMenu.class);

        assertThat("Failed to find custom fragment", menu, notNullValue());
    }

}
