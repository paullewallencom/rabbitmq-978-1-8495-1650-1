package rmqexample;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class BenchConsumer extends ReliableClient {
	private long receivedMessages;
	private int maxMessages;
	long t_start;
	long t_end;
	final Integer completionFlag;
	
	public long getReceivedMessages() {
		return receivedMessages;
	}

	BenchConsumer()  {
		super();
		receivedMessages = 0;
		completionFlag = new Integer(0);
	}
	
	@Override
	protected void waitForConnection() throws InterruptedException {
		super.waitForConnection();

		try {
			channel.basicConsume(Constants.queue, true, new Consumer() {

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
				public void handleDelivery(String consumerTag,
						Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					++receivedMessages;
					// not if autoAck = true
					// channel.basicAck(envelope.getDeliveryTag(), false);
					if (receivedMessages == 1) {
						// note: this happens at message #1, not on message #0!
						t_start = System.nanoTime();
					}
					if (receivedMessages == maxMessages) {
						t_end = System.nanoTime();
						synchronized(completionFlag) {
							completionFlag.notify();
						}
					}
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
	}

	public void setMaxMessages(int maxMessages) {
		this.maxMessages = maxMessages;
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
	
	public double getMessagesPerSecond() {
		return (double)(receivedMessages-1)*1e+9/(double)(t_end - t_start); 
	}
}
