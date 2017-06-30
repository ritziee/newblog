(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('KDialogController', KDialogController);

    KDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'K'];

    function KDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, K) {
        var vm = this;

        vm.k = entity;
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
            if (vm.k.id !== null) {
                K.update(vm.k, onSaveSuccess, onSaveError);
            } else {
                K.save(vm.k, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blogApp:kUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
