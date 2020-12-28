package rmqexample;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConsumerMain {
  private static long count = 0;

  public static void main(String[] args) {
    try {
      System.out.println(Constants.HEADER);

      ReliableConsumer reliableConsumer = new ReliableConsumer();
      reliableConsumer.setWorker(new MessageWorker() {
        @Override
        public void handle(String message) throws WorkerException {
          System.out.println("received: " + message);
          ++count;
        }
      });

      reliableConsumer.StartAsynchronousConsumer();
      System.out.println("press any key to terminate");
      System.in.read();
      reliableConsumer.StopAsynchronousConsumer();
      System.out.printf("received %d messages\n", count);
      System.out.println("execution completed");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
