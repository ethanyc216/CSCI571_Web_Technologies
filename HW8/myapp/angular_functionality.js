var app = angular.module('form_app', ['ngAnimate','ngMap']);
app.controller('body_controller', function($scope, $http, $animate) 
{
   $scope.enableDetailsButton = false;
   $scope.detailsId = "";
   $scope.default_values = {Keyword:"", Category:"Default", Distance:"10",Distance1:"miles"};
   $scope.reset = function() 
   {
     $scope.user = angular.copy($scope.default_values);
	 $scope.hide_this = true;
 	 $scope.table_content="";	
     $scope.location="current_location";
     $scope.show_favor = false;
     $scope.detail_button = false;
     $scope.show_detail = false;
	 $scope.check_empty_table=false; 
   };
	
    localStorage.clear();
	$scope.user = angular.copy($scope.default_values);
    $scope.table_content="";	
    $scope.location="current_location";
    
    $scope.detail_button = false; //disable detail button
    $scope.show_detail = false;//hide detail form
	$scope.check_empty_table=false;
	$scope.progress=false;
	$scope.table= [];
    $scope.table.push({table_content:undefined,table_statuscode:undefined,table_statustext:undefined});
	
	if($scope.location=="current_location")
		{
	$http.get("http://ip-api.com/json")
        .then(function(response) {
            $scope.lat1 = response.data.lat;
            $scope.lon1= response.data.lon;
        });
		}

    $scope.search_function = function () 
	{
    $scope.progress = true;
        var params="";
		params={};
		
		if($scope.user.Category=="Default")
	$scope.segmentId="";
else if($scope.user.Category=="music")
	$scope.segmentId="KZFzniwnSyZfZ7v7nJ";
else if($scope.user.Category=="sports")
	$scope.segmentId="KZFzniwnSyZfZ7v7nE";
else if($scope.user.Category=="art")
	$scope.segmentId="KZFzniwnSyZfZ7v7na";
else if($scope.user.Category=="film")
	$scope.segmentId="KZFzniwnSyZfZ7v7nn";
else if($scope.user.Category=="miscellaneous")
	$scope.segmentId="KZFzniwnSyZfZ7v7n1";
      
		$scope.check_empty_table=false;
		params.Keyword=$scope.user.Keyword;
		params.Distance=$scope.user.Distance;
		params.segmentId=$scope.segmentId;
		params.Category=$scope.user.Category;

		if($scope.location=="current_location")
		{
		params.lat1 = $scope.lat1;
		params.lon1 = $scope.lon1;
		$http.get ("http://nodealekhya-env.eejrfgdrsx.us-east-2.elasticbeanstalk.com/here_button?Keyword="+params.Keyword+"&segmentId="+params.segmentId+"&Distance="+params.Distance+"&lat1="+params.lat1+"&lon1="+params.lon1)
		.then(function (response)
		{
		console.log(response);
		$scope.embed=response.data._embedded;
		if($scope.embed!=undefined)
		{
			$scope.table_content=response.data._embedded.events;
		}
	    else
			{
			$scope.table_content="";
			$scope.check_empty_table=true;
			}
             $scope.table_statuscode = response.status;
             $scope.table_statustext = response.statusText;
             $scope.detail_button = true;
             $scope.progress = false;
		}
		)
		}
	if($scope.location=="other_location")
	{
	   $scope.location_text=$scope.other_location_t;
		$scope.en=(encodeURI($scope.location_text));
		//modified API key as it is sensitive information
	$http.get("https://maps.googleapis.com/maps/api/geocode/json?address="+$scope.en+"&key=AIzayAyiqlzeWLjsBUxhF3sYRWST4IXA4-7_Q")
	.then(function (response)
		{
    $scope.lat_o = response.data.results[0].geometry.location.lat;
	$scope.lon_o = response.data.results[0].geometry.location.lng;
		
		params.lat1 = $scope.lat_o;
		params.lon1 = $scope.lon_o;
		
		$http.get ("http://nodealekhya-env.eejrfgdrsx.us-east-2.elasticbeanstalk.com/here_button?Keyword="+params.Keyword+"&segmentId="+params.segmentId+"&Distance="+params.Distance+"&lat1="+params.lat1+"&lon1="+params.lon1)
		.then(function (response)
		{
		console.log(response);
				$scope.embed=response.data._embedded;
		if($scope.embed!=undefined)
		{
				$scope.table_content=response.data._embedded.events;
				
		
		}else
		{ $scope.table_content="";
	$scope.check_empty_table=true;
	}
                $scope.table_statuscode = response.status;
                $scope.table_statustext = response.statusText;
                $scope.detail_button = true;
                $scope.progress = false;
		})
	    });
	} 
	}
	

	$scope.enableDetails = function(event) 
	{
		$scope.enableDetailsButton = true;
		$scope.detailsEvent = event;
		$scope.current_x=event;
		$scope.ven_loc=[$scope.detailsEvent._embedded.venues[0].location.latitude, $scope.detailsEvent._embedded.venues[0].location.longitude];
		if($scope.current_x.name in $scope.favor)
		{
            document.getElementById("check_star").classList.remove("fa-star-o");
			
            document.getElementById("check_star").classList.add("fa-star");
        }
        else
		{
            document.getElementById("check_star").classList.remove("fa-star");
			
            document.getElementById("check_star").classList.add("fa-star-o");
        }
	}
	$scope.recent_detail = function () 
	{
        $scope.show_detail = true;
		$scope.twitter_text = "https://twitter.com/intent/tweet?text=Check out "+$scope.detailsEvent.name+" locate at "+$scope.detailsEvent.name+". Website:"+$scope.detailsEvent.url;
    }
    $scope.list_function = function () 
	{
        $scope.show_detail = false;
    }
	
	$scope.upco_check=false;
	
	$scope.songkick=function()
	{
		$http.get ("http://nodealekhya-env.eejrfgdrsx.us-east-2.elasticbeanstalk.com/songkick?api=9afr29ONgztak9D7&venue="+$scope.detailsEvent._embedded.venues[0].name)
		.then( function(response)
		{
			if(response.data.resultsPage!=undefined && response.data.resultsPage.results!=undefined&&response.data.resultsPage.results.event!=undefined)
			$scope.songkick_data=response.data.resultsPage.results.event;
			else $scope.upco_check=true;
		});
	}
	$scope.sort_by_field = "";
    $scope.rever = false;
    $scope.sort_function = function (sort,r)
	{
        $scope.sort_by_field = sort;
        $scope.rev = r;
    }
	
	$scope.asc=function()
	{
		$scope.rev = false;
	}
	$scope.dsc=function()
	{
		$scope.rev = true;
	}
    
	
    $scope.favor = {};
	
	$scope.show_favor=false;
	
	var index = 0;
	
    for(x in localStorage)
	{	if(index > 6)
			$scope.favor[x]= JSON.parse(localStorage.getItem(x));
		index ++;
    }
    $scope.check_favor = !angular.equals($scope.favor,{});
    $scope.set_favor = function(x) 
	{
        if(document.getElementById(x.name).classList.contains("fa-star-o"))
		{
            document.getElementById(x.name).classList.remove("fa-star-o");
			
            document.getElementById(x.name).classList.add("fa-star");
            
			localStorage.setItem(x.name,JSON.stringify(x));
			
			$scope.favor[x.name]=x;
            
        }
		else
		{
            
			document.getElementById(x.name).classList.remove("fa-star");
            
			document.getElementById(x.name).classList.add("fa-star-o");
            
			localStorage.removeItem(x.name);
            
			delete $scope.favor[x.name];

        }
        $scope.check_favor = !angular.equals($scope.favor,{});
        //$scope.$apply();

    }
    $scope.remove_favor = function (x,i) 
	{
		
		   localStorage.removeItem(x.name);
			delete $scope.favor[x.name];
		
        $scope.check_favor = !angular.equals($scope.favor,{});
       //$scope.$apply();
    }
    $scope.results_to_favor = function() 
	{
        $scope.show_favor = true;
        $scope.show_detail = false;
		localStorage.clear();
    }
    $scope.favor_to_results = function() 
	{
        $scope.show_favor = false;
        $scope.show_detail = false;
    }
	$scope.auto=function()
	{
		var params="";
		params={};
		params.Keyword=$scope.user.Keyword;
		$http.get ("http://nodealekhya-env.eejrfgdrsx.us-east-2.elasticbeanstalk.com/auto_complete?Keyword="+params.Keyword)
		.then(function (response)
		{
			if(response.data!=undefined&&response.data._embedded!=undefined&&response.data._embedded.attractions!=undefined)
				$scope.get_auto_details=response.data._embedded.attractions;
			console.log($scope.get_auto_details[0].name);
			//console.log($scope.get_auto_details[1].name);
		}
	);
	}
	$scope.google_check=false;
	$scope.google_photos_func=function()
	{
		var params="";
		params={};
		params.Keyword=[];
		$scope.get_google_details=[];
		for(var i=0;i<2;i++)
		{
			params.Keyword[i]=$scope.detailsEvent._embedded.attractions[i].name;
			$http.get ("http://nodealekhya-env.eejrfgdrsx.us-east-2.elasticbeanstalk.com/google_photos?Keyword="+params.Keyword[i])
			.then(function (response) {
				if(response.data!=undefined&&response.data.items!=undefined)
					$scope.get_google_details.push(response.data.items);
				else $scope.google_check=true;
				console.log($scope.get_google_details[0].link);
				//console.log($scope.get_auto_details[1].name);
			});
		}
	}
	
	$scope.fill_text = function(string)
	{  
           $scope.user.Keyword= string;  
           $scope.hide_this = true;  
      } 
 $scope.limit=5;
	 $scope.showMore=function()
	 {
		 $scope.limit=$scope.songkick_data.length;
	 }
	 $scope.showLess = function() 
	 {
     $scope.limit = 5;
	};

$scope.spotify_check=false;
$scope.spotify=function()
{
	params.Keyword=$scope.detailsEvent._embedded.attractions[0].name;
	$http.get("http://nodealekhya-env.eejrfgdrsx.us-east-2.elasticbeanstalk.com/spotify?Keyword="+params.Keyword)
	.then(response)
	{
		if(response.data!=undefined&&response.data.artists!=undefined&&response.data.items!=undefined)
		{
			$scope.spot_data=response.data.items;
			$scope.spotify_check=true;
		}
		else
			$scope.spotify_check=false;
	}
}

$scope.check_artist_team=function()
{
	if($scope.detailsEvent.classifications[0].segment.name=='sports')
		$scope.google_photos_func();
	else if($scope.detailsEvent.classifications[0].segment.name=='music')
	{
		$scope.spotify();
		$scope.google_photos_func();
	}
	
}  
	
	});

    

