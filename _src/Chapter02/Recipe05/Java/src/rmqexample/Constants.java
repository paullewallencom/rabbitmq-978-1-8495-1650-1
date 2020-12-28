package rmqexample;

public interface Constants {
	public static final String RECIPE_NR         = "02/05"; 
	public static final String HEADER            = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". alternate exchanges . **";
	public static final String exchange          = "stat_exchange_" + RECIPE_NR;
	public static final String alternateExchange = "missing_alerts_exchange_" + RECIPE_NR;
	public static final String missingAlertQueue = "missing_alerts_queue_" + RECIPE_NR;
	public static final String infoRK            = "info";
	public static final String alertRK           = "alert";
}
