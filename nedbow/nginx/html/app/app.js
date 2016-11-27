// Define the `phonecatApp` module
var app = angular.module('app', []).config(function ( $compileProvider) {
  $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|uber):/);
      // whitelists non-http: protocols. specifically we need coui for coherent.
});

app.controller('mainController', ['$scope', '$http', function($scope, $http) {
  var config = {
      params: {
        'url': APP_SETTINGS.URL
      }
  };

  $scope.markSelected = false;

  var myMap;
  var selectedMark = null;
  $scope.closest_text = 'Ближайший к Вам';
  $scope.run_time = '';
  $scope.point_address = 'Адрес';
  $scope.work_time = 'Время работы';
  $scope.orgPoints = {};

  function getCurrentLocation() {
    var options = {
      enableHighAccuracy: true,
      timeout: 8000,
      maximumAge: 0
    };

    function success(pos) {
      var coords = pos.coords;
      var LOCATION_SETTINGS = {};
      LOCATION_SETTINGS.search = 'Евразия';
      LOCATION_SETTINGS.lat = coords.latitude;
      LOCATION_SETTINGS.lon = coords.longitude;
      LOCATION_SETTINGS.spn_lat = 0.528876;
      LOCATION_SETTINGS.spn_lon = 1.161804;
      console.log(LOCATION_SETTINGS);
      var partnerPoints = findOrganization(LOCATION_SETTINGS);
   };

   function error(err) {
     console.warn('ERROR(' + err.code + '): ' + err.message);
   };

   navigator.geolocation.getCurrentPosition(success, error, options);
  }

  function bildMap(orgPoints, userLocation) {
    $scope.userLocation = userLocation;
    ymaps.ready(init);

    function init(){
      myMap = new ymaps.Map("map", {
          center: [userLocation.lat, userLocation.lon],
          zoom: 12
      }),
      myPlacemark = new ymaps.Placemark(myMap.getCenter(), {
          hintContent: 'Собственный значок метки',
          balloonContent: 'Вы здесь'
      }, {
          // Опции.
          // Необходимо указать данный тип макета.
          iconLayout: 'default#image',
          // Своё изображение иконки метки.
          iconImageHref: 'img/myLocation.png',
          // Размеры метки.
          iconImageSize: [28, 28],
          // Смещение левого верхнего угла иконки относительно
          // её "ножки" (точки привязки).
          iconImageOffset: [0, 0]
      });

      /*myGeoObject = new ymaps.GeoObject({
          geometry: {
              type: "Circle",// тип геометрии - точка
              coordinates: [userLocation.lat, userLocation.lon], // координаты точки
              radius: 30
         }
      });*/
      myMap.geoObjects.add(myPlacemark); // Размещение геообъекта на карте.

      var coords = [],
          myCollection = new ymaps.GeoObjectCollection({}, {
             preset: 'islands#redIcon', //все метки красные
             draggable: true // и их можно перемещать
          });

      $scope.orgPoints = orgPoints;
      console.log(orgPoints);

      for(var i = 0; i < orgPoints.organization_points.length; i++) {
        var point = orgPoints.organization_points[i];
        coords.push([point.lat, point.lon]);
      }

      for (var i = 0; i < coords.length; i++) {
          var placemark = new ymaps.Placemark(coords[i], {id: i, coords: coords[i]}, {preset: 'islands#blueIcon', draggable: 0});
          myCollection.add(placemark);
          placemark.events.add('click', function (e) {
            var target = e.get('target'),
                type = e.get('type');
            console.log(APP_SETTINGS.DATA.group_id);
            changeCouponDescription(APP_SETTINGS.DATA.group_id);
            changeMarkDescription(e.get('target').properties.get('id'), e.get('target').properties.get('coords'), orgPoints);
            target.options.set('preset', 'islands#blackIcon');
            if(selectedMark) selectedMark.options.set('preset', 'islands#blueIcon');
            selectedMark = target;
          });
      }

      myMap.geoObjects.add(myCollection);

      //console.log(orgPoints.right_down_lat + 0.5);

      var COEF = 0.015;

      myMap.setBounds([[orgPoints.right_down_lat+COEF, orgPoints.right_down_lon-COEF], [orgPoints.left_up_lat-COEF, orgPoints.left_up_lon+COEF]]);

      var closest_lat = orgPoints.organization_points[orgPoints.closest_point].lat;
      var closest_lon = orgPoints.organization_points[orgPoints.closest_point].lon;

      changeMarkDescription(orgPoints.closest_point, [closest_lat, closest_lon], orgPoints);

      /*$scope.closest_lat = orgPoints.organization_points[orgPoints.closest_point].lat;
      $scope.closest_lon = orgPoints.organization_points[orgPoints.closest_point].lon;
      $scope.point_address = orgPoints.organization_points[orgPoints.closest_point].address;
      $scope.work_time = orgPoints.organization_points[orgPoints.closest_point].availability;*/

      //[[userLocation.lat, userLocation.lon], [closest_lat, closest_lon]]
    }
  }

  function changeCouponDescription(group_id) {
    console.log('change coupon');
    $http.get('/api/settings?group_id='+group_id).
     success(function(data) {
        setTimeout(function() {
          renderCoupon(data);
        }, 500);
    });
  }

  function renderCoupon(data) {
    $scope.coupon_title = data.coupon_title;
    $scope.coupon_content = data.coupon_content;
    $scope.$apply();
  }

  function changeMarkDescription(id, coords, orgPoints) {
    var desc = orgPoints.organization_points[id];
    $scope.point_address = desc.address;
    $scope.work_time = desc.availability;
    $scope.uber_link = encodeURI(desc.uberLink);
    $scope.phone = desc.phone;
    $scope.buildRoute(coords);
    $scope.markSelected = true;
    $scope.$apply();
  }

  $scope.buildRoute = function(coords) {
    var time;
    coords = (!coords) ? [$scope.closest_lat, $scope.closest_lon] : coords;
    console.log(coords);
    //$scope.closest_text = 'Hello';
    var rt = ymaps.route([[$scope.userLocation.lat, $scope.userLocation.lon], coords]).then(
      function (route) {
          //myMap.geoObjects.add(route);
          var firstPath = route.getPaths().get(0);
          time = Math.floor(Math.floor(firstPath.getTime()) / 60);
          $scope.run_time = '~ ' + time + ' мин.';
          //$scope.run_time = 'Расстояние ' + time + ' км';
      },
      function (error) {
          alert('Возникла ошибка: ' + error.message);
      }
    );
    //$scope.closest_text = rt.getLength();
    //console.log('buildRoute');
    //console.log(time);
    //this.run_time = 'Расстояние ' + time + ' км';
    //$scope.closest_text = 'Расстояние ' + myMap.geoObjects.getLength() + ' км';
    //$scope.run_time = firstPath.getTime();
  }

  $http.get('/processing', config).
   success(function(data) {
      APP_SETTINGS.DATA = data;
      //setupGroupName(APP_SETTINGS.DATA.group_id);
      //$scope.group = APP_SETTINGS.CURRENT_GROUP;
      //console.log(APP_SETTINGS);
      //$scope.group(APP_SETTINGS.DATA.group_id);
      //setupGroup(APP_SETTINGS.DATA.group_id, APP_SETTINGS.DATA.access_token);
      //testConnect();
      getCurrentLocation();
  });

  function setupGroup(group_id, access_token) {
    $http.jsonp('https://api.vk.com/method/groups.getById?group_id='+group_id+'&access_token='+access_token+'&v=5.60&callback=JSON_CALLBACK').
     success(function(data) {
        $scope.group = data.response[0];
    });
  }

  function findOrganization(location) {
    $http.get('/api/find_organization', {params: location}).
     success(function(data) {
        bildMap(data, location);
    });
  }
}]);
