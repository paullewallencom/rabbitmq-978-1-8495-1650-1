using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ServiceModel;
using WcfLib;


public class ClientService : ClientBase<IService1>, IService1
{
    public ClientService(string configurationName)
        : base(configurationName) { }


    #region IService1 Members

    public void SendMachineInfo(MachineInfo machineInfo)
    {
        base.Channel.SendMachineInfo(machineInfo);
    }

    #endregion
}

