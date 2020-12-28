'''
Created on Feb 8, 2013

@author: gabriele
'''
import pika;
import sys;
if __name__ == '__main__':
    RECIPE_NR="6";
    print ' ** RabbitmqCookBook - Recipe number 6. Brodcasting Messages (Python)**'
    RabbitmqHost = "localhost";
    
    if (len(sys.argv) > 1):
        RabbitmqHost = sys.argv[1];

    connection = pika.BlockingConnection(pika.ConnectionParameters(RabbitmqHost));
    print 'Connected:' + RabbitmqHost
    
    channel = connection.channel()
    declare = channel.queue_declare(exclusive=True)
    queue_name = declare.method.queue
    myExchange = "myLastnews.fanout_"+RECIPE_NR;
    channel.exchange_declare(exchange=myExchange, type="fanout");
    
    channel.queue_bind(exchange=myExchange, queue=queue_name)
    print 'Consumer Ready! Bind on ',myExchange

    def brodcast_callback(ch, method, properties, body):
        print "Python-Received",body
        
    channel.basic_consume(brodcast_callback, queue=queue_name, no_ack=True)

    channel.start_consuming()


    pass