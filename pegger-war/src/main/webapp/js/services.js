var peggerServices = angular.module('peggerServices', []);

peggerServices.factory('games', ['$http', function($http) {
    return {
        post: function(success, error) {
            return $http.post('/games').then(function(response) {
                success(response);
            }, function(response) {
                error(response);
            });
        },
        byId: function(gameId) {
            return {
                pegs: {
                    byId: function(pegId) {
                        return {
                            put: function(data, success, error) {
                                return $http.put('/games/'+gameId+'/pegs/'+pegId, data).then(function(response) {
                                    success(response);
                                }, function(response) {
                                    error(response);
                                });
                            }
                        }
                    }
                }
            }
        }
    }
}]);
