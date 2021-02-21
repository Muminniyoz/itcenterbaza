import { element, by, ElementFinder } from 'protractor';

export class RegionsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-regions div table .btn-danger'));
  title = element.all(by.css('jhi-regions div h2#page-heading span')).first();
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

export class RegionsUpdatePage {
  pageTitle = element(by.id('jhi-regions-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  titleInput = element(by.id('field_title'));
  infoInput = element(by.id('field_info'));
  googleUrlInput = element(by.id('field_googleUrl'));

  directorSelect = element(by.id('field_director'));

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

  async setGoogleUrlInput(googleUrl: string): Promise<void> {
    await this.googleUrlInput.sendKeys(googleUrl);
  }

  async getGoogleUrlInput(): Promise<string> {
    return await this.googleUrlInput.getAttribute('value');
  }

  async directorSelectLastOption(): Promise<void> {
    await this.directorSelect.all(by.tagName('option')).last().click();
  }

  async directorSelectOption(option: string): Promise<void> {
    await this.directorSelect.sendKeys(option);
  }

  getDirectorSelect(): ElementFinder {
    return this.directorSelect;
  }

  async getDirectorSelectedOption(): Promise<string> {
    return await this.directorSelect.element(by.css('option:checked')).getText();
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

export class RegionsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-regions-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-regions'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
