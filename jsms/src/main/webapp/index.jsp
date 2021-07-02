<!DOCTYPE html>
<html ng-app="jsmsApp">
<head>
<meta charset="ISO-8859-1">
<title>Job Seekers Management System</title>
	<script src="resources/angular/angular.min.js" type="text/javascript"></script>
	<script src="resources/js/jsms.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/main.css">
	<link rel="stylesheet" type="text/css" href="resources/css/jsmsLayout.css">
	<link rel="stylesheet" type="text/css" href="resources/css/tableLayout.css">
	<base target="_blank">

</head>
<body>

<div class="header">
  <a href="#default" class="logo">JSMS</a>
  <div class="header-right">
    <a class="active" href="#home">Home</a>
    <a href="#contact">Contact</a>
    <a href="#about">About</a>
  </div>
</div>

<div ng-controller="jsmsController" ng-init=init() style="width:100%;" >
	<div class="leftDiv">
		<form name="employerListForm">
			<div class="searchDiv">
				<fieldset>
					<legend style="font-weight:bold;">Job Seekers List :</legend>
						<div style="width:100%; float:right;padding-right:2px;">
			  				<table style="border:15px solid #ddd">
			  			    <tr style="border:1px solid #ddd">
			  			    	<td class="tab-cell-padding cellBorder fullWidth"><input type="text" ng-model="searchKey" class="fullWidth"/></td>
			  			    	<td class="tab-cell-padding tab-head cellBorder"><button ng-click="search()" class="tab-head fontBold fullWidth buttonStyle">Search</button></td>
			  			    </tr>
			  				</table>
		  				</div>
		  			<div class="listTableDiv">
					<table class="listTable">  
			         <tr class="tab-head">
			          <th class="tab-cell-padding" style="width:20px;"><input type="checkbox" ng-model="all"/></th>  
			          <th class="tab-cell-padding" style="width:150px;">Name</th>  
			          <th class="tab-cell-padding" style="width:100px;">DOB</th>  
			          <th class="tab-cell-padding" style="width:120px;">Village/Town</th>
			          <th class="tab-cell-padding" style="width:120px;">Contact</th>
			          <th class="tab-cell-padding" style="width:120px;">Action</th>  
			         </tr> 
			         <tr ng-if="showCustomerDatas==false" class="tab-row cellBorder">
			      		<td colspan="6" class="tab-cell-padding cellBorder" style="text-align:center;">No data records found</td>
					</tr>
				         <tr ng-if="showCustomerDatas==true" ng-repeat = "customerDetail in customerDetails" class="tab-row">  
					          <td class="tab-cell-padding"><input type="checkbox" ng-model="selected" value="{{ customerDetail.id }}"  ng-change="getSelectedCheckboxes(customerDetail.id, selected)" ng-checked="all"/></td>
					          <td class="tab-cell-padding">{{ customerDetail.customerDetail.firstName }} </td>  
					          <td class="tab-cell-padding">{{ customerDetail.customerDetail.dobStr }}</td>  
					          <td class="tab-cell-padding">{{ customerDetail.customerDetail.address.addressLine2 }}</td>  
					          <td class="tab-cell-padding">{{ customerDetail.customerDetail.contactNumber }}</td>  
					          <td align="middle">
						          <div id="cudButtons">
									<table>  
										<tr style="border:1px solid #ddd">
										    	<td class="tab-cell-padding tab-head cellBorder"><button ng-click="update(customerDetail, customerDetail.selected)" class="tab-head"  style="padding:2px;">Update</button></td>
										    	<td class="tab-cell-padding tab-head cellBorder"><button ng-click="viewRecord(customerDetail, customerDetail.selected)" class="tab-head"  style="padding:2px;">View</button></td>
										    	<td class="tab-cell-padding tab-head cellBorder"><button ng-click="downloadPdf(customerDetail)" class="tab-head"  style="padding:2px;">PDF</button></td>
							  			</tr>
							  		</table>
								</div>
							</td>
				         </tr>  
			        </table> 
				</div>
				
					<div id="cudButtons" style="float:right;">
						<table>  
							<tr style="border:1px solid #ddd">
							    	<td class="tab-cell-padding tab-head cellBorder"><button ng-click="create()" class="tab-head buttonStyle" >Create</button></td>
							    	<td class="tab-cell-padding tab-head cellBorder"><button ng-click="deleteRecords()" class="tab-head buttonStyle" >Delete</button></td>
				  			</tr>
				  		</table>
					</div>
				</fieldset>
		  		</div>
		  	</form>
	  	</div>
	  	
	  	<div class="detailsDiv">
	  	
	  		<div id="createUpdateView" ng-if="hideShow==false"  style="width:100%;height:650px;padding-top:18px;">
				<table style="width:100%;height:597px;border:3px solid #ddd;overflow:auto;">
					<tr>
						<td align="middle" style="color:dodgerblue;font-size:20px;">Please select a record to view customer details <td>
					</tr>
				</table>
			</div>
			
			
			<div id="createUpdateView" ng-if="hideShow"  style="width:100%;padding-top:10px;">
				<div style="float:right;width:100%;height:655px;">
				<fieldset>
				<legend style="font-weight:bold;">Customer Details:</legend>
				<div style="padding-right:5px;">
				
    			<form action="">
				<input type="hidden" ng-model=custID id="custID" name="custID" value="">
				<table class="createUpdateViewTable" style="width:100%;">
					<tr>
		  			    	<td class="tab-head tdwidth">First Name<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="customerDetail.firstName" ng-value="customerDetail.firstName" required maxlength="50"  class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Last Name : </td>
		  			    	<td class="tab-cell-padding cellBorder" style=""><input type="text" ng-model="customerDetail.lastName" ng-value="customerDetail.lastName" maxlength="50" class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Date of Birth<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.dobStr" ng-value="customerDetail.dobStr" required maxlength="10" placeholder="MM/DD/YYYY" class="fullWidth"/></td>
		  			    </tr>
		  			    <tr>
		  			    	<td class="tab-head tdwidth" >Street Name : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.address.addressLine1" ng-value="customerDetail.address.addressLine1" required maxlength="50"  class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Village or Town<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="customerDetail.address.addressLine2"  ng-value="customerDetail.address.addressLine2" maxlength="50" class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >City<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="customerDetail.address.city" ng-value="customerDetail.address.city" maxlength="50" class="fullWidth"/></td>
		  			    </tr>
		  			    
		  			     <tr>
		  			    	<td class="tab-head tdwidth" >Pin/Zip<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text"  ng-model="customerDetail.address.zip" ng-value="customerDetail.address.zip" required maxlength="10"  class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >State<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="customerDetail.address.state" ng-value="customerDetail.address.state" maxlength="50" class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Country<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.address.country" ng-value="customerDetail.address.country" maxlength="50" class="fullWidth"/></td>
		  			    </tr>
		  			    
		  			     <tr>
		  			    	<td class="tab-head tdwidth " >Aadhar No : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.aadharNumber" ng-value="customerDetail.aadharNumber" required maxlength="50"  class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Voter Id : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.voterIdNumber" ng-value="customerDetail.voterIdNumber"  maxlength="20" class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Passport No : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.passportNumber" ng-value="customerDetail.passportNumber" maxlength="20" class="fullWidth"/></td>
		  			    </tr>
		  			    
		  			    <tr>
		  			    	<td class="tab-head tdwidth " >Ration Card No : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.rationCardNumber" ng-value="customerDetail.rationCardNumber" required maxlength="20"  class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Dr License No : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.drivingLicenseNumber" ng-value="customerDetail.drivingLicenseNumber" maxlength="20" class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Email : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="customerDetail.email"  ng-value="customerDetail.email" maxlength="50" class="fullWidth"/></td>
		  			    </tr>
		  			    
		  			    <tr>
		  			    	<td class="tab-head tdwidth " >Contact No<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="customerDetail.contactNumber" ng-value="customerDetail.contactNumber" required maxlength="30"  class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Sex<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><select class="fullWidth" required ng-model="customerDetail.sex" ng-options="sex for sex  in sexTypes"></select></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Married<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder"  align="left"><input type="checkbox" ng-model="customerDetail.maritalStatus"  style="float:left;"/></td>
		  			    </tr>
		  			    <tr>
		  			    	<td colspan="8"></td>
		  			    </tr>
		  			</table>
		  		</form>	
		  	</div>
		  	
		  	<div style="overflow:auto;height:480px;padding-top:20px;" >
		  		<div>
		  		<fieldset>
	    		<legend style="font-weight:bold;">Education Details:</legend>
			  		<div style="height:160px;">
				  		<div style="height:131px;overflow:auto;">

				  			<table class="edu listTable" style="width:100%;"> 
				  				<thead> 
							         <tr class="tab-head">
								          <th class="tab-cell-padding">Qualification<span class="mandatory">*</span></th>  
								          <th class="tab-cell-padding">Department<span class="mandatory">*</span></th>  
								          <th class="tab-cell-padding">Institution<span class="mandatory">*</span></th>
								          <th class="tab-cell-padding">University/Board<span class="mandatory">*</span></th>
								          <th class="tab-cell-padding">Marks(%)<span class="mandatory">*</span></th>
								          <th class="tab-cell-padding" style="width:10px;">Action</th>   
							         </tr>
						         </thead>
						         <tbody>
							         <tr ng-if="showQualificationDatas==false" class="tab-row cellBorder">
					      				<td colspan="6" class="tab-cell-padding cellBorder" style="text-align:center;">No records found</td>
									</tr>
									<tr ng-if="showQualificationDatas==true" ng-repeat = "qualification in customerDetail.qualificationDetails track by $index" class="tab-row">
										<td class="tab-cell-padding cellBorder"><input type="text" ng-model="qualification.qualificationName" class="fullWidth"/></td>
								        <td class="tab-cell-padding cellBorder"><input type="text" ng-model="qualification.department" class="fullWidth"/></td>  
								        <td class="tab-cell-padding cellBorder"><input type="text" ng-model="qualification.institution" class="fullWidth"/></td>  
								        <td class="tab-cell-padding cellBorder"><input type="text" ng-model="qualification.university" class="fullWidth"/></td>  
								        <td class="tab-cell-padding cellBorder"><input type="text" ng-model="qualification.marks" class="fullWidth" /></td>
								        <td class="tab-cell-padding cellBorder"><button ng-click="deleteQualification($index)" class="fullWidth" class="tab-head"  style="padding:2px;">Delete</button></td>
								    </tr>
						        </tbody>
						   </table>
						</div>
						<div id="cudButtons" style="float:right;">
						<table>  
							<tr style="border:1px solid #ddd">
							    <td class="tab-cell-padding tab-head cellBorder"><button ng-click="addQualification()" class="tab-head" style="padding:2px;">Add</button></td>
				  			</tr>
				  		</table>
					</div>
					</div>
				</fieldset>
				</div>
		  		<div>
		  		<fieldset>
	    		<legend style="font-weight:bold;">Award/Certification Details:</legend>
			  		<div style="height:60px;">
				  		<div>
				  			
				  			<table class="scroll">
    							<thead>
       								<tr class="tab-head">
							          	<th class="tab-cell-padding">Award/Certification Name<span class="mandatory">*</span></th>  
							         	<th class="tab-cell-padding">Institution<span class="mandatory">*</span></th>
							          	<th class="tab-cell-padding">Description<span class="mandatory">*</span></th>
							          	<th class="tab-cell-padding" style="width:10px;">Action</th>   
						         	</tr>
    							</thead>
   							<tbody ng-if="showAwardDetails==false">
								 <tr  class="tab-row cellBorder">
				      				<td colspan="4" class="tab-cell-padding cellBorder" style="text-align:center;width:100%;">No records found</td>
								</tr>
							</tbody>
							<tbody ng-if="showAwardDetails==true">
								<tr  ng-repeat = "award in customerDetail.awardDetails track by $index" class="tab-row">
									<td class="tab-cell-padding cellBorder"><input type="text" ng-model="award.awardName" maxlength="50" class="fullWidth"/></td>
							        <td class="tab-cell-padding cellBorder"><input type="text" ng-model="award.institution" maxlength="50" class="fullWidth"/></td>  
							        <td class="tab-cell-padding cellBorder"><textarea id="award.awardDesc" maxlength="500" ng-model="award.awardDesc" ng-value="award.awardDesc" style="width:100%;" rows="1" cols="10"></textarea></td>  
							        <td class="tab-cell-padding cellBorder" style="width:5%;"><button ng-click="deleteAward($index)" class="fullWidth" class="tab-head"  style="padding:2px;">Delete</button></td>
						        </tr>
						    </tbody>
							</table>
						</div>
						<div id="cudButtons" style="float:right;">
						<table>  
							<tr style="border:1px solid #ddd">
							    <td class="tab-cell-padding tab-head cellBorder"><button ng-click="addAward()" class="tab-head" style="padding:2px;">Add</button></td>
				  			</tr>
				  		</table>
					</div>
					</div>
				</fieldset>
				</div>
				<div>
					<div ng-if="showDeleteExp==false">
						<fieldset>
			    			<legend style="font-weight:bold;">Work Experience Details:</legend>
							<div  class="tab-cell-padding" style="text-align:center;"> There is no work experience details available</div>
						</fieldset>
					</div>
		  			<div ng-if="showDeleteExp==true" ng-repeat = "workExperience in customerDetail.workExperienceDetails track by $index" >
		  			<fieldset>
	    			<legend style="font-weight:bold;">Work Experience Details - {{$index + 1}}:</legend>
	    			<div>
		  			<table class="createUpdateViewTable" style="width:100%;">
					<tr>
		  			    	<td class="tab-head tdwidth">Employer Name<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="workExperience.employerName" ng-value="workExperience.employerName" required maxlength="50"  class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >Start Date<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" style=""><input type="text"  required ng-model="workExperience.startDateStr" ng-value="workExperience.startDateStr" placeholder="MM/DD/YYYY" maxlength="10" class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >End Date<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="workExperience.endDateStr" ng-value="workExperience.endDateStr" maxlength="10" placeholder="MM/DD/YYYY" class="fullWidth"/></td>
		  			    </tr>
		  			    <tr>
		  			    	<td class="tab-head tdwidth" >Designation<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" ng-model="workExperience.designation" ng-value="workExperience.designation" required maxlength="25"  class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >City<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="workExperience.address.city" ng-value="workExperience.address.city" maxlength="20" class="fullWidth"/></td>
		  			    	<td class="tab-cell-padding redBackground"></td>
		  			    	<td class="tab-head tdwidth" >State<span class="mandatory">*</span> : </td>
		  			    	<td class="tab-cell-padding cellBorder" ><input type="text" required ng-model="workExperience.address.state" ng-value="workExperience.address.state" maxlength="20" class="fullWidth"/></td>
		  			    </tr>
		  			    
		  			     <tr>
		  			    	<td class="tab-head tdwidth" >Job Desc<span class="mandatory">*</span> : </td>
		  			    	<td colspan="7" class="tab-cell-padding cellBorder" ><textarea id="comments" required maxlength="1000" ng-model="workExperience.jobDesc" ng-value="workExperience.jobDesc" style="width:100%;" rows="4" cols="81"></textarea></td>
		  			    </tr>
		  			    
		  			   </table>
		  			   </div>
		  			   <div id="cudButtons" style="float:right;">
						<table>  
							<tr style="border:1px solid #ddd">
							    <td class="tab-cell-padding tab-head cellBorder"><button ng-click="deleteWorkExperience($index)" class="tab-head" style="padding:2px;">Delete</button></td>
				  			</tr>
				  		</table>
					</div>
		  			</fieldset>
		  			
		  			</div>
		  			
		  		</div>
				
		  	</div>

		  					<div ng-if="saveDiv==true" style="float:right;">
		  			    		<table style="">  
									<tr style="border:1px solid #ddd">
					  			    	<td class="tab-cell-padding tab-head"><button ng-click="save(customerDetail, customerDetail.qualificationDetails)" class="tab-head buttonStyle" >Save</button></td>
					  			    	<td class="tab-cell-padding tab-head"><button ng-click="cancel()" class="tab-head buttonStyle" >Cancel</button></td>
					  			    	<td class="tab-cell-padding tab-head"><button ng-click="create()" class="tab-head buttonStyle" >Clear</button></td>
					  			    	<td class="tab-cell-padding tab-head"><button ng-click="addWorkExperience()" class="tab-head buttonStyle" >Add Work Experience</button></td>
					  			    </tr>
		  			    		</table>	
		  			    	</div>
		  			
		</fieldset>
	  	</div>
	  	
	</div>
	</div>
	  	
</div>