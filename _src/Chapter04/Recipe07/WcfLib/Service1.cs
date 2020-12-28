using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.IO;
using System.Threading;

namespace WcfLib
{
    // NOTE: If you change the class name "Service1" here, you must also update the reference to "Service1" in App.config.
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class Service1 : IService1
    {

        private String FILE_STORE = "c:\\warningstatus.store";
        public   void SendMachineInfo(MachineInfo machineInfo)
        {

            if (!File.Exists(FILE_STORE))
            {
                var myFile = File.Create(FILE_STORE);
                myFile.Close();
            }

            using (System.IO.StreamWriter file = new System.IO.StreamWriter(FILE_STORE, true))
            {
                file.WriteLine("Date Event:" +machineInfo.DateEvent +  " CPU: " + machineInfo.CPUsage +" - Memory: " + machineInfo.Memory);
            }
            
        }
    }
}
