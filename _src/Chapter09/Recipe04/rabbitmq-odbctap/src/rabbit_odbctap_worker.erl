-module(rabbit_odbctap_worker).
-include("rabbit_odbctap.hrl").

-behaviour(gen_server).

-export([start_link/1]).

-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
         terminate/2, code_change/3]).

-include_lib("amqp_client/include/amqp_client.hrl").

-record(state, {odbcHandle,
								channel,
								consumerTag
								}).

start_link([Config]) ->
    gen_server:start_link({global, ?MODULE}, ?MODULE, [Config], []).

%---------------------------
% Gen Server Implementation
% --------------------------

init([Config = #odbctap_config{dsn=Dsn, user=User, password=Password, queue=Queue}]) ->
		rabbit_log:info("INIT -> DSN: ~s~nUser: ~s~nPassword: ~s~nQueue: ~s~n", 
										[Dsn,
										 User,
										 "********",  % Password,
										 Queue]),
		odbc:start(),
		CString = "DSN=" ++  Dsn ++ 
							";UID=" ++ User ++ 
							";PWD=" ++ Password,
		{ok, Ohandle} = odbc:connect(CString, []),
		odbc:sql_query(Ohandle, "CREATE TABLE rmqmessages (ts TIMESTAMP, msg CHAR VARYING(32))"),
    {ok, Connection} = amqp_connection:start(#amqp_params_direct{}),
    {ok, Channel} = amqp_connection:open_channel(Connection),
    #'queue.declare_ok'{} = amqp_channel:call(
															Channel,
															#'queue.declare'{queue = list_to_binary(Queue)}),
		Sub = #'basic.consume'{queue = list_to_binary(Queue)},
    #'basic.consume_ok'{consumer_tag = Tag} = amqp_channel:call(Channel, Sub),
    {ok, #state{odbcHandle = Ohandle, 
								channel = Channel, 
								consumerTag = Tag}}.

handle_call(_Msg, _From, State) ->
    {reply, unknown_command, State}.

handle_cast(_, State) ->
    {noreply,State}.


handle_info(#'basic.consume_ok'{}, State) ->
		io:format("~nconsume ok !~n"),
		{noreply, State};

handle_info(#'basic.cancel_ok'{}, State) ->
		io:format("~ncancel ok !~n"),
		{noreply, State};

handle_info({#'basic.deliver'{delivery_tag = Tag}, #amqp_msg{payload = Payload}}, 
						State = #state{channel=Channel, odbcHandle = Ohandle}) ->
		Query = "INSERT INTO rmqmessages VALUES ('now','"++ binary_to_list(Payload)  ++"')",
		{updated, _} = odbc:sql_query(Ohandle, Query),
		amqp_channel:cast(Channel, #'basic.ack'{delivery_tag = Tag}),
		{noreply, State};

handle_info(_Info, State) ->
    {noreply, State}.

terminate(_, #state{odbcHandle = Ohandle, 
								    channel = Channel, 
								    consumerTag = Tag}) ->
		amqp_channel:call(Channel, #'basic.cancel'{consumer_tag = Tag}),
    amqp_channel:call(Channel, #'channel.close'{}),
		odbc:disconnect(Ohandle),
		odbc:stop(),
    ok.

code_change(_OldVsn, State, _Extra) ->
    {ok, State}.
