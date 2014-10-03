var peggerApp = angular.module('peggerApp', ['ngRoute', 'peggerControllers', 'peggerServices']);

peggerApp.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/play', {
            templateUrl: 'partials/board.html',
            controller: 'PeggerCtrl'
        })
        .otherwise({
            templateUrl: 'partials/home.html'
        });
}]);
