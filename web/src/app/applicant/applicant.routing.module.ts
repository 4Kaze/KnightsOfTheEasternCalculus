import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AppApplicantComponent} from './app-applicant/app-applicant.component';
import {HomePageComponent} from './home-page/home-page.component';
import {MyProfileComponent} from './my-profile/my-profile.component';
import {TestListComponent} from './test-list/test-list.component';
import {SolvedTestComponent} from './solved-test/solved-test.component';
import {SolveTestComponent} from './solve-test/solve-test.component';
import { IdGuardService } from './id-guard.service';
const routes: Routes = [
  {
    path: '', component: AppApplicantComponent, children: [
      {
        path: '',
        component: HomePageComponent
      },
      {
        path: 'profile',
        component: MyProfileComponent
      },
      {
        path: 'tests',
        component: TestListComponent
      },
      {
        path: 'tests/:id',
        component: SolvedTestComponent,
        data: {id: ':id'}
      },
      {
        path: 'solve-test/:id',
        component: SolveTestComponent,
        canActivate: [IdGuardService],
        data: {id: 'id'}
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplicantRoutingModule { }
