package rmqexample;

public final class Constants {
	private Constants() {}
	
	public static final String RECIPE_NR   = "08/02"; 
	public static final String HEADER      = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". Testing performance. **";
	public static final String exchange    = "perf_exchange_" + RECIPE_NR;
	public static final String queue       = "perf_queue_" + RECIPE_NR;
	public static final boolean durableQueue = true;
	public static final boolean exclusiveQueue = false;
	public static final boolean autodeleteQueue = false;
	public static final String routingKey  = "";
	public static final String hosts[]     = {"localhost"};
	public static final int    port        = 5672;
	
}

