(function() {
    'use strict';

    angular
        .module('blogApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('k', {
            parent: 'entity',
            url: '/k',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.k.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/k/ks.html',
                    controller: 'KController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('k');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('k-detail', {
            parent: 'k',
            url: '/k/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.k.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/k/k-detail.html',
                    controller: 'KDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('k');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'K', function($stateParams, K) {
                    return K.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'k',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('k-detail.edit', {
            parent: 'k-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/k/k-dialog.html',
                    controller: 'KDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['K', function(K) {
                            return K.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('k.new', {
            parent: 'k',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/k/k-dialog.html',
                    controller: 'KDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('k', null, { reload: 'k' });
                }, function() {
                    $state.go('k');
                });
            }]
        })
        .state('k.edit', {
            parent: 'k',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/k/k-dialog.html',
                    controller: 'KDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['K', function(K) {
                            return K.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('k', null, { reload: 'k' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('k.delete', {
            parent: 'k',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/k/k-delete-dialog.html',
                    controller: 'KDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['K', function(K) {
                            return K.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('k', null, { reload: 'k' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
