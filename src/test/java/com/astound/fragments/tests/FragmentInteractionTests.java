package com.astound.fragments.tests;

import com.astound.fragments.FireFoxTests;
import com.astound.fragments.elements.Fragment;
import com.astound.fragments.pages.WikiPage;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class FragmentInteractionTests extends FireFoxTests {

    private Fragment bodyFragment;

    @BeforeClass public void initBodyFragment() throws Exception {
        bodyFragment = createPage(WikiPage.class).findFragment(By.tagName("body"));
    }

    @BeforeMethod public void openWikiPage() {
        navigateToWiki();
    }

    @Test public void shouldClickOnFragment() {
        bodyFragment.click();
    }

    @Test public void shouldRetrieveHtml() {
        assertThat("Failed to retrieve HTML", bodyFragment.getHtml(), notNullValue());
    }

    @Test public void shouldRetrieveTextContent() {
        assertThat("Failed to retrieve TextContent", bodyFragment.getTextContent(), notNullValue());
    }


    @Test public void shouldClickWitJs() {
        bodyFragment.jsClick();
    }
}
