import { element, by, ElementFinder } from 'protractor';

export class CenterComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-center div table .btn-danger'));
  title = element.all(by.css('jhi-center div h2#page-heading span')).first();
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

export class CenterUpdatePage {
  pageTitle = element(by.id('jhi-center-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  titleInput = element(by.id('field_title'));
  infoInput = element(by.id('field_info'));
  startDateInput = element(by.id('field_startDate'));
  googleMapUrlInput = element(by.id('field_googleMapUrl'));

  modifiedBySelect = element(by.id('field_modifiedBy'));
  regionsSelect = element(by.id('field_regions'));
  managerSelect = element(by.id('field_manager'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTitleInput(title: string): Promise<void> {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput(): Promise<string> {
    return await this.titleInput.getAttribute('value');
  }

  async setInfoInput(info: string): Promise<void> {
    await this.infoInput.sendKeys(info);
  }

  async getInfoInput(): Promise<string> {
    return await this.infoInput.getAttribute('value');
  }

  async setStartDateInput(startDate: string): Promise<void> {
    await this.startDateInput.sendKeys(startDate);
  }

  async getStartDateInput(): Promise<string> {
    return await this.startDateInput.getAttribute('value');
  }

  async setGoogleMapUrlInput(googleMapUrl: string): Promise<void> {
    await this.googleMapUrlInput.sendKeys(googleMapUrl);
  }

  async getGoogleMapUrlInput(): Promise<string> {
    return await this.googleMapUrlInput.getAttribute('value');
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

  async regionsSelectLastOption(): Promise<void> {
    await this.regionsSelect.all(by.tagName('option')).last().click();
  }

  async regionsSelectOption(option: string): Promise<void> {
    await this.regionsSelect.sendKeys(option);
  }

  getRegionsSelect(): ElementFinder {
    return this.regionsSelect;
  }

  async getRegionsSelectedOption(): Promise<string> {
    return await this.regionsSelect.element(by.css('option:checked')).getText();
  }

  async managerSelectLastOption(): Promise<void> {
    await this.managerSelect.all(by.tagName('option')).last().click();
  }

  async managerSelectOption(option: string): Promise<void> {
    await this.managerSelect.sendKeys(option);
  }

  getManagerSelect(): ElementFinder {
    return this.managerSelect;
  }

  async getManagerSelectedOption(): Promise<string> {
    return await this.managerSelect.element(by.css('option:checked')).getText();
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

export class CenterDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-center-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-center'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
