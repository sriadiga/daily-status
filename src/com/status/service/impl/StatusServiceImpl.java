package com.status.service.impl;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.status.dao.StatusDao;
import com.status.domain.ErrorUtil;
import com.status.domain.StatusErrorCode;
import com.status.model.Status;
import com.status.service.StatusService;

@Service
public class StatusServiceImpl implements StatusService {
	
	@Autowired
	private ErrorUtil errorCode;
	
	@Autowired
	private StatusDao statusDao;

	@Transactional
	public List<Status> getStatus(Date date) {
		return statusDao.getStatus(date);
	}
	
	@Transactional
	public Status addStatus(Status status) {
		status.setDate(Calendar.getInstance().getTime());
		if (StringUtils.isBlank(status.getStatus())) {
			status.setStatus(errorCode.getError(StatusErrorCode.invalid_status));
		} else if (StringUtils.isBlank(status.getEmail())) {
			status.setEmail(errorCode.getError(StatusErrorCode.invalid_email));
		} else {
			statusDao.addStatus(status);
		}	
		return status;
	}
}
