package rmqexample;

import java.util.ArrayDeque;
import java.util.Queue;

/*
 * This queue is used in the example from both the producer and the consumer:
 * 
 * the producer cache the messages to be sent to the broker in an instance of 
 * this class.
 * 
 * The producer get the message from the queue in an instance of this class and
 * notify the broker that the message has been successfully managed after all 
 * the required operations have been performed on the it.
 */
public class DataQueue {
	Queue<DataItem> dataQueue;
	long lastID;
	
	public DataQueue() {
		dataQueue = new ArrayDeque<DataItem>();
		lastID = 0;
	}
	
	// no persistence in this example: in a production system it should also 
	// store the lastID in a transactional way, upon the insertion of
	// a new item in the queue, saving the queue itself as well.
	// Furthermore, it should check and limit the maximum number of cached
	// items in dataQueue, to avoid to consume too many resources that would
	// lead the system to become unreliable.
	public synchronized long add(String data) {
		++lastID;
		dataQueue.add(new DataItem(data,lastID));
		return lastID;
	}
	// requeue an old item - no new ID is allocated
	public synchronized void add(DataItem item) {
		dataQueue.add(item);
	}
	
	public synchronized boolean isEmpty() {
		return dataQueue.isEmpty();
	}
	
	public synchronized DataItem peek() {
		return dataQueue.peek();
	}
	
	public synchronized DataItem remove() {
		return dataQueue.remove();
	}
}
