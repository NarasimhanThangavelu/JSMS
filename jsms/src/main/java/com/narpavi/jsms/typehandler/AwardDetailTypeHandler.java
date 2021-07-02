package com.narpavi.jsms.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.google.gson.Gson;
import com.narpavi.common.model.AwardDetails;

public class AwardDetailTypeHandler implements TypeHandler<AwardDetails>{
	
	@Override
	public void setParameter(PreparedStatement ps, int i, AwardDetails parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, serialize(parameter));
	}
	@Override
	public AwardDetails getResult(ResultSet cs, String columnName) throws SQLException {
		return deserialize(cs.getString(columnName));
	}
	@Override
	public AwardDetails getResult(ResultSet cs, int columnIndex) throws SQLException {
		return deserialize(cs.getString(columnIndex));
	}
	@Override
	public AwardDetails getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return deserialize(cs.getString(columnIndex));
	}
	
	private String serialize(AwardDetails awardDetails) {
		Gson gson = new Gson();
		return gson.toJson(awardDetails);
	}
	
	private AwardDetails deserialize(String value) {
		Gson gson = new Gson();
		return gson.fromJson(value, AwardDetails.class);
	}

}
