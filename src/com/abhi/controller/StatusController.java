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
	
	private StatusService statservice;
	
	@Autowired
	public void setStatservice(StatusService statservice ) {
		this.statservice = statservice;
	}

	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public List<Status> Read(@RequestParam("date")String date){
		List<Status> s= statservice.getCurrentStatus(date);
		System.out.println(s);
		System.out.println(date);
		return s;
	}
		
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Status add(@RequestBody Status inobj ){		
		
		return statservice.storeStat(inobj);		
	}
	
	
	
}
