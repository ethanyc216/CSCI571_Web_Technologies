import { Component, OnInit, Output, EventEmitter, ChangeDetectorRef, Input } from '@angular/core';
import { NgForm, FormGroup, FormControl, FormBuilder, Validators } from "@angular/forms";
import { SearchService } from "../../service/search.service";
import {Observable} from 'rxjs';
import {startWith, map} from 'rxjs/operators';
import * as CanvasJS from '../../../assets/canvasjs.min';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'ngbd-modal-content',
  styleUrls: ['./weekly.component.css'],
  template: `
    <div class="modal-header" style="background-color: rgb(91,135,162)">
      <h4 class="modal-title">{{date}}</h4>
      <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body container" ngbAutofocus style="background-color: rgb(147,202,238);">
      <table class="table table-borderless">
        <tr>
          <td>
            <h4>{{city}}</h4>
            <h2>{{temperature}}<img src="https://cdn3.iconfinder.com/data/icons/virtual-notebook/16/button_shape_oval-512.png" style="width: 3%; length: 3%; margin-top: -10%"> F</h2>
            <h6>{{summary}}</h6>
          </td>
          <td>
            <img src={{icon}} class="imggg">
          </td>
        <tr>
      </table>
      <hr>
      <div class="class1">
        <h6>Precipitation : {{precipIntensity}}</h6>
        <h6>Chance of Rain : {{precipProbability}} %</h6>
        <h6>Wind Speed : {{windSpeed}} mph</h6>
        <h6>Humidity : {{humidity}} %</h6>
        <h6>Visibility : {{visibility}} miles</h6>
      </div>
    </div>
  `
})
export class NgbdModalContent {

  @Input() date;
  @Input() city;
  @Input() temperature;
  @Input() summary;
  @Input() icon;
  @Input() precipIntensity;
  @Input() precipProbability;
  @Input() windSpeed;
  @Input() humidity;
  @Input() visibility;
  
  constructor(public activeModal: NgbActiveModal) {}
}



@Component({
  selector: 'app-weekly',
  templateUrl: './weekly.component.html',
  styleUrls: ['./weekly.component.css']
})
export class WeeklyComponent implements OnInit {

  resultsJson = null;


  constructor(private searchService: SearchService, private fb: FormBuilder, private cdRef: ChangeDetectorRef, private modalService: NgbModal) {
    this.searchService.getResultsJson$.subscribe(data => {
      this.resultsJson = data;
      let dataPointss = [];
      for (let i = 0; i < data["weeklyTime"].length; i++) {
        dataPointss.push({ x: (i+1)*10, y:[data["weeklyTemperatureLow"][i], data["weeklyTemperatureHigh"][i]], label: data["weeklyTime"][i], dori: data["weeklyTimeOri"][i], lat: data["lat"], lng: data["lng"], city: data["city"] });
      }

      this.searchService.getactiveId$.subscribe(data2 => {
        let chart = new CanvasJS.Chart("chartContainer", {
        animationEnabled: true,
        exportEnabled: false,
        zoomEnabled: true,
        title: {
          text: "Weekly Weather"
        },
        dataPointWidth: 17,
        axisX: {
          title: "Days"
        },
        axisY: {
          includeZero: false,
          title: "Temperature in Fahrenheit",
          interval: 10,
          gridThickness: 0
        }, 
        legend: { verticalAlign: "top" },
        data: [{
          type: "rangeBar",
          showInLegend: true,
          click: (e) => {this.opens(e.dataPoint.lat, e.dataPoint.lng, e.dataPoint.dori, e.dataPoint.label, e.dataPoint.city); },
          color: "rgb(145, 202, 238)",
          indexLabel: "{y[#index]}",
          legendText: "Day wise temperature range",
          toolTipContent: "<b>{label}</b>: {y[0]} to {y[1]}",
          dataPoints: dataPointss
        }]
      });
        setTimeout(function(){ 
      chart.render();
      }, 2000);
      });
    });
  }

