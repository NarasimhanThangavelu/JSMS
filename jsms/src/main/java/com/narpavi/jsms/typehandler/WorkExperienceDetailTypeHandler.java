package com.narpavi.jsms.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.google.gson.Gson;
import com.narpavi.common.model.WorkExperienceDetails;

public class WorkExperienceDetailTypeHandler implements TypeHandler<WorkExperienceDetails>{
	
	@Override
	public void setParameter(PreparedStatement ps, int i, WorkExperienceDetails parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, serialize(parameter));
	}
	@Override
	public WorkExperienceDetails getResult(ResultSet cs, String columnName) throws SQLException {
		return deserialize(cs.getString(columnName));
	}
	@Override
	public WorkExperienceDetails getResult(ResultSet cs, int columnIndex) throws SQLException {
		return deserialize(cs.getString(columnIndex));
	}
	@Override
	public WorkExperienceDetails getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return deserialize(cs.getString(columnIndex));
	}
	
	private String serialize(WorkExperienceDetails workExperienceDetails) {
		Gson gson = new Gson();
		return gson.toJson(workExperienceDetails);
	}
	
	private WorkExperienceDetails deserialize(String value) {
		Gson gson = new Gson();
		return gson.fromJson(value, WorkExperienceDetails.class);
	}

}
