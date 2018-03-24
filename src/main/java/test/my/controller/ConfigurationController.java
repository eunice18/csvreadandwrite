package test.my.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConfigurationController {

	@RequestMapping(value = "/Configure", method = RequestMethod.GET)
	public String config() {
		return "index";
	}
}
