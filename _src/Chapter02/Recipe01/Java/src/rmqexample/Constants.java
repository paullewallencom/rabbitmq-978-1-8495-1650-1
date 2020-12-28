package rmqexample;

public interface Constants {
	public static final String RECIPE_NR  = "02/01"; 
	public static final String HEADER     = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". How to let messages expire. **";
	public static final String exchange   = "monitor_exchange_" + RECIPE_NR;
	public static final String queue      = "stat_queue_" + RECIPE_NR;
	public static final String routingKey = "java stats";
}
