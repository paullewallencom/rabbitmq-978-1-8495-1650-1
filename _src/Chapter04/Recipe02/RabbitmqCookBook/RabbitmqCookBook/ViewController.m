//
//  ViewController.m
//  RabbitmqCookBook
//
//  Created by Gabriele Santomaggio on 5/22/13.
//  Copyright (c) 2013 Gabriele Santomaggio. All rights reserved.
//

#import "ViewController.h"

#include "mosquitto.h"

@interface ViewController ()


@end

@implementation ViewController





static void on_subscribe(struct mosquitto *mosq, void *obj, int message_id, int qos_count, const int *granted_qos)
{
    ViewController* client = (ViewController*)obj;
     client.txtmessages.text = [client.txtmessages.text stringByAppendingString:@"\n Subscribe"];
}

static void on_message(struct mosquitto *mosq, void *obj, const struct mosquitto_message *message)
{
    ViewController* client = (ViewController*)obj;
   // NSString *topic = [NSString stringWithUTF8String: message->topic];
    NSString *payload = [[[NSString alloc] initWithBytes:message->payload
                                                  length:message->payloadlen
                                                encoding:NSUTF8StringEncoding] autorelease];

   
    client.txtmessages.text = [client.txtmessages.text stringByAppendingString:@"\n"];
    client.txtmessages.text = [client.txtmessages.text stringByAppendingString:payload];
   
    
    
}


static void on_connect(struct mosquitto *mosq, void *obj, int rc)
{
    ViewController* client = (ViewController*)obj;
    client.txtmessages.text = [client.txtmessages.text stringByAppendingString:@"\n Connected"];
}



struct mosquitto *mosq;
@synthesize timer;

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) loop: (NSTimer *)timer {
    mosquitto_loop(mosq, 1, 1);
}

- (void)dealloc {
    [_txtmessages release];

    [_btnonoff release];
    [super dealloc];
}

- (void)Connect {
    mosq = mosquitto_new(NULL, true,self);
    mosquitto_connect_callback_set(mosq, on_connect);
    NSString *ip = [_edipbroker text];
    mosquitto_username_pw_set(mosq, NULL, NULL);
    const char *cip=[ip UTF8String];
    mosquitto_connect(mosq, cip, 1883, 60);
    timer = [NSTimer scheduledTimerWithTimeInterval:0.01 // 10ms
                                             target:self
                                           selector:@selector(loop:)
                                           userInfo:nil
                                            repeats:YES];

    
    mosquitto_subscribe_callback_set(mosq,on_subscribe);
    mosquitto_message_callback_set(mosq,on_message);
    mosquitto_subscribe(mosq, NULL, "tecnology.rabbitmq.ebook", 0);
    _txtmessages.text = [_txtmessages.text stringByAppendingString:@"\n Done!"];
}


- (IBAction)onoffevent:(id)sender {
    BOOL value = [_btnonoff isOn];
    if (value){
       [self Connect];
    } else {
        [self Disconnet];
    }
}

- (void) Disconnet {
    
    if (timer) {
        [timer invalidate];
        timer = nil;
    }
    
     if (mosq) {
         mosquitto_unsubscribe(mosq, NULL, "tecnology.rabbitmq.ebook");
         mosquitto_disconnect(mosq);
         mosquitto_destroy(mosq);
         mosq = NULL;
         _txtmessages.text = [_txtmessages.text stringByAppendingString:@"\n bye bye!"];

    }
    

    
    
    
}






@end
