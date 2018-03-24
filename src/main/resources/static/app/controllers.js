(function(angular) {
  var AppController = function($scope, Item, ngTableParams, $filter) {

	  $scope.items=[];
	  $scope.prcs=[{
		  name:"",
		  prc:""
	  }];
	  $scope.newPrc={
		  name:"",
		  prc:""
	  };
      $scope.tableParams = new ngTableParams({
          page: 1,            // show first page
          count: 10,           // count per page
          sorting: {
              name: 'asc'     // initial sorting
          }
      }, {
          total: $scope.items.length, // length of data
          getData: function($defer, params) {
              // use build-in angular filter
              var orderedData = params.sorting() ?
                      $filter('orderBy')($scope.items, params.orderBy()) :
                    	  $scope.items;

              $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
          }
      });
      
    Item.query(function(response) {
      $scope.items = response ? response : [];
      $scope.tableParams.reload();
    });
    
    $scope.addItem = function(newItem) {
      new Item({
		  name:newItem.name,
		  ftpLocation:newItem.ftpLocation,
		  lastFileCreationDate: new Date(),
		  inventoryConfig:{
			  brandNameColumnNumber:newItem.inventoryConfig.brandNameColumnNumber,
			  itemIdColumnNumber:newItem.inventoryConfig.itemIdColumnNumber,
			  inventoryValueColumnNumber:newItem.inventoryConfig.inventoryValueColumnNumber,
		  },
		  prcMaps:$scope.prcs
      }).$save(function(item) {
        $scope.items.push(item);
        $scope.tableParams.reload();
        $scope.newItem = {
      		  name:"",
      		  ftpLocation:"",
      		  inventoryConfig:{
      			  brandNameColumnNumber:"",
      			  itemIdColumnNumber:"",
      			  inventoryValueColumnNumber:"",
      		  }
        };
        $scope.prcs=[{
  		  name:"",
  		  prc:""
  	  	}];
      });
    };
    
    $scope.updateItem = function(item) {
      item.$update();
    };
    
    $scope.deleteItem = function(item) {
      item.$remove(function() {
        $scope.items.splice($scope.items.indexOf(item), 1);
        $scope.tableParams.reload();
      });
    };
    
    $scope.addPrc = function() {
		$scope.prcs.push(angular.copy($scope.newPrc));
	}
    
    $scope.deletePrc = function(prc) {
        $scope.prcs.splice($scope.prcs.indexOf(prc), 1);
	}
  };
  
  AppController.$inject = ['$scope', 'Item', 'ngTableParams', '$filter'];
  angular.module("myApp.controllers").controller("AppController", AppController);
}(angular));