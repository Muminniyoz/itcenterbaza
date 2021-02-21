import { element, by, ElementFinder } from 'protractor';

export class StudentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-student div table .btn-danger'));
  title = element.all(by.css('jhi-student div h2#page-heading span')).first();
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

export class StudentUpdatePage {
  pageTitle = element(by.id('jhi-student-heading'));
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
  thumbnailPhotoUrlInput = element(by.id('field_thumbnailPhotoUrl'));
  fullPhotoUrlInput = element(by.id('field_fullPhotoUrl'));
  idNumberInput = element(by.id('field_idNumber'));

  modifiedBySelect = element(by.id('field_modifiedBy'));

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

  async setThumbnailPhotoUrlInput(thumbnailPhotoUrl: string): Promise<void> {
    await this.thumbnailPhotoUrlInput.sendKeys(thumbnailPhotoUrl);
  }

  async getThumbnailPhotoUrlInput(): Promise<string> {
    return await this.thumbnailPhotoUrlInput.getAttribute('value');
  }

  async setFullPhotoUrlInput(fullPhotoUrl: string): Promise<void> {
    await this.fullPhotoUrlInput.sendKeys(fullPhotoUrl);
  }

  async getFullPhotoUrlInput(): Promise<string> {
    return await this.fullPhotoUrlInput.getAttribute('value');
  }

  async setIdNumberInput(idNumber: string): Promise<void> {
    await this.idNumberInput.sendKeys(idNumber);
  }

  async getIdNumberInput(): Promise<string> {
    return await this.idNumberInput.getAttribute('value');
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

export class StudentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-student-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-student'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
