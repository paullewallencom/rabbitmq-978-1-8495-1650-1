import sys
import urllib2,base64
import json
import logging
#init logger
logger = logging.getLogger('myMonitorRMQ');
hdlr = logging.FileHandler('/var/tmp/myMonitorRMQ.log');
outp = logging.StreamHandler(sys.stdout)
formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s');
hdlr.setFormatter(formatter);
outp.setFormatter(formatter);
logger.addHandler(hdlr); 
logger.addHandler(outp); 
logger.setLevel(logging.INFO);


def calljsonAPI(rabbitmqhost,api):
    request = urllib2.Request("http://"+rabbitmqhost+":15672/api/"+api);
    base64string = base64.encodestring('%s:%s' % ('guest', 'guest')).replace('\n', '')
    request.add_header("Authorization", "Basic %s" % base64string);
    data = json.load(urllib2.urlopen(request));   
    return data;
    
def sendAlarmStatus(message):
    import smtplib  
    # modify this parameters to send a mail
    fromaddr = 'fromuser@gmail.com'  
    toaddrs  = 'to@gmail.com'  
    msg = 'System alarm, error message ' +error_message  
    logger.info("Prepare to send email, message:"+message);
        
  
    # Credentials (if needed)  
    username = 'username'  
    password = 'password'  
    try:
    
        
        server = smtplib.SMTP('smtp.gmail.com:587')  
        server.starttls()  
        server.login(username,password)  
        server.sendmail(fromaddr, toaddrs, msg)  
        server.quit() 
        logger.info("Successfully sent email")
    except:
       logger.error("Error: unable to send email")

if __name__ == '__main__':

    RECIPE_NR="03/09";
    logger.info(' ** RabbitmqCookBook - Recipe number ' + RECIPE_NR+' Monitring RabbitMQ via JSON API**');
    RabbitmqHost = "localhost";
    
    if (len(sys.argv) > 1):
        RabbitmqHost = sys.argv[1];

    error_message="";

    logger.info("Calling nodes API:");
    running_state =False;
    mem_alarm = True;
    try:
        data = calljsonAPI(RabbitmqHost,"nodes");
        logger.info("nodes API called!");
        for r in data:
            running_state=r.get('running');
            if running_state:
                logger.info("Node name: "+ r.get('name') +" - running state:" + str(running_state))
            else:
                error_message =error_message+  "[The Node " + r.get('name') + " is not running ]";
                logger.error("Node name: "+ r.get('name') +" - running state:" + str(running_state))

            mem_alarm=r.get('mem_alarm');
            if not mem_alarm:
                logger.info("Node name: "+ r.get('name') +" - memory alarm state:" + str(mem_alarm))
            else:
                error_message =error_message+  "[The Node " + r.get('name') + " has a memory alarm ]";
                logger.error("Node name: "+ r.get('name') +" - memory alarm state:" + str(mem_alarm))
    except:
        error_message =error_message+  "[Error during get nodes]";
        logger.error("Error during get nodes");

    logger.info("Calling connections API:");
    try:
        data = calljsonAPI(RabbitmqHost,"connections");
    except:
        error_message =error_message+  "[Error during get connections]";
        logger.error("Error during get connections");

    logger.info("Calling aliveness API:");
    try:
        data = calljsonAPI(RabbitmqHost,"aliveness-test/%2f");
        if (data.get('status').lower() == "ok".lower()):
            logger.info("Test stats OK!!");
    except:
        error_message = error_message +  " - [Error during the test]";
        logger.error("Error during the test");


  # if you want you can add yours variables  


if error_message != "":
    sendAlarmStatus(error_message); 
