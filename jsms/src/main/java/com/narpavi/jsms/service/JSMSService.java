package com.narpavi.jsms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.narpavi.common.model.CustomerDetails;
import com.narpavi.jsms.dao.JSMSDao;

public class JSMSService {
	@Autowired
	private JSMSDao jsmsDao;
	
	public List<CustomerDetails> getAllJobSeekers() {
		return jsmsDao.getAllJobSeekers();
	}
	
	public List<CustomerDetails> search(String searchKey) {
		return jsmsDao.search(searchKey);
	}

	public boolean create(CustomerDetails customerDetails) throws Exception {
		boolean returnValue = false;
		Integer status = jsmsDao.create(customerDetails);
		if (status > 0) {
			returnValue = true;
		}
		return returnValue;
	}

	public boolean update(CustomerDetails customerDetails) throws Exception {
		boolean returnValue = false;
		Integer status = jsmsDao.update(customerDetails);
		if (status > 0) {
			returnValue = true;
		}
		return returnValue;

	}

	public boolean delete(List<Integer> customerIds) throws Exception {
		boolean returnValue = false;
		Integer status = jsmsDao.delete(customerIds);
		if (status > 0) {
			returnValue = true;
		}
		return returnValue;
	}
}
