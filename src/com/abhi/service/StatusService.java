package com.abhi.service;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abhi.dao.StatusDao;
import com.abhi.model.Status;

@Service("offerservice")
public class StatusService {
	
	@Autowired
	private StatusDao statDao;
	
	
	public void setStatDao(StatusDao statDao) {
		this.statDao = statDao;
	}
	
	public List<Status> getCurrentStatus() {
		return statDao.getEmp();
	}

	public List<Status> getCurrentStatus(String date) {
		return statDao.getEmp(date);
	}
	
	public Status storeStat(Status stat) {
		long millis=System.currentTimeMillis();  
		Date date = new Date(millis);
		if( date.toString().equals(stat.getDate().toString()) ) {
			statDao.storeStatus(stat);
			return stat;
		}
		else{
			 return stat;
		    }
	}
	
}
