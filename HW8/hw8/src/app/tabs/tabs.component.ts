import { Component, OnInit } from '@angular/core';
import { SearchService } from "../service/search.service";
import { FavoritesService } from "../service/favorites.service";

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.css']
})
export class TabsComponent implements OnInit {

  resultsJson = null;

  isFavorite = false;
  stari = 'star_border';

	private tabs = [
    { id: "current-tab", title: "Current" },
    { id: "hourly-tab", title: "Hourly" },
    { id: "weekly-tab", title: "Weekly" }
  ];

  resTabs: any;

  private activeId = "current-tab";

  setActiveId(id) {
    this.activeId = id;
    this.searchService.activeIdSS(this.activeId);
  }

  constructor(private searchService: SearchService, private favoritesService: FavoritesService) {
    this.searchService.getResultsJson$.subscribe(data => {
      this.resultsJson = data;
      this.setActiveId('current-tab');
      this.checkFavorite();
    });

    this.searchService.getresbtn$.subscribe(data => {
      this.checkFavorite();
    });
  }

  setFavorite() {
    if (this.isFavorite) {
      this.favoritesService.removeFavorite(this.resultsJson['city']+this.resultsJson['state']);
      this.isFavorite = false;
      this.stari = 'star_border';
    } else {
      this.favoritesService.saveFavorite(
        this.resultsJson["lat"],
        this.resultsJson["lng"],
        this.resultsJson["city"],
        this.resultsJson["state"],
        this.resultsJson["seal"]
      );
      this.isFavorite = true;
      this.stari = 'star';
    }
  }

  checkFavorite() {
    if (this.resultsJson) {
      this.isFavorite = this.favoritesService.isFavorited(this.resultsJson['city']+this.resultsJson['state']);
      if (this.isFavorite) {this.stari = 'star';}
      else {this.stari = 'star_border';}
    }
  }

  tweet() {
    let url = "https://twitter.com/intent/tweet?text=";
    url += `The current temperature at ${this.resultsJson["city"]} is ${this.resultsJson["currentTemperature"]}°F. The weather conditions are ${this.resultsJson["currentSummary"]}. `;
    url += "&hashtags=CSCO571WeatherSearch";
    window.open(url, "tweet");
  }

  ngOnInit() {
  }

}
