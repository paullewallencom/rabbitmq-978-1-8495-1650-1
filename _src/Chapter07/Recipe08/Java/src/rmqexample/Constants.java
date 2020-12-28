package rmqexample;

public final class Constants {
	private Constants() {}
	
	public static final String RECIPE_NR   = "07/09"; 
	public static final String HEADER      = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". How to deploy reliable clients. **";
	public static final String exchange    = "monitor_exchange_" + RECIPE_NR;
	public static final String queue       = "mirr.stat_queue_" + RECIPE_NR;
	public static final boolean durableQueue = true;
	public static final boolean exclusiveQueue = false;
	public static final boolean autodeleteQueue = false;
	public static final String routingKey  = "java stats";
	public static final String hosts[]     = {"node01", "node02"};
	public static final int    port        = 5672;
}

