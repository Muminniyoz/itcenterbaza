import { element, by, ElementFinder } from 'protractor';

export class EventHistoryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-event-history div table .btn-danger'));
  title = element.all(by.css('jhi-event-history div h2#page-heading span')).first();
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

export class EventHistoryUpdatePage {
  pageTitle = element(by.id('jhi-event-history-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  typeSelect = element(by.id('field_type'));
  textInput = element(by.id('field_text'));
  timeInput = element(by.id('field_time'));

  centerSelect = element(by.id('field_center'));
  userSelect = element(by.id('field_user'));
  openedUserSelect = element(by.id('field_openedUser'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTypeSelect(type: string): Promise<void> {
    await this.typeSelect.sendKeys(type);
  }

  async getTypeSelect(): Promise<string> {
    return await this.typeSelect.element(by.css('option:checked')).getText();
  }

  async typeSelectLastOption(): Promise<void> {
    await this.typeSelect.all(by.tagName('option')).last().click();
  }

  async setTextInput(text: string): Promise<void> {
    await this.textInput.sendKeys(text);
  }

  async getTextInput(): Promise<string> {
    return await this.textInput.getAttribute('value');
  }

  async setTimeInput(time: string): Promise<void> {
    await this.timeInput.sendKeys(time);
  }

  async getTimeInput(): Promise<string> {
    return await this.timeInput.getAttribute('value');
  }

  async centerSelectLastOption(): Promise<void> {
    await this.centerSelect.all(by.tagName('option')).last().click();
  }

  async centerSelectOption(option: string): Promise<void> {
    await this.centerSelect.sendKeys(option);
  }

  getCenterSelect(): ElementFinder {
    return this.centerSelect;
  }

  async getCenterSelectedOption(): Promise<string> {
    return await this.centerSelect.element(by.css('option:checked')).getText();
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

  async openedUserSelectLastOption(): Promise<void> {
    await this.openedUserSelect.all(by.tagName('option')).last().click();
  }

  async openedUserSelectOption(option: string): Promise<void> {
    await this.openedUserSelect.sendKeys(option);
  }

  getOpenedUserSelect(): ElementFinder {
    return this.openedUserSelect;
  }

  async getOpenedUserSelectedOption(): Promise<string> {
    return await this.openedUserSelect.element(by.css('option:checked')).getText();
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

export class EventHistoryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-eventHistory-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-eventHistory'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
