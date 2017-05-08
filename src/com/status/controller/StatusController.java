package com.status.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.status.model.Status;
import com.status.service.StatusService;

@Controller
@RequestMapping("/")
public class StatusController {	

	@Autowired
	private StatusService statusSvc;
		
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<Status> getStatus(
		@RequestParam(value = "date",required = false)
		@DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		return statusSvc.getStatus(date);
	}
		
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Status addStatus(@RequestBody Status status) {		
		return statusSvc.addStatus(status);		
	}
}
