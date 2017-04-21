package com.abhi.model;

import java.sql.Date;

public class Status {

	private String email;
	
	private Date date;
	
	private String status;
	
	public Status() {
		
	}
		
	public Status(String email,Date date, String status) {
		this.email = email;
		this.date = date;
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(String  str) {
		this.date =Date.valueOf(str);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "Status [email=" + email + ", date=" + date + ", status=" + status + "]";
	}
}
