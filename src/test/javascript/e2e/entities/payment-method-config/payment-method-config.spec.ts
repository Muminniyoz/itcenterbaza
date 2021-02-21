import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  PaymentMethodConfigComponentsPage,
  PaymentMethodConfigDeleteDialog,
  PaymentMethodConfigUpdatePage,
} from './payment-method-config.page-object';

const expect = chai.expect;

describe('PaymentMethodConfig e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentMethodConfigComponentsPage: PaymentMethodConfigComponentsPage;
  let paymentMethodConfigUpdatePage: PaymentMethodConfigUpdatePage;
  let paymentMethodConfigDeleteDialog: PaymentMethodConfigDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PaymentMethodConfigs', async () => {
    await navBarPage.goToEntity('payment-method-config');
    paymentMethodConfigComponentsPage = new PaymentMethodConfigComponentsPage();
    await browser.wait(ec.visibilityOf(paymentMethodConfigComponentsPage.title), 5000);
    expect(await paymentMethodConfigComponentsPage.getTitle()).to.eq('itcenterbazaApp.paymentMethodConfig.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(paymentMethodConfigComponentsPage.entities), ec.visibilityOf(paymentMethodConfigComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PaymentMethodConfig page', async () => {
    await paymentMethodConfigComponentsPage.clickOnCreateButton();
    paymentMethodConfigUpdatePage = new PaymentMethodConfigUpdatePage();
    expect(await paymentMethodConfigUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.paymentMethodConfig.home.createOrEditLabel');
    await paymentMethodConfigUpdatePage.cancel();
  });

  it('should create and save PaymentMethodConfigs', async () => {
    const nbButtonsBeforeCreate = await paymentMethodConfigComponentsPage.countDeleteButtons();

    await paymentMethodConfigComponentsPage.clickOnCreateButton();

    await promise.all([
      paymentMethodConfigUpdatePage.setKeyInput('key'),
      paymentMethodConfigUpdatePage.setValueInput('value'),
      paymentMethodConfigUpdatePage.setNoteInput('note'),
      paymentMethodConfigUpdatePage.methodSelectLastOption(),
    ]);

    expect(await paymentMethodConfigUpdatePage.getKeyInput()).to.eq('key', 'Expected Key value to be equals to key');
    expect(await paymentMethodConfigUpdatePage.getValueInput()).to.eq('value', 'Expected Value value to be equals to value');
    expect(await paymentMethodConfigUpdatePage.getNoteInput()).to.eq('note', 'Expected Note value to be equals to note');
    const selectedEnabled = paymentMethodConfigUpdatePage.getEnabledInput();
    if (await selectedEnabled.isSelected()) {
      await paymentMethodConfigUpdatePage.getEnabledInput().click();
      expect(await paymentMethodConfigUpdatePage.getEnabledInput().isSelected(), 'Expected enabled not to be selected').to.be.false;
    } else {
      await paymentMethodConfigUpdatePage.getEnabledInput().click();
      expect(await paymentMethodConfigUpdatePage.getEnabledInput().isSelected(), 'Expected enabled to be selected').to.be.true;
    }

    await paymentMethodConfigUpdatePage.save();
    expect(await paymentMethodConfigUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await paymentMethodConfigComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PaymentMethodConfig', async () => {
    const nbButtonsBeforeDelete = await paymentMethodConfigComponentsPage.countDeleteButtons();
    await paymentMethodConfigComponentsPage.clickOnLastDeleteButton();

    paymentMethodConfigDeleteDialog = new PaymentMethodConfigDeleteDialog();
    expect(await paymentMethodConfigDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.paymentMethodConfig.delete.question');
    await paymentMethodConfigDeleteDialog.clickOnConfirmButton();

    expect(await paymentMethodConfigComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
