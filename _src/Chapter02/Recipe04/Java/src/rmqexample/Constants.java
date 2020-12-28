package rmqexample;

public interface Constants {
	public static final String RECIPE_NR  = "02/04"; 
	public static final String HEADER     = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + ". dead letter . **";
	public static final String exchange   = "monitor_exchange_" + RECIPE_NR;
	public static final String exchange_dead_letter   = "dead_letter_exchange_" + RECIPE_NR;
	public static final String queue      = "stat_queue_" + RECIPE_NR;
	public static final String queue_dead_letter      = "queue_dead_letter" + RECIPE_NR;
}
