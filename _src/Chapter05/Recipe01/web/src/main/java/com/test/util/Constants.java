package com.test.util;

public interface Constants {
	public static final String RECIPE_NR  = "05_01"; 
	public static final String HEADER     = " ** RabbitmqCookBook - Recipe number " + RECIPE_NR + " **";
	public static final String exchange   = "monitor_exchange_" + RECIPE_NR;
	public static final String routingKey = "stats";
}
