package rmqexample;



public class ConsumerMain {
	public static void main(String[] args) {
	
		
		
		int threadNumber = Runtime.getRuntime().availableProcessors();
		if (args.length>0)
			threadNumber = Integer.valueOf(args[0]);
		
		int prefetchcount=10;
		if (args.length>1)
			prefetchcount = Integer.valueOf(args[1]);
		
		boolean autoAck=false;
		if (args.length>2)
			autoAck = (Integer.valueOf(args[2]) != 0) ;
		
		boolean print_thread_consumer = false;
		if (args.length>3)
			print_thread_consumer = (Integer.valueOf(args[3]) != 0) ;


		System.out.println(Constants.HEADER);
		System.out.println("Consumer threads: " + threadNumber + " - prefetch count:"+prefetchcount + " - autoAck : "+autoAck + "- print_thread_consumer: "+ print_thread_consumer );

		
		BenchConsumer benchConsumer = new BenchConsumer(threadNumber,prefetchcount,autoAck,print_thread_consumer);
	
		benchConsumer.start();
		benchConsumer.waitForCompletion();
		benchConsumer.stop();
		System.out.println("Done");

		
	}
}
