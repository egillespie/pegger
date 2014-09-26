var peggerControllers = angular.module('peggerControllers', []);

peggerControllers.controller('PeggerCtrl', ['$scope', 'games', function ($scope, games) {
    $scope.selectedPeg = null;

    games.post(function(response) {
        $scope.game = response.data;
        $scope.messageKey = '';
    }, function(response) {
        $scope.messageKey = 'games.post.error';
    });

    findPeg = function(row, column) {
        var pegs = $scope.game.pegs;
        for (var i = 0; i < pegs.length; i++) {
            if (pegs[i].position.row == row && pegs[i].position.column == column) {
                return pegs[i];
            }
        }
        return null;
    };

    selectPeg = function(row, column) {
        $scope.selectedPeg = findPeg(row, column);
    };

    $scope.pegStyle = function(row, column) {
        var style = '';
        var peg = findPeg(row, column);
        if (peg) {
            style += peg.type[0];
        }
        if ($scope.selectedPeg && $scope.selectedPeg.position.row == row
                && $scope.selectedPeg.position.column == column) {
            style += " selected";
        }
        return style;
    };

    $scope.pegClicked = function(row, column) {
        if ($scope.selectedPeg) {
            var peg = JSON.parse(JSON.stringify($scope.selectedPeg));
            peg.position.row = row;
            peg.position.column = column;
            games.byId($scope.game.gameId).pegs.byId(peg.pegId).put(peg, function(response) {
                $scope.selectedPeg = null;
                $scope.game = response.data;
                $scope.messageKey = '';
                $scope.message = '';
            }, function(response) {
                $scope.selectedPeg = null;
                switch (response.status) {
                    case 400:
                        $scope.messageKey = 'pegs.notSelected.error';
                        break;
                    case 403:
                        $scope.messageKey = 'pegs.user.error';
                        $scope.message = response.data.message;
                        break;
                    case 404:
                        $scope.messageKey = 'games.notFound.error';
                        break;
                    case 409:
                        $scope.messageKey = 'pegs.conflict.error';
                        break;
                    default:
                        $scope.messageKey = 'pegs.put.error';
                        break;
                }
            });
        } else {
            selectPeg(row, column);
        }
    };
}]);
