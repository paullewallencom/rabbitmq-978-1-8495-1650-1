package rmqexample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;



/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Chapter 02 Recipe 01. How to let messages expire on the producer side.
 * */

public class ProducerMain {

	public static void main(String[] args) {
		try {
			int milliseconds = Integer.valueOf(args[0]);
			int bufferSize = Integer.valueOf(args[1]);
			String hostname = Constants.hosts[0];
			if (args.length > 2) {
				hostname = args[2];
			}
			
			
			System.out.println(Constants.HEADER);

			BenchProducer benchProducer = new BenchProducer(hostname);
			benchProducer.setBuffer(bufferSize);
			benchProducer.start();
			Thread.sleep(milliseconds);
			benchProducer.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
