<?php
$street = $city = $state = "";
$json = $geoJson = $useGeoJson = null;

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $street = $_POST["street"];
    $city = $_POST["city"];
    $state = $_POST["state"];
    $useGeoJson = $_POST["useGeoJson"];

    if ($useGeoJson == "true") {
        $geoJson = $_POST["geoJson"];
    } else {
        $geoJson = get_place($street, $city, $state);
    }
    $json = get_weather($geoJson);
    exit($json);
}

if (isset($_REQUEST["url"])) {
    $url = $_REQUEST["url"];
    get_details($url);
}

function get_place($street, $city, $state) {
    $result = "result";
    $geometry = "geometry";
    $location = "location";
    $url = "https://maps.googleapis.com/maps/api/geocode/xml?address=".urlencode($street).",+".urlencode($city).",+".urlencode($state)."&key=AIzaSyBSDUpJbxH_8u5SyNgPR76wEqoirG0yKhg";
    $xml = simplexml_load_string(file_get_contents($url));
    $json = json_encode($xml);
    $jsonObj = json_decode($json);
    $jsonObj = $jsonObj->$result->$geometry->$location;
    $json = json_encode($jsonObj);
    return $json;
}

function get_weather($geoJson) {
    $jsonObj = json_decode($geoJson);
    $lat = "lat";
    $lng = "lng";
    $lat = $jsonObj->$lat;
    $lng = $jsonObj->$lng;
    $url = "https://api.forecast.io/forecast/a852f76c14e853fb9de2b2aaac7e1dd5/".$lat.",".$lng."?exclude=minutely,hourly,alerts,flags";
    $json = file_get_contents($url);
    return $json;
}

function get_details($url) {
    $details = file_get_contents($url);   
    exit("$details");
}

?>


<!DOCTYPE html>
<html>
<head>
    <title>Weather Search</title>
    <meta charset="utf-8">
    <meta name="referrer" content="no-referrer">
    <style type="text/css">
        body {
            text-align: center;
        }

        a {
            color: rgb(255, 255, 255);
            text-decoration: none;
        }

        a:active {
            color: rgb(255, 255, 255);
        }

        a:visited {
            color: rgb(255, 255, 255);
        }

        .arrow_up {
            display: block;
            height: 30px;
            width: 50px;
            margin: auto;
            background-size: 100%;
            background-image: url("http://csci571.com/hw/hw6/images/arrow_up.png");
        }

        .arrow_down {
            display: block;
            height: 30px;
            width: 50px;
            margin: auto;
            background-size: 100%;
            background-image: url("http://csci571.com/hw/hw6/images/arrow_down.png");
        }

        #formbox {
            width: 720px;
            height: 230px;
            margin: 20px auto;
            padding: 0px;
            background-color: rgb(0, 173, 42);
            text-align: left;
            border-radius: 10px;
        }

        #search_form h2 {
            font-family: serif;
            font-style: italic;
            text-align: center;
            font-size: 40px;
            padding-top: 8px;
            margin: 0px;
            font-weight: lighter;
            color: rgb(255, 255, 255);
        }

        #search_form label {
            font-family: serif;
            font-size: 18px;
            padding-left: 50px;
            color: rgb(255, 255, 255);
        }

        #search_form input {
            font-family: serif;
            width: 100px;
        }

        #search_form select {
            font-family: serif;
            width: 200px;
            margin-top: 10px;
        }

        #results {
            margin: auto;
            margin-top: 20px;
            text-align: center;
        }

        table {
            border-collapse: collapse;
        }

        #results table {
            margin: auto;
            border-radius: 10px;
            background-color: rgb(53, 199, 247);
            color: rgb(255, 255, 255);
        }

        #results table tr td {
            width: 60px;
            border: none;
        }

        #results table tr td img {
            height: 30px;
        }

        #check_input {
            border: 2px solid;
            border-color: rgb(170, 170, 170);
            background-color: rgb(240, 240, 240);
            width: 350px;
            text-align: center;
            margin: auto;
            font-weight: lighter;
        }

        #buttons {
            margin-top: 45px;
            margin-left: 270px;  
        }
    </style>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
