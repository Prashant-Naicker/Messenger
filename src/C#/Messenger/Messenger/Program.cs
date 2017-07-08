using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Messenger
{
    static class Program
    {
        public static SynchronizationContext UIThread { get; private set; }
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            LoginForm form = new LoginForm();
            UIThread = SynchronizationContext.Current;
            form.ShowDialog();

            if (form.DidLoginSuccessfully)
            {
                ChatForm chatForm = new ChatForm();
                chatForm.ShowDialog();
            }
        }
    }
}
