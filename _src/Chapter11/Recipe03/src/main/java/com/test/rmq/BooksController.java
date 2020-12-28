package com.test.rmq;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.book.Book;

@Controller
@RequestMapping("rest")
public class BooksController {

	private static final Logger logger = LoggerFactory.getLogger(BooksController.class);
	
	@Autowired AmqpTemplate amqpTemplate;
	private HashMap<Integer,Book> myBooks = new HashMap<Integer,Book>();
	public BooksController(){
		for (int i = 1; i <= 5; i++) {
			Book res = new Book();
			res.setId(i);
			res.setTitle("My book title numer:"+ i);
			res.setPrice(10+i);	
            myBooks.put(i, res);
		}	
	}
	
	@RequestMapping("books")
	@ResponseBody public Collection<Book> getBooks() {
		return myBooks.values();
	}
	
	
	@RequestMapping(value = "/buybook/{bookid}", method = RequestMethod.GET)
	public @ResponseBody String buybook(@PathVariable("bookid") int bookid) throws JsonGenerationException, JsonMappingException, IOException{
		Book bbook =  myBooks.get(bookid);
		//// you should store the order to the DataBase
		logger.info("Request buybook for book id:" +bookid);
		ObjectMapper mapper = new ObjectMapper();
		String message = mapper.writeValueAsString(bbook);
		amqpTemplate.convertAndSend("myorders_11",message);
		logger.info("Message sent for book id:" +bookid);
		
		return "you have bought the book:" + bbook.getTitle() +"!";	
		
	}
	
	
}
