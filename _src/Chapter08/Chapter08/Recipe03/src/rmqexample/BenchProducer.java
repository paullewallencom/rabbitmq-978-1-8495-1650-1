package rmqexample;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Channel;

public class BenchProducer extends ReliableClient {
	private ExecutorService exService;
	byte buffer[];
	long sentMessages;
	boolean terminated;

	int threadNumber;

	public BenchProducer(int _threadNumber) {
		super();
		sentMessages = 0;
		terminated = false;
		threadNumber = _threadNumber;
		exService = Executors.newFixedThreadPool(threadNumber);
	}

	public void start() {
		try {
			waitForConnection();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < threadNumber; i++) {
			exService.execute(new Runnable() {

				@Override
				public void run() {
						publishMessages();
				
				}
			});
		}

	}

	public void stop() {
		terminated = true;
		disconnect();
		exService.shutdownNow();
	}

	public long getSentMessages() {
		return sentMessages;
	}

	void setBuffer(int size) {
		buffer = new byte [size];
	}

	private void publishMessages() {
		Channel internalChannel = null;
		try {
			internalChannel = connection.createChannel();
		
			while (!terminated) {
		
				internalChannel.basicPublish("", Constants.queue, null, buffer);
				++sentMessages;
			}
		
					
		} catch (IOException e) {
			e.printStackTrace();

		}

		try {
			if (internalChannel!= null)
				internalChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
