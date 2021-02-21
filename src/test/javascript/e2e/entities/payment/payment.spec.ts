import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PaymentComponentsPage, PaymentDeleteDialog, PaymentUpdatePage } from './payment.page-object';

const expect = chai.expect;

describe('Payment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentComponentsPage: PaymentComponentsPage;
  let paymentUpdatePage: PaymentUpdatePage;
  let paymentDeleteDialog: PaymentDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Payments', async () => {
    await navBarPage.goToEntity('payment');
    paymentComponentsPage = new PaymentComponentsPage();
    await browser.wait(ec.visibilityOf(paymentComponentsPage.title), 5000);
    expect(await paymentComponentsPage.getTitle()).to.eq('itcenterbazaApp.payment.home.title');
    await browser.wait(ec.or(ec.visibilityOf(paymentComponentsPage.entities), ec.visibilityOf(paymentComponentsPage.noResult)), 1000);
  });

  it('should load create Payment page', async () => {
    await paymentComponentsPage.clickOnCreateButton();
    paymentUpdatePage = new PaymentUpdatePage();
    expect(await paymentUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.payment.home.createOrEditLabel');
    await paymentUpdatePage.cancel();
  });

  it('should create and save Payments', async () => {
    const nbButtonsBeforeCreate = await paymentComponentsPage.countDeleteButtons();

    await paymentComponentsPage.clickOnCreateButton();

    await promise.all([
      paymentUpdatePage.setPaymentDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      paymentUpdatePage.setPaymentProviderInput('paymentProvider'),
      paymentUpdatePage.setAmountInput('5'),
      paymentUpdatePage.paymentStatusSelectLastOption(),
      paymentUpdatePage.setCurencyInput('curency'),
      paymentUpdatePage.setCustomerNameInput('customerName'),
      paymentUpdatePage.modifiedBySelectLastOption(),
      paymentUpdatePage.studentSelectLastOption(),
      paymentUpdatePage.methodSelectLastOption(),
    ]);

    expect(await paymentUpdatePage.getPaymentDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected paymentDate value to be equals to 2000-12-31'
    );
    expect(await paymentUpdatePage.getPaymentProviderInput()).to.eq(
      'paymentProvider',
      'Expected PaymentProvider value to be equals to paymentProvider'
    );
    expect(await paymentUpdatePage.getAmountInput()).to.eq('5', 'Expected amount value to be equals to 5');
    expect(await paymentUpdatePage.getCurencyInput()).to.eq('curency', 'Expected Curency value to be equals to curency');
    expect(await paymentUpdatePage.getCustomerNameInput()).to.eq(
      'customerName',
      'Expected CustomerName value to be equals to customerName'
    );
    const selectedIsEnough = paymentUpdatePage.getIsEnoughInput();
    if (await selectedIsEnough.isSelected()) {
      await paymentUpdatePage.getIsEnoughInput().click();
      expect(await paymentUpdatePage.getIsEnoughInput().isSelected(), 'Expected isEnough not to be selected').to.be.false;
    } else {
      await paymentUpdatePage.getIsEnoughInput().click();
      expect(await paymentUpdatePage.getIsEnoughInput().isSelected(), 'Expected isEnough to be selected').to.be.true;
    }
    const selectedIsConfirmed = paymentUpdatePage.getIsConfirmedInput();
    if (await selectedIsConfirmed.isSelected()) {
      await paymentUpdatePage.getIsConfirmedInput().click();
      expect(await paymentUpdatePage.getIsConfirmedInput().isSelected(), 'Expected isConfirmed not to be selected').to.be.false;
    } else {
      await paymentUpdatePage.getIsConfirmedInput().click();
      expect(await paymentUpdatePage.getIsConfirmedInput().isSelected(), 'Expected isConfirmed to be selected').to.be.true;
    }

    await paymentUpdatePage.save();
    expect(await paymentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await paymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Payment', async () => {
    const nbButtonsBeforeDelete = await paymentComponentsPage.countDeleteButtons();
    await paymentComponentsPage.clickOnLastDeleteButton();

    paymentDeleteDialog = new PaymentDeleteDialog();
    expect(await paymentDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.payment.delete.question');
    await paymentDeleteDialog.clickOnConfirmButton();

    expect(await paymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
