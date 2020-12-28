-module(rabbit_odbctap_sup).

-behaviour(supervisor).

-export([start_link/1, init/1]).

start_link(Config) ->
    supervisor:start_link({local, ?MODULE}, ?MODULE, [Config]).

init([Config]) ->
    {ok, {{one_for_one, 3, 10},
          [{rabbit_odbctap_worker,
            {rabbit_odbctap_worker, start_link, [[Config]]},
            permanent,
            10000,
            worker,
            [rabbit_odbctap_worker]}
          ]}}.
