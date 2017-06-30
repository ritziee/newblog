(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('SeController', SeController);

    SeController.$inject = ['Se'];

    function SeController(Se) {

        var vm = this;

        vm.ses = [];

        loadAll();

        function loadAll() {
            Se.query(function(result) {
                vm.ses = result;
                vm.searchQuery = null;
            });
        }
    }
})();
