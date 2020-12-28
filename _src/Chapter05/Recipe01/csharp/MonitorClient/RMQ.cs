using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using RabbitMQ.Client;

namespace MonitorClient
{

    public class RMQ
    {
        ConnectionFactory connection_factory = new ConnectionFactory();
        IConnection conn = null;
        IModel channel = null;
        public void Connect(String RabbitMQhost)
        {
            connection_factory.Uri = "amqp://guest:guest@" + RabbitMQhost + ":5672/";
            conn = connection_factory.CreateConnection();
           channel=  conn.CreateModel();
        }

        public void Publish(String message)
        {



      
            channel.BasicPublish("monitor_exchange_05_01", "stats", null, System.Text.Encoding.UTF8.GetBytes(message));
        }


        public void Disonnect()
        {
            channel.Close();
            channel.Dispose();
            conn.Close();
            conn.Dispose();
        }



    }
}
