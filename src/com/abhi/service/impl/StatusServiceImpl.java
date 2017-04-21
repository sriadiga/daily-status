package com.abhi.service.impl;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abhi.dao.StatusDao;
import com.abhi.model.Status;
import com.abhi.service.StatusService;

@Service("offerservice")
public class StatusServiceImpl implements StatusService {
	
	@Autowired
	private StatusDao statusDao;

	public List<Status> getStatus(Date date) {
		return statusDao.getStatus(date);
	}
	
	public Status addStatus(Status status) {
		status.setDate(new Date(Calendar.getInstance().getTime().getTime()));
		statusDao.addStatus(status);
		return status;
	}
}
