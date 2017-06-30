(function() {
    'use strict';

    angular
        .module('blogApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('store', {
            parent: 'entity',
            url: '/store',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.store.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/store/stores.html',
                    controller: 'StoreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('store');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('store-detail', {
            parent: 'store',
            url: '/store/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.store.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/store/store-detail.html',
                    controller: 'StoreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('store');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Store', function($stateParams, Store) {
                    return Store.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'store',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('store-detail.edit', {
            parent: 'store-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store/store-dialog.html',
                    controller: 'StoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Store', function(Store) {
                            return Store.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('store.new', {
            parent: 'store',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store/store-dialog.html',
                    controller: 'StoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                publishers: null,
                                history: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('store', null, { reload: 'store' });
                }, function() {
                    $state.go('store');
                });
            }]
        })
        .state('store.edit', {
            parent: 'store',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store/store-dialog.html',
                    controller: 'StoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Store', function(Store) {
                            return Store.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('store', null, { reload: 'store' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('store.delete', {
            parent: 'store',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store/store-delete-dialog.html',
                    controller: 'StoreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Store', function(Store) {
                            return Store.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('store', null, { reload: 'store' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
