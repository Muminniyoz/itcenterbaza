import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItcenterbazaSharedModule } from 'app/shared/shared.module';
import { ParticipantComponent } from './participant.component';
import { ParticipantDetailComponent } from './participant-detail.component';
import { ParticipantUpdateComponent } from './participant-update.component';
import { ParticipantDeleteDialogComponent } from './participant-delete-dialog.component';
import { participantRoute } from './participant.route';

@NgModule({
  imports: [ItcenterbazaSharedModule, RouterModule.forChild(participantRoute)],
  declarations: [ParticipantComponent, ParticipantDetailComponent, ParticipantUpdateComponent, ParticipantDeleteDialogComponent],
  entryComponents: [ParticipantDeleteDialogComponent],
})
export class ItcenterbazaParticipantModule {}
