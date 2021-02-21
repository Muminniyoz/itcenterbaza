import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ParticipantComponentsPage, ParticipantDeleteDialog, ParticipantUpdatePage } from './participant.page-object';

const expect = chai.expect;

describe('Participant e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let participantComponentsPage: ParticipantComponentsPage;
  let participantUpdatePage: ParticipantUpdatePage;
  let participantDeleteDialog: ParticipantDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Participants', async () => {
    await navBarPage.goToEntity('participant');
    participantComponentsPage = new ParticipantComponentsPage();
    await browser.wait(ec.visibilityOf(participantComponentsPage.title), 5000);
    expect(await participantComponentsPage.getTitle()).to.eq('itcenterbazaApp.participant.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(participantComponentsPage.entities), ec.visibilityOf(participantComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Participant page', async () => {
    await participantComponentsPage.clickOnCreateButton();
    participantUpdatePage = new ParticipantUpdatePage();
    expect(await participantUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.participant.home.createOrEditLabel');
    await participantUpdatePage.cancel();
  });

  it('should create and save Participants', async () => {
    const nbButtonsBeforeCreate = await participantComponentsPage.countDeleteButtons();

    await participantComponentsPage.clickOnCreateButton();

    await promise.all([
      participantUpdatePage.setStartingDateInput('2000-12-31'),
      participantUpdatePage.statusSelectLastOption(),
      participantUpdatePage.setContractNumberInput('contractNumber'),
      participantUpdatePage.studentSelectLastOption(),
      participantUpdatePage.courseSelectLastOption(),
    ]);

    expect(await participantUpdatePage.getStartingDateInput()).to.eq(
      '2000-12-31',
      'Expected startingDate value to be equals to 2000-12-31'
    );
    const selectedActive = participantUpdatePage.getActiveInput();
    if (await selectedActive.isSelected()) {
      await participantUpdatePage.getActiveInput().click();
      expect(await participantUpdatePage.getActiveInput().isSelected(), 'Expected active not to be selected').to.be.false;
    } else {
      await participantUpdatePage.getActiveInput().click();
      expect(await participantUpdatePage.getActiveInput().isSelected(), 'Expected active to be selected').to.be.true;
    }
    expect(await participantUpdatePage.getContractNumberInput()).to.eq(
      'contractNumber',
      'Expected ContractNumber value to be equals to contractNumber'
    );

    await participantUpdatePage.save();
    expect(await participantUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await participantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Participant', async () => {
    const nbButtonsBeforeDelete = await participantComponentsPage.countDeleteButtons();
    await participantComponentsPage.clickOnLastDeleteButton();

    participantDeleteDialog = new ParticipantDeleteDialog();
    expect(await participantDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.participant.delete.question');
    await participantDeleteDialog.clickOnConfirmButton();

    expect(await participantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
