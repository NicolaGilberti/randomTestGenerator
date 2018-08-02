

import custom_classes.BoardNames;
import custom_classes.Id;
import custom_classes.IdeasPosts;
import custom_classes.NotWentWellPosts;
import custom_classes.PeopleNames;
import custom_classes.WentWellPosts;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import po.home.pages.HomeContainerPage;
import po.login.pages.LoginContainerPage;
import po.retrospective.pages.RetrospectiveContainerPage;
import po.retrospective.pages.modals.MenuPage;
import po_utils.DriverProvider;
import po_utils.NotInTheRightPageObjectException;
import po_utils.NotTheRightInputValuesException;


public class Class000 {
	private Object currentPage = null;

	public Class000() {
		WebDriver driver = DriverProvider.getActiveDriver();
		this.currentPage = new LoginContainerPage(driver);
	}

	public void createNewSessionHomeContainerPage() {
		if ((this.currentPage) instanceof HomeContainerPage) {
			HomeContainerPage page = ((HomeContainerPage) (this.currentPage));
			page.tabComponent.clickCreate();
			page.homeComponent.clickCreateSession();
			this.currentPage = new RetrospectiveContainerPage(page.homeComponent.getDriver());
		}else {
			throw new NotInTheRightPageObjectException(("createNewSessionHomeContainerPage: expected po.home.pages.HomeContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void goToBoardHomeContainerPage(BoardNames boardName) {
		if ((this.currentPage) instanceof HomeContainerPage) {
			HomeContainerPage page = ((HomeContainerPage) (this.currentPage));
			if ((page.tabComponent.isPreviousTabActive()) && (page.homeComponent.isBoardPresent(boardName.value()))) {
				page.homeComponent.clickOnBoard(boardName.value());
				this.currentPage = new RetrospectiveContainerPage(page.tabComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("goToBoard: previous tab is not active or board " + (boardName.value())) + " is not present"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("goToBoardHomeContainerPage: expected po.home.pages.HomeContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void goToHomeHomeContainerPage() {
		if ((this.currentPage) instanceof HomeContainerPage) {
			HomeContainerPage page = ((HomeContainerPage) (this.currentPage));
			page.navbarComponent.clickHomeLink();
			this.currentPage = new HomeContainerPage(page.navbarComponent.getDriver());
		}else {
			throw new NotInTheRightPageObjectException(("goToHomeHomeContainerPage: expected po.home.pages.HomeContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void goToPreviousViewHomeContainerPage() {
		if ((this.currentPage) instanceof HomeContainerPage) {
			HomeContainerPage page = ((HomeContainerPage) (this.currentPage));
			if (page.tabComponent.isPreviousTabPresent()) {
				page.tabComponent.clickPrevious();
				this.currentPage = new HomeContainerPage(page.tabComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException("goToPreviousView: previous tab is not present");
			}
		}else {
			throw new NotInTheRightPageObjectException(("goToPreviousViewHomeContainerPage: expected po.home.pages.HomeContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void logoutHomeContainerPage() {
		if ((this.currentPage) instanceof HomeContainerPage) {
			HomeContainerPage page = ((HomeContainerPage) (this.currentPage));
			page.tabComponent.clickAdvanced();
			page.homeComponent.clickLogout();
			this.currentPage = new LoginContainerPage(page.homeComponent.getDriver());
		}else {
			throw new NotInTheRightPageObjectException(("logoutHomeContainerPage: expected po.home.pages.HomeContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void leaveSessionMenuPage() {
		if ((this.currentPage) instanceof MenuPage) {
			MenuPage page = ((MenuPage) (this.currentPage));
			page.clickOn(By.xpath("((//body/div)[2]/div//button)[1]"));
			page.clickOutsideTheModal();
			this.currentPage = new HomeContainerPage(page.getDriver());
		}else {
			throw new NotInTheRightPageObjectException(("leaveSessionMenuPage: expected po.retrospective.pages.modals.MenuPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void logoutMenuPage() {
		if ((this.currentPage) instanceof MenuPage) {
			MenuPage page = ((MenuPage) (this.currentPage));
			page.clickOn(By.xpath("((//body/div)[2]/div//button)[2]"));
			page.clickOutsideTheModal();
			this.currentPage = new LoginContainerPage(page.getDriver());
		}else {
			throw new NotInTheRightPageObjectException(("logoutMenuPage: expected po.retrospective.pages.modals.MenuPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void toggleSummaryModeMenuPage() {
		if ((this.currentPage) instanceof MenuPage) {
			MenuPage page = ((MenuPage) (this.currentPage));
			if (!(page.isSummaryModeOn())) {
				page.clickOn(By.xpath("(//body/div)[2]/div//input[@label=\"Summary Mode\"]"));
				page.clickOutsideTheModal();
				this.currentPage = new RetrospectiveContainerPage(page.getDriver());
			}else {
				throw new NotTheRightInputValuesException("toggleSummaryMode: summary mode is already on");
			}
		}else {
			throw new NotInTheRightPageObjectException(("toggleSummaryModeMenuPage: expected po.retrospective.pages.modals.MenuPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void untoggleSummaryModeMenuPage() {
		if ((this.currentPage) instanceof MenuPage) {
			MenuPage page = ((MenuPage) (this.currentPage));
			if (page.isSummaryModeOn()) {
				page.clickOn(By.xpath("(//body/div)[2]/div//input[@label=\"Summary Mode\"]"));
				page.clickOutsideTheModal();
				this.currentPage = new RetrospectiveContainerPage(page.getDriver());
			}else {
				throw new NotTheRightInputValuesException("toggleSummaryMode: summary mode is off");
			}
		}else {
			throw new NotInTheRightPageObjectException(("untoggleSummaryModeMenuPage: expected po.retrospective.pages.modals.MenuPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void changeIdeasPostContentRetrospectiveContainerPage(Id id, IdeasPosts ideasPost) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isIdeasPostPresent(id.value))) && (!(page.retrospectiveComponent.isIdeasPostShared(id.value)))) {
				page.retrospectiveComponent.changeContentOfIdeasPost(id.value, ideasPost.value());
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("changeIdeasPostContent: summary mode is on or post with id " + (id.value)) + " is not present or it is shared"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("changeIdeasPostContentRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void changeNotWentWellPostContentRetrospectiveContainerPage(Id id, NotWentWellPosts notWentWellPost) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isNotWentWellPostPresent(id.value))) && (!(page.retrospectiveComponent.isNotWentWellPostShared(id.value)))) {
				page.retrospectiveComponent.changeContentOfNotWentWellPost(id.value, notWentWellPost.value());
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("changeNotWentWellPostContent: summary mode is on or post with id " + (id.value)) + " is not present post or it is shared"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("changeNotWentWellPostContentRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void changeWentWellPostContentRetrospectiveContainerPage(Id id, WentWellPosts wentWellPost) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isWentWellPostPresent(id.value))) && (!(page.retrospectiveComponent.isWentWellPostShared(id.value)))) {
				page.retrospectiveComponent.changeContentOfWentWellPost(id.value, wentWellPost.value());
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("changeWentWellPostContent: summary mode is on or post with id " + (id.value)) + " is not present or it is shared"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("changeWentWellPostContentRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void createIdeasPostRetrospectiveContainerPage(IdeasPosts ideasPost) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (!(page.retrospectiveComponent.isSummaryModeOn())) {
				page.retrospectiveComponent.typeIdeasPost(ideasPost.value());
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException("createIdeasPost: summary mode is on");
			}
		}else {
			throw new NotInTheRightPageObjectException(("createIdeasPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void createNotWentWellPostRetrospectiveContainerPage(NotWentWellPosts notWentWellPost) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (!(page.retrospectiveComponent.isSummaryModeOn())) {
				page.retrospectiveComponent.typeNotWentWellPost(notWentWellPost.value());
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException("createNotWentWellPost: summary mode is on");
			}
		}else {
			throw new NotInTheRightPageObjectException(("createNotWentWellPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void createWentWellPostRetrospectiveContainerPage(WentWellPosts wentWellPost) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (!(page.retrospectiveComponent.isSummaryModeOn())) {
				page.retrospectiveComponent.typeWentWellPost(wentWellPost.value());
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException("createWentWellPost: summary mode is on");
			}
		}else {
			throw new NotInTheRightPageObjectException(("createWentWellPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void deleteIdeasPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isIdeasPostPresent(id.value))) && (!(page.retrospectiveComponent.isIdeasPostShared(id.value)))) {
				page.retrospectiveComponent.deleteIdeasPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("deleteIdeasPost: summary mode is on or post with id " + (id.value)) + " is not present or it is shared"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("deleteIdeasPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void deleteNotWentWellPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isNotWentWellPostPresent(id.value))) && (!(page.retrospectiveComponent.isNotWentWellPostShared(id.value)))) {
				page.retrospectiveComponent.deleteNotWentWellPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("deleteNotWentWellPost: summary mode is on or post with id " + (id.value)) + " is not present or it is shared"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("deleteNotWentWellPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void deleteWentWellPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if (((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isWentWellPostPresent(id.value))) && (!(page.retrospectiveComponent.isWentWellPostShared(id.value)))) {
				page.retrospectiveComponent.deleteWentWellPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("deleteWentWellPost: summary mode is on or post with id " + (id.value)) + " is not present or it is shared"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("deleteWentWellPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void dislikeIdeasPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if ((((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isIdeasPostPresent(id.value))) && (page.retrospectiveComponent.isIdeasPostShared(id.value))) && (page.retrospectiveComponent.isIdeasPostEnabled(id.value))) {
				page.retrospectiveComponent.dislikeIdeasPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("dislikeIdeasPost: summary mode is on or post with id " + (id.value)) + " is not present or it is not shared or it is not enabled"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("dislikeIdeasPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void dislikeNotWentWellPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if ((((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isNotWentWellPostPresent(id.value))) && (page.retrospectiveComponent.isNotWentWellPostShared(id.value))) && (page.retrospectiveComponent.isNotWentWellPostEnabled(id.value))) {
				page.retrospectiveComponent.dislikeNotWentWellPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("dislikeNotWentWellPost: summary mode is on or post with id " + (id.value)) + " is not present or it is not shared or it is not enabled"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("dislikeNotWentWellPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void dislikeWentWellPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if ((((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isWentWellPostPresent(id.value))) && (page.retrospectiveComponent.isWentWellPostShared(id.value))) && (page.retrospectiveComponent.isWentWellPostEnabled(id.value))) {
				page.retrospectiveComponent.dislikeWentWellPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("dislikeWentWellPost: summary mode is on or post with id " + (id.value)) + " is not present or it is not shared or it is not enabled"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("dislikeWentWellPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void goToHomeRetrospectiveContainerPage() {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			page.navbarComponent.clickHomeLink();
			this.currentPage = new HomeContainerPage(page.navbarComponent.getDriver());
		}else {
			throw new NotInTheRightPageObjectException(("goToHomeRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void likeIdeasPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if ((((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isIdeasPostPresent(id.value))) && (page.retrospectiveComponent.isIdeasPostShared(id.value))) && (page.retrospectiveComponent.isIdeasPostEnabled(id.value))) {
				page.retrospectiveComponent.likeIdeasPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("likeIdeasPost: summary mode is on or post with id " + (id.value)) + " is not present or it is not shared or it is not enabled"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("likeIdeasPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void likeNotWentWellPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if ((((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isNotWentWellPostPresent(id.value))) && (page.retrospectiveComponent.isNotWentWellPostShared(id.value))) && (page.retrospectiveComponent.isNotWentWellPostEnabled(id.value))) {
				page.retrospectiveComponent.likeNotWentWellPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("likeNotWentWellPost: summary mode is on or post with id " + (id.value)) + " is not present or it is not shared or it is not enabled"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("likeNotWentWellPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void likeWentWellPostRetrospectiveContainerPage(Id id) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			if ((((!(page.retrospectiveComponent.isSummaryModeOn())) && (page.retrospectiveComponent.isWentWellPostPresent(id.value))) && (page.retrospectiveComponent.isWentWellPostShared(id.value))) && (page.retrospectiveComponent.isWentWellPostEnabled(id.value))) {
				page.retrospectiveComponent.likeWentWellPost(id.value);
				this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
			}else {
				throw new NotTheRightInputValuesException((("likeWentWellPost: summary mode is on or post with id " + (id.value)) + " is not present or it is not shared or it is not enabled"));
			}
		}else {
			throw new NotInTheRightPageObjectException(("likeWentWellPostRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void openMenuRetrospectiveContainerPage() {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			page.navbarComponent.clickOpenMenu();
			this.currentPage = new MenuPage(page.navbarComponent.getDriver());
		}else {
			throw new NotInTheRightPageObjectException(("openMenuRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void renameBoardRetrospectiveContainerPage(BoardNames newBoardName) {
		if ((this.currentPage) instanceof RetrospectiveContainerPage) {
			RetrospectiveContainerPage page = ((RetrospectiveContainerPage) (this.currentPage));
			page.retrospectiveComponent.renameBoard(newBoardName.value());
			this.currentPage = new RetrospectiveContainerPage(page.retrospectiveComponent.getDriver());
		}else {
			throw new NotInTheRightPageObjectException(("renameBoardRetrospectiveContainerPage: expected po.retrospective.pages.RetrospectiveContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}

	public void loginLoginContainerPage(PeopleNames peopleName) {
		if ((this.currentPage) instanceof LoginContainerPage) {
			LoginContainerPage page = ((LoginContainerPage) (this.currentPage));
			page.loginComponent.typeName(peopleName.name());
			page.loginComponent.clickStart();
			long timeout = 100;
			if (page.loginComponent.waitForElementBeingPresentOnPage(By.xpath("//button[text()=\"Create a new session\"]"), timeout, TimeUnit.MILLISECONDS)) {
				this.currentPage = new HomeContainerPage(page.loginComponent.getDriver());
			}else {
				this.currentPage = new RetrospectiveContainerPage(page.loginComponent.getDriver());
			}
		}else {
			throw new NotInTheRightPageObjectException(("loginLoginContainerPage: expected po.login.pages.LoginContainerPage, found " + (this.currentPage.getClass().getSimpleName())));
		}
	}
}

