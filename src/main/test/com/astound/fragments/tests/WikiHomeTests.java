package com.astound.fragments.tests;

import com.astound.fragments.pages.WikiHomePage;
import com.astound.fragments.pages.WikiPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WikiHomeTests extends BaseTest {

	@BeforeClass public void openWikiPage(){
		navigateToWiki();
	}

	@Test public void shouldOpenHomePage() throws Exception {
		WikiPage wikiPage = createPage(WikiPage.class);

		wikiPage.openHomePage();

		WikiHomePage wikiHomePage = createPage(WikiHomePage.class);

		Assert.assertTrue(wikiHomePage.isValidPage());
	}

}