</head>
<body>
    <div id="formbox">
        <form id="search_form" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>" >
            <h2>Weather Search</h2>
            <div style="float: left; width: 395px;">
                <label for="street"><b>Street&nbsp</b></label>
                <input id="street" name="street" type="text" value="<?php echo $street ?>">
                <br>

                <label for="city"><b>City&nbsp&nbsp &nbsp</b></label>
                <input id="city" name="city" type="text" value="<?php echo $city ?>" style="margin-top: 10px;">
                <br>

                <label for="state"><b>State&nbsp</b></label>
                <select name="state" id="state">
                    <option value="State">State</option>
                    <option value="dashline">----------------------------------------------</option>
                    <option value="AL">Alabama</option>
                    <option value="AK">Alaska</option>
                    <option value="AZ">Arizona</option>
                    <option value="AR">Arkansas</option>
                    <option value="CA">California</option>
                    <option value="CO">Colorado</option>
                    <option value="CT">Connecticut</option>
                    <option value="DE">Delaware</option>
                    <option value="DC">District Of Columbia</option>
                    <option value="FL">Florida</option>
                    <option value="GA">Georgia</option>
                    <option value="HI">Hawaii</option>
                    <option value="ID">Idaho</option>
                    <option value="IL">Illinois</option>
                    <option value="IN">Indiana</option>
                    <option value="IA">Iowa</option>
                    <option value="KS">Kansas</option>
                    <option value="KY">Kentucky</option>
                    <option value="LA">Louisiana</option>
                    <option value="ME">Maine</option>
                    <option value="MD">Maryland</option>
                    <option value="MA">Massachusetts</option>
                    <option value="MI">Michigan</option>
                    <option value="MN">Minnesota</option>
                    <option value="MS">Mississippi</option>
                    <option value="MO">Missouri</option>
                    <option value="MT">Montana</option>
                    <option value="NE">Nebraska</option>
                    <option value="NV">Nevada</option>
                    <option value="NH">New Hampshire</option>
                    <option value="NJ">New Jersey</option>
                    <option value="NM">New Mexico</option>
                    <option value="NY">New York</option>
                    <option value="NC">North Carolina</option>
                    <option value="ND">North Dakota</option>
                    <option value="OH">Ohio</option>
                    <option value="OK">Oklahoma</option>
                    <option value="OR">Oregon</option>
                    <option value="PA">Pennsylvania</option>
                    <option value="RI">Rhode Island</option>
                    <option value="SC">South Carolina</option>
                    <option value="SD">South Dakota</option>
                    <option value="TN">Tennessee</option>
                    <option value="TX">Texas</option>
                    <option value="UT">Utah</option>
                    <option value="VT">Vermont</option>
                    <option value="VA">Virginia</option>
                    <option value="WA">Washington</option>
                    <option value="WV">West Virginia</option>
                    <option value="WI">Wisconsin</option>
                    <option value="WY">Wyoming</option>
                </select>
                <br>

                <div id="buttons">
                    <button id="search" name="search" style="font-size: 13px; width: 55px; font-family: serif; padding: 0px; padding-top: 2px;">search</button>
                    <input id="clear" name="clear" onclick="clearpage()" type="button" value="clear" style="font-size: 13px; width: 45px; font-family: serif; padding: 0px; padding-top: 2px; margin-left: -6px;">
                </div>
            </div>

            <div style="float:left; width: 5px; height: 115px; background: rgb(255, 255, 255); border-radius: 2px;"></div> 

            <div style="float: left; width: 320px;">
                <input type="checkbox" id="currentLocation" value="currentLocation" name="currentLocation" style="margin-left: 65px;">
                <label for="currentLocation" style="margin-left: -90px;">Current Location</label>
                <input type="hidden" name="geoJson" id="geoJson">
                <input type="hidden" name="useGeoJson" id="useGeoJson">
            </div>

        </form>
    </div>

    <div id="results"></div>
</body>

