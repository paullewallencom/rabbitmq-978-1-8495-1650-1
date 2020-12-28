package com.test.rmq;

import java.util.Locale;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired AmqpTemplate amqpTemplate;
	//	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		String connFactroy = "You connection factory is null";	
		if (amqpTemplate!=null){
			connFactroy = "Connection factory init with success!";
		}
		model.addAttribute("connFactroy", connFactroy );
		return "home";
	}


}
