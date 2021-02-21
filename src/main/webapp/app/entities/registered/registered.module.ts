import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItcenterbazaSharedModule } from 'app/shared/shared.module';
import { RegisteredComponent } from './registered.component';
import { RegisteredDetailComponent } from './registered-detail.component';
import { RegisteredUpdateComponent } from './registered-update.component';
import { RegisteredDeleteDialogComponent } from './registered-delete-dialog.component';
import { registeredRoute } from './registered.route';

@NgModule({
  imports: [ItcenterbazaSharedModule, RouterModule.forChild(registeredRoute)],
  declarations: [RegisteredComponent, RegisteredDetailComponent, RegisteredUpdateComponent, RegisteredDeleteDialogComponent],
  entryComponents: [RegisteredDeleteDialogComponent],
})
export class ItcenterbazaRegisteredModule {}
