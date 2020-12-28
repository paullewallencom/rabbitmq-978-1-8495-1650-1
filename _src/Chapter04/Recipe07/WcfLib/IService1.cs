using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WcfLib
{
    // NOTE: If you change the interface name "IService1" here, you must also update the reference to "IService1" in App.config.
    [ServiceContract]
    public interface IService1
    {
        [OperationContract(IsOneWay = true)]
        void SendMachineInfo(MachineInfo machineInfo);

        // TODO: Add your service operations here
    }



   


}
