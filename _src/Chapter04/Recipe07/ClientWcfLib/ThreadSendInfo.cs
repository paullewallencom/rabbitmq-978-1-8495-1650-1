using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using WcfLib;
using System.Diagnostics;


public class ThreadSendInfo
{
    ClientService client = new ClientService("Service1");


    PerformanceCounter cpuCounter;
    PerformanceCounter ramCounter;
    private volatile bool stopThread = false;
    public ThreadSendInfo()
    {
        cpuCounter = new PerformanceCounter();

        cpuCounter.CategoryName = "Processor";
        cpuCounter.CounterName = "% Processor Time";
        cpuCounter.InstanceName = "_Total";
        ramCounter = new PerformanceCounter("Memory", "Available MBytes");

    }
    private Thread StartSubrscriberThread()
    {
        stopThread = false;
        var t = new Thread(() => InternalStartTask(client));
        t.Start();
        return t;
    }



    private void InternalStartTask(ClientService client)
    {

    
        while (!stopThread)
        {
            try
            {
                MachineInfo info = GetMachineInfo();
                info.CPUsage = cpuCounter.NextValue();
                info.Memory = ramCounter.NextValue();
                info.DateEvent = DateTime.Now;
                bool toSend = false ;
                if (info.CPUsage > 80)
                {
                    info.Message = " ** CPU ALARM ** ";
                    toSend = true;
                }
                if (info.Memory > 800)
                {
                    toSend = true;
                    info.Message += " ** MEMORY ALARM ** ";
                }

                if (toSend)
                {
                    client.SendMachineInfo(info);
                    Console.WriteLine(DateTime.Now + ": Info sent " + info.Message);
                }
                else Console.WriteLine(DateTime.Now + ": Everything ok");
                
                Thread.Sleep(1000);
            }
            catch (Exception e)
            {
                stopThread = true;
                Console.WriteLine(e);
            }
        }

    }

    private MachineInfo GetMachineInfo()
    {
        MachineInfo info = new MachineInfo();

        return info;

    }
    Thread t = null;
    public void Start()
    {
        t = StartSubrscriberThread();
    }
    public void Stop()
    {
        stopThread = true;
        Console.WriteLine("Closing..."); 
        client.ChannelFactory.Close();
        Console.WriteLine("Closed!"); 


    }


}
