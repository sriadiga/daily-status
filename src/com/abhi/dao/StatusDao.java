package com.abhi.dao;

import java.sql.Date;
import java.util.List;

import com.abhi.model.Status;

public interface StatusDao {
	List<Status> getStatus(Date date);
	
	void addStatus(Status stat);
}
