import { element, by, ElementFinder } from 'protractor';

export class PaymentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-payment div table .btn-danger'));
  title = element.all(by.css('jhi-payment div h2#page-heading span')).first();
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

export class PaymentUpdatePage {
  pageTitle = element(by.id('jhi-payment-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  paymentDateInput = element(by.id('field_paymentDate'));
  paymentProviderInput = element(by.id('field_paymentProvider'));
  amountInput = element(by.id('field_amount'));
  paymentStatusSelect = element(by.id('field_paymentStatus'));
  curencyInput = element(by.id('field_curency'));
  customerNameInput = element(by.id('field_customerName'));
  isEnoughInput = element(by.id('field_isEnough'));
  isConfirmedInput = element(by.id('field_isConfirmed'));

  modifiedBySelect = element(by.id('field_modifiedBy'));
  studentSelect = element(by.id('field_student'));
  methodSelect = element(by.id('field_method'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setPaymentDateInput(paymentDate: string): Promise<void> {
    await this.paymentDateInput.sendKeys(paymentDate);
  }

  async getPaymentDateInput(): Promise<string> {
    return await this.paymentDateInput.getAttribute('value');
  }

  async setPaymentProviderInput(paymentProvider: string): Promise<void> {
    await this.paymentProviderInput.sendKeys(paymentProvider);
  }

  async getPaymentProviderInput(): Promise<string> {
    return await this.paymentProviderInput.getAttribute('value');
  }

  async setAmountInput(amount: string): Promise<void> {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput(): Promise<string> {
    return await this.amountInput.getAttribute('value');
  }

  async setPaymentStatusSelect(paymentStatus: string): Promise<void> {
    await this.paymentStatusSelect.sendKeys(paymentStatus);
  }

  async getPaymentStatusSelect(): Promise<string> {
    return await this.paymentStatusSelect.element(by.css('option:checked')).getText();
  }

  async paymentStatusSelectLastOption(): Promise<void> {
    await this.paymentStatusSelect.all(by.tagName('option')).last().click();
  }

  async setCurencyInput(curency: string): Promise<void> {
    await this.curencyInput.sendKeys(curency);
  }

  async getCurencyInput(): Promise<string> {
    return await this.curencyInput.getAttribute('value');
  }

  async setCustomerNameInput(customerName: string): Promise<void> {
    await this.customerNameInput.sendKeys(customerName);
  }

  async getCustomerNameInput(): Promise<string> {
    return await this.customerNameInput.getAttribute('value');
  }

  getIsEnoughInput(): ElementFinder {
    return this.isEnoughInput;
  }

  getIsConfirmedInput(): ElementFinder {
    return this.isConfirmedInput;
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

  async methodSelectLastOption(): Promise<void> {
    await this.methodSelect.all(by.tagName('option')).last().click();
  }

  async methodSelectOption(option: string): Promise<void> {
    await this.methodSelect.sendKeys(option);
  }

  getMethodSelect(): ElementFinder {
    return this.methodSelect;
  }

  async getMethodSelectedOption(): Promise<string> {
    return await this.methodSelect.element(by.css('option:checked')).getText();
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

export class PaymentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-payment-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-payment'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
