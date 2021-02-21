import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RegionsComponentsPage, RegionsDeleteDialog, RegionsUpdatePage } from './regions.page-object';

const expect = chai.expect;

describe('Regions e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let regionsComponentsPage: RegionsComponentsPage;
  let regionsUpdatePage: RegionsUpdatePage;
  let regionsDeleteDialog: RegionsDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Regions', async () => {
    await navBarPage.goToEntity('regions');
    regionsComponentsPage = new RegionsComponentsPage();
    await browser.wait(ec.visibilityOf(regionsComponentsPage.title), 5000);
    expect(await regionsComponentsPage.getTitle()).to.eq('itcenterbazaApp.regions.home.title');
    await browser.wait(ec.or(ec.visibilityOf(regionsComponentsPage.entities), ec.visibilityOf(regionsComponentsPage.noResult)), 1000);
  });

  it('should load create Regions page', async () => {
    await regionsComponentsPage.clickOnCreateButton();
    regionsUpdatePage = new RegionsUpdatePage();
    expect(await regionsUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.regions.home.createOrEditLabel');
    await regionsUpdatePage.cancel();
  });

  it('should create and save Regions', async () => {
    const nbButtonsBeforeCreate = await regionsComponentsPage.countDeleteButtons();

    await regionsComponentsPage.clickOnCreateButton();

    await promise.all([
      regionsUpdatePage.setTitleInput('title'),
      regionsUpdatePage.setInfoInput('info'),
      regionsUpdatePage.setGoogleUrlInput('googleUrl'),
      regionsUpdatePage.directorSelectLastOption(),
    ]);

    expect(await regionsUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await regionsUpdatePage.getInfoInput()).to.eq('info', 'Expected Info value to be equals to info');
    expect(await regionsUpdatePage.getGoogleUrlInput()).to.eq('googleUrl', 'Expected GoogleUrl value to be equals to googleUrl');

    await regionsUpdatePage.save();
    expect(await regionsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await regionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Regions', async () => {
    const nbButtonsBeforeDelete = await regionsComponentsPage.countDeleteButtons();
    await regionsComponentsPage.clickOnLastDeleteButton();

    regionsDeleteDialog = new RegionsDeleteDialog();
    expect(await regionsDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.regions.delete.question');
    await regionsDeleteDialog.clickOnConfirmButton();

    expect(await regionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
