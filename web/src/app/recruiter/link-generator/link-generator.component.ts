import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticatorComponent} from 'aws-amplify-angular/dist/src/components/authenticator/authenticator/authenticator.factory';
import {AuthenticationUserService} from '../../services/authentication-user.service';
import {AuthenticationRecruiterService} from '../../services/authentication-recruiter.service';

@Component({
  selector: 'app-link-generator',
  templateUrl: './link-generator.component.html',
  styleUrls: ['./link-generator.component.scss']
})
export class LinkGeneratorComponent implements OnInit {
  private mail: string;
  private password: string;

  constructor(private router: Router, private authService: AuthenticationRecruiterService) { }

  ngOnInit() {
  }

  send() {
    this.authService.sendMail(this.mail).subscribe(result => {
      console.log('link-get');
      this.router.navigateByUrl('/');
    }, err => {
      console.log('error', err);
    });
  }
}
