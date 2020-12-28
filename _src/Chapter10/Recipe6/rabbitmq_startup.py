#!/usr/bin/env python
import json
import urllib2
import base64

if __name__ == '__main__':
    prefix ='rabbitmq cookbook chapter 10 .6'
    from subprocess import call
    call(["rabbitmqctl", "stop_app"])
    call(["rabbitmqctl", "reset"])
    try:
        url = 'http://internal-myloadbalamcer-900022750.us-east-1.elb.amazonaws.com:15672/api/nodes'
        print prefix + 'Get json info from ..' + url
        request = urllib2.Request(_url)

        base64_string = base64.encodestring('%s:%s' % ('guest', 'guest')).replace('\n', '')
        request.add_header("Authorization", "Basic %s" % base64string)
        data = json.load(urllib2.urlopen(request))
        ##if the script got an error here you can assume that it's the first machine and then 
        ## exit without controll the error. Remember to add the new machine to the balancer
        print prefix + 'request ok... finding for running node'
        

        for r in data:
            if r.get('running'):
                print prefix + 'found running node to bind..'
                print prefix + 'node name: '+ r.get('name') +'- running:' + str(r.get('running'))
                from subprocess import call
                call(["rabbitmqctl", "join_cluster",r.get('name')])
                break;
        pass
    except Exception, e:
        print prefix + 'error during add node'
    finally:
        from subprocess import call
        call(["rabbitmqctl", "start_app"])


    pass
