import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { StudentComponentsPage, StudentDeleteDialog, StudentUpdatePage } from './student.page-object';

const expect = chai.expect;

describe('Student e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let studentComponentsPage: StudentComponentsPage;
  let studentUpdatePage: StudentUpdatePage;
  let studentDeleteDialog: StudentDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Students', async () => {
    await navBarPage.goToEntity('student');
    studentComponentsPage = new StudentComponentsPage();
    await browser.wait(ec.visibilityOf(studentComponentsPage.title), 5000);
    expect(await studentComponentsPage.getTitle()).to.eq('itcenterbazaApp.student.home.title');
    await browser.wait(ec.or(ec.visibilityOf(studentComponentsPage.entities), ec.visibilityOf(studentComponentsPage.noResult)), 1000);
  });

  it('should load create Student page', async () => {
    await studentComponentsPage.clickOnCreateButton();
    studentUpdatePage = new StudentUpdatePage();
    expect(await studentUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.student.home.createOrEditLabel');
    await studentUpdatePage.cancel();
  });

  it('should create and save Students', async () => {
    const nbButtonsBeforeCreate = await studentComponentsPage.countDeleteButtons();

    await studentComponentsPage.clickOnCreateButton();

    await promise.all([
      studentUpdatePage.setFirstNameInput('firstName'),
      studentUpdatePage.setLastNameInput('lastName'),
      studentUpdatePage.setMiddleNameInput('middleName'),
      studentUpdatePage.setEmailInput('email'),
      studentUpdatePage.setDateOfBirthInput('2000-12-31'),
      studentUpdatePage.genderSelectLastOption(),
      studentUpdatePage.setRegisterationDateInput('2000-12-31'),
      studentUpdatePage.setTelephoneInput('telephone'),
      studentUpdatePage.setMobileInput('mobile'),
      studentUpdatePage.setThumbnailPhotoUrlInput('thumbnailPhotoUrl'),
      studentUpdatePage.setFullPhotoUrlInput('fullPhotoUrl'),
      studentUpdatePage.setIdNumberInput('5'),
      studentUpdatePage.modifiedBySelectLastOption(),
    ]);

    expect(await studentUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await studentUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await studentUpdatePage.getMiddleNameInput()).to.eq('middleName', 'Expected MiddleName value to be equals to middleName');
    expect(await studentUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await studentUpdatePage.getDateOfBirthInput()).to.eq('2000-12-31', 'Expected dateOfBirth value to be equals to 2000-12-31');
    expect(await studentUpdatePage.getRegisterationDateInput()).to.eq(
      '2000-12-31',
      'Expected registerationDate value to be equals to 2000-12-31'
    );
    expect(await studentUpdatePage.getTelephoneInput()).to.eq('telephone', 'Expected Telephone value to be equals to telephone');
    expect(await studentUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await studentUpdatePage.getThumbnailPhotoUrlInput()).to.eq(
      'thumbnailPhotoUrl',
      'Expected ThumbnailPhotoUrl value to be equals to thumbnailPhotoUrl'
    );
    expect(await studentUpdatePage.getFullPhotoUrlInput()).to.eq(
      'fullPhotoUrl',
      'Expected FullPhotoUrl value to be equals to fullPhotoUrl'
    );
    expect(await studentUpdatePage.getIdNumberInput()).to.eq('5', 'Expected idNumber value to be equals to 5');

    await studentUpdatePage.save();
    expect(await studentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await studentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Student', async () => {
    const nbButtonsBeforeDelete = await studentComponentsPage.countDeleteButtons();
    await studentComponentsPage.clickOnLastDeleteButton();

    studentDeleteDialog = new StudentDeleteDialog();
    expect(await studentDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.student.delete.question');
    await studentDeleteDialog.clickOnConfirmButton();

    expect(await studentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
