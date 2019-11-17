import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable, Subject } from "rxjs";
import { DatePipe } from '@angular/common';

const httpOptions = {
  headers: new HttpHeaders({ "Content-Type": "application/json" })
};

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  lat = 0;
  lng = 0; 
  street = '';
  city = '';
  state = '';
  showResult = false;

  getResultsJson$: Observable<any>;
  private resultsJson = new Subject<any>();

  getactiveId$: Observable<any>;
  private activeId = new Subject<any>();

  getresbtn$: Observable<any>;
  private resbtn = new Subject<any>();


  constructor(private http: HttpClient, private datePipe: DatePipe) { 
    this.getResultsJson$ = this.resultsJson.asObservable();
    this.getactiveId$ = this.activeId.asObservable();
    this.getresbtn$ = this.resbtn.asObservable();
  }

  getResultsJson(data) {
    this.resultsJson.next(data);
  }

  getactiveId(data) {
    this.activeId.next(data);
  }

  getresbtn(data) {
    this.resbtn.next(data);
  }

  getCurrentGeo() {
    const url = "http://ip-api.com/json";
    return this.http.get(url);
  }

  getAutocomplete(city) {
    const url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/autocomplete?city="
              + city;
    return this.http.get(url);
  }

  getModal(latitude, longitude, time) {
    const url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/modal?latitude="
              + latitude
              + "&longitude="
              + longitude
              + "&time="
              + time;
    return this.http.get(url);
  }

  getGoogleGeo(street, city, state) {
    const url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/geo?street="
                  + street
                  + "&city="
                  + city
                  + "&state="
                  + state;
    return this.http.get(url);
  }

  getWeather(latitude, longitude) {
    const url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/weather?latitude="
              + latitude
              + "&longitude="
              + longitude;
    return this.http.get(url);
  }

  getSeal(state) {
    const url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/seal?state="
              + state;
    return this.http.get(url);
  }

  transformDate(date) {
    return this.datePipe.transform(date, 'd/M/yyyy');
  }

  activeIdSS(activeId) {
    this.getactiveId(activeId);
  }

  resbtnSS(resbtn) {
    this.getresbtn(resbtn);
  }

  search(form, currentLocation, test) {
    this.showResult = true;
    if (form.get('current_location').value) {
      this.lat = currentLocation['lat'];
      this.lng = currentLocation['lng'];
      this.city = currentLocation['city'];
      this.state = currentLocation['state'];
      this.getWeather(this.lat, this.lng).subscribe(data => {

        let tmpJson = {};
        tmpJson["test"] = test;
        tmpJson["inva"] = false;
        tmpJson["lat"] = this.lat;
        tmpJson["lng"] = this.lng;
        tmpJson["city"] = this.city;
        tmpJson["state"] = this.state;
        tmpJson["timezone"] = data["timezone"];
        tmpJson["currentTemperature"] = Math.round(data["currently"]["temperature"]);
        tmpJson["currentSummary"] = data["currently"]["summary"];
        tmpJson["currentHumidity"] = data["currently"]["humidity"];
        tmpJson["currentPressure"] = data["currently"]["pressure"];
        tmpJson["currentWindSpeed"] = data["currently"]["windSpeed"];
        tmpJson["currentVisibility"] = data["currently"]["visibility"];
        tmpJson["currentCloudCover"] = data["currently"]["cloudCover"];
        tmpJson["currentOzone"] = data["currently"]["ozone"];

        let tmpArrayTime = [];
        let tmpArrayTemperature = [];
        let tmpArrayPressure = [];
        let tmpArrayHumidity = [];
        let tmpArrayOzone = [];
        let tmpArrayVisibility = [];
        let tmpArrayWindSpeed = [];
        for (let i = 0; i < Math.min(data["hourly"]["data"].length, 24); i++) {

          tmpArrayTime.push(i);
          tmpArrayTemperature.push(Math.round(data["hourly"]["data"][i]['temperature']));
          tmpArrayPressure.push(data["hourly"]["data"][i]['pressure']);
          tmpArrayHumidity.push(data["hourly"]["data"][i]['humidity']*100);
          tmpArrayOzone.push(data["hourly"]["data"][i]['ozone']);
          tmpArrayVisibility.push(data["hourly"]["data"][i]['visibility']);
          tmpArrayWindSpeed.push(data["hourly"]["data"][i]['windSpeed']);

        }
        tmpJson["hourlyTime"] = tmpArrayTime;
        tmpJson["hourlyTemperature"] = tmpArrayTemperature;
        tmpJson["hourlyPressure"] = tmpArrayPressure;
        tmpJson["hourlyHumidity"] = tmpArrayHumidity;
        tmpJson["hourlyOzone"] = tmpArrayOzone;
        tmpJson["hourlyVisibility"] = tmpArrayVisibility;
        tmpJson["hourlyWindSpeed"] = tmpArrayWindSpeed;

        let tmpArrayDay = [];
        let tmpArrayDayOri = [];
        let tmpArrayTemperatureLow = [];
        let tmpArrayTemperatureHigh = [];
        for (let i = 0; i < Math.min(data["daily"]["data"].length, 7); i++) {

          var myFormattedDate = this.transformDate(new Date(data["daily"]["data"][i]['time']*1000));
          tmpArrayDay.push(myFormattedDate);
          tmpArrayDayOri.push(data["daily"]["data"][i]['time']);
          tmpArrayTemperatureLow.push(Math.round(data["daily"]["data"][i]['temperatureLow']));
          tmpArrayTemperatureHigh.push(Math.round(data["daily"]["data"][i]['temperatureHigh']));

        }
        tmpJson["weeklyTime"] = tmpArrayDay.reverse();
        tmpJson["weeklyTimeOri"] = tmpArrayDayOri.reverse();
        tmpJson["weeklyTemperatureLow"] = tmpArrayTemperatureLow.reverse();
        tmpJson["weeklyTemperatureHigh"] = tmpArrayTemperatureHigh.reverse();

        this.getSeal(this.state).subscribe(data => {
          tmpJson['seal'] = data['items'][0]['link'];
          this.getResultsJson(tmpJson);
        });
      });
    } 
    else {
      this.street = form.get('street').value;
      this.city = form.get('city').value;
      this.state = form.get('state').value;
      this.getGoogleGeo(form.get('street').value, form.get('city').value, form.get('state').value).subscribe(data => {
        if (data["status"] == 'OK') {
          this.lat = data['results'][0]['geometry']['location']['lat'];
          this.lng = data['results'][0]['geometry']['location']['lng'];
          this.getWeather(this.lat, this.lng).subscribe(data => {
            let tmpJson = {};
            tmpJson["test"] = test;
            tmpJson["inva"] = false;
            tmpJson["lat"] = this.lat;
            tmpJson["lng"] = this.lng;
            tmpJson["city"] = this.city;
            tmpJson["state"] = this.state;
            tmpJson["timezone"] = data["timezone"];
            tmpJson["currentTemperature"] = Math.round(data["currently"]["temperature"]);
            tmpJson["currentSummary"] = data["currently"]["summary"];
            tmpJson["currentHumidity"] = data["currently"]["humidity"];
            tmpJson["currentPressure"] = data["currently"]["pressure"];
            tmpJson["currentWindSpeed"] = data["currently"]["windSpeed"];
            tmpJson["currentVisibility"] = data["currently"]["visibility"];
            tmpJson["currentCloudCover"] = data["currently"]["cloudCover"];
            tmpJson["currentOzone"] = data["currently"]["ozone"];

            let tmpArrayTime = [];
            let tmpArrayTemperature = [];
            let tmpArrayPressure = [];
            let tmpArrayHumidity = [];
            let tmpArrayOzone = [];
            let tmpArrayVisibility = [];
            let tmpArrayWindSpeed = [];
            for (let i = 0; i < Math.min(data["hourly"]["data"].length, 24); i++) {

              tmpArrayTime.push(i);
              tmpArrayTemperature.push(Math.round(data["hourly"]["data"][i]['temperature']));
              tmpArrayPressure.push(data["hourly"]["data"][i]['pressure']);
              tmpArrayHumidity.push(data["hourly"]["data"][i]['humidity']);
              tmpArrayOzone.push(data["hourly"]["data"][i]['ozone']);
              tmpArrayVisibility.push(data["hourly"]["data"][i]['visibility']);
              tmpArrayWindSpeed.push(data["hourly"]["data"][i]['windSpeed']);

            }
            tmpJson["hourlyTime"] = tmpArrayTime;
            tmpJson["hourlyTemperature"] = tmpArrayTemperature;
            tmpJson["hourlyPressure"] = tmpArrayPressure;
            tmpJson["hourlyHumidity"] = tmpArrayHumidity;
            tmpJson["hourlyOzone"] = tmpArrayOzone;
            tmpJson["hourlyVisibility"] = tmpArrayVisibility;
            tmpJson["hourlyWindSpeed"] = tmpArrayWindSpeed;

            let tmpArrayDay = [];
            let tmpArrayDayOri = [];
            let tmpArrayTemperatureLow = [];
            let tmpArrayTemperatureHigh = [];
            for (let i = 0; i <  Math.min(data["daily"]["data"].length, 7); i++) {
              
              var myFormattedDate = this.transformDate(new Date(data["daily"]["data"][i]['time']*1000));
              tmpArrayDay.push(myFormattedDate);
              tmpArrayDayOri.push(data["daily"]["data"][i]['time']);
              tmpArrayTemperatureLow.push(Math.round(data["daily"]["data"][i]['temperatureLow']));
              tmpArrayTemperatureHigh.push(Math.round(data["daily"]["data"][i]['temperatureHigh']));

            }
            tmpJson["weeklyTime"] = tmpArrayDay.reverse();
            tmpJson["weeklyTimeOri"] = tmpArrayDayOri.reverse();
            tmpJson["weeklyTemperatureLow"] = tmpArrayTemperatureLow.reverse();
            tmpJson["weeklyTemperatureHigh"] = tmpArrayTemperatureHigh.reverse();
            
            this.getSeal(this.state).subscribe(data => {
              tmpJson['seal'] = data['items'][0]['link'];
              this.getResultsJson(tmpJson);
            });
          });
        }
        else {
          this.street = 'test';
          this.city = 'la';
          this.state = 'CA';
          this.getGoogleGeo(this.street, this.city, this.state).subscribe(data => {
            this.lat = data['results'][0]['geometry']['location']['lat'];
            this.lng = data['results'][0]['geometry']['location']['lng'];
            this.getWeather(this.lat, this.lng).subscribe(data => {
              let tmpJson = {};
              tmpJson["test"] = test;
              tmpJson["inva"] = true;
              tmpJson["lat"] = this.lat;
              tmpJson["lng"] = this.lng;
              tmpJson["city"] = this.city;
              tmpJson["state"] = this.state;
              tmpJson["timezone"] = data["timezone"];
              tmpJson["currentTemperature"] = Math.round(data["currently"]["temperature"]);
              tmpJson["currentSummary"] = data["currently"]["summary"];
              tmpJson["currentHumidity"] = data["currently"]["humidity"];
              tmpJson["currentPressure"] = data["currently"]["pressure"];
              tmpJson["currentWindSpeed"] = data["currently"]["windSpeed"];
              tmpJson["currentVisibility"] = data["currently"]["visibility"];
              tmpJson["currentCloudCover"] = data["currently"]["cloudCover"];
              tmpJson["currentOzone"] = data["currently"]["ozone"];

              let tmpArrayTime = [];
              let tmpArrayTemperature = [];
              let tmpArrayPressure = [];
              let tmpArrayHumidity = [];
              let tmpArrayOzone = [];
              let tmpArrayVisibility = [];
              let tmpArrayWindSpeed = [];
              for (let i = 0; i < Math.min(data["hourly"]["data"].length, 24); i++) {

                tmpArrayTime.push(i);
                tmpArrayTemperature.push(Math.round(data["hourly"]["data"][i]['temperature']));
                tmpArrayPressure.push(data["hourly"]["data"][i]['pressure']);
                tmpArrayHumidity.push(data["hourly"]["data"][i]['humidity']);
                tmpArrayOzone.push(data["hourly"]["data"][i]['ozone']);
                tmpArrayVisibility.push(data["hourly"]["data"][i]['visibility']);
                tmpArrayWindSpeed.push(data["hourly"]["data"][i]['windSpeed']);

              }
              tmpJson["hourlyTime"] = tmpArrayTime;
              tmpJson["hourlyTemperature"] = tmpArrayTemperature;
              tmpJson["hourlyPressure"] = tmpArrayPressure;
              tmpJson["hourlyHumidity"] = tmpArrayHumidity;
              tmpJson["hourlyOzone"] = tmpArrayOzone;
              tmpJson["hourlyVisibility"] = tmpArrayVisibility;
              tmpJson["hourlyWindSpeed"] = tmpArrayWindSpeed;

              let tmpArrayDay = [];
              let tmpArrayDayOri = [];
              let tmpArrayTemperatureLow = [];
              let tmpArrayTemperatureHigh = [];
              for (let i = 0; i <  Math.min(data["daily"]["data"].length, 7); i++) {
                
                var myFormattedDate = this.transformDate(new Date(data["daily"]["data"][i]['time']*1000));
                tmpArrayDay.push(myFormattedDate);
                tmpArrayDayOri.push(data["daily"]["data"][i]['time']);
                tmpArrayTemperatureLow.push(Math.round(data["daily"]["data"][i]['temperatureLow']));
                tmpArrayTemperatureHigh.push(Math.round(data["daily"]["data"][i]['temperatureHigh']));

              }
              tmpJson["weeklyTime"] = tmpArrayDay.reverse();
              tmpJson["weeklyTimeOri"] = tmpArrayDayOri.reverse();
              tmpJson["weeklyTemperatureLow"] = tmpArrayTemperatureLow.reverse();
              tmpJson["weeklyTemperatureHigh"] = tmpArrayTemperatureHigh.reverse();
              
              this.getSeal(this.state).subscribe(data => {
                tmpJson['seal'] = data['items'][0]['link'];
                this.getResultsJson(tmpJson);
              });
            });
          });
        }
      });
    }
  }
  
}
