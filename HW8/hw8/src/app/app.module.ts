import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchFormComponent } from './search-form/search-form.component';
import { ResultsComponent } from './results/results.component';

import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from '@angular/common/http';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { CurrentComponent } from './tabs/current/current.component';
import { HourlyComponent } from './tabs/hourly/hourly.component';
import { WeeklyComponent, NgbdModalContent } from './tabs/weekly/weekly.component';
import { TabsComponent } from './tabs/tabs.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import { ChartsModule } from 'ng2-charts';
import { DatePipe } from '@angular/common';
import { SrComponent } from './sr/sr.component';
import { FavComponent } from './fav/fav.component';
import { LoaderComponent } from './loader/loader.component';

@NgModule({
  declarations: [
    AppComponent,
    SearchFormComponent,
    ResultsComponent,
    CurrentComponent,
    HourlyComponent,
    WeeklyComponent,
    TabsComponent,
    NgbdModalContent,
    SrComponent,
    FavComponent,
    LoaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    NgbModule,
    ChartsModule
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent],
  entryComponents: [NgbdModalContent]
})
export class AppModule { }
