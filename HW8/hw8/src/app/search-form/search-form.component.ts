import { Component, OnInit, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { NgForm, FormGroup, FormControl, FormBuilder, Validators } from "@angular/forms";
import { SearchService } from "../service/search.service";
import {Observable} from 'rxjs';
import {startWith, map} from 'rxjs/operators';



@Component({
  selector: 'app-search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.css']
})
export class SearchFormComponent implements OnInit {
  currentLocation: Object;
  gotgeojson: boolean = false;
  searchForm: FormGroup;

  cities = [];
  filteredCities: Observable<string[]>;

  constructor(private searchService: SearchService, private cdRef: ChangeDetectorRef, private fb: FormBuilder) { 
  }

  clearsh() {
    this.cities = [];
    this.filteredCities = this.searchForm.get('current_location').valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value))
    );
    this.searchService.search(this.fb.group({street: ['test', Validators.required], city: ['Los Angeles', Validators.required], state: ['CA', Validators.required], current_location: false}), this.currentLocation, true);
  }

  getCurrentGeo() {
    this.searchService.getCurrentGeo().subscribe(data => {
      this.currentLocation = {
        lat: data["lat"],
        lng: data["lon"],
        city: data["city"],
        state: data["region"]
      };
      this.gotgeojson = true;
    });
  }

  getAutocomplete(city) {
    this.searchService.getAutocomplete(city).subscribe(data => {
      this.cities.length = 0;
      let array = data['predictions'];
      for (let i = 0; i < Math.min(5, array.length); i++) {
        this.cities.push(array[i]['structured_formatting']['main_text']);
      }
      this.filteredCities = this.searchForm.get('current_location').valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value))
      );
    });
  }

  get f() { return this.searchForm.controls; }

  onSubmit(){
    this.searchService.search(this.searchForm, this.currentLocation, false);
  }

  onChangesCurrentLocation() {
    this.searchForm.get('current_location').valueChanges
    .subscribe(checked => {
      if (checked) {
        this.searchForm.get('street').disable();
        this.searchForm.get('city').disable();
        this.searchForm.get('state').disable();
      }
      else {
        this.searchForm.get('street').enable();
        this.searchForm.get('city').enable();
        this.searchForm.get('state').enable();
      }
    });
  }

  onChangesCity() {
    if (this.searchForm.get('city').value) {
      this.getAutocomplete(this.searchForm.get('city').value);
    }
  }

  ngOnInit() {
    this.searchForm = this.fb.group({
      street: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      current_location: false
    });

    this.getCurrentGeo();
    this.onChangesCurrentLocation();

    this.searchService.search(this.fb.group({street: ['test', Validators.required], city: ['la', Validators.required], state: ['CA', Validators.required], current_location: false}), this.currentLocation, true);
  }

  private _filter(value: string): string[] {
    const filterValue = this._normalizeValue(value);
    return this.cities.filter(city => this._normalizeValue(city).includes(filterValue));
  }

  private _normalizeValue(value: string): string {
    return value.toLowerCase().replace(/\s/g, '');
  }
}
