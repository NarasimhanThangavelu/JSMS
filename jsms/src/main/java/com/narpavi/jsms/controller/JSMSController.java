package com.narpavi.jsms.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.narpavi.common.model.CustomerDetail;
import com.narpavi.common.model.CustomerDetails;
import com.narpavi.common.model.ServiceResponse;
import com.narpavi.jsms.service.ExportPdf;
import com.narpavi.jsms.service.JSMSService;


@Component
@RequestMapping(value = "/jsmsservice")
public class JSMSController {

	@Autowired
	private JSMSService jsmsService;
	@Autowired
	ExportPdf exportPdf;
	
	@RequestMapping(value = "/jobseekers", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ServiceResponse getAllEmployerDetails(Model model) {
		ServiceResponse response = new ServiceResponse();
		Map<String, Object> payLoad = new HashMap<String, Object>();
		List<CustomerDetails> jobseekers = jsmsService.getAllJobSeekers();
		payLoad.put("allJobSeekers", jobseekers);
		response.setPayload(payLoad);
		model.addAttribute("response", response);
		return response;
	}
	
	@RequestMapping(value = "/jobseekers/search", method = RequestMethod.GET)
	public @ResponseBody ServiceResponse search(@RequestParam(value="searchKey") String searchKey, Model model) {
		ServiceResponse response = new ServiceResponse();
		Map<String, Object> payLoad = new HashMap<String, Object>();
		List<CustomerDetails> jobseekers = jsmsService.search(searchKey);
		payLoad.put("allJobSeekers", jobseekers);
		response.setPayload(payLoad);
		model.addAttribute("response", response);
		return response;
	}
	
	@RequestMapping(value = "/jobseekers/create", method = RequestMethod.POST)
	public @ResponseBody ServiceResponse create(@RequestBody CustomerDetails customerDetails, Model model) {
		ServiceResponse response = new ServiceResponse();
		Map<String, Object> payLoad = new HashMap<String, Object>();
		try {
			if (null == customerDetails || null == customerDetails.getCustomerDetail()) {
				payLoad.put("exceptions", "Please fill the required fields");
			} else {
			
				List<CustomerDetails> allDeliquentCustomers = null;
				boolean result = jsmsService.create(customerDetails);
				if (result) {
					allDeliquentCustomers = jsmsService.getAllJobSeekers();
				}
				payLoad.put("allJobSeekers", allDeliquentCustomers);
				}
			response.setPayload(payLoad);
			model.addAttribute("response", response);
		} catch (Exception e) {
			response.setMessage("Exceptions: " +e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = "/jobseekers/update", method = RequestMethod.POST)
	public @ResponseBody ServiceResponse update(@RequestBody CustomerDetails customerDetails, Model model) {
		ServiceResponse response = new ServiceResponse();
		Map<String, Object> payLoad = new HashMap<String, Object>();
		try {
			if (null == customerDetails 
					|| null == customerDetails.getCustomerDetail() 
					|| (null != customerDetails.getCustomerDetail().getQualificationDetails()
					&& customerDetails.getCustomerDetail().getQualificationDetails().size() == 0)) {
				response.setMessage("Please fill the required fields");
			} else {
			
				List<CustomerDetails> allJobSeekers = null;
				boolean result = jsmsService.update(customerDetails);
				if (result) {
					allJobSeekers = jsmsService.getAllJobSeekers();
				}
				payLoad.put("allJobSeekers", allJobSeekers);
				}
			response.setPayload(payLoad);
			model.addAttribute("response", response);
		} catch (Exception e) {
			response.setMessage("Exceptions: " +e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = "/jobseekers/delete", method = RequestMethod.POST)
	public @ResponseBody ServiceResponse delete(@RequestBody List<Integer> employerIds, Model model) {
		ServiceResponse response = new ServiceResponse();
		Map<String, Object> payLoad = new HashMap<String, Object>();
		try {
			if (null == employerIds || employerIds.size() == 0) {
				response.setMessage("Please select the records to delete");
			} else {
			
				List<CustomerDetails> allJobSeekers = null;
				boolean result = jsmsService.delete(employerIds);
				if (result) {
					allJobSeekers = jsmsService.getAllJobSeekers();
				}
				payLoad.put("allJobSeekers", allJobSeekers);
				}
			response.setPayload(payLoad);
			model.addAttribute("response", response);
		} catch (Exception e) {
			response.setMessage("Exceptions: " +e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = "/jobseekers/downloadPdf", method = RequestMethod.POST)
	public @ResponseBody ServiceResponse downloadPdf(@RequestBody CustomerDetail customerDetail, Model model) {
		ServiceResponse response = new ServiceResponse();
		Map<String, Object> payLoad = new HashMap<String, Object>();
		String file ="";
		try {
			file = exportPdf.pdfWriter(customerDetail);
			payLoad.put("path", file);
			response.setPayload(payLoad);
			model.addAttribute("response", response);
		} catch (Exception e) {
			response.setMessage("Exception : " +e.getMessage());
		}
//		byte[] encoded = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
		return response;
	}
}
