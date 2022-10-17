package com.team.prj.calendar.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

	@GetMapping("/calendar")
	public String calendar() {
		return "calendar/calendar";
	}

}
