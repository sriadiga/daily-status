package com.abhi.service;

import java.sql.Date;
import java.util.List;

import com.abhi.model.Status;

public interface StatusService {
	List<Status> getStatus(Date date);
	Status addStatus(Status status);
	
}
