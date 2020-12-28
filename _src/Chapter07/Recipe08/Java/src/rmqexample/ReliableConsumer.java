package rmqexample;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class ReliableConsumer extends ReliableClient {
  long lastItem;
  Set<Long> moreReceivedItems;
  MessageWorker worker;
  private ExecutorService exService;

  public void setWorker(MessageWorker worker) {
    this.worker = worker;
  }

  public ReliableConsumer() {
    super();
    lastItem = 0;
    moreReceivedItems = new HashSet<Long>();
  }

  @Override
  protected void waitForConnection() throws InterruptedException {
    super.waitForConnection();

    try {
      channel.basicConsume(Constants.queue, false, new Consumer() {

        @Override
        public void handleCancel(String consumerTag) throws IOException {
          System.out.println("got handleCancel signal");
        }

        @Override
        public void handleCancelOk(String consumerTag) {
          System.out.println("got handleCancelOk signal");
        }

        @Override
        public void handleConsumeOk(String consumerTag) {
          System.out.println("got handleConsumeOK signal");
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope,
            BasicProperties properties, byte[] body) throws IOException {

          long messageId = Long.parseLong(properties.getMessageId());
          if (worker != null) {
            // if the message is not a re-delivery, sure it is not a
            // retransmission
            if (!envelope.isRedeliver() || toBeWorked(messageId)) {
              try {
                worker.handle(new String(body));
                // the message is ack'ed just after it has been
                // secured (handled, stored in database...)
                setAsWorked(messageId);
                channel.basicAck(envelope.getDeliveryTag(), false);
              } catch (WorkerException e) {
                // the message worker has reported an exception,
                // so the message
                // can not be considered to be handled properly,
                // so requeue it
                channel.basicReject(envelope.getDeliveryTag(), true);
              }
            }
          }
        }

        @Override
        public void handleRecoverOk(String consumerTag) {
          System.out.println("got recoverOK signal");
        }

        @Override
        public void handleShutdownSignal(String consumerTag,
            ShutdownSignalException cause) {
          System.out.println("got shutdown signal");

        }

      });
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  protected void setAsWorked(Long messageId) {
    synchronized (moreReceivedItems) {
      if (lastItem + 1 == messageId) {
        lastItem = messageId;
      } else {
        moreReceivedItems.add(messageId);
        while (moreReceivedItems.contains(lastItem + 1)) {
          lastItem++;
          moreReceivedItems.remove(lastItem);
        }
      }
    }
  }

  protected boolean toBeWorked(Long messageId) {
    synchronized (moreReceivedItems) {
      return messageId > lastItem && !moreReceivedItems.contains(messageId);
    }
  }

  public void StartAsynchronousConsumer() {
    exService = Executors.newSingleThreadExecutor();
    exService.execute(new Runnable() {

      @Override
      public void run() {
        try {
          for (;;) {
            waitForConnection();
            synchronized (this) {
              // this is very simple: reconnect every 5 seconds always. This
              // could impact negatively
              // the performance. More sophisticated approach would be,
              // reconnect if no messages have
              // been received for 1 second. Reconnect always after say 5
              // minutes.
              this.wait(5000);
            }
            disconnect();
          }
        } catch (InterruptedException ex) {
          // disconnect and exit
          disconnect();
        }
      }
    });
  }

  public void StopAsynchronousConsumer() {
    exService.shutdownNow();
  }
}
