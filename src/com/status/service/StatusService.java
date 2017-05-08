package com.status.service;

import java.util.Date;
import java.util.List;
import com.status.model.Status;

public interface StatusService {
	
	List<Status> getStatus(Date date);
	
	Status addStatus(Status status);
	
}
