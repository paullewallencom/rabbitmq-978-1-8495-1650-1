package rmqexample;

public interface MessageWorker {
	public void handle(String message) throws WorkerException;
}
