import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SkillComponentsPage, SkillDeleteDialog, SkillUpdatePage } from './skill.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Skill e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let skillComponentsPage: SkillComponentsPage;
  let skillUpdatePage: SkillUpdatePage;
  let skillDeleteDialog: SkillDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Skills', async () => {
    await navBarPage.goToEntity('skill');
    skillComponentsPage = new SkillComponentsPage();
    await browser.wait(ec.visibilityOf(skillComponentsPage.title), 5000);
    expect(await skillComponentsPage.getTitle()).to.eq('itcenterbazaApp.skill.home.title');
    await browser.wait(ec.or(ec.visibilityOf(skillComponentsPage.entities), ec.visibilityOf(skillComponentsPage.noResult)), 1000);
  });

  it('should load create Skill page', async () => {
    await skillComponentsPage.clickOnCreateButton();
    skillUpdatePage = new SkillUpdatePage();
    expect(await skillUpdatePage.getPageTitle()).to.eq('itcenterbazaApp.skill.home.createOrEditLabel');
    await skillUpdatePage.cancel();
  });

  it('should create and save Skills', async () => {
    const nbButtonsBeforeCreate = await skillComponentsPage.countDeleteButtons();

    await skillComponentsPage.clickOnCreateButton();

    await promise.all([
      skillUpdatePage.setTitleUzInput('titleUz'),
      skillUpdatePage.setTitleRuInput('titleRu'),
      skillUpdatePage.setTitleEnInput('titleEn'),
      skillUpdatePage.setAboutInput('about'),
      skillUpdatePage.setPlanFileInput(absolutePath),
    ]);

    expect(await skillUpdatePage.getTitleUzInput()).to.eq('titleUz', 'Expected TitleUz value to be equals to titleUz');
    expect(await skillUpdatePage.getTitleRuInput()).to.eq('titleRu', 'Expected TitleRu value to be equals to titleRu');
    expect(await skillUpdatePage.getTitleEnInput()).to.eq('titleEn', 'Expected TitleEn value to be equals to titleEn');
    expect(await skillUpdatePage.getAboutInput()).to.eq('about', 'Expected About value to be equals to about');
    expect(await skillUpdatePage.getPlanFileInput()).to.endsWith(
      fileNameToUpload,
      'Expected PlanFile value to be end with ' + fileNameToUpload
    );

    await skillUpdatePage.save();
    expect(await skillUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await skillComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Skill', async () => {
    const nbButtonsBeforeDelete = await skillComponentsPage.countDeleteButtons();
    await skillComponentsPage.clickOnLastDeleteButton();

    skillDeleteDialog = new SkillDeleteDialog();
    expect(await skillDeleteDialog.getDialogTitle()).to.eq('itcenterbazaApp.skill.delete.question');
    await skillDeleteDialog.clickOnConfirmButton();

    expect(await skillComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
