package rmqexample;

public interface Constants {
	public static final String RECIPE_NR  = "02/09"; 
	public static final String HEADER     = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". Embedding message destinations within messages (cc bcc) . **";
	public static final String exchange   = "stats_exchange_" + RECIPE_NR;
	public static final String alert_routing_key   = "alert_rkey";
	public static final String backup_alert_routing_key   = "backup_alert_rkey";
	public static final String send_alert_routing_key    = "send_alert_rkey";
	
	
	
	
	
}
