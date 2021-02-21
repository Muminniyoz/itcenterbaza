import { element, by, ElementFinder } from 'protractor';

export class SkillComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-skill div table .btn-danger'));
  title = element.all(by.css('jhi-skill div h2#page-heading span')).first();
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

export class SkillUpdatePage {
  pageTitle = element(by.id('jhi-skill-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  titleUzInput = element(by.id('field_titleUz'));
  titleRuInput = element(by.id('field_titleRu'));
  titleEnInput = element(by.id('field_titleEn'));
  aboutInput = element(by.id('field_about'));
  planFileInput = element(by.id('file_planFile'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTitleUzInput(titleUz: string): Promise<void> {
    await this.titleUzInput.sendKeys(titleUz);
  }

  async getTitleUzInput(): Promise<string> {
    return await this.titleUzInput.getAttribute('value');
  }

  async setTitleRuInput(titleRu: string): Promise<void> {
    await this.titleRuInput.sendKeys(titleRu);
  }

  async getTitleRuInput(): Promise<string> {
    return await this.titleRuInput.getAttribute('value');
  }

  async setTitleEnInput(titleEn: string): Promise<void> {
    await this.titleEnInput.sendKeys(titleEn);
  }

  async getTitleEnInput(): Promise<string> {
    return await this.titleEnInput.getAttribute('value');
  }

  async setAboutInput(about: string): Promise<void> {
    await this.aboutInput.sendKeys(about);
  }

  async getAboutInput(): Promise<string> {
    return await this.aboutInput.getAttribute('value');
  }

  async setPlanFileInput(planFile: string): Promise<void> {
    await this.planFileInput.sendKeys(planFile);
  }

  async getPlanFileInput(): Promise<string> {
    return await this.planFileInput.getAttribute('value');
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

export class SkillDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-skill-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-skill'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
