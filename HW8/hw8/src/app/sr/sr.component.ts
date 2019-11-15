import { Component, OnInit } from '@angular/core';
import { SearchService } from "../service/search.service";
import { FavoritesService } from "../service/favorites.service";

@Component({
  selector: 'app-sr',
  templateUrl: './sr.component.html',
  styleUrls: ['./sr.component.css']
})
export class SrComponent implements OnInit {

  isR = true;
  isF = false;

  isShowResult = false;
  isShowFavorite = false;

  resultShowClass = "btn showbtn";
  favoriteShowClass = "btn hidebtn";

  clear = false;
  resultsJson = null;

  inva = false;

  constructor(private searchService: SearchService, private favoritesService: FavoritesService) {
  	this.searchService.getResultsJson$.subscribe(data => {
      this.resultsJson = data; 
      if (data['test']) {
        this.inva = false;
        this.showResult();
        this.isShowResult = false;
      }
      else if (data['inva']) {
        this.inva = true;
        this.showResult();
        this.isR = true;
        this.isShowResult = false;
      }
      else if (this.resultsJson && !data['test'] && !data['inva']) { 
        this.isShowResult = true;
        this.inva = false;
        this.isR = true;
        this.isF = false;
        this.isShowFavorite = false;
      }
    });
  }

  ngOnInit() {
  }

  showResult() {
    this.clear = false;
    this.isShowFavorite = false;
    if (this.resultsJson && !this.resultsJson['test'] && !this.resultsJson['inva']) {
      this.isShowResult = true;
    }
    this.isR = true;
    this.isF = false;
    this.resultShowClass = "btn showbtn";
    this.favoriteShowClass = "btn hidebtn";
    this.searchService.resbtnSS('HAHA');
  }

  showFavorite() {
    this.clear = false;
    this.isShowResult = false;
    this.isShowFavorite = true;
    this.isR = false;
    this.isF = true;
    this.resultShowClass = "btn hidebtn";
    this.favoriteShowClass = "btn showbtn";
    this.favoritesService.getFavDDD('HAHA');
    this.searchService.resbtnSS('HAHA');
  }

}
