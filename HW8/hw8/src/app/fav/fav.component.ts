import { Component, OnInit } from '@angular/core';
import { FavoritesService } from "../service/favorites.service";
import { SearchService } from "../service/search.service";
import { NgForm, FormGroup, FormControl, FormBuilder, Validators } from "@angular/forms";

@Component({
  selector: 'app-fav',
  templateUrl: './fav.component.html',
  styleUrls: ['./fav.component.css']
})
export class FavComponent implements OnInit {

  favorites: any;
  favoritesss = false;

  constructor(private searchService: SearchService, private favoritesService: FavoritesService, private fb: FormBuilder) {
  	this.favoritesService.getFav$.subscribe(data => {
      this.favorites = data['allFav'];
    });
  }

  removeFavorite(key) {
    this.favoritesService.removeFavorite(key);
  }

  loadFavorite() {
    this.favoritesService.getAllFavorite();
  }

  searchf(lat, lng, city, state) {
    this.searchService.search(this.fb.group({street: ['test', Validators.required], city: [city, Validators.required], state: [state, Validators.required], current_location: true}), {lat: lat, lng: lng, city: city, state: state}, false);
  }

  ngOnInit() {
    this.favoritesService.getFav$.subscribe(data => {
      this.favorites = data['allFav'];
    });
    this.favoritesService.getFavDD$.subscribe(data => {
      this.favorites = data['allFav'];
    });
  }

}
