import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AppApplicantComponent} from './app-applicant/app-applicant.component';
import {HomePageComponent} from './home-page/home-page.component';
import {MyProfileComponent} from './my-profile/my-profile.component';
import {TestListComponent} from './test-list/test-list.component';
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
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplicantRoutingModule { }