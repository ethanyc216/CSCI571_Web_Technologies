import { Component, OnInit, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { NgForm, FormGroup, FormControl, FormBuilder, Validators } from "@angular/forms";
import { SearchService } from "../../service/search.service";
import {Observable} from 'rxjs';
import {startWith, map} from 'rxjs/operators';

@Component({
  selector: 'app-hourly',
  templateUrl: './hourly.component.html',
  styleUrls: ['./hourly.component.css']
})
export class HourlyComponent implements OnInit {

  resultsJson = null;

  hourlyDisForm: FormGroup;
  max = 90;
  min = 50;
  stepSize = 5; 

  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true,
    legend: {onClick: (e) => e.stopPropagation()},
    scales: {yAxes: [{scaleLabel: { display: true, labelString: 'Fahrenheit', fontColor: 'rgb(91, 91, 91)'}, ticks: {max: this.max, min: this.min, stepSize: this.stepSize}}], xAxes: [{scaleLabel: { display: true, labelString: 'Time difference from current hour', fontColor: 'rgb(91, 91, 91)' }}]}
  };

  public barChartLabels = [];
  public barChartType = 'bar';
  public barChartLegend = true;
  public barChartColors = [
    { backgroundColor: 'rgb(145, 202, 238)' }
  ]

  public barChartData = [];

  get fd() { return this.hourlyDisForm.controls; }

  constructor(private searchService: SearchService, private fb: FormBuilder, private cdRef: ChangeDetectorRef) {
    this.searchService.getResultsJson$.subscribe(data => {
      this.hourlyDisForm.get('hourlyDis').setValue('hourlyTemperature');
      this.resultsJson = data;
      this.barChartLabels = data['hourlyTime'];
      this.barChartData.length = 0;
      this.max = Math.ceil(Math.max(...data['hourlyTemperature'])+2);
      this.min = Math.floor(Math.min(...data['hourlyTemperature'])-5);   
      let range =  this.max - this.min;
      if (1.0*range/10 <= 1) {
        this.stepSize =  1;
      } 
      else if (1.0*range/10 <= 2) {
        this.stepSize =  2;
        this.min = this.min + this.min%2;
        range =  this.max - this.min;
        this.max = this.max + range%2;
      }
      else {
        this.stepSize =  5;
        this.min = this.min - this.min%5;
        range =  this.max - this.min;
        this.max = this.max + (5 - range%5);
      } 
      this.barChartData.push({data: data['hourlyTemperature'], label: 'temperature'});
      this.barChartOptions = { legend: {onClick: (e) => e.stopPropagation()}, scaleShowVerticalLines: false, responsive: true, scales: {yAxes: [{scaleLabel: { display: true, labelString: 'Fahrenheit', fontColor: 'rgb(91, 91, 91)'}, ticks: {max: this.max, min: this.min, stepSize: this.stepSize}}], xAxes: [{scaleLabel: { display: true, labelString: 'Time difference from current hour', fontColor: 'rgb(91, 91, 91)' }}]}};
      this.barChartColors = [{ backgroundColor: 'rgb(145, 202, 238)' }];
    });
  }

  getDis(mode) {
    this.barChartData.length = 0;
    if (mode == 'hourlyTemperature') {
      this.barChartData.push({data: this.resultsJson['hourlyTemperature'], label: 'temperature'});
      this.max = Math.ceil(Math.max(...this.resultsJson['hourlyTemperature'])+2);
      this.min = Math.floor(Math.min(...this.resultsJson['hourlyTemperature'])-5);  
      let range =  this.max - this.min;
      if (1.0*range/10 <= 1) {
        this.stepSize =  1;
      } 
      else if (1.0*range/10 <= 2) {
        this.stepSize =  2;
        this.min = this.min + this.min%2;
        range =  this.max - this.min;
        this.max = this.max + range%2;
      }
      else {
        this.stepSize =  5;
        this.min = this.min - this.min%5;
        range =  this.max - this.min;
        this.max = this.max + (5 - range%5);
      }  
      this.barChartOptions = { legend: {onClick: (e) => e.stopPropagation()}, scaleShowVerticalLines: false, responsive: true, scales: {yAxes: [{scaleLabel: { display: true, labelString: 'Fahrenheit', fontColor: 'rgb(91, 91, 91)'}, ticks: {max: this.max, min: this.min, stepSize: this.stepSize}}], xAxes: [{scaleLabel: { display: true, labelString: 'Time difference from current hour', fontColor: 'rgb(91, 91, 91)' }}]}};
    }
    else if (mode == 'hourlyPressure') {
      this.barChartData.push({data: this.resultsJson['hourlyPressure'], label: 'pressure'});
      this.max = Math.ceil(Math.max(...this.resultsJson['hourlyPressure'])+2);
      this.min = Math.max(Math.floor(Math.min(...this.resultsJson['hourlyPressure'])-5), 0);   
      let range =  this.max - this.min;
      if (1.0*range/10 <= 1) {
        this.stepSize =  1;
      } 
      else if (1.0*range/10 <= 2) {
        this.stepSize =  2;
        this.min = this.min + this.min%2;
        range =  this.max - this.min;
        this.max = this.max + range%2;
      }
      else {
        this.stepSize =  5;
        this.min = this.min - this.min%5;
        range =  this.max - this.min;
        this.max = this.max + (5 - range%5);
      } 
      this.barChartOptions = { legend: {onClick: (e) => e.stopPropagation()}, scaleShowVerticalLines: false, responsive: true, scales: {yAxes: [{scaleLabel: { display: true, labelString: 'Millibars', fontColor: 'rgb(91, 91, 91)'}, ticks: {max: this.max, min: this.min, stepSize: this.stepSize}}], xAxes: [{scaleLabel: { display: true, labelString: 'Time difference from current hour', fontColor: 'rgb(91, 91, 91)'}}]}};
    }
    else if (mode == 'hourlyHumidity') {
      this.barChartData.push({data: this.resultsJson['hourlyHumidity'], label: 'humidity'});
      this.max = Math.ceil(Math.max(...this.resultsJson['hourlyHumidity'])+2);
      this.min = Math.max(Math.floor(Math.min(...this.resultsJson['hourlyHumidity'])-5), 0);   
      let range =  this.max - this.min;
      if (1.0*range/10 <= 1) {
        this.stepSize =  1;
      } 
      else if (1.0*range/10 <= 2) {
        this.stepSize =  2;
        this.min = this.min + this.min%2;
        range =  this.max - this.min;
        this.max = this.max + range%2;
      }
      else {
        this.stepSize =  5;
        this.min = this.min - this.min%5;
        range =  this.max - this.min;
        this.max = this.max + (5 - range%5);
      } 
      this.barChartOptions = { legend: {onClick: (e) => e.stopPropagation()}, scaleShowVerticalLines: false, responsive: true, scales: {yAxes: [{scaleLabel: { display: true, labelString: '% Humidity', fontColor: 'rgb(91, 91, 91)'}, ticks: {max: this.max, min: this.min, stepSize: this.stepSize}}], xAxes: [{scaleLabel: { display: true, labelString: 'Time difference from current hour', fontColor: 'rgb(91, 91, 91)' }}]}};
    }
    else if (mode == 'hourlyOzone') {
      this.barChartData.push({data: this.resultsJson['hourlyOzone'], label: 'ozone'});
      this.max = Math.ceil(Math.max(...this.resultsJson['hourlyOzone'])+2);
      this.min = Math.max(Math.floor(Math.min(...this.resultsJson['hourlyOzone'])-5), 0); 
      let range =  this.max - this.min;
      if (1.0*range/10 <= 1) {
        this.stepSize =  1;
      } 
      else if (1.0*range/10 <= 2) {
        this.stepSize =  2;
        this.min = this.min + this.min%2;
        range =  this.max - this.min;
        this.max = this.max + range%2;
      }
      else {
        this.stepSize =  5;
        this.min = this.min - this.min%5;
        range =  this.max - this.min;
        this.max = this.max + (5 - range%5);
      }   
      this.barChartOptions = { legend: {onClick: (e) => e.stopPropagation()}, scaleShowVerticalLines: false, responsive: true, scales: {yAxes: [{scaleLabel: { display: true, labelString: 'Dobson Unit', fontColor: 'rgb(91, 91, 91)'}, ticks: {max: this.max, min: this.min, stepSize: this.stepSize}}], xAxes: [{scaleLabel: { display: true, labelString: 'Time difference from current hour', fontColor: 'rgb(91, 91, 91)' }}]}};
    }
    else if (mode == 'hourlyVisibility') {
      this.barChartData.push({data: this.resultsJson['hourlyVisibility'], label: 'visibility'});
      this.max = Math.ceil(Math.max(...this.resultsJson['hourlyVisibility'])+2);
      this.min = Math.max(Math.floor(Math.min(...this.resultsJson['hourlyVisibility'])-5), 0);  
      let range =  this.max - this.min;
      if (1.0*range/10 <= 1) {
        this.stepSize =  1;
      } 
      else if (1.0*range/10 <= 2) {
        this.stepSize =  2;
        this.min = this.min + this.min%2;
        range =  this.max - this.min;
        this.max = this.max + range%2;
      }
      else {
        this.stepSize =  5;
        this.min = this.min - this.min%5;
        range =  this.max - this.min;
        this.max = this.max + (5 - range%5);
      } 
      this.barChartOptions = { legend: {onClick: (e) => e.stopPropagation()}, scaleShowVerticalLines: false, responsive: true, scales: {yAxes: [{scaleLabel: { display: true, labelString: 'Miles (Maximum 10)', fontColor: 'rgb(91, 91, 91)'}, ticks: {max: this.max, min: this.min, stepSize: this.stepSize}}], xAxes: [{scaleLabel: { display: true, labelString: 'Time difference from current hour', fontColor: 'rgb(91, 91, 91)' }}]}};
    }
    else if (mode == 'hourlyWindSpeed') {
      this.barChartData.push({data: this.resultsJson['hourlyWindSpeed'], label: 'windSpeed'});
      this.max = Math.ceil(Math.max(...this.resultsJson['hourlyWindSpeed'])+2);
      this.min = Math.max(Math.floor(Math.min(...this.resultsJson['hourlyWindSpeed'])-5), 0);  
      let range =  this.max - this.min;
      if (1.0*range/10 <= 1) {
        this.stepSize =  1;
      } 
      else if (1.0*range/10 <= 2) {
        this.stepSize =  2;
        this.min = this.min + this.min%2;
        range =  this.max - this.min;
        this.max = this.max + range%2;
      }
      else {
        this.stepSize =  5;
        this.min = this.min - this.min%5;
        range =  this.max - this.min;
        this.max = this.max + (5 - range%5);
      } 
      this.barChartOptions = { legend: {onClick: (e) => e.stopPropagation()}, scaleShowVerticalLines: false, responsive: true, scales: {yAxes: [{scaleLabel: { display: true, labelString: 'Miles per Hour', fontColor: 'rgb(91, 91, 91)'}, ticks: {max: this.max, min: this.min, stepSize: this.stepSize}}], xAxes: [{scaleLabel: { display: true, labelString: 'Time difference from current hour', fontColor: 'rgb(91, 91, 91)' }}]}};
    }
    this.barChartColors = [{ backgroundColor: 'rgb(145, 202, 238)' }];
  }

  onChangesDis() {
    this.getDis(this.hourlyDisForm.get('hourlyDis').value);
  }

  ngOnInit() {
    this.hourlyDisForm = this.fb.group({
      hourlyDis: 'hourlyTemperature'
    });
  }

}
