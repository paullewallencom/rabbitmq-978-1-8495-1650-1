using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.Management;
using System.Runtime.InteropServices;
using System.Threading;

namespace MonitorClient
{
    class Program
    {

        private static void DisplayTotalRam()
        {
            string Query = "SELECT MaxCapacity FROM Win32_PhysicalMemoryArray";
            ManagementObjectSearcher searcher = new ManagementObjectSearcher(Query);
            foreach (ManagementObject WniPART in searcher.Get())
            {
                UInt32 SizeinKB = Convert.ToUInt32(WniPART.Properties["MaxCapacity"].Value);
                UInt32 SizeinMB = SizeinKB / 1024;
                UInt32 SizeinGB = SizeinMB / 1024;
                Console.WriteLine("Size in KB: {0}, Size in MB: {1}, Size in GB: {2}", SizeinKB, SizeinMB, SizeinGB);
            }
        }


        
        static void Main(string[] args)
        {
            PerformanceCounter cpuCounter = new PerformanceCounter();
            cpuCounter.CategoryName = "Processor";
            cpuCounter.CounterName = "% Processor Time";
            cpuCounter.InstanceName = "_Total";

            Console.WriteLine("Insert RabbitMQ host (dafault localhost): ");
            String rabbitmqhost = Console.ReadLine();
            if (rabbitmqhost.Trim() == "")
                rabbitmqhost = "localhost";
            
            Console.WriteLine("Insert Server ID:");
            String id = Console.ReadLine();
            int ServerId = 0; 
            try
            {
                ServerId = Int32.Parse(id);

            }
            catch (Exception)
            {
                Console.WriteLine("Insert a valid number, any key to exit");
                Console.ReadLine();
                return;
                
            }


            RMQ r = new RMQ();
            r.Connect(rabbitmqhost);
            while (true)
            {
                Int64 phav = PerformanceInfo.GetPhysicalAvailableMemoryInMiB();
                Int64 tot = PerformanceInfo.GetTotalMemoryInMiB();
                decimal percentFree = ((decimal)phav / (decimal)tot) * 100;
                decimal percentOccupied = 100 - percentFree;
                Console.WriteLine("Available Physical Memory (MiB) " + phav.ToString());
                Console.WriteLine("Total Memory (MiB) " + tot.ToString());
                Console.WriteLine("Free (%) " + percentFree.ToString());
                Console.WriteLine("Occupied (%) " + percentOccupied.ToString());
                float cpu = cpuCounter.NextValue();
                Console.WriteLine("CPU " + cpu );
                r.Publish("{\"UPDATETIME\":\""+DateTime.Now +"\",\"SERVERID\":\""+ServerId+ "\",\"CPU\":"+ Math.Truncate(cpu)+",\"MEM\":"+ Math.Truncate(percentOccupied) +"}");
                Thread.Sleep(1000);
            }

        }

        public static class PerformanceInfo
        {
            [DllImport("psapi.dll", SetLastError = true)]
            [return: MarshalAs(UnmanagedType.Bool)]
            public static extern bool GetPerformanceInfo([Out] out PerformanceInformation PerformanceInformation, [In] int Size);

            [StructLayout(LayoutKind.Sequential)]
            public struct PerformanceInformation
            {
                public int Size;
                public IntPtr CommitTotal;
                public IntPtr CommitLimit;
                public IntPtr CommitPeak;
                public IntPtr PhysicalTotal;
                public IntPtr PhysicalAvailable;
                public IntPtr SystemCache;
                public IntPtr KernelTotal;
                public IntPtr KernelPaged;
                public IntPtr KernelNonPaged;
                public IntPtr PageSize;
                public int HandlesCount;
                public int ProcessCount;
                public int ThreadCount;
            }

            public static Int64 GetPhysicalAvailableMemoryInMiB()
            {
                PerformanceInformation pi = new PerformanceInformation();
                if (GetPerformanceInfo(out pi, Marshal.SizeOf(pi)))
                {
                    return Convert.ToInt64((pi.PhysicalAvailable.ToInt64() * pi.PageSize.ToInt64() / 1048576));
                }
                else
                {
                    return -1;
                }

            }

            public static Int64 GetTotalMemoryInMiB()
            {
                PerformanceInformation pi = new PerformanceInformation();
                if (GetPerformanceInfo(out pi, Marshal.SizeOf(pi)))
                {
                    return Convert.ToInt64((pi.PhysicalTotal.ToInt64() * pi.PageSize.ToInt64() / 1048576));
                }
                else
                {
                    return -1;
                }

            }
        }
    }

}

