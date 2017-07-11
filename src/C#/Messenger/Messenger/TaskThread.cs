using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Messenger
{
    class TaskThread
    {
        private BlockingCollection<TaskThreadItem> _workQueue;
        TcpClient s = new TcpClient();

        public TaskThread()
        {
            _workQueue = new BlockingCollection<TaskThreadItem>();
            ThreadPool.QueueUserWorkItem(o => TCPConnect());
        } 

        private void TCPConnect()
        {
            s.Connect("192.168.1.2", 8081);
        }
        public void SendMessage(string m)
        {
            s.Close();
        }

        public void AddWork(TaskThreadItem workItem)
        {
            _workQueue.Add(workItem);
        }
    }
}
