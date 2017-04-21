package com.abhi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.abhi.model.Status;
import com.abhi.service.StatusService;

@Controller
@RequestMapping("/")
public class StatusController {
	
	@Autowired
	private StatusService statsSvc;
	
	public void setStatservice(StatusService statsSvc ) {
		this.statsSvc = statsSvc;
	}

	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public List<Status> read(@RequestParam(value="date",required=false)String date) {
		if( date == null ) {
			List<Status> s= statsSvc.getCurrentStatus();
			return s;
		}
		else {
			List<Status> s= statsSvc.getCurrentStatus(date);
			return s;
		}
		
	}
		
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Status add(@RequestBody Status inobj ) {		
		return statsSvc.storeStat(inobj);		
	}
	
	
	
}
