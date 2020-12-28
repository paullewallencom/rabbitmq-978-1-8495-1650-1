package rmqexample;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class BenchConsumer extends ReliableClient {
	
	
	long t_start;
	long t_end;
	final Integer completionFlag;
	int threadNumber;
	
	private ExecutorService exService;
	
	
	BenchConsumer(int _threadNumber)  {
		super();
		completionFlag = new Integer(0);
		threadNumber = _threadNumber;
		exService = Executors.newFixedThreadPool(threadNumber);
	}
	
	@Override
	protected void waitForConnection() throws InterruptedException {
		super.waitForConnection();

		for (int i = 0; i < threadNumber; i++) {
			exService.execute(new Runnable() {

				@Override
				public void run() {
					final Channel internalChannel;
					try {
						internalChannel = connection.createChannel();
						internalChannel.basicConsume(Constants.queue, false, new Consumer() {

							@Override
							public void handleCancel(String consumerTag) throws IOException {
								// System.out.println("got handleCancel signal");
							}

							@Override
							public void handleCancelOk(String consumerTag) {
								// System.out.println("got handleCancelOk signal");
							}

							@Override
							public void handleConsumeOk(String consumerTag) {
								// System.out.println("got handleConsumeOK signal");
							}

							@Override
							public void handleDelivery(String consumerTag,Envelope envelope, BasicProperties properties,
									byte[] body) throws IOException {
								internalChannel.basicAck(envelope.getDeliveryTag(), false);
								
								
							}

							@Override
							public void handleRecoverOk(String consumerTag) {
							//	System.out.println("got recoverOK signal");
							}

							@Override
							public void handleShutdownSignal(String consumerTag,
									ShutdownSignalException cause) {
							//	System.out.println("got shutdown signal");
							}
						});
					} catch (IOException e) {
						e.printStackTrace();
					}

				
				}
			});
		}

		
		
	}

	public void start() {
		try {
			waitForConnection();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		disconnect();
		exService.shutdownNow();
		
		
	}


	
	public void waitForCompletion() {
		synchronized(completionFlag) {
			try {
				completionFlag.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
