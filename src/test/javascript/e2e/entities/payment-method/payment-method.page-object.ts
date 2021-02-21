import { element, by, ElementFinder } from 'protractor';

export class PaymentMethodComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-payment-method div table .btn-danger'));
  title = element.all(by.css('jhi-payment-method div h2#page-heading span')).first();
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

export class PaymentMethodUpdatePage {
  pageTitle = element(by.id('jhi-payment-method-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  paymentMethodInput = element(by.id('field_paymentMethod'));
  descriptionInput = element(by.id('field_description'));
  activeInput = element(by.id('field_active'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setPaymentMethodInput(paymentMethod: string): Promise<void> {
    await this.paymentMethodInput.sendKeys(paymentMethod);
  }

  async getPaymentMethodInput(): Promise<string> {
    return await this.paymentMethodInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  getActiveInput(): ElementFinder {
    return this.activeInput;
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

export class PaymentMethodDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-paymentMethod-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-paymentMethod'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
