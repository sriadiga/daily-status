package com.abhi.dao.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.abhi.dao.StatusDao;
import com.abhi.model.Status;

@Component
public class StatusDaoImpl implements StatusDao {
	private NamedParameterJdbcTemplate jdbc;
	
	@Autowired
	public void setDataSrc(DataSource dao) {
		this.jdbc = new NamedParameterJdbcTemplate(dao);
	}
	
	public List<Status> getStatus(Date date) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = "select * from STATUS;";
		if (date != null) {
			params.addValue("date", date);
			query = "select * from STATUS where DATE=:date;";
		}
		
		return jdbc.query(query, params, new RowMapper<Status>() {

			@Override
			public Status mapRow(ResultSet rs, int arg1) throws SQLException {
				Status stat = new Status();	
				stat.setDate(rs.getDate("DATE"));
				stat.setEmail(rs.getString("EMAIL"));
				stat.setStatus(rs.getString("STATUS"));
				return stat;
			}
		});
	}
	
	public void addStatus(Status stat) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("date",stat.getDate());
		params.addValue("email",stat.getEmail());
		params.addValue("status",stat.getStatus());
		jdbc.update("insert into STATUS values(:email, :date, :status);", params);
	}
}
