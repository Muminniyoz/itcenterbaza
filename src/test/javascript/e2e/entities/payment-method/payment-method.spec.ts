import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PaymentMethodComponentsPage, PaymentMethodDeleteDialog, PaymentMethodUpdatePage } from './payment-method.page-object';

const expect = chai.expect;

describe('PaymentMethod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentMethodComponentsPage: PaymentMethodComponentsPage;
  let paymentMethodUpdatePage: PaymentMethodUpdatePage;
  let paymentMethodDeleteDialog: PaymentMethodDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PaymentMethods', async () => {
    await navBarPage.goToEntity('payment-method');
    paymentMethodComponentsPage = new PaymentMethodComponentsPage();
    await browser.wait(ec.visibilityOf(paymentMethodComponentsPage.title), 5000);
    expect(await paymentMethodComponentsPage.getTitle()).to.eq('itcenterbazaApp.paymentMethod.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(paymentMethodComponentsPage.entities), ec.visibilityOf(paymentMethodComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PaymentMethod page', async () => {
    await paymentMethodComponentsPage.clickOnCreateButton();
    paymentMethodUpdatePage = new PaymentMethodUpdatePage();
    expect(await paymentMethodUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.paymentMethod.home.createOrEditLabel');
    await paymentMethodUpdatePage.cancel();
  });

  it('should create and save PaymentMethods', async () => {
    const nbButtonsBeforeCreate = await paymentMethodComponentsPage.countDeleteButtons();

    await paymentMethodComponentsPage.clickOnCreateButton();

    await promise.all([
      paymentMethodUpdatePage.setPaymentMethodInput('paymentMethod'),
      paymentMethodUpdatePage.setDescriptionInput('description'),
    ]);

    expect(await paymentMethodUpdatePage.getPaymentMethodInput()).to.eq(
      'paymentMethod',
      'Expected PaymentMethod value to be equals to paymentMethod'
    );
    expect(await paymentMethodUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    const selectedActive = paymentMethodUpdatePage.getActiveInput();
    if (await selectedActive.isSelected()) {
      await paymentMethodUpdatePage.getActiveInput().click();
      expect(await paymentMethodUpdatePage.getActiveInput().isSelected(), 'Expected active not to be selected').to.be.false;
    } else {
      await paymentMethodUpdatePage.getActiveInput().click();
      expect(await paymentMethodUpdatePage.getActiveInput().isSelected(), 'Expected active to be selected').to.be.true;
    }

    await paymentMethodUpdatePage.save();
    expect(await paymentMethodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await paymentMethodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last PaymentMethod', async () => {
    const nbButtonsBeforeDelete = await paymentMethodComponentsPage.countDeleteButtons();
    await paymentMethodComponentsPage.clickOnLastDeleteButton();

    paymentMethodDeleteDialog = new PaymentMethodDeleteDialog();
    expect(await paymentMethodDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.paymentMethod.delete.question');
    await paymentMethodDeleteDialog.clickOnConfirmButton();

    expect(await paymentMethodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
