import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EventHistoryComponentsPage, EventHistoryDeleteDialog, EventHistoryUpdatePage } from './event-history.page-object';

const expect = chai.expect;

describe('EventHistory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let eventHistoryComponentsPage: EventHistoryComponentsPage;
  let eventHistoryUpdatePage: EventHistoryUpdatePage;
  let eventHistoryDeleteDialog: EventHistoryDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load EventHistories', async () => {
    await navBarPage.goToEntity('event-history');
    eventHistoryComponentsPage = new EventHistoryComponentsPage();
    await browser.wait(ec.visibilityOf(eventHistoryComponentsPage.title), 5000);
    expect(await eventHistoryComponentsPage.getTitle()).to.eq('itcenterbazaApp.eventHistory.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(eventHistoryComponentsPage.entities), ec.visibilityOf(eventHistoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create EventHistory page', async () => {
    await eventHistoryComponentsPage.clickOnCreateButton();
    eventHistoryUpdatePage = new EventHistoryUpdatePage();
    expect(await eventHistoryUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.eventHistory.home.createOrEditLabel');
    await eventHistoryUpdatePage.cancel();
  });

  it('should create and save EventHistories', async () => {
    const nbButtonsBeforeCreate = await eventHistoryComponentsPage.countDeleteButtons();

    await eventHistoryComponentsPage.clickOnCreateButton();

    await promise.all([
      eventHistoryUpdatePage.typeSelectLastOption(),
      eventHistoryUpdatePage.setTextInput('text'),
      eventHistoryUpdatePage.setTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      eventHistoryUpdatePage.centerSelectLastOption(),
      eventHistoryUpdatePage.userSelectLastOption(),
      // eventHistoryUpdatePage.openedUserSelectLastOption(),
    ]);

    expect(await eventHistoryUpdatePage.getTextInput()).to.eq('text', 'Expected Text value to be equals to text');
    expect(await eventHistoryUpdatePage.getTimeInput()).to.contain('2001-01-01T02:30', 'Expected time value to be equals to 2000-12-31');

    await eventHistoryUpdatePage.save();
    expect(await eventHistoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await eventHistoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last EventHistory', async () => {
    const nbButtonsBeforeDelete = await eventHistoryComponentsPage.countDeleteButtons();
    await eventHistoryComponentsPage.clickOnLastDeleteButton();

    eventHistoryDeleteDialog = new EventHistoryDeleteDialog();
    expect(await eventHistoryDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.eventHistory.delete.question');
    await eventHistoryDeleteDialog.clickOnConfirmButton();

    expect(await eventHistoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
