angular.module('employerController').factory('employerService', ['$http', '$q', function($http, $q){
 
    var factory = {
        fetchAllUsers: fetchAllEmployers
    };
 
    return factory;
 
    function fetchAllEmployers() {
        var deferred = $q.defer();
        $http.get("http://localhost:8080/ems/employerservice/employers")
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while fetching Users');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }
}]);