import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RegisteredComponentsPage, RegisteredDeleteDialog, RegisteredUpdatePage } from './registered.page-object';

const expect = chai.expect;

describe('Registered e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let registeredComponentsPage: RegisteredComponentsPage;
  let registeredUpdatePage: RegisteredUpdatePage;
  let registeredDeleteDialog: RegisteredDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Registereds', async () => {
    await navBarPage.goToEntity('registered');
    registeredComponentsPage = new RegisteredComponentsPage();
    await browser.wait(ec.visibilityOf(registeredComponentsPage.title), 5000);
    expect(await registeredComponentsPage.getTitle()).to.eq('itcenterbazaApp.registered.home.title');
    await browser.wait(ec.or(ec.visibilityOf(registeredComponentsPage.entities), ec.visibilityOf(registeredComponentsPage.noResult)), 1000);
  });

  it('should load create Registered page', async () => {
    await registeredComponentsPage.clickOnCreateButton();
    registeredUpdatePage = new RegisteredUpdatePage();
    expect(await registeredUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.registered.home.createOrEditLabel');
    await registeredUpdatePage.cancel();
  });

  it('should create and save Registereds', async () => {
    const nbButtonsBeforeCreate = await registeredComponentsPage.countDeleteButtons();

    await registeredComponentsPage.clickOnCreateButton();

    await promise.all([
      registeredUpdatePage.setFirstNameInput('firstName'),
      registeredUpdatePage.setLastNameInput('lastName'),
      registeredUpdatePage.setMiddleNameInput('middleName'),
      registeredUpdatePage.setEmailInput('email'),
      registeredUpdatePage.setDateOfBirthInput('2000-12-31'),
      registeredUpdatePage.genderSelectLastOption(),
      registeredUpdatePage.setRegisterationDateInput('2000-12-31'),
      registeredUpdatePage.setTelephoneInput('telephone'),
      registeredUpdatePage.setMobileInput('mobile'),
      registeredUpdatePage.modifiedBySelectLastOption(),
      registeredUpdatePage.courseSelectLastOption(),
    ]);

    expect(await registeredUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await registeredUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await registeredUpdatePage.getMiddleNameInput()).to.eq('middleName', 'Expected MiddleName value to be equals to middleName');
    expect(await registeredUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await registeredUpdatePage.getDateOfBirthInput()).to.eq('2000-12-31', 'Expected dateOfBirth value to be equals to 2000-12-31');
    expect(await registeredUpdatePage.getRegisterationDateInput()).to.eq(
      '2000-12-31',
      'Expected registerationDate value to be equals to 2000-12-31'
    );
    expect(await registeredUpdatePage.getTelephoneInput()).to.eq('telephone', 'Expected Telephone value to be equals to telephone');
    expect(await registeredUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');

    await registeredUpdatePage.save();
    expect(await registeredUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await registeredComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Registered', async () => {
    const nbButtonsBeforeDelete = await registeredComponentsPage.countDeleteButtons();
    await registeredComponentsPage.clickOnLastDeleteButton();

    registeredDeleteDialog = new RegisteredDeleteDialog();
    expect(await registeredDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.registered.delete.question');
    await registeredDeleteDialog.clickOnConfirmButton();

    expect(await registeredComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
