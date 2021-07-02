package com.narpavi.jsms.dao;

import java.util.List;

import com.narpavi.common.model.CustomerDetails;

public interface JSMSDao {
	
	public List<CustomerDetails> getAllJobSeekers();
	public List<CustomerDetails> search(String searchKey);
	public Integer create(CustomerDetails customerDetails) throws Exception;
	public Integer update(CustomerDetails customerDetails) throws Exception;
	public Integer delete(List<Integer> customerIds) throws Exception;
}
