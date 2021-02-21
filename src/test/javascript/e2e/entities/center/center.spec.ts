import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CenterComponentsPage, CenterDeleteDialog, CenterUpdatePage } from './center.page-object';

const expect = chai.expect;

describe('Center e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let centerComponentsPage: CenterComponentsPage;
  let centerUpdatePage: CenterUpdatePage;
  let centerDeleteDialog: CenterDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Centers', async () => {
    await navBarPage.goToEntity('center');
    centerComponentsPage = new CenterComponentsPage();
    await browser.wait(ec.visibilityOf(centerComponentsPage.title), 5000);
    expect(await centerComponentsPage.getTitle()).to.eq('itcenterbazaApp.center.home.title');
    await browser.wait(ec.or(ec.visibilityOf(centerComponentsPage.entities), ec.visibilityOf(centerComponentsPage.noResult)), 1000);
  });

  it('should load create Center page', async () => {
    await centerComponentsPage.clickOnCreateButton();
    centerUpdatePage = new CenterUpdatePage();
    expect(await centerUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.center.home.createOrEditLabel');
    await centerUpdatePage.cancel();
  });

  it('should create and save Centers', async () => {
    const nbButtonsBeforeCreate = await centerComponentsPage.countDeleteButtons();

    await centerComponentsPage.clickOnCreateButton();

    await promise.all([
      centerUpdatePage.setTitleInput('title'),
      centerUpdatePage.setInfoInput('info'),
      centerUpdatePage.setStartDateInput('2000-12-31'),
      centerUpdatePage.setGoogleMapUrlInput('googleMapUrl'),
      centerUpdatePage.modifiedBySelectLastOption(),
      centerUpdatePage.regionsSelectLastOption(),
      centerUpdatePage.managerSelectLastOption(),
    ]);

    expect(await centerUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await centerUpdatePage.getInfoInput()).to.eq('info', 'Expected Info value to be equals to info');
    expect(await centerUpdatePage.getStartDateInput()).to.eq('2000-12-31', 'Expected startDate value to be equals to 2000-12-31');
    expect(await centerUpdatePage.getGoogleMapUrlInput()).to.eq('googleMapUrl', 'Expected GoogleMapUrl value to be equals to googleMapUrl');

    await centerUpdatePage.save();
    expect(await centerUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await centerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Center', async () => {
    const nbButtonsBeforeDelete = await centerComponentsPage.countDeleteButtons();
    await centerComponentsPage.clickOnLastDeleteButton();

    centerDeleteDialog = new CenterDeleteDialog();
    expect(await centerDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.center.delete.question');
    await centerDeleteDialog.clickOnConfirmButton();

    expect(await centerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
