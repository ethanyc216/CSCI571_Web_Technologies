var createError = require('http-errors');
var express = require('express');
var path = require('path');
//var cookieParser = require('cookie-parser');
var logger = require('morgan');
var http = require('http');
var https = require('https');
var url = require("url");
var port = process.env.PORT || 3000;

var app = express();

//var indexRouter = require('./routes/index');
//var usersRouter = require('./routes/users');

app.use(express.static(path.join(__dirname, 'dist/hw8')));
app.get('/', function(req, res) {
    res.sendFile(path.join(__dirname, 'dist/hw8/index.html'));
});


app.get('/autocomplete', function (req, res) {
    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var params = url.parse(req.url, true).query;

    var url_text = 'https://maps.googleapis.com/maps/api/place/autocomplete/json?input='
                 + params.city
                 + '&types=(cities)&language=en&key=AIzaSyBSDUpJbxH_8u5SyNgPR76wEqoirG0yKhg';

    https.get(url_text, function(req2,res2){
        var res_text = "";
        req2.on('data', function(data){
            res_text+=data;
        });
        req2.on('end', function(){
            return res.send(res_text);
        });

    });
    console.log("autocomplete GET");
})

app.get('/weather', function (req, res) {
    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var params = url.parse(req.url, true).query;

    var url_text = 'https://api.darksky.net/forecast/a852f76c14e853fb9de2b2aaac7e1dd5/'
                 + params.latitude
                 + ','
                 + params.longitude;
                 
    https.get(url_text, function(req2,res2){
        var res_text = "";
        req2.on('data', function(data){
            res_text+=data;
        });
        req2.on('end', function(){
            return res.send(res_text);
        });

    });
    console.log("weather GET");
})

app.get('/seal', function (req, res) {
    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var params = url.parse(req.url, true).query;

    var url_text = 'https://www.googleapis.com/customsearch/v1?q='
                 + params.state
                 + '%20State%20Seal&cx=002930201954421587923:cgyoboerh2r&imgSize=huge&imgType=news&num=1&searchType=image&key=AIzaSyBSDUpJbxH_8u5SyNgPR76wEqoirG0yKhg';
                 
    https.get(url_text, function(req2,res2){
        var res_text = "";
        req2.on('data', function(data){
            res_text+=data;
        });
        req2.on('end', function(){
            return res.send(res_text);
        });

    });
    console.log("seal GET");
})

app.get('/modal', function (req, res) {
    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var params = url.parse(req.url, true).query;

    var url_text = 'https://api.darksky.net/forecast/a852f76c14e853fb9de2b2aaac7e1dd5/'
                 + params.latitude
                 + ','
                 + params.longitude
                 + ','
                 + params.time;
                 
    https.get(url_text, function(req2,res2){
        var res_text = "";
        req2.on('data', function(data){
            res_text+=data;
        });
        req2.on('end', function(){
            return res.send(res_text);
        });

    });
    console.log("modal GET");
})

app.get('/geo', function (req, res) {
    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var params = url.parse(req.url, true).query;

    var url_text = 'https://maps.googleapis.com/maps/api/geocode/json?address='
                 + params.street
                 + ','
                 + params.city
                 + ','
                 + params.state
                 + '&key=AIzaSyBSDUpJbxH_8u5SyNgPR76wEqoirG0yKhg';
                 
    https.get(url_text, function(req2,res2){
        var res_text = "";
        req2.on('data', function(data){
            res_text+=data;
        });
        req2.on('end', function(){
            return res.send(res_text);
        });

    });
    console.log("geo GET");
})

app.get('/geoCity', function (req, res) {
    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var params = url.parse(req.url, true).query;

    var url_text = 'https://maps.googleapis.com/maps/api/geocode/json?address='
                 + params.city
                 + '&key=AIzaSyBSDUpJbxH_8u5SyNgPR76wEqoirG0yKhg';
                 
    https.get(url_text, function(req2,res2){
        var res_text = "";
        req2.on('data', function(data){
            res_text+=data;
        });
        req2.on('end', function(){
            return res.send(res_text);
        });

    });
    console.log("geoCity GET");
})

app.get('/photo', function (req, res) {
    res.setHeader("Content-Type","text/plain");
    res.setHeader("Access-Control-Allow-Origin","*");
    var params = url.parse(req.url, true).query;

    var url_text = 'https://www.googleapis.com/customsearch/v1?q='
                 + params.city
                 + '&cx=002930201954421587923:cgyoboerh2r&imgSize=large&imgType=news&num=8&searchType=image&key=AIzaSyBSDUpJbxH_8u5SyNgPR76wEqoirG0yKhg';
                 
    https.get(url_text, function(req2,res2){
        var res_text = "";
        req2.on('data', function(data){
            res_text+=data;
        });
        req2.on('end', function(){
            return res.send(res_text);
        });

    });
    console.log("photo GET");
})

/*
// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
//app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//app.use('/', indexRouter);
//app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});
*/
app.listen(port);

