package rmqexample;

public interface Constants {
	public static final String RECIPE_NR  = "02/08"; 
	public static final String HEADER     = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". exchange-to-exchange . **";
	public static final String exchange   = "trace_exchange_" + RECIPE_NR;
	public static final String rountingKey1_8   = "#";
	
	// this exchange is created by 
	// chapter 1 recipe 8
	public static final String rif_exchange_c1_8 = "myTopicExchangeRoutingKey_01/08";
	
	// this exchange is created by 
	// chapter 1 recipe 6
	public static final String rif_exchange_c1_6 = "myLastnews.fanout_01/06";
		
	
	
}
