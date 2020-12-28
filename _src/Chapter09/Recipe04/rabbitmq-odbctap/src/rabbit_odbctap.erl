-module(rabbit_odbctap).
-include("rabbit_odbctap.hrl").
-behaviour(application).

-export([start/2, stop/1]).

start(normal, []) ->
		Config = read_config(),
    rabbit_odbctap_sup:start_link(Config).

stop(_State) ->
    ok.

read_config() ->
		{ok, Dsn}      = application:get_env(dsn),
		{ok, User}     = application:get_env(user),
		{ok, Password} = application:get_env(password),
		{ok, Queue}    = application:get_env(queue),
		Config = #odbctap_config{
														 dsn = Dsn,
														 user = User,
														 password = Password,
														 queue = Queue},
		Config.
