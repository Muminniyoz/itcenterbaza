import { element, by, ElementFinder } from 'protractor';

export class TeacherComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-teacher div table .btn-danger'));
  title = element.all(by.css('jhi-teacher div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class TeacherUpdatePage {
  pageTitle = element(by.id('jhi-teacher-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  firstNameInput = element(by.id('field_firstName'));
  lastNameInput = element(by.id('field_lastName'));
  middleNameInput = element(by.id('field_middleName'));
  emailInput = element(by.id('field_email'));
  dateOfBirthInput = element(by.id('field_dateOfBirth'));
  genderSelect = element(by.id('field_gender'));
  registerationDateInput = element(by.id('field_registerationDate'));
  telephoneInput = element(by.id('field_telephone'));
  mobileInput = element(by.id('field_mobile'));
  fullPhotoUrlInput = element(by.id('field_fullPhotoUrl'));
  activeInput = element(by.id('field_active'));
  keyInput = element(by.id('field_key'));
  aboutInput = element(by.id('field_about'));
  portfoliaInput = element(by.id('field_portfolia'));
  infoInput = element(by.id('file_info'));
  leaveDateInput = element(by.id('field_leaveDate'));
  isShowingHomeInput = element(by.id('field_isShowingHome'));
  imageInput = element(by.id('file_image'));

  modifiedBySelect = element(by.id('field_modifiedBy'));
  userSelect = element(by.id('field_user'));
  skillsSelect = element(by.id('field_skills'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setFirstNameInput(firstName: string): Promise<void> {
    await this.firstNameInput.sendKeys(firstName);
  }

  async getFirstNameInput(): Promise<string> {
    return await this.firstNameInput.getAttribute('value');
  }

  async setLastNameInput(lastName: string): Promise<void> {
    await this.lastNameInput.sendKeys(lastName);
  }

  async getLastNameInput(): Promise<string> {
    return await this.lastNameInput.getAttribute('value');
  }

  async setMiddleNameInput(middleName: string): Promise<void> {
    await this.middleNameInput.sendKeys(middleName);
  }

  async getMiddleNameInput(): Promise<string> {
    return await this.middleNameInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setDateOfBirthInput(dateOfBirth: string): Promise<void> {
    await this.dateOfBirthInput.sendKeys(dateOfBirth);
  }

  async getDateOfBirthInput(): Promise<string> {
    return await this.dateOfBirthInput.getAttribute('value');
  }

  async setGenderSelect(gender: string): Promise<void> {
    await this.genderSelect.sendKeys(gender);
  }

  async getGenderSelect(): Promise<string> {
    return await this.genderSelect.element(by.css('option:checked')).getText();
  }

  async genderSelectLastOption(): Promise<void> {
    await this.genderSelect.all(by.tagName('option')).last().click();
  }

  async setRegisterationDateInput(registerationDate: string): Promise<void> {
    await this.registerationDateInput.sendKeys(registerationDate);
  }

  async getRegisterationDateInput(): Promise<string> {
    return await this.registerationDateInput.getAttribute('value');
  }

  async setTelephoneInput(telephone: string): Promise<void> {
    await this.telephoneInput.sendKeys(telephone);
  }

  async getTelephoneInput(): Promise<string> {
    return await this.telephoneInput.getAttribute('value');
  }

  async setMobileInput(mobile: string): Promise<void> {
    await this.mobileInput.sendKeys(mobile);
  }

  async getMobileInput(): Promise<string> {
    return await this.mobileInput.getAttribute('value');
  }

  async setFullPhotoUrlInput(fullPhotoUrl: string): Promise<void> {
    await this.fullPhotoUrlInput.sendKeys(fullPhotoUrl);
  }

  async getFullPhotoUrlInput(): Promise<string> {
    return await this.fullPhotoUrlInput.getAttribute('value');
  }

  getActiveInput(): ElementFinder {
    return this.activeInput;
  }

  async setKeyInput(key: string): Promise<void> {
    await this.keyInput.sendKeys(key);
  }

  async getKeyInput(): Promise<string> {
    return await this.keyInput.getAttribute('value');
  }

  async setAboutInput(about: string): Promise<void> {
    await this.aboutInput.sendKeys(about);
  }

  async getAboutInput(): Promise<string> {
    return await this.aboutInput.getAttribute('value');
  }

  async setPortfoliaInput(portfolia: string): Promise<void> {
    await this.portfoliaInput.sendKeys(portfolia);
  }

  async getPortfoliaInput(): Promise<string> {
    return await this.portfoliaInput.getAttribute('value');
  }

  async setInfoInput(info: string): Promise<void> {
    await this.infoInput.sendKeys(info);
  }

  async getInfoInput(): Promise<string> {
    return await this.infoInput.getAttribute('value');
  }

  async setLeaveDateInput(leaveDate: string): Promise<void> {
    await this.leaveDateInput.sendKeys(leaveDate);
  }

  async getLeaveDateInput(): Promise<string> {
    return await this.leaveDateInput.getAttribute('value');
  }

  getIsShowingHomeInput(): ElementFinder {
    return this.isShowingHomeInput;
  }

  async setImageInput(image: string): Promise<void> {
    await this.imageInput.sendKeys(image);
  }

  async getImageInput(): Promise<string> {
    return await this.imageInput.getAttribute('value');
  }

  async modifiedBySelectLastOption(): Promise<void> {
    await this.modifiedBySelect.all(by.tagName('option')).last().click();
  }

  async modifiedBySelectOption(option: string): Promise<void> {
    await this.modifiedBySelect.sendKeys(option);
  }

  getModifiedBySelect(): ElementFinder {
    return this.modifiedBySelect;
  }

  async getModifiedBySelectedOption(): Promise<string> {
    return await this.modifiedBySelect.element(by.css('option:checked')).getText();
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async skillsSelectLastOption(): Promise<void> {
    await this.skillsSelect.all(by.tagName('option')).last().click();
  }

  async skillsSelectOption(option: string): Promise<void> {
    await this.skillsSelect.sendKeys(option);
  }

  getSkillsSelect(): ElementFinder {
    return this.skillsSelect;
  }

  async getSkillsSelectedOption(): Promise<string> {
    return await this.skillsSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class TeacherDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-teacher-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-teacher'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
