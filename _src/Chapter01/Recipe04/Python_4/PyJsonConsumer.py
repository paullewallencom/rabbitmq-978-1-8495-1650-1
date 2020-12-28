'''
Created on Feb 2, 2013

@author: gabriele
'''
import pika;
import json;
import sys;

def consumer_callback(ch, method, properties, body):
		print " JSON Message received."
		new_books=json.loads(body); # the body contains json string.
		print " Count books:",len(new_books);
		# Books List like the Publish 							
		for item in new_books:
			print 'ID:',item['bookID'], '-Description:',item['bookDescription'],' -Author:',item['author']
		
	
		print " ";


if __name__ == '__main__':
	RECIPE_NR="4"; 
	print ' ** RabbitmqCookBook - Recipe number '+RECIPE_NR+'. Body Serialization with JSON **'
	rabbitmq_host = "localhost";
	
	if (len(sys.argv) > 1):
		rabbitmq_host = sys.argv[1];

	connection = pika.BlockingConnection(pika.ConnectionParameters(rabbitmq_host));
	print 'Connected:' + rabbitmq_host
	
	channel = connection.channel()
	my_queue = "myJSONBodyQueue_"+RECIPE_NR;
	channel.queue_declare(queue=my_queue)

	print 'Consumer ready, on ',my_queue

	
	channel.basic_consume(consumer_callback, queue=my_queue, no_ack=True) 

	channel.start_consuming()
	
	print "Closed"
	pass