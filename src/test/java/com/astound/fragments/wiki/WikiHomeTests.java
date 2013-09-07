package com.astound.fragments.wiki;

import com.astound.fragments.FireFoxTests;
import com.astound.fragments.pages.WikiHomePage;
import com.astound.fragments.pages.WikiPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class WikiHomeTests extends FireFoxTests {

	@BeforeClass public void openWikiPage() {
		navigateToWiki();
	}

	@Test public void shouldOpenHomePage() throws Exception {
		WikiPage wikiPage = createPage(WikiPage.class);

		wikiPage.openHomePage();

		WikiHomePage wikiHomePage = createPage(WikiHomePage.class);

		Assert.assertTrue(wikiHomePage.isValidPage());
	}

	@Test public void shouldCloseParticipationMenu() {
		WikiHomePage wikiHomePage = createPage(WikiHomePage.class);

		wikiHomePage.participationMenu.hide();

		assertThat("menu was not closed", wikiHomePage.participationMenu.isClosed());
	}

}
