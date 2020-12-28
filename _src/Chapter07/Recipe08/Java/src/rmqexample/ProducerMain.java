package rmqexample;

import java.io.IOException;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Chapter 02 Recipe 01. How to let messages expire on the producer side.
 * */

public class ProducerMain {

	public static void main(String[] args) {
		System.out.println(Constants.HEADER);
		ReliableProducer rc = new ReliableProducer();
		rc.startAsynchronousPublisher();

		int c = 0;
		int loop = 0;
		while (c != 'q') {
			for (int i=1; i <= 100; ++i) {
				System.out.println("sending message #" + Integer.toString(100*loop+i));
				rc.send("message #" + Integer.toString(100*loop+i));
			}

			System.out.println("press q to terminate, any other key to send 100 more messages");
			try {
				c = System.in.read();
			} catch (IOException e) {
			}
			++loop;
		}
		rc.stopAsynchronousPublisher();
		System.out.println("execution completed");
	}
}
