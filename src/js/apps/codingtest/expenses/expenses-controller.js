"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config", "restalchemy", function ExpensesCtrl($rootScope, $scope, $config, $restalchemy) {
	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

	var restExpenses = $restalchemy.init({ root: $config.apiroot }).at("expenses");

	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(expenses) {
			$scope.expenses = expenses;
		});
	}

	var calculateVat = function(val) {
		var vat = "";
		// If the value is not null
		if (val != null) {
			// Check to match a value and currency code
            var regEx = val.match(/(^\d*[\.\,]?\d*)|([A-Za-z]{3})/g);
            if (regEx.length > 0) {
            	var currency = 'GBP';
            	// Check the currency code
            	if (regEx.length > 1) {
            		currency = regEx[1];
				}
				// Only calculate VAT to display if we're working in GBP.
				if (currency == 'GBP') {
                    var amount = regEx[0]
                    if (amount > 0) {
                        vat = (amount * 0.2).toFixed(2);
                    }
				}
            }
		}
		return vat;
	}

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
			// Post the expense via REST
			restExpenses.post($scope.newExpense).then(function() {
				// Reload new expenses list
				loadExpenses();
			}).error(function(response) {
				// No UX error message display element yet, but log to console for development.
				console.log(response.message);
			});
		}
	};

	$scope.amountUpdate = function() {
        $scope.newExpense.vat = calculateVat($scope.newExpense.amount);
	}

	$scope.clearExpense = function() {
		$scope.newExpense = {};
	};

	// Initialise scope variables
	loadExpenses();
	$scope.clearExpense();
}]);
