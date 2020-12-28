package rmqexample;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BenchProducer extends ReliableClient {
	private ExecutorService exService;
	private byte buffer[];
	private long sentMessages;
	private boolean terminated;
	private long t_start;
	private long t_end;

	public BenchProducer(String hostname) {
		super();
		sentMessages = 0;
		terminated = false;
		exService = Executors.newSingleThreadExecutor();
	}

	public void start() {
		exService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					waitForConnection();
					publishMessages();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					disconnect();
				}
			}
		});
	}

	public void stop() {
		terminated = true;
		exService.shutdownNow();
	}
	
	public long getSentMessages() {
		return sentMessages;
	}

	void setBuffer(int size) {
		buffer = new byte [size];
	}

	private void publishMessages() {
		try {
			t_start = System.nanoTime();
			while (!terminated) {
				channel.basicPublish("", Constants.queue, null, buffer);
				++sentMessages;
			}
			t_end = System.nanoTime();
			System.out.printf("Sent messages = %d\n", getSentMessages());
			System.out.printf("message rate: %f msg/second\n", getMessagesPerSecond());			
		} catch (IOException e) {
		}
	}

	public double getMessagesPerSecond() {
		return (double)(sentMessages)*1e+9/(double)(t_end - t_start); 
	}

}
