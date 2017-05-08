package com.status.dao.impl;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.status.dao.StatusDao;
import com.status.model.Status;

@Repository
public class StatusDaoImpl implements StatusDao {

    @Autowired
    private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<Status> getStatus(Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Status.class);
		if (date != null) {
			criteria.add(Restrictions.eq("date", new java.sql.Date(date.getTime())));
		}		
		return criteria.list();		
	}
	
	public void addStatus(Status stat) {
		sessionFactory.getCurrentSession().save(stat);
	}
}
