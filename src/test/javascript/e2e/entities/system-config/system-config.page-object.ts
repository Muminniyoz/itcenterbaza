import { element, by, ElementFinder } from 'protractor';

export class SystemConfigComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-system-config div table .btn-danger'));
  title = element.all(by.css('jhi-system-config div h2#page-heading span')).first();
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

export class SystemConfigUpdatePage {
  pageTitle = element(by.id('jhi-system-config-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  keyInput = element(by.id('field_key'));
  valueInput = element(by.id('field_value'));
  noteInput = element(by.id('field_note'));
  enabledInput = element(by.id('field_enabled'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setKeyInput(key: string): Promise<void> {
    await this.keyInput.sendKeys(key);
  }

  async getKeyInput(): Promise<string> {
    return await this.keyInput.getAttribute('value');
  }

  async setValueInput(value: string): Promise<void> {
    await this.valueInput.sendKeys(value);
  }

  async getValueInput(): Promise<string> {
    return await this.valueInput.getAttribute('value');
  }

  async setNoteInput(note: string): Promise<void> {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput(): Promise<string> {
    return await this.noteInput.getAttribute('value');
  }

  getEnabledInput(): ElementFinder {
    return this.enabledInput;
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

export class SystemConfigDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-systemConfig-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-systemConfig'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
