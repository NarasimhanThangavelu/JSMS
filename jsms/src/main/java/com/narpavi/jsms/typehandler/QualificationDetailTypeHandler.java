package com.narpavi.jsms.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.google.gson.Gson;
import com.narpavi.common.model.QualificationDetails;

public class QualificationDetailTypeHandler implements TypeHandler<QualificationDetails>{
	
	@Override
	public void setParameter(PreparedStatement ps, int i, QualificationDetails parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, serialize(parameter));
	}
	@Override
	public QualificationDetails getResult(ResultSet cs, String columnName) throws SQLException {
		return deserialize(cs.getString(columnName));
	}
	@Override
	public QualificationDetails getResult(ResultSet cs, int columnIndex) throws SQLException {
		return deserialize(cs.getString(columnIndex));
	}
	@Override
	public QualificationDetails getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return deserialize(cs.getString(columnIndex));
	}
	
	private String serialize(QualificationDetails qualificationDetails) {
		Gson gson = new Gson();
		return gson.toJson(qualificationDetails);
	}
	
	private QualificationDetails deserialize(String value) {
		Gson gson = new Gson();
		return gson.fromJson(value, QualificationDetails.class);
	}

}
