import { element, by, ElementFinder } from 'protractor';

export class CourseComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-course div table .btn-danger'));
  title = element.all(by.css('jhi-course div h2#page-heading span')).first();
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

export class CourseUpdatePage {
  pageTitle = element(by.id('jhi-course-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  titleInput = element(by.id('field_title'));
  priceInput = element(by.id('field_price'));
  startDateInput = element(by.id('field_startDate'));
  statusSelect = element(by.id('field_status'));
  durationInput = element(by.id('field_duration'));
  planFileInput = element(by.id('file_planFile'));

  teacherSelect = element(by.id('field_teacher'));
  centerSelect = element(by.id('field_center'));
  skillSelect = element(by.id('field_skill'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTitleInput(title: string): Promise<void> {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput(): Promise<string> {
    return await this.titleInput.getAttribute('value');
  }

  async setPriceInput(price: string): Promise<void> {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput(): Promise<string> {
    return await this.priceInput.getAttribute('value');
  }

  async setStartDateInput(startDate: string): Promise<void> {
    await this.startDateInput.sendKeys(startDate);
  }

  async getStartDateInput(): Promise<string> {
    return await this.startDateInput.getAttribute('value');
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

  async setDurationInput(duration: string): Promise<void> {
    await this.durationInput.sendKeys(duration);
  }

  async getDurationInput(): Promise<string> {
    return await this.durationInput.getAttribute('value');
  }

  async setPlanFileInput(planFile: string): Promise<void> {
    await this.planFileInput.sendKeys(planFile);
  }

  async getPlanFileInput(): Promise<string> {
    return await this.planFileInput.getAttribute('value');
  }

  async teacherSelectLastOption(): Promise<void> {
    await this.teacherSelect.all(by.tagName('option')).last().click();
  }

  async teacherSelectOption(option: string): Promise<void> {
    await this.teacherSelect.sendKeys(option);
  }

  getTeacherSelect(): ElementFinder {
    return this.teacherSelect;
  }

  async getTeacherSelectedOption(): Promise<string> {
    return await this.teacherSelect.element(by.css('option:checked')).getText();
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

  async skillSelectLastOption(): Promise<void> {
    await this.skillSelect.all(by.tagName('option')).last().click();
  }

  async skillSelectOption(option: string): Promise<void> {
    await this.skillSelect.sendKeys(option);
  }

  getSkillSelect(): ElementFinder {
    return this.skillSelect;
  }

  async getSkillSelectedOption(): Promise<string> {
    return await this.skillSelect.element(by.css('option:checked')).getText();
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

export class CourseDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-course-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-course'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
