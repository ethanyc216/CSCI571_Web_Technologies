import { Component, OnInit, HostBinding, Output, OnChanges, Input} from '@angular/core';
import { SearchService } from "../service/search.service";
import { ResultsService } from "../service/results.service";
import { FavoritesService } from "../service/favorites.service";

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {
  resultsJson = null;
  showResults = false;
  isFavorite: any;

  error: boolean = false;



  constructor(private searchService: SearchService, private resultsService: ResultsService, private favoritesService: FavoritesService) {
      this.searchService.getResultsJson$.subscribe(data => {
        if (data === null) {
          this.error = true;
          this.showResults = true;
        } 
        else if (data === undefined) {
        }
        else {
          this.resultsJson = data;
          this.error = false;
          this.showResults = true;
        }
      });
    }

  ngOnInit() {

  }

}
