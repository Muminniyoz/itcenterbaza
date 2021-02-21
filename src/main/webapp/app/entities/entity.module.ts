import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'regions',
        loadChildren: () => import('./regions/regions.module').then(m => m.ItcenterbazaRegionsModule),
      },
      {
        path: 'center',
        loadChildren: () => import('./center/center.module').then(m => m.ItcenterbazaCenterModule),
      },
      {
        path: 'skill',
        loadChildren: () => import('./skill/skill.module').then(m => m.ItcenterbazaSkillModule),
      },
      {
        path: 'teacher',
        loadChildren: () => import('./teacher/teacher.module').then(m => m.ItcenterbazaTeacherModule),
      },
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.ItcenterbazaCourseModule),
      },
      {
        path: 'registered',
        loadChildren: () => import('./registered/registered.module').then(m => m.ItcenterbazaRegisteredModule),
      },
      {
        path: 'student',
        loadChildren: () => import('./student/student.module').then(m => m.ItcenterbazaStudentModule),
      },
      {
        path: 'participant',
        loadChildren: () => import('./participant/participant.module').then(m => m.ItcenterbazaParticipantModule),
      },
      {
        path: 'payment-method-config',
        loadChildren: () =>
          import('./payment-method-config/payment-method-config.module').then(m => m.ItcenterbazaPaymentMethodConfigModule),
      },
      {
        path: 'payment-method',
        loadChildren: () => import('./payment-method/payment-method.module').then(m => m.ItcenterbazaPaymentMethodModule),
      },
      {
        path: 'system-config',
        loadChildren: () => import('./system-config/system-config.module').then(m => m.ItcenterbazaSystemConfigModule),
      },
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.ItcenterbazaPaymentModule),
      },
      {
        path: 'event-history',
        loadChildren: () => import('./event-history/event-history.module').then(m => m.ItcenterbazaEventHistoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class ItcenterbazaEntityModule {}
