import { element, by, ElementFinder } from 'protractor';

export class ParticipantComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-participant div table .btn-danger'));
  title = element.all(by.css('jhi-participant div h2#page-heading span')).first();
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

export class ParticipantUpdatePage {
  pageTitle = element(by.id('jhi-participant-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  startingDateInput = element(by.id('field_startingDate'));
  activeInput = element(by.id('field_active'));
  statusSelect = element(by.id('field_status'));
  contractNumberInput = element(by.id('field_contractNumber'));

  studentSelect = element(by.id('field_student'));
  courseSelect = element(by.id('field_course'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setStartingDateInput(startingDate: string): Promise<void> {
    await this.startingDateInput.sendKeys(startingDate);
  }

  async getStartingDateInput(): Promise<string> {
    return await this.startingDateInput.getAttribute('value');
  }

  getActiveInput(): ElementFinder {
    return this.activeInput;
  }

  async setStatusSelect(status: string): Promise<void> {
    await this.statusSelect.sendKeys(status);
  }

  async getStatusSelect(): Promise<string> {
    return await this.statusSelect.element(by.css('option:checked')).getText();
  }

  async statusSelectLastOption(): Promise<void> {
    await this.statusSelect.all(by.tagName('option')).last().click();
  }

  async setContractNumberInput(contractNumber: string): Promise<void> {
    await this.contractNumberInput.sendKeys(contractNumber);
  }

  async getContractNumberInput(): Promise<string> {
    return await this.contractNumberInput.getAttribute('value');
  }

  async studentSelectLastOption(): Promise<void> {
    await this.studentSelect.all(by.tagName('option')).last().click();
  }

  async studentSelectOption(option: string): Promise<void> {
    await this.studentSelect.sendKeys(option);
  }

  getStudentSelect(): ElementFinder {
    return this.studentSelect;
  }

  async getStudentSelectedOption(): Promise<string> {
    return await this.studentSelect.element(by.css('option:checked')).getText();
  }

  async courseSelectLastOption(): Promise<void> {
    await this.courseSelect.all(by.tagName('option')).last().click();
  }

  async courseSelectOption(option: string): Promise<void> {
    await this.courseSelect.sendKeys(option);
  }

  getCourseSelect(): ElementFinder {
    return this.courseSelect;
  }

  async getCourseSelectedOption(): Promise<string> {
    return await this.courseSelect.element(by.css('option:checked')).getText();
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

export class ParticipantDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-participant-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-participant'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
