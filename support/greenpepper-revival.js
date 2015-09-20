/**
 *Déclaration de l'application ipcApp
 */
var gprApp = angular.module('gprApp', ['gprServices', 'gprControllers']);
var gprServices = angular.module('gprServices', []);
var gprControllers = angular.module('gprControllers', ['ui.router']);

//support/ipc.js
'use strict';

gprControllers.config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider,   $urlRouterProvider) {
        $urlRouterProvider.when('', '/home');
        
        $stateProvider.state("home", {
            url: '/home',
            templateUrl: 'home.html',
            controller : 'gprCtrl'
        })
        .state("projects",{
            url: '/projects',
            templateUrl: 'projects.html',
            controller : 'gprCtrl'
        })
        .state("documentation", {
            url: '/documentation',
            templateUrl:'documentation.html',
            controller : 'DocumentationController'
        })
        .state("documentation.10minutes-testing", {
            url: '/10minutes-testing',
            templateUrl:'components/documentation/10minutes-testing.html',
            controller : 'DocumentationController'
        })
        .state("documentation.faq", {
            url: '/faq',
            templateUrl:'components/documentation/faq.html',
            controller : 'DocumentationController'
        })
        .state("documentation.specification-guide", {
            url: '/specification-guide',
            templateUrl:'components/documentation/specification-guide.html',
            controller : 'DocumentationController'
        })
        .state("documentation.developers-guide", {
            url: '/developers-guide',
            templateUrl:'components/documentation/developers-guide.html',
            controller : 'DocumentationController'
        })
        .state("documentation.maven-plugin", {
            url: '/maven-plugin',
            templateUrl:'components/documentation/maven-plugin.html',
            controller : 'DocumentationController'
        })
        .state("documentation.remote-agent", {
            url: '/remote-agent',
            templateUrl:'components/documentation/remote-agent.html',
            controller : 'DocumentationController'
        })
        .state("documentation.confluence-plugin", {
            url: '/confluence-plugin',
            templateUrl:'components/documentation/confluence-plugin.html',
            controller : 'DocumentationController'
        })
        .state("downloads", {
            url: '/downloads',
            templateUrl:'downloads.html',
            controller : 'gprCtrl'
        })
        .state("about", {
            url: '/about',
            templateUrl:'about.html',
            controller : 'gprCtrl'
        });
    }]);


gprControllers.controller('menuCtrl', ['$scope', '$location', function($scope,$location) {
    $scope.activateMenu = function(page) 
    {
        var currentRoute = $location.path().substring(1) || 'home';
        return page === currentRoute  || currentRoute.indexOf(page + "/") == 0? 'active' : '';
    };

}]);

gprControllers.controller('gprCtrl', ['$scope', function($scope) {
	
}]); 