using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WcfLib;

namespace PublishNews
{
    class Program{

        static void Main(string[] args)
        {
            Console.WriteLine("RabbitMQ Cookbook Chapter 4 Recipe 2");
            Console.WriteLine("Init thread send info");
            ThreadSendInfo threadinfo = new ThreadSendInfo();
            threadinfo.Start();
            Console.WriteLine("Init Completed... any key to exit");
            Console.ReadLine();
            threadinfo.Stop();
           
               
        }
    }
}
