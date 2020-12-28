package com.test.rmq;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

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
	 * Simply selects the home view to render by returning its name.
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
	
	@RequestMapping("/searchbook")
	@ResponseBody
	public DeferredResult<String> searchbook(@RequestParam String bookkey) {
	  DeferredResult<String> deferredResult = new DeferredResult<String>(3000);// we set a time-out request. If the request is slow the onTimeout event is raised
	  final String uuidRequest = java.util.UUID.randomUUID().toString(); // each request is marked with a UIID
	  
	  rmq.makeRequest(uuidRequest,bookkey, deferredResult); 
	  deferredResult.onTimeout(new Runnable() {
		    public void run() {
		        System.out.println("Time out request..:" + uuidRequest);
		      	///here you should notify the time-out requests...
		     	// for example publish a message to some monitor exchange
		     	//and then send a message via e-mail or directly to your //smartphone as we have seen to the Chapter 4
		      
		    	rmq.requests.remove(uuidRequest); // the request is removed from the map.
		      }
	 });

	  return deferredResult;
	}
	
	
	
	
	
	
}
