import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FavoritesService {

  getFav$: Observable<any>;
  private fav = new Subject<any>();

  getFavDD$: Observable<any>;
  private favDD = new Subject<any>();

  constructor() {
    this.getFav$ = this.fav.asObservable();
    this.getFavDD$ = this.favDD.asObservable();
  }

  getFav(data) {
    this.fav.next(data);
  }

  getFavDD(data) {
    this.favDD.next(data);
  }

  getFavDDD(data) {
    this.getFavDD(this.getAllFavoritein());
  }

  saveFavorite(lat, lng, city, state, seal) {
    let timestamp = new Date();
    let favJson = {
      lat: lat,
      lng: lng,
      city: city,
      state: state,
      seal: seal,
      timestamp: timestamp.getTime()
    };
    localStorage.setItem(city+state, JSON.stringify(favJson));
    this.getAllFavorite();
  }

  isFavorited(key) {
    if (!localStorage.getItem(key)) {
      return false;
    } else {
      return true;
    }
  }

  removeFavorite(key) {
    localStorage.removeItem(key);
    this.getAllFavorite();
  }

  getAllFavorite() {
    if (localStorage.length == 0) {
      this.getFav({ allFav: null});
      return;
    }
    let localStorageArray = new Array(localStorage.length);
    for (let i = 0; i < localStorage.length; i++) {
      localStorageArray[i] = JSON.parse(
        localStorage.getItem(localStorage.key(i))
      );
    }

    localStorageArray.sort((a, b) => {
      if (a.timestamp > b.timestamp) {
        return 1;
      } else {
        return -1;
      }
    });

    let returnJson = {
      allFav: localStorageArray
    };
    this.getFav(returnJson);
  }

  getAllFavoritein() {
    if (localStorage.length == 0) {
      return null;
    }
    let localStorageArray = new Array(localStorage.length);
    for (let i = 0; i < localStorage.length; i++) {
      localStorageArray[i] = JSON.parse(
        localStorage.getItem(localStorage.key(i))
      );
    }

    localStorageArray.sort((a, b) => {
      if (a.timestamp > b.timestamp) {
        return 1;
      } else {
        return -1;
      }
    });

    let returnJson = {
      allFav: localStorageArray
    };
    return returnJson;
  }
}
