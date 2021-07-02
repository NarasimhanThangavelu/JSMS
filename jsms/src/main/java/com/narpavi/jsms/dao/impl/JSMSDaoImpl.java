package com.narpavi.jsms.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.narpavi.common.impl.BaseDaoImpl;
import com.narpavi.common.model.AwardDetails;
import com.narpavi.common.model.CustomerDetails;
import com.narpavi.common.model.QualificationDetails;
import com.narpavi.common.model.WorkExperienceDetails;
import com.narpavi.common.util.StringUtils;
import com.narpavi.jsms.dao.JSMSDao;

public class JSMSDaoImpl extends BaseDaoImpl implements JSMSDao {

	@Override
	public List<CustomerDetails> getAllJobSeekers() {
		return this.getSqlSession().selectList("com.narpavi.jsms.dao.JSMSDaoMapper.getAllJobSeekers");
	}
	
	@Override
	public List<CustomerDetails> search(String searchKey) {
		if (!StringUtils.isBlank(searchKey)) {
			searchKey = searchKey+"*";
		} else {
			return getAllJobSeekers();
		}
		return this.getSqlSession().selectList("com.narpavi.jsms.dao.JSMSDaoMapper.searchCustomerDetails", searchKey);
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, readOnly=false,timeout=100,rollbackFor=Exception.class)
	@Override
	public Integer create(CustomerDetails customerDetails) throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", 0);
		paramMap.put("details", customerDetails.getCustomerDetail());
		paramMap.put("customerDetailClob", customerDetails.getCustomerDetail());
		Integer status = this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.createCustomerDetail", paramMap);
		if (status > 0) {
			Map<String, Object> branchMap = new HashMap<String, Object>();
			branchMap.put("id", paramMap.get("id"));
			
			for (QualificationDetails qualificationDetails : customerDetails.getCustomerDetail().getQualificationDetails()) {
				branchMap.put("qualificationDetail", qualificationDetails);
				branchMap.put("qualificationDetailClob", qualificationDetails);
				status = this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.createQualificationDetail", branchMap);
				BigInteger in = (BigInteger)branchMap.get("Id");
				qualificationDetails.setId(in.intValue());
			}
			if (null != customerDetails.getCustomerDetail().getWorkExperienceDetails()) {
				for (WorkExperienceDetails  workExperienceDetail  : customerDetails.getCustomerDetail().getWorkExperienceDetails()) {
					branchMap.put("workExperienceDetail", workExperienceDetail);
					status = this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.createWorkExperienceDetail", branchMap);
					BigInteger in = (BigInteger)branchMap.get("Id");
					workExperienceDetail.setId(in.intValue());
				}
			}
			
			if (null != customerDetails.getCustomerDetail().getAwardDetails()) {
				for (AwardDetails  awardDetail  : customerDetails.getCustomerDetail().getAwardDetails()) {
					branchMap.put("awardDetail", awardDetail);
					status = this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.createAwardDetail", branchMap);
					BigInteger in = (BigInteger)branchMap.get("Id");
					awardDetail.setId(in.intValue());
				}
			}
		}
		
