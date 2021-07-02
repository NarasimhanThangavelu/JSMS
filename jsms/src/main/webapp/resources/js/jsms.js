'user strict';
var app = angular.module("jsmsApp", []);
app.controller("jsmsController", function($scope, $http) {
	$scope.chkboxList = [];
	$scope.selected = [];
	$scope.customerDetail = {};
//	$scope.tempEmployerDetail = {};
//	$scope.hideShow = false;
//	$scope.sample = {};
////	$scope.model = {edit:angular.copy(result.data),show:angular.copy(result.data)};
//	var i = 0;
	$scope.qualification = {};
//	$scope.paymentTypes = ["---Select---","Daily","Monthly","Weekly"];
	$scope.sexTypes = ["---Select---","Female","Male","Aravani"];
//	$scope.loanStatuses = ["---Select---","Closed","Delinquency","Open"];
//	$scope.datePattern = "/^(0[1-9]|[1-2][0-9]|3[0-1])\/(0[1-9]|1[0-2])\/[0-9]{4}$/";
	
//	$scope.paymentTypes.setDefault=$scope.paymentTypes[1];
	$scope.init = function() {
		$http.get('http://localhost:8080/jsms/jsmsservice/jobseekers', angular.toJson($scope.chkboxList)).
		then(onJobseekersSuccess, onError);
	};
	
var onJobseekersSuccess = function (response) {
		
		if (response.data.message == undefined && response.data.payload.allJobSeekers != undefined) {
			$scope.customerDetails = response.data.payload.allJobSeekers;
			$scope.showCustomerDatas=response.data.payload.allJobSeekers.length > 0 ? true : false;
			$scope.hideShow = false;
		} else {
			alert("The selected record not deleted successfully because of " + response.data.message);
			$scope.hideShow = false;
		}
    };
    
    var onError = function (response) {
		alert("The selected record not deleted successfully" +angular.toJson(response));
    }
    
	
	$scope.save = function(customerDetailObj, branchDetails) {
		
		if (validate(customerDetailObj)) {
			var customerDetails;
			
			if ($scope.custID == undefined || $scope.custID == "" ) {
				customerDetails = { customerDetail : customerDetailObj};
				$http.post('http://localhost:8080/jsms/jsmsservice/jobseekers/create', angular.toJson(customerDetails)).
				then(function(response) {
					if (response.data.payload.allJobSeekers != undefined) {
						alert("The new customer added successfully");
						$scope.customerDetails = response.data.payload.allJobSeekers;
						$scope.showCustomerDatas=response.data.payload.allJobSeekers.length > 0 ? true : false;
//						angular.copy(response.data.payload.allEmployerDetails,$scope.tempEmployerDetail);
						$scope.hideShow = false;
					} else {
						alert("The new customer not added successfully");
						$scope.hideShow = true;
					}
				}).catch(function (err) {alert("The new customer not added successfully : exception : " + angular.toJson(err))});
			} else {
				customerDetails = { "id":$scope.custID, customerDetail : customerDetailObj};
				$http.post('http://localhost:8080/jsms/jsmsservice/jobseekers/update', angular.toJson(customerDetails)).
				then(function(response) {
					if (response.data.payload.allJobSeekers != undefined) {
						alert("The selected record updated successfully");
						$scope.customerDetails = response.data.payload.allJobSeekers;
						$scope.showCustomerDatas=response.data.payload.allJobSeekers.length > 0 ? true : false;
//						angular.copy(response.data.payload.allEmployerDetails,$scope.tempEmployerDetail);
						$scope.hideShow = false;
					} else {
						alert("The selected record not updated successfully");
						$scope.hideShow = true;
					}
				}).catch(function (err) {alert(angular.toJson(err))});;
			}
			
		} else {
			var commonMsg = "Please fill mandatory(*) fields in both customer and qualification details, if award and experience not required please delete and save!";
			if (errors.length !=0) {
				errors.push ("\n"+commonMsg);
			} else {
				errors.push (commonMsg);
			}
			alert(errors);
		}
		
	};
    var errors = [];
	var validate = function(customerDetailObj) {
		errors = [];
		if (isValid(customerDetailObj.firstName) 
				&& (isValid(customerDetailObj.dobStr) && dateValidate(customerDetailObj.dobStr, "DOB"))
				&& isValid(customerDetailObj.address.addressLine2)
				&& isValid(customerDetailObj.address.city)
				&& (isValid(customerDetailObj.address.zip) && numberValidate(customerDetailObj.address.zip, "Zip"))
				&& isValid(customerDetailObj.address.state)
				&& (isValid(customerDetailObj.contactNumber) && numberValidate(customerDetailObj.contactNumber, "Contact")) 
				&& customerDetailObj.qualificationDetails.length > 0) {
			
			if (customerDetailObj.sex == "---Select---") {
				errors.push("Please select your sex");
				return false;
			}
			
			var flag = 0;
			angular.forEach(customerDetailObj.qualificationDetails, function(qualificationDetail){
				if (isValid(qualificationDetail.qualificationName) 
					&& isValid(qualificationDetail.department) 
					&& isValid(qualificationDetail.institution) 
					&& isValid(qualificationDetail.university) 
					&& isValid(qualificationDetail.marks)) {
					flag++;
				}
			});
			
			var weFlag = 0;
			
			angular.forEach(customerDetailObj.workExperienceDetails, function(workExperienceDetail){
				if (isValid(workExperienceDetail.employerName) 
					&& isValid(workExperienceDetail.designation) 
					&& isValid(workExperienceDetail.startDateStr) && dateValidate(workExperienceDetail.startDateStr, "Start Date")
					&& isValid(workExperienceDetail.endDateStr) && dateValidate(workExperienceDetail.endDateStr, "End Date")
					&& isValid(workExperienceDetail.address.city)
					&& isValid(workExperienceDetail.address.state)
					&& isValid(workExperienceDetail.jobDesc)) { 
					weFlag++;
				} 
			});
			
			var awardFlag = 0;
			angular.forEach(customerDetailObj.awardDetails, function(awardDetail){
				if (isValid(awardDetail.awardName) 
					&& isValid(awardDetail.institution) 
					&& isValid(awardDetail.awardDesc)) { 
					awardFlag++;
				} 
			});
			
			if (weFlag == customerDetailObj.workExperienceDetails.length 
					&& awardFlag == customerDetailObj.awardDetails.length
					&& flag == customerDetailObj.qualificationDetails.length) {
				return true;
			}
		}
		return false;
		
	};
	
	var numberValidate = function(number, fieldName) {
		if ("Zip" == fieldName ) {
			if (isNaN(number) || number.length != 6) {
				errors.push("Zip (6 digit) should be a number ");
				return false;
			}
		}
		if ("Contact" == fieldName ) {
			if (isNaN(number) || number.length != 10) {
				errors.push("Contact Number(10 digit) should be a number ");
				return false;
			}
		}
		return true;
	};
	
	var dateValidate=function(date, fieldName) {
		let dob = isDateValid(date, fieldName);
		if (dob == "Not valid") {
			return false;
		}
		return true;
	};
	
	var isDateValid = function(date, fieldName) {
		if (isValid(date)) {
			let valid = new Date(date);
			if (valid != "Invalid Date") {
				return valid;
			} else {
				errors.push("Please give valid date with format (MM/DD/YYYY) for "+ fieldName);
				return "Not valid";
			}
		}
		return "empty";
	};
	
	var isValid = function(value) {
		var isNotEmpty = false;
		if (value != undefined) {
			if (isNaN(value)) {
				isNotEmpty =  value.trim() != "";
			} else {
				isNotEmpty =  value > 0;
			}
		}
		return isNotEmpty
	};
//	
	$scope.update = function(customerDetails, active){
		$scope.custID = customerDetails.id;
		$scope.customerDetail = customerDetails.customerDetail;
		$scope.hideShow = true;
		$scope.saveDiv = true;
		$scope.showQualificationDatas=true;
		if ($scope.customerDetail.awardDetails.length == 0) {
			$scope.showAwardDetails=false;
		} else {
			$scope.showAwardDetails=true;
		}
		
		if ($scope.customerDetail.workExperienceDetails.length == 0) {
			$scope.showDeleteExp=false;
		} else {
			$scope.showDeleteExp=true;
		}
		$scope.deleteWE=true;
	};
	
	$scope.deleteRecords = function() {
		if ($scope.chkboxList.length == 0) {
			alert("Please select record(s) to delete!");
		} else if(confirm("Are you sure to delete the selected record(s)?")) {
			$http.post('http://localhost:8080/jsms/jsmsservice/jobseekers/delete', angular.toJson($scope.chkboxList)).
			then(onSuccess, onError);
		}
	};
	
	var onSuccess = function (response) {
		
		if (response.data.message == undefined && response.data.payload.allJobSeekers != undefined) {
			alert("The selected record(s) deleted successfully");
			$scope.customerDetails = response.data.payload.allJobSeekers;
			$scope.showCustomerDatas=response.data.payload.allJobSeekers.length > 0 ? true : false;
			$scope.hideShow = false;
			$scope.customerDetail = {};
			$scope.chkboxList = [];
		} else {
			alert("The selected record not deleted successfully because of " + response.data.message);
			$scope.hideShow = false;
		}
    };
	
	var onError = function (response) {
		alert("The selected record not deleted successfully" +angular.toJson(response));
    }
	
	$scope.search = function() {
		if ($scope.searchKey == undefined) {
			$scope.searchKey = "";
		}
		$http.get('http://localhost:8080/jsms/jsmsservice/jobseekers/search.json?searchKey='+$scope.searchKey).
		then(function(response) {
			$scope.customerDetails = response.data.payload.allJobSeekers;
			$scope.showCustomerDatas=response.data.payload.allJobSeekers.length > 0 ? true : false;
//		angular.copy(response.data.payload.allEmployerDetails,$scope.tempEmployerDetail);
		$scope.hideShow = false;
		});
	};
	
	$scope.create = function(){
		$scope.saveDiv = true;
		$scope.custID = "";
		$scope.customerDetail = {};
		$scope.customerDetail.qualificationDetails = [];
		$scope.customerDetail.address = {};
		$scope.customerDetail.awardDetails=[];
		$scope.customerDetail.workExperienceDetails=[];
		$scope.customerDetail.workExperienceDetails.address={};
		if ($scope.customerDetail.qualificationDetails.length == 0) {
			$scope.addQualification();
			$scope.addAward();
			$scope.addWorkExperience();
		}
		$scope.customerDetail.sex = $scope.sexTypes[0];
		$scope.hideShow = true;
		$scope.showQualificationDatas=true;
		$scope.showAwardDetails=true;
		$scope.showDeleteExp=true;
		$scope.deleteWE=true;
		
	}
//	
	$scope.addQualification = function() {
		var qualification = {};
		$scope.customerDetail.qualificationDetails.push(qualification);
	};
//	
	$scope.deleteQualification = function(index) {
		if ($scope.customerDetail.qualificationDetails.length-1 > 0) {
			var test = $scope.customerDetail.qualificationDetails.splice(index,1);
		} else {
			alert("Minimum one qualification details required to create qualification details! ");
		}
	};
	$scope.addAward = function() {
		var award = {};
		$scope.customerDetail.awardDetails.push(award);
		$scope.showAwardDetails=true;
	};
	
	$scope.addWorkExperience = function() {
		var work = {};
		$scope.customerDetail.workExperienceDetails.push(work);
		if ($scope.customerDetail.workExperienceDetails.length > 0) {
			$scope.showDeleteExp=true;
		}
	};
	
	$scope.deleteWorkExperience = function(index) {
		if ($scope.customerDetail.workExperienceDetails.length > 0) {
			if (confirm("Are you sure to delete the experience details which recently added?")) {
				var test = $scope.customerDetail.workExperienceDetails.splice(index,1);
				if ($scope.customerDetail.workExperienceDetails == 0) {
					$scope.showDeleteExp=false;
				}
			}
		} else {
			alert("Please add experience details to delete!");
		}
	};
	
	$scope.deleteAward = function(index) {
			var test = $scope.customerDetail.awardDetails.splice(index,1);
			if ($scope.customerDetail.awardDetails == 0) {
				$scope.showAwardDetails=false;
			}
	};
	
//	
	$scope.viewRecord = function(customerDetails, active){
		$scope.customerDetail = customerDetails.customerDetail;
		$scope.hideShow = true;
		$scope.saveDiv = false;
		$scope.showQualificationDatas=true;
		if ($scope.customerDetail.awardDetails.length == 0) {
			$scope.showAwardDetails=false;
		} else {
			$scope.showAwardDetails=true;
		}
		
		if ($scope.customerDetail.workExperienceDetails.length == 0) {
			$scope.showDeleteExp=false;
		} else {
			$scope.showDeleteExp=true;
		}
	};

	$scope.getSelectedCheckboxes = function(selectedCustId, event){
		
		if (event) {
			if (!$scope.chkboxList.includes(selectedCustId)) {
				$scope.chkboxList.push(selectedCustId);
			} 			
		} else {
			
			for(var i=0 ; i < this.chkboxList.length; i++) {
			       if(this.chkboxList[i] == selectedCustId) {
			         this.chkboxList.splice(i,1);
			      }
			}
		}
		};

	$scope.cancel = function() {
		$scope.hideShow = false;
	};
	
	$scope.clear = function() {
		$scope.customerDetail = {};
	};
	
	$scope.downloadPdf = function(customerDetail) {

		if (confirm("Are you sure want to generate resume?")) {		
			$http.post('http://localhost:8080/jsms/jsmsservice/jobseekers/downloadPdf', angular.toJson(customerDetail.customerDetail)).
			then(function(response) {
				
				if (response.data.payload.path != undefined) {
					var msg = [];
					msg.push(customerDetail.customerDetail.firstName + " resume created in the following location");
					msg.push("\n" +response.data.payload.path);
					alert(msg);
				}
				
			}).catch(function (err) {alert(angular.toJson(err))});;
		}
	};
	
	
	
});