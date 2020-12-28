package rmqexample;



public class ConsumerMain {
	public static void main(String[] args) {
		int maxMessages = Integer.parseInt(args[0]);
		System.out.println(Constants.HEADER);

		BenchConsumer benchConsumer = new BenchConsumer();
		benchConsumer.setMaxMessages(maxMessages);

		benchConsumer.start();
		benchConsumer.waitForCompletion();
		benchConsumer.stop();
		System.out.printf("received %d messages\n", benchConsumer.getReceivedMessages());
		System.out.printf("message rate: %f msg/second\n", benchConsumer.getMessagesPerSecond());
	}
}
