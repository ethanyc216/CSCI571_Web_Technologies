import { Component, OnInit } from '@angular/core';
import { SearchService } from "../../service/search.service";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

declare var $:any;
$(document).ready(function() {
  $('[data-toggle="tooltip"]').tooltip();   
});

@Component({
  selector: 'app-current',
  templateUrl: './current.component.html',
  styleUrls: ['./current.component.css']
})

export class CurrentComponent implements OnInit {

  resultsJson = null;

  private icons = [
    { title: "Humidity", field: "currentHumidity", iconp: "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-16-512.png" },
    { title: "Pressure", field: "currentPressure", iconp: "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-25-512.png" },
    { title: "Wind Speed", field: "currentWindSpeed", iconp: "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-27-512.png" },
    { title: "Visibility", field: "currentVisibility", iconp: "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-30-512.png" },
    { title: "CloudCover", field: "currentCloudCover", iconp: "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-28-512.png" },
    { title: "Ozone", field: "currentOzone", iconp: "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-24-512.png" }
  ];

  constructor(private searchService: SearchService) {
    this.searchService.getResultsJson$.subscribe(data => {
      this.resultsJson = data;
    });
  }

  ngOnInit() {
  }

}
