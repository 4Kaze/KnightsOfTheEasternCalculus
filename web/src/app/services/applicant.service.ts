import { Injectable } from '@angular/core';
import { Applicant } from '../model/applicant';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import * as Globals from '../app-consts';
import 'rxjs-compat/add/operator/catch';

@Injectable({
  providedIn: 'root'
})
export class ApplicantService {

  public apiUrl = Globals.apiBaseUrl + '/applicants';

  constructor(public http: HttpClient) {}

  // supposed to return 10 of the latest added applicants
  getAllApplicants(): Observable<Applicant[]> {
    return this.http.get<Applicant[]>(this.apiUrl);
  }

  getApplicants(lastName: string): Observable<Applicant[]> {
    const params = new HttpParams().set('lastName', lastName);
    return this.http.get<Applicant[]>(this.apiUrl, { params });
  }

  getApplicant(id: string): Observable<Applicant> {
    return this.http.get<Applicant>(this.apiUrl + `/${id}`);
  }

}