<script type="text/javascript">

    /** for test
    result_div = document.getElementById("results");
    if (result_div.firstChild) {
        remove_all_child("results");
    }
    var node = document.createElement("div");
    node.innerHTML = "<b>"+document.getElementById("geoJson").value+"<b>";
    node.id = "no_record";
    result_div.appendChild(node);
    **/

    var form = document.getElementById("search_form");
    var currentLocation_check = document.getElementById("currentLocation");
    var street_input = document.getElementById("street");
    var city_input = document.getElementById("city");
    var state_input = document.getElementById("state");
    var geoJson_input = document.getElementById("geoJson");
    var useGeoJson_input = document.getElementById("useGeoJson");
    var cityValue = "";
    var clear = document.getElementById("clear");

    function remove_all_child(nodename) {
        var node = document.getElementById(nodename);
        while (node && node.firstChild) {
            node.removeChild(node.firstChild);
        }
    }

    currentLocation_check.addEventListener("change", function() {
        if (this.checked) {
            street_input.value = "<?php echo $street ?>";
            city_input.value = "<?php echo $city ?>";
            state_input.value = "State";
            street_input.disabled = true;
            city_input.disabled = true;
            state.disabled = true;
        } else {
            street_input.disabled = false;
            city_input.disabled = false;
            state.disabled = false;
        }
    })

    function clearpage() {
        var street_input = document.getElementById("street");
        var city_input = document.getElementById("city");
        var state_input = document.getElementById("state");
        street_input.disabled = false;
        city_input.disabled = false;
        state.disabled = false;
        form.reset();
        remove_all_child("results");
    }

    form.addEventListener("submit", function(event) {
        event.preventDefault();

        var city_input = document.getElementById("city");

        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "http://ip-api.com/json", false);
        xhttp.send();

        if (xhttp.readyState == 4 && xhttp.status == 200) {
            geoJson = JSON.parse(xhttp.responseText);
            console.log(geoJson);
            currentLocation = {
                "lat": geoJson["lat"],
                "lng": geoJson["lon"]
            }
            geoJson_input.value = JSON.stringify(currentLocation);
            cityValue = geoJson["city"];
        }

        if (currentLocation_check.checked) {
            useGeoJson_input.value = "true";
        } else {
            if ((street_input && street_input.value) && (city_input && city_input.value) && (state_input && state_input.value != "State" && state_input.value != "dashline")) {
                useGeoJson_input.value = "false";
                cityValue = city_input.value;
            } else {
                result_div = document.getElementById("results");
                if (result_div.firstChild) {
                    remove_all_child("results");
                }
                var node = document.createElement("div");
                node.innerHTML = "<b>Please check the input address.<b>";
                node.id = "check_input";
                result_div.appendChild(node);
                return;
            }

        }

        var url = form.action;
        var params = "";
        var data = new FormData(form);
        for (const entry of data) {
            params += entry[0] + "=" + encodeURIComponent(entry[1]) + "&";
        }
        params = params.slice(0, -1);
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", url, false);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send(params);
        console.log(xhttp.responseText);
        jsonObj = JSON.parse(xhttp.responseText);
        //currentLocation = results["geoJson"];
        show_result(jsonObj);
        
    }, false);

    function unixTime(unixtime) {
        var u = new Date(unixtime*1000);
          return u.getUTCFullYear() + '-' + ('0' + u.getUTCMonth()).slice(-2) + '-' + ('0' + u.getUTCDate()).slice(-2)
    };

    function sunTime(unixtime) {
        s = new Date(unixtime).toLocaleTimeString("en-US").split(":")
        return s[0]
    };

    function show_result(results) {
        if (results == null) {
            return;
        }
        result_div = document.getElementById("results");
        if (result_div.firstChild) {
            remove_all_child("results");
        }
        
        latitude = results["latitude"];
        longitude = results["longitude"];
        timezoneValue = results["timezone"];
        curValue = results["currently"];
        temperatureValue = curValue["temperature"];
        summaryValue = curValue["summary"];
        humidityValue = curValue["humidity"];
        pressureValue = curValue["pressure"];
        windSpeedValue = curValue["windSpeed"];
        visibilityValue = curValue["visibility"];
        cloudCoverValue = curValue["cloudCover"];
        ozoneValue = curValue["ozone"];

        dailyValue = results["daily"];
        dailyDataList = dailyValue["data"];

        var table = document.createElement("table");
        
        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("colspan", "6");
        thc.innerHTML = "<b>"+cityValue+"</b>";
        thc.setAttribute("style", "text-align: left; font-size: 28px; font-weight: normal; padding-top: 20px; padding-left: 20px;");
        th.appendChild(thc);        
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("colspan", "6");
        thc.innerHTML = "<b>"+timezoneValue+"</b>";
        thc.setAttribute("style", "text-align: left; font-size: 13px; font-weight: lighter; margin-top: 0px; padding-left: 20px;");
        th.appendChild(thc);        
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("colspan", "6");
        thc.setAttribute("style", "text-align: left;");
        thc.innerHTML = "<span style = \"text-align: left; font-size: 75px; font-weight: bolder; margin-top: 0px; padding-left: 20px;\">"+temperatureValue+"</span><img src='https://cdn3.iconfinder.com/data/icons/virtual-notebook/16/button_shape_oval-512.png' style = 'padding-bottom: 50px; width: 10px; height: 10px;'><span style = 'font-size: 40px; font-weight: bolder;'> F</span>";
        th.appendChild(thc);      
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("colspan", "6");
        thc.setAttribute("style", "text-align: left; font-size: 30px; font-weight: bolder; padding-left: 20px;");
        thc.innerHTML = summaryValue;
        th.appendChild(thc);        
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        var img = document.createElement("img");
        img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-16-512.png";
        img.title="Humidity";
        thc.appendChild(img);
        th.appendChild(thc);
        var thc = document.createElement("td");
        var img = document.createElement("img");
        img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-25-512.png";
        img.title="Pressure";
        thc.appendChild(img);
        th.appendChild(thc);
        var thc = document.createElement("td");
        var img = document.createElement("img");
        img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-27-512.png";
        img.title="WindSpeed";
        thc.appendChild(img);
        th.appendChild(thc);
        var thc = document.createElement("td");
        var img = document.createElement("img");
        img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-30-512.png";
        img.title="Visibility";
        thc.appendChild(img);
        th.appendChild(thc);
        var thc = document.createElement("td");
        var img = document.createElement("img");
        img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-28-512.png";
        img.title="CloudCover";
        thc.appendChild(img);
        th.appendChild(thc);
        var thc = document.createElement("td");
        var img = document.createElement("img");
        img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-24-512.png";
        img.title="Ozone";
        thc.appendChild(img);
        th.appendChild(thc);
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "font-size: 18px; font-weight: bolder; padding-bottom: 20px;");
        if (humidityValue){
            thc.innerHTML = humidityValue;
        } else {
            thc.innerHTML = "None";
        }
        th.appendChild(thc); 
        var thc = document.createElement("td");
        thc.setAttribute("style", "font-size: 18px; font-weight: bolder; padding-bottom: 20px;");
        if (pressureValue) {
            thc.innerHTML = pressureValue;
        } else {
            thc.innerHTML = "None";
        }
        th.appendChild(thc); 
        var thc = document.createElement("td");
        thc.setAttribute("style", "font-size: 18px; font-weight: bolder; padding-bottom: 20px;");
        if (windSpeedValue) {
            thc.innerHTML = windSpeedValue;
        } else {
            thc.innerHTML = "None";
        }
        th.appendChild(thc); 
        var thc = document.createElement("td");
        thc.setAttribute("style", "font-size: 18px; font-weight: bolder; padding-bottom: 20px;");
        if (visibilityValue) {
            thc.innerHTML = visibilityValue;
        } else {
            thc.innerHTML = "None";
        }
        th.appendChild(thc); 
        var thc = document.createElement("td");
        thc.setAttribute("style", "font-size: 18px; font-weight: bolder; padding-bottom: 20px;");
        if (cloudCoverValue) {
            thc.innerHTML = cloudCoverValue;
        } else {
            thc.innerHTML = "None";
        }
        th.appendChild(thc); 
        var thc = document.createElement("td");
        thc.setAttribute("style", "font-size: 18px; font-weight: bolder; padding-bottom: 20px;");
        if (ozoneValue) {
            thc.innerHTML = ozoneValue;
        } else {
            thc.innerHTML = "None";
        }
        th.appendChild(thc);       
        table.appendChild(th);

        result_div.appendChild(table);

        var table = document.createElement("table");
        table.setAttribute("style", "margin-top: 20px; border-radius: 0px; background-color: rgb(151, 202, 240); color: rgb(255, 255, 255);");
        var th = table.insertRow();
        var thc1 = document.createElement("td");
        thc1.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; width: 100px; font-weight: bolder;");
        var thc2 = document.createElement("td");
        thc2.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; width: 30px; font-weight: bolder;");
        var thc3 = document.createElement("td");
        thc3.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; width: 330px; font-weight: bolder;");
        var thc4 = document.createElement("td");
        thc4.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; width: 140px; font-weight: bolder;");
        var thc5 = document.createElement("td");
        thc5.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; width: 140px; font-weight: bolder;");
        var thc6 = document.createElement("td");
        thc6.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; width: 100px; font-weight: bolder;");
        thc1.innerHTML = "<b>Date</b>";
        thc2.innerHTML = "<b>Status</b>";
        thc3.innerHTML = "<b>Summary</b>";
        thc4.innerHTML = "<b>TemperatureHigh</b>";
        thc5.innerHTML = "<b>TemperatureLow</b>";
        thc6.innerHTML = "<b>Wind Speed</b>";
        th.appendChild(thc1);
        th.appendChild(thc2);
        th.appendChild(thc3);
        th.appendChild(thc4);
        th.appendChild(thc5);
        th.appendChild(thc6);
        table.appendChild(th);
        var i = 1;
        for (var dailyData of dailyDataList) {
            var tr = table.insertRow();
            tr.id = "row" + i;
            i = i + 1;

            var td = tr.insertCell();
            var time = unixTime(dailyData["time"]);
            td.innerHTML = time;
            td.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; font-weight: bolder;");

            var td = tr.insertCell();
            td.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; font-weight: bolder;");
            var img = document.createElement("img");
            var iconType = dailyData["icon"];
            if (iconType == "clear-day") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-12-512.png";
            } else if (iconType == "clear-night") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-12-512.png";
            } else if (iconType == "rain") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-04-512.png";
            } else if (iconType == "snow") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-19-512.png";
            } else if (iconType == "sleet") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-07-512.png";
            } else if (iconType == "wind") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-27-512.png";
            } else if (iconType == "fog") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-28-512.png";
            } else if (iconType == "cloudy") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-01-512.png";
            } else if (iconType == "partly-cloudy-day") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-02-512.png";
            } else if (iconType == "partly-cloudy-night") {
                img.src = "https://cdn2.iconfinder.com/data/icons/weather-74/24/weather-02-512.png";
            }
            td.appendChild(img);

            var td = tr.insertCell();
            td.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; font-weight: bolder;");
            var name = document.createElement("a");
            name.innerHTML = dailyData["summary"];
            name.href = "javascript:detail(" + dailyData["time"] + ", " + latitude + ", " + longitude + ")";
            td.appendChild(name);

            var td = tr.insertCell();
            td.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; font-weight: bolder;");
            td.innerHTML = dailyData["temperatureHigh"];
            var td = tr.insertCell();
            td.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; font-weight: bolder;");
            td.innerHTML = dailyData["temperatureLow"];
            var td = tr.insertCell();
            td.setAttribute("style", "border: 2px solid;; border-color: rgb(49, 159, 200); font-size: 16px; font-weight: bolder;");
            td.innerHTML = dailyData["windSpeed"];
        }
        result_div.appendChild(table);
    }

    function detail(time, lat, lng) {
        var url = "https://api.darksky.net/forecast/a852f76c14e853fb9de2b2aaac7e1dd5/"+lat+","+lng+","+time+"?exclude=minutely";
        remove_all_child("results");
        var xmlhttp = new XMLHttpRequest();
        var details;
        xmlhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                // console.log(xmlhttp.responseText);
                details = JSON.parse(xmlhttp.responseText);
                console.log(details);
                show_detail(details);
            }
        }
        xmlhttp.open("GET", "OmG_hw6.php?url=" + url, false);
        xmlhttp.send();
    }

    function drawBasic() {
      hourlyDataList = hourlyValue["data"];

      var data = new google.visualization.DataTable();
      data.addColumn('number', 'X');
      data.addColumn('number', 'T');

      var temperatures = [];
      var i;
      for (i = 0; i < 24; i++) {
        temperatures.push([i, hourlyDataList[i]["temperature"]]);
      };

      data.addRows(temperatures);
      var options = {
        'width': 600,
        hAxis: {
          title: 'Time',
          ticks: [0, 5, 10, 15, 20],
          textStyle: {fontName: 'Times-Roman',}, 
        },
        vAxis: {
          title: 'Temperature',
          textPosition: 'none',
          textStyle: {fontName: 'Times-Roman',},
        },
      };
      var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
      chart.draw(data, options);
    }

    function show_chart() {
        var arrow_div = document.getElementById("arrow_div");
        var chart_div = document.getElementById("chart_div");
        if (chart_div.style.display === "none") {
            chart_div.style.display = "block";
            arrow_div.className = "arrow_up";
        } else {
            chart_div.style.display = "none";
            arrow_div.className = "arrow_down";
        } 
    }

    function show_detail(details) {
        hourlyValue = details["hourly"];
        curValue = details["currently"];
        temperatureValue = Math.round(curValue["temperature"]);
        summaryValue = curValue["summary"];
        iconValue = curValue["icon"];
        precipitationValue = curValue["precipIntensity"];
        chanceOfRainValue = curValue["precipProbability"] * 100;
        windSpeedValue = curValue["windSpeed"];
        humidityValue = Math.floor(curValue["humidity"] * 100);
        visibilityValue = curValue["visibility"];
        sunriseValue = details["daily"]["data"][0]["sunriseTime"];
        sunsetValue = details["daily"]["data"][0]["sunsetTime"];

        result_div = document.getElementById("results");
        if (result_div.firstChild) {
            remove_all_child("results");
        }

        var name = document.createElement("p");
        name.setAttribute("style", "font-size: 30px;");
        name.innerHTML = "<b>Daily Weather Detail</b>";
        result_div.appendChild(name);

        var table = document.createElement("table");
        table.setAttribute("style", "margin-top: 20px; background-color: rgb(158, 209, 218); color: rgb(255, 255, 255);");
        
        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "text-align: left; font-size: 30px; font-weight: bolder; padding-left: 20px; padding-top: 40px; width: 240px;");
        thc.innerHTML = "<b>"+summaryValue+"</b>";
        th.appendChild(thc);

        var thc = document.createElement("td");
        thc.setAttribute("rowspan", "2");
        thc.setAttribute("style", "padding-top: 10px; width: 200px;");
        var img = document.createElement("img");
        img.setAttribute("style", "height: 200px; padding-right: 20px;");
        if (iconValue == "clear-day") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/sun-512.png";
        } else if (iconValue == "clear-night") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/sun-512.png";
        } else if (iconValue == "rain") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/rain-512.png";
        } else if (iconValue == "snow") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/snow-512.png";
        } else if (iconValue == "sleet") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/lightning-512.png";
        } else if (iconValue == "wind") {
            img.src = "https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_10-512.png";
        } else if (iconValue == "fog") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/cloudy-512.png";
        } else if (iconValue == "cloudy") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/cloud-512.png";
        } else if (iconValue == "partly-cloudy-day") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/sunny-512.png";
        } else if (iconValue == "partly-cloudy-night") {
            img.src = "https://cdn3.iconfinder.com/data/icons/weather-344/142/sunny-512.png";
        }
        thc.appendChild(img);
        th.appendChild(thc);
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "text-align: left; font-weight: bolder; padding-left: 20px; padding-top: 0px;");
        thc.innerHTML = "<span style = 'font-size: 80px; font-weight: bolder;'>"+temperatureValue+"</span><img src='https://cdn3.iconfinder.com/data/icons/virtual-notebook/16/button_shape_oval-512.png' style = 'padding-bottom: 50px; width: 10px; height: 10px;'><span style = 'font-size: 40px; font-weight: bolder;'> F</span>";
        th.appendChild(thc);      
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "text-align: left;");
        thc.setAttribute("colspan", "2");
        if (precipitationValue <= 0.001){
            thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 200px; padding-top: 0px;'>Precipitation: </span><span style = 'font-size: 24px; font-weight: bolder;'>None</span>";
        } else if (precipitationValue <= 0.015) {
            thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 200px; padding-top: 0px;'>Precipitation: </span><span style = 'font-size: 24px; font-weight: bolder;'>Very Light</span>";
        } else if (precipitationValue <= 0.05) {
            thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 200px; padding-top: 0px;'>Precipitation: </span><span style = 'font-size: 24px; font-weight: bolder;'>Light</span>";
        } else if (precipitationValue <= 0.1) {
            thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 200px; padding-top: 0px;'>Precipitation: </span><span style = 'font-size: 24px; font-weight: bolder;'>Moderate</span>";
        } else {
            thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 200px; padding-top: 0px;'>Precipitation: </span><span style = 'font-size: 24px; font-weight: bolder;'>Heavy</span>";
        } 
        th.appendChild(thc);
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "text-align: left;");
        thc.setAttribute("colspan", "2");
        thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 178px; padding-top: 0px;'>Chance of Rain: </span><span style = 'font-size: 24px; font-weight: bolder;'>"+chanceOfRainValue+"</span><span style = 'font-size: 16px; font-weight: bolder;'> %</span>";
        th.appendChild(thc);
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "text-align: left;");
        thc.setAttribute("colspan", "2");
        thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 207px; padding-top: 0px;'>Wind Speed: </span><span style = 'font-size: 24px; font-weight: bolder;'>"+windSpeedValue+"</span><span style = 'font-size: 16px; font-weight: bolder;'> mph</span>";
        th.appendChild(thc);
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "text-align: left;");
        thc.setAttribute("colspan", "2");
        thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 228px; padding-top: 0px;'>Humidity: </span><span style = 'font-size: 24px; font-weight: bolder;'>"+humidityValue+"</span><span style = 'font-size: 16px; font-weight: bolder;'> %</span>";
        th.appendChild(thc);
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "text-align: left;");
        thc.setAttribute("colspan", "2");
        thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 233px; padding-top: 0px;'>Visibility: </span><span style = 'font-size: 24px; font-weight: bolder;'>"+visibilityValue+"</span><span style = 'font-size: 16px; font-weight: bolder;'> mi</span>";
        th.appendChild(thc);
        table.appendChild(th);

        var th = table.insertRow();
        var thc = document.createElement("td");
        thc.setAttribute("style", "text-align: left; padding-bottom: 20px;");
        thc.setAttribute("colspan", "2");
        sunriseTime = sunTime(sunriseValue);
        sunsetValue = sunTime(sunsetValue);
        thc.innerHTML = "<span style = 'font-size: 20px; font-weight: bolder; padding-left: 173px; padding-top: 0px;'>Sunrise / Sunset: </span><span style = 'font-size: 24px; font-weight: bolder;'>"+sunriseTime+"</span><span style = 'font-size: 16px; font-weight: bolder;'> AM/ </span><span style = 'font-size: 24px; font-weight: bolder;'>"+sunsetValue+"</span><span style = 'font-size: 16px; font-weight: bolder;'> PM</span>";
        th.appendChild(thc);
        table.appendChild(th);

        result_div.appendChild(table);

        var name = document.createElement("p");
        name.setAttribute("style", "font-size: 30px;");
        name.innerHTML = "<b>Day's Hourly Weather</b>";
        result_div.appendChild(name);

        var arrow_div = document.createElement("div");
        arrow_div.id = "arrow_div";
        arrow_div.className = "arrow_down";
        result_div.appendChild(arrow_div);

        var chart_div = document.createElement("div");
        chart_div.id = "chart_div";
        chart_div.className = "chart_div";
        arrow_div.onclick = show_chart;
        chart_div.setAttribute("style", "width: 600px; margin: auto; display: none;");
        google.charts.load('current', {packages: ['corechart', 'line']});
        google.charts.setOnLoadCallback(drawBasic);
        result_div.appendChild(chart_div);
    }
</script>
</html>