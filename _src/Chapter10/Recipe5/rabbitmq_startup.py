# -*- coding: utf-8 -*-
import base64
import json
import urllib2

if __name__ == '__main__':
    prefix = 'RabbitMQ Cookbook Chapter 10 .5'
    url = 'http://youinternalbalancer:15672/api/nodes'
    print(prefix + 'Get JSON info from ..' + url)
    request = urllib2.Request(url)

    base64_string = base64.encodestring('%s:%s' % ('guest',
                                                   'guest')).replace('\n', '')
    request.add_header("Authorization", "Basic %s" % base64_string)
    data = json.load(urllib2.urlopen(request))
    # If the script got an error here, you can assume that it's the first
    # machine and then exit without control the error.
    # Remember to add the new machine to the balancer.
    print(prefix + 'Request Ok... Finding for running node')

    for r in data:
        if r.get('running'):
            print(prefix + 'found running node to bind..')
            print(prefix + 'node name: ' + r.get('name') + '- running:' +
                  str(r.get('running')))
            from subprocess import call
            call(["rabbitmqctl", "stop_app"])
            call(["rabbitmqctl", "reset"])
            call(["rabbitmqctl", "join_cluster", r.get('name')])
            call(["rabbitmqctl", "start_app"])
            break;
    pass
