package rmqexample;



public class ConsumerMain {
	public static void main(String[] args) {
	
		int threadNumber = Runtime.getRuntime().availableProcessors();
		if (args.length>1)
			threadNumber = Integer.valueOf(args[1]);

		System.out.println(Constants.HEADER);

		BenchConsumer benchConsumer = new BenchConsumer(threadNumber);
	
		benchConsumer.start();
		benchConsumer.waitForCompletion();
		benchConsumer.stop();
		System.out.println("Done");

		
	}
}
