(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('RdrController', RdrController);

    RdrController.$inject = ['Rdr'];

    function RdrController(Rdr) {

        var vm = this;

        vm.rdrs = [];

        loadAll();

        function loadAll() {
            Rdr.query(function(result) {
                vm.rdrs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
