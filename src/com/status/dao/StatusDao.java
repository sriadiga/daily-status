package com.status.dao;

import java.util.Date;
import java.util.List;
import com.status.model.Status;

public interface StatusDao {
	
	List<Status> getStatus(Date date);
	
	void addStatus(Status stat);
}
