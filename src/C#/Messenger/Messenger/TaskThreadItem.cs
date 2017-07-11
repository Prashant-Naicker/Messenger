using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Messenger
{
    public abstract class TaskThreadItem
    {
        Socket s;

        public abstract void doWork();
        public void setSocket(Socket socket)
        {
            s = socket;
        }
    }
}
