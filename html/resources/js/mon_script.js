map=null;
CenterLat=45.52;
CenterLon=-73.58;
marker=null;
position=null;

$(window).bind("load",  loadChirp());

function loadChirp(){
   $.getJSON("http://" + window.location.hostname + ":8888/data.json", 
    function( data ) {
      position = data;
      var items = "";
      $.each( data, function( key, val ) {
        items += "<div id='"+key+"'><h1> " + key + "</h1>";
        
        
        items += "<table class='table table-striped'>";
        $.each( val, function( key2, val2 ) {

          
          items += "<TR><TH> " + key2 + "</TH><TD> " + val2 + "</TD></TR>";
          
          });
        items += "</table></div>";
       
        
        
      });
     console.log(items);
     //update google map
     var newPoint = new google.maps.LatLng(position.ourPosition.latitude, position.ourPosition.longitude);

      
      if (marker) {
        // Marker already created - Move it
        marker.setPosition(newPoint);
      }
      else {
        // Marker does not exist - Create it
        marker = new google.maps.Marker({
          position: newPoint,
          map: map
        });
      }

      // Center the map on the new position
      map.setCenter(newPoint);
     
      $('#message').html(items);
      

   setTimeout("loadChirp()",200);
}
)};

function initialize(){
        var mapOptions = {
            center: new google.maps.LatLng(CenterLat, CenterLon),
            zoom: 10,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
}



