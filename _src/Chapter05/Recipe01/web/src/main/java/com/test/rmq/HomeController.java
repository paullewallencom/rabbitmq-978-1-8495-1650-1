package com.test.rmq;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.test.bean.RabbitMQInstance;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
    private RabbitMQInstance rmq;
	
	
	/**
	 * This request is created by default form SPING MVC Template.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		String message = "";
		// here we check the client RabbitMQ connection error.
		if (rmq!=null){
			message = rmq.getLastError();
		} else
			message = " Oh! Your RabbitMQ instance is null";
		
		if (message.equalsIgnoreCase(""))
			message = "Your RabbitMQ works correctly on: " + rmq.getRabbitMQhost(); 
		
		logger.info("check rabbitmq status:" + message);
		model.addAttribute("rabbitmqstate", message);
		return "home";
	}
	
}
