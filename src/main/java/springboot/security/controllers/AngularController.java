package springboot.security.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularController {

	@RequestMapping("/angular")
	public String angular(){
		return "angular";
	}
	
}
