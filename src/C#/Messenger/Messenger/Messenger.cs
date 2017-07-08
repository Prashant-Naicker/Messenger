using Newtonsoft.Json.Linq;
using System;
using System.Diagnostics;
using System.Windows.Forms;

namespace Messenger
{
    public partial class Messenger : Form
    {
        public Messenger()
        {
            InitializeComponent();
        }

        private void btLogin_Click(object sender, EventArgs e)
        {
            performLogin(tbUser.Text.Trim());
        }

        private void performLogin(string name)
        {
            API req = new API("https://192.168.1.2:8080/login");

            JObject reqObj = new JObject();

            reqObj.Add("username", name);

            req.SetResponseHandler((ex, data) =>
            {
                if (ex != null) { Debug.WriteLine("There was an exception."); }
                if (data != null) { Debug.WriteLine("Response Received."); }
            });

            req.setRequestObject(reqObj);
            req.send();
        }
    }
}
