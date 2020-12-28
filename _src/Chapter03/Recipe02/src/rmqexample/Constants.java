package rmqexample;

public interface Constants {
	public static final String RECIPE_NR  = "03/02"; 
	public static final String HEADER     = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". Using users. **";
	public static final String exchange   = "stat_exchange_" + RECIPE_NR;
	public static final String queue      = "stat_queue_" + RECIPE_NR;
	public static final String routingKey = "java stats";
}
