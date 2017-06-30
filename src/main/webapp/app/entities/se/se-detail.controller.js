(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('SeDetailController', SeDetailController);

    SeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Se'];

    function SeDetailController($scope, $rootScope, $stateParams, previousState, entity, Se) {
        var vm = this;

        vm.se = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blogApp:seUpdate', function(event, result) {
            vm.se = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
