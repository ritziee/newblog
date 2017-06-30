(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('StoreDialogController', StoreDialogController);

    StoreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Store'];

    function StoreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Store) {
        var vm = this;

        vm.store = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.store.id !== null) {
                Store.update(vm.store, onSaveSuccess, onSaveError);
            } else {
                Store.save(vm.store, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blogApp:storeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
