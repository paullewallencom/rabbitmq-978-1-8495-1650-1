#!/bin/bash
/etc/init.d/rabbitmq-server stop  >> /var/log/rabbitmqstartup.log
/etc/init.d/rabbitmq-server start  >> /var/log/rabbitmqstartup.log
rabbitmqctl stop_app >> /var/log/rabbitmqstartup.log
rabbitmqctl reset >> /var/log/rabbitmqstartup.log
rabbitmqctl join_cluster rabbit@REPLACE_WITH_YOUR_MASTER_IP >> /var/log/rabbitmqstartup.log
rabbitmqctl start_app >> /var/log/rabbitmqstartup.log
rabbitmqctl cluster_status >> /var/log/rabbitmqstartup.log