using Newtonsoft.Json.Linq;
using System;
using System.Diagnostics;
using System.Windows.Forms;

namespace Messenger
{
    public partial class LoginForm : Form
    {
        public bool DidLoginSuccessfully { get; private set; }

        public LoginForm()
        {
            DidLoginSuccessfully = false;
            InitializeComponent();
        }

        private void btLogin_Click(object sender, EventArgs e)
        {
            performLogin(tbUser.Text.Trim());
        }

        private void performLogin(string name)
        {
            API req = new API("https://messenger.jobjot.co.nz:8080/login");

            JObject reqObj = new JObject();

            reqObj.Add("username", name);

            req.SetResponseHandler((ex, data) =>
            {
                if (ex != null) { Debug.WriteLine("There was an exception."); }
                if (data != null)
                {
                    DidLoginSuccessfully = true;
                    Debug.WriteLine("Response Received.");
                    Close();
                }
            });

            req.setRequestObject(reqObj);
            req.send();
        }

    }
}
