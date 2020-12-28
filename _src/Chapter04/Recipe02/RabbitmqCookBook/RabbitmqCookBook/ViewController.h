//
//  ViewController.h
//  RabbitmqCookBook
//
//  Created by Gabriele Santomaggio on 5/22/13.
//  Copyright (c) 2013 Gabriele Santomaggio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "mosquitto.h"

@interface ViewController : UIViewController

@property NSTimer* timer;
@property (retain, nonatomic) IBOutlet UISwitch *btnonoff;
@property (retain, nonatomic) IBOutlet UITextField *edipbroker;
@property (retain, nonatomic) IBOutlet UITextView *txtmessages;
- (IBAction)onoffevent:(id)sender;


@end