		updateClob(customerDetails, paramMap.get("id"));
		return status;
	}
	
	private void updateClob(CustomerDetails customerDetails, Object empId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("custId", empId);
		paramMap.put("customerDetailClob", customerDetails.getCustomerDetail());
		this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.updateCustomerDetailClob", paramMap);
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, readOnly=false,timeout=100,rollbackFor=Exception.class)
	@Override
	public Integer update(CustomerDetails customerDetails) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", customerDetails.getId());
		paramMap.put("customerDetailClob", customerDetails.getCustomerDetail());
		
		Map<String, Object> branchMap = new HashMap<String, Object>();
		branchMap.put("custId", paramMap.get("id"));
		branchMap.put("id", paramMap.get("id"));
		List<Integer> uiList = new ArrayList<Integer>();
		List<Integer> qualifiactionIds = this.getSqlSession().selectList("com.narpavi.jsms.dao.JSMSDaoMapper.getQualificationIds", branchMap);
		
		for (QualificationDetails qualificationDetails : customerDetails.getCustomerDetail().getQualificationDetails()) {
			
			branchMap.put("qualificationDetail", qualificationDetails);
			branchMap.put("qualificationDetailClob", qualificationDetails);
			if (null != qualificationDetails.getId() && qualificationDetails.getId() > 0) {
				uiList.add(qualificationDetails.getId());
				this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.updateQualificationDetail", branchMap);
			} else {
				this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.createQualificationDetail", branchMap);
				BigInteger in = (BigInteger)branchMap.get("Id");
				qualificationDetails.setId(in.intValue());
			}
		}
		for (Integer qualId : qualifiactionIds) {
			if (!uiList.contains(qualId)) {
				branchMap.put("qualId", qualId);
				this.getSqlSession().delete("com.narpavi.jsms.dao.JSMSDaoMapper.deleteQualificationDetail", branchMap);
			}
		}
		uiList = new ArrayList<Integer>();
		List<Integer> weIds = this.getSqlSession().selectList("com.narpavi.jsms.dao.JSMSDaoMapper.getWEIds", branchMap);
		if (null != customerDetails.getCustomerDetail().getWorkExperienceDetails()) {
			
			for (WorkExperienceDetails  workExperienceDetail  : customerDetails.getCustomerDetail().getWorkExperienceDetails()) {
				branchMap.put("workExperienceDetail", workExperienceDetail);
				if (null != workExperienceDetail.getId() && workExperienceDetail.getId() > 0) {
					uiList.add(workExperienceDetail.getId());
					this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.updateWorkExperienceDetail", branchMap);
				} else {
					this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.createWorkExperienceDetail", branchMap);
					BigInteger in = (BigInteger)branchMap.get("Id");
					workExperienceDetail.setId(in.intValue());
				}
			}
		}
		
		for (Integer weId : weIds) {
			if (!uiList.contains(weId)) {
				branchMap.put("expId", weId);
				this.getSqlSession().delete("com.narpavi.jsms.dao.JSMSDaoMapper.deleteWorkExperienceDetail", branchMap);
			}
		}
		uiList = new ArrayList<Integer>();
		List<Integer> awardIds = this.getSqlSession().selectList("com.narpavi.jsms.dao.JSMSDaoMapper.getAwardIds", branchMap);
		if (null != customerDetails.getCustomerDetail().getAwardDetails()) {
			for (AwardDetails  awardDetail  : customerDetails.getCustomerDetail().getAwardDetails()) {
				branchMap.put("awardDetail", awardDetail);
				if (null != awardDetail.getId() && awardDetail.getId() > 0) {
					uiList.add(awardDetail.getId());
					this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.updateAwardDetail", branchMap);
				} else {
					this.getSqlSession().insert("com.narpavi.jsms.dao.JSMSDaoMapper.createAwardDetail", branchMap);
					BigInteger in = (BigInteger)branchMap.get("Id");
					awardDetail.setId(in.intValue());
				}
			}
		}
		for (Integer awardId : awardIds) {
			if (!uiList.contains(awardId)) {
				branchMap.put("awardId", awardId);
				this.getSqlSession().delete("com.narpavi.jsms.dao.JSMSDaoMapper.deleteAwardDetail", branchMap);
			}
		}
		return this.getSqlSession().update("com.narpavi.jsms.dao.JSMSDaoMapper.updateCustomerDetails", paramMap);
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, readOnly=false,timeout=100,rollbackFor=Exception.class)
	@Override
	public Integer delete(List<Integer> customerIds) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("customerIds", customerIds);
		Integer status = 0;
			for (Integer empID : customerIds) {
				paramMap.put("custID", empID);
				this.getSqlSession().delete("com.narpavi.jsms.dao.JSMSDaoMapper.deleteQualificationDetailByCustomer", paramMap);
				this.getSqlSession().delete("com.narpavi.jsms.dao.JSMSDaoMapper.deleteWorkExperienceDetailByCustomer", paramMap);
				this.getSqlSession().delete("com.narpavi.jsms.dao.JSMSDaoMapper.deleteAwardDetailByCustomer", paramMap);
				status = status + this.getSqlSession().delete("com.narpavi.jsms.dao.JSMSDaoMapper.deleteCustomerDetail", paramMap);
			}
		return status;
	}

}
