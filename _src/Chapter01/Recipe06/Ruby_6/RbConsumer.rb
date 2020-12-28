=begin
	
RabbitmqCookBook [TAG_TO_REPLACE]
 
Recipe number 4. broadcasting messages 

In this Example we used:
https://github.com/ruby-amqp/bunny
Please read the Getting started
http://rubybunny.info/articles/getting_started.html
	
=end
 

require "rubygems"
require "bunny"


if ARGV[0]..to_s == ''
  RabbitmqHost = ARGV[0]
else
  RabbitmqHost = "localhost"
end
RECIPE_NR="6";
puts ' ** RabbitmqCookBook - Recipe number 6. Brodcasting Messages (Ruby)**'
connection = Bunny.new("amqp://guest:guest@#{RabbitmqHost}:5672")
connection.start
puts ' Connected:#{RabbitmqHost}'
chanel  = connection.create_channel
myExchange  = chanel.fanout("myLastnews.fanout_"+RECIPE_NR)
q = chanel.queue("", :auto_delete => true,:exclusive => true).bind(myExchange)
q.subscribe do |delivery_info, properties, body|
    puts "Ruby-received: #{body}"
end
puts " Ruby-Consumer Ready!"
puts "Press any key to exit"
a =  $stdin.gets.chomp

chanel.close
connection.close
