import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from "rxjs";
import { LoaderState } from "./loader";
import { LoaderService } from "../loader.service";
import { SearchService } from "../service/search.service";

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent implements OnInit {

  show = false;
  resultsJson = null;

  constructor(private searchService: SearchService) {
    this.searchService.getResultsJson$.subscribe(data => {
      this.resultsJson = data; 
      if (data['test']) {
        this.show = false;
      }
      else {
        this.show = true;
        setTimeout(() => { this.show = false }, 100);
      }
    });
  }

  ngOnInit() {
  }

  ngOnDestroy() {
  }
}