  opens(lat, lng, dori, date, city) {
    this.searchService.getModal(lat, lng, dori).subscribe(data => {
      const modalRef = this.modalService.open(NgbdModalContent);
      modalRef.componentInstance.date = date;
      modalRef.componentInstance.city = city;
      modalRef.componentInstance.temperature = Math.round(data['currently']['temperature']);
      modalRef.componentInstance.summary = data['currently']['summary'];
      if (data['currently']['icon'] == 'clear-day' || data['currently']['icon'] == 'clear-night') {
        modalRef.componentInstance.icon = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/sun-512.png';
      }
      else if (data['currently']['icon'] == 'rain') {
        modalRef.componentInstance.icon = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/rain-512.png';
      }
      else if (data['currently']['icon'] == 'snow') {
        modalRef.componentInstance.icon = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/snow-512.png';
      }
      else if (data['currently']['icon'] == 'sleet') {
        modalRef.componentInstance.icon = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/lightning-512.png';
      }
      else if (data['currently']['icon'] == 'wind') {
        modalRef.componentInstance.icon = 'https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_10-512.png';
      }
      else if (data['currently']['icon'] == 'fog') {
        modalRef.componentInstance.icon = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/cloudy-512.png';
      }
      else if (data['currently']['icon'] == 'cloudy') {
        modalRef.componentInstance.icon = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/cloud-512.png';
      }
      else if (data['currently']['icon'] == 'partly-cloudy-day' || data['currently']['icon'] == 'partly-cloudy-night') {
        modalRef.componentInstance.icon = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/sunny-512.png';
      }
      else {
        modalRef.componentInstance.icon = '';
      }

      if (!(data['currently']['precipIntensity']) && data['currently']['precipIntensity'] != 0) {
        modalRef.componentInstance.precipIntensity = 'N/A';
      }
      else {
        if (data['currently']['precipIntensity'].toFixed(2) == 0) {
          modalRef.componentInstance.precipIntensity = 0;
        }
        else {
          if (data['currently']['precipIntensity'].toFixed(2) % 1 == 0) {
            modalRef.componentInstance.precipIntensity = Math.round(data['currently']['precipIntensity'].toFixed(2));
          }
          else {
            modalRef.componentInstance.precipIntensity = data['currently']['precipIntensity'].toFixed(2);
          }
        }
      }
      if (!(data['currently']['precipProbability']) && data['currently']['precipProbability'] != 0) {
        modalRef.componentInstance.precipProbability = 'N/A';
      }
      else {
        if (Math.round(data['currently']['precipProbability']*100) == 0) {
          modalRef.componentInstance.precipProbability = 0;
        }
        else {
          modalRef.componentInstance.precipProbability = Math.round(data['currently']['precipProbability']*100);
        }
      }
      if (!(data['currently']['windSpeed']) && data['currently']['windSpeed'] != 0) {
        modalRef.componentInstance.windSpeed = 'N/A';
      }
      else {
        if (data['currently']['windSpeed'].toFixed(2) == 0) {
          modalRef.componentInstance.windSpeed = 0;
        }
        else {
          if (data['currently']['windSpeed'].toFixed(2) % 1 == 0) {
            modalRef.componentInstance.windSpeed = Math.round(data['currently']['windSpeed'].toFixed(2));
          }
          else {
            modalRef.componentInstance.windSpeed = data['currently']['windSpeed'].toFixed(2);
          }
        }
      }
      if (!(data['currently']['humidity']) && data['currently']['humidity'] != 0) {
        modalRef.componentInstance.humidity = 'N/A';
      }
      else {
        if (Math.round(data['currently']['humidity']*100) == 0) {
          modalRef.componentInstance.humidity = 0;
        }
        else {
          modalRef.componentInstance.humidity = Math.round(data['currently']['humidity']*100);
        }
      }
      if (!(data['currently']['visibility']) && data['currently']['visibility'] != 0) {
        modalRef.componentInstance.visibility = 'N/A';
      }
      else {
        if (data['currently']['visibility'] == 0) {
          modalRef.componentInstance.visibility = 0;
        }
        else {
          if (data['currently']['visibility'].toFixed(2) % 1 == 0) {
            modalRef.componentInstance.visibility = Math.round(data['currently']['visibility'].toFixed(2));
          }
          else {
            modalRef.componentInstance.visibility = data['currently']['visibility'].toFixed(2);
          }
        } 
      }
    });
  }

  ngOnInit() {
  }

}
