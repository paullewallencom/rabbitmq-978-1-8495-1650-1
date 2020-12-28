package rmqexample;

public interface Constants {
	public static final String RECIPE_NR         = "02/06"; 
	public static final String HEADER            = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". set userid. **";
	public static final String queue         = "order_queue_" + RECIPE_NR;
	public static final String senderip          = "senderip";
	
	
}
