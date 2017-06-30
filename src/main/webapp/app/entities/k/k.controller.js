(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('KController', KController);

    KController.$inject = ['K'];

    function KController(K) {

        var vm = this;

        vm.ks = [];

        loadAll();

        function loadAll() {
            K.query(function(result) {
                vm.ks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
