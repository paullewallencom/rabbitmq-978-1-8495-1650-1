package rmqexample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ReliableProducer extends ReliableClient {
  private DataQueue dataQueue;
  private Map<Long, DataItem> pendingItems;
  private ExecutorService exService;

  public ReliableProducer() {
    super();
    dataQueue = new DataQueue();
    pendingItems = new HashMap<Long, DataItem>();
  }

  @Override
  protected void waitForConnection() throws InterruptedException {
    super.waitForConnection();
    try {
      channel.confirmSelect();
    } catch (IOException e) {
      // should never happen - not important for the example scope
      e.printStackTrace();
    }
    channel.addConfirmListener(new ConfirmListener() {

      @Override
      public void handleAck(long deliveryTag, boolean multiple)
          throws IOException {
        if (multiple) {
          ReliableProducer.this.removeItemsUpto(deliveryTag);
        } else {
          ReliableProducer.this.removeItem(deliveryTag);
        }
      }

      @Override
      public void handleNack(long deliveryTag, boolean multiple)
          throws IOException {
        if (multiple) {
          ReliableProducer.this.requeueItemsUpto(deliveryTag);
        } else {
          ReliableProducer.this.requeueItem(deliveryTag);
        }
      }

    });
  }

  protected void requeueItemsUpto(long deliveryTag) {
    synchronized (pendingItems) {
      Iterator<Map.Entry<Long, DataItem>> it = pendingItems.entrySet()
          .iterator();
      while (it.hasNext()) {
        Map.Entry<Long, DataItem> entry = it.next();
        if (entry.getKey() <= deliveryTag) {
          dataQueue.add(entry.getValue());
          it.remove();
        }
      }
    }
  }

  protected void removeItemsUpto(long deliveryTag) {
    synchronized (pendingItems) {
      Iterator<Map.Entry<Long, DataItem>> it = pendingItems.entrySet()
          .iterator();
      while (it.hasNext()) {
        Map.Entry<Long, DataItem> entry = it.next();
        if (entry.getKey() <= deliveryTag) {
          it.remove();
        }
      }
    }
  }

  protected void requeueItem(long deliveryTag) {
    synchronized (pendingItems) {
      DataItem item = pendingItems.get(deliveryTag);
      pendingItems.remove(deliveryTag);
      dataQueue.add(item);
    }
  }

  protected void removeItem(long deliveryTag) {
    synchronized (pendingItems) {
      pendingItems.remove(deliveryTag);
    }
  }

  public void send(String data) {
    synchronized (dataQueue) {
      dataQueue.add(data);
      dataQueue.notify();
    }
  }

  public void startAsynchronousPublisher() {
    exService = Executors.newSingleThreadExecutor();
    exService.execute(new Runnable() {

      @Override
      public void run() {
        try {
          for (;;) {
            waitForConnection();
            publishFromLocalQueue();
            disconnect();
          }
        } catch (InterruptedException ex) {
          // disconnect and exit
          disconnect();
        }
      }

    });
  }

  public void stopAsynchronousPublisher() {
    exService.shutdownNow();
  }

  protected void publishFromLocalQueue() throws InterruptedException {
    try {
      for (;;) {
        synchronized (dataQueue) {
          if (dataQueue.isEmpty()) {
            dataQueue.wait(1000);
            // if the queue stays empty for more then one second, disconnect and
            // wait offline
            if (dataQueue.isEmpty()) {
              System.out.println("disconnected for inactivity");
              disconnect();
              dataQueue.wait();
              waitForConnection();
            }
          }
        }
        DataItem item = dataQueue.peek();
        BasicProperties messageProperties = new BasicProperties.Builder()
            .messageId(Long.toString(item.getId())).deliveryMode(2).build();
        long deliveryTag = channel.getNextPublishSeqNo();
        channel.basicPublish("", Constants.queue, messageProperties, item
            .getData().getBytes());
        // only after successfully publishing, move the item to the
        // container of pending items. They will be removed from it only
        // upon the
        // reception of the confirms from the broker.
        synchronized (pendingItems) {
          pendingItems.put(deliveryTag, item);
        }
        dataQueue.remove();
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
      }
    } catch (IOException e) {
      // do nothing: the connection will be closed and then retried
    }
  }
}
