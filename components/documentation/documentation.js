gprControllers.controller('DocumentationController', ["$scope", '$location', function ($scope, $location) {
	$scope.activateMenu = function(page) 
    {
		
        var currentRoute = $location.path().substring(1) || 'documentation/10minutes-testing';
        var clazz = "documentation/" + page === currentRoute ? 'active' : '';
        return clazz;
    };
	
  }]);