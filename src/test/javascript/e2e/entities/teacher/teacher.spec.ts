import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TeacherComponentsPage, TeacherDeleteDialog, TeacherUpdatePage } from './teacher.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Teacher e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let teacherComponentsPage: TeacherComponentsPage;
  let teacherUpdatePage: TeacherUpdatePage;
  let teacherDeleteDialog: TeacherDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Teachers', async () => {
    await navBarPage.goToEntity('teacher');
    teacherComponentsPage = new TeacherComponentsPage();
    await browser.wait(ec.visibilityOf(teacherComponentsPage.title), 5000);
    expect(await teacherComponentsPage.getTitle()).to.eq('itcenterbazaApp.teacher.home.title');
    await browser.wait(ec.or(ec.visibilityOf(teacherComponentsPage.entities), ec.visibilityOf(teacherComponentsPage.noResult)), 1000);
  });

  it('should load create Teacher page', async () => {
    await teacherComponentsPage.clickOnCreateButton();
    teacherUpdatePage = new TeacherUpdatePage();
    expect(await teacherUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.teacher.home.createOrEditLabel');
    await teacherUpdatePage.cancel();
  });

  it('should create and save Teachers', async () => {
    const nbButtonsBeforeCreate = await teacherComponentsPage.countDeleteButtons();

    await teacherComponentsPage.clickOnCreateButton();

    await promise.all([
      teacherUpdatePage.setFirstNameInput('firstName'),
      teacherUpdatePage.setLastNameInput('lastName'),
      teacherUpdatePage.setMiddleNameInput('middleName'),
      teacherUpdatePage.setEmailInput('email'),
      teacherUpdatePage.setDateOfBirthInput('2000-12-31'),
      teacherUpdatePage.genderSelectLastOption(),
      teacherUpdatePage.setRegisterationDateInput('2000-12-31'),
      teacherUpdatePage.setTelephoneInput('telephone'),
      teacherUpdatePage.setMobileInput('mobile'),
      teacherUpdatePage.setFullPhotoUrlInput('fullPhotoUrl'),
      teacherUpdatePage.setKeyInput('key'),
      teacherUpdatePage.setAboutInput('about'),
      teacherUpdatePage.setPortfoliaInput('portfolia'),
      teacherUpdatePage.setInfoInput(absolutePath),
      teacherUpdatePage.setLeaveDateInput('2000-12-31'),
      teacherUpdatePage.setImageInput(absolutePath),
      teacherUpdatePage.modifiedBySelectLastOption(),
      teacherUpdatePage.userSelectLastOption(),
      // teacherUpdatePage.skillsSelectLastOption(),
    ]);

    expect(await teacherUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await teacherUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await teacherUpdatePage.getMiddleNameInput()).to.eq('middleName', 'Expected MiddleName value to be equals to middleName');
    expect(await teacherUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await teacherUpdatePage.getDateOfBirthInput()).to.eq('2000-12-31', 'Expected dateOfBirth value to be equals to 2000-12-31');
    expect(await teacherUpdatePage.getRegisterationDateInput()).to.eq(
      '2000-12-31',
      'Expected registerationDate value to be equals to 2000-12-31'
    );
    expect(await teacherUpdatePage.getTelephoneInput()).to.eq('telephone', 'Expected Telephone value to be equals to telephone');
    expect(await teacherUpdatePage.getMobileInput()).to.eq('mobile', 'Expected Mobile value to be equals to mobile');
    expect(await teacherUpdatePage.getFullPhotoUrlInput()).to.eq(
      'fullPhotoUrl',
      'Expected FullPhotoUrl value to be equals to fullPhotoUrl'
    );
    const selectedActive = teacherUpdatePage.getActiveInput();
    if (await selectedActive.isSelected()) {
      await teacherUpdatePage.getActiveInput().click();
      expect(await teacherUpdatePage.getActiveInput().isSelected(), 'Expected active not to be selected').to.be.false;
    } else {
      await teacherUpdatePage.getActiveInput().click();
      expect(await teacherUpdatePage.getActiveInput().isSelected(), 'Expected active to be selected').to.be.true;
    }
    expect(await teacherUpdatePage.getKeyInput()).to.eq('key', 'Expected Key value to be equals to key');
    expect(await teacherUpdatePage.getAboutInput()).to.eq('about', 'Expected About value to be equals to about');
    expect(await teacherUpdatePage.getPortfoliaInput()).to.eq('portfolia', 'Expected Portfolia value to be equals to portfolia');
    expect(await teacherUpdatePage.getInfoInput()).to.endsWith(fileNameToUpload, 'Expected Info value to be end with ' + fileNameToUpload);
    expect(await teacherUpdatePage.getLeaveDateInput()).to.eq('2000-12-31', 'Expected leaveDate value to be equals to 2000-12-31');
    const selectedIsShowingHome = teacherUpdatePage.getIsShowingHomeInput();
    if (await selectedIsShowingHome.isSelected()) {
      await teacherUpdatePage.getIsShowingHomeInput().click();
      expect(await teacherUpdatePage.getIsShowingHomeInput().isSelected(), 'Expected isShowingHome not to be selected').to.be.false;
    } else {
      await teacherUpdatePage.getIsShowingHomeInput().click();
      expect(await teacherUpdatePage.getIsShowingHomeInput().isSelected(), 'Expected isShowingHome to be selected').to.be.true;
    }
    expect(await teacherUpdatePage.getImageInput()).to.endsWith(
      fileNameToUpload,
      'Expected Image value to be end with ' + fileNameToUpload
    );

    await teacherUpdatePage.save();
    expect(await teacherUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await teacherComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Teacher', async () => {
    const nbButtonsBeforeDelete = await teacherComponentsPage.countDeleteButtons();
    await teacherComponentsPage.clickOnLastDeleteButton();

    teacherDeleteDialog = new TeacherDeleteDialog();
    expect(await teacherDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.teacher.delete.question');
    await teacherDeleteDialog.clickOnConfirmButton();

    expect(await teacherComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
