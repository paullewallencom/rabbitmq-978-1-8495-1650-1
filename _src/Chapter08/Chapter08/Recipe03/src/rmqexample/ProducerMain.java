package rmqexample;



/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Chapter 02 Recipe 01. How to let messages expire on the producer side.
 * */

public class ProducerMain {

	public static void main(String[] args) {
		try {
			int threadNumber = Runtime.getRuntime().availableProcessors();;
			if (args.length>0)
    			threadNumber = Integer.valueOf(args[0]);

			
			int milliseconds = Integer.valueOf(args[0]);
			if (args.length>1)
				milliseconds = Integer.valueOf(args[1]);

			int bufferSize = 64000;
			if (args.length>2)
				bufferSize = Integer.valueOf(args[2]);
			
			

			System.out.println(Constants.HEADER);
			System.out.println("Producer threads: " + threadNumber +" - milliseconds: " + milliseconds +"- bufferSize:" +bufferSize);

			BenchProducer benchProducer = new BenchProducer(threadNumber);
			benchProducer.setBuffer(bufferSize);
			benchProducer.start();
			Thread.sleep(milliseconds);
			benchProducer.stop();
			System.out.println("Done");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
