using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Net;
using System.Net.Http;
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;
using System.Threading;

namespace Messenger
{
    public delegate void responseHandler(Exception ex, string data);
    class API
    {
        private static string _url;
        private JObject _reqObj;
        private responseHandler _responseHandler;

        public API(string url)
        {
            _url = url;
        }

        // Request/Response.
        public void setRequestObject(JObject reqObj)
        {
            _reqObj = reqObj;
        }

        public void SetResponseHandler(responseHandler handler)
        {
            _responseHandler = handler;
        }

        // Send
        public void send()
        {
            ThreadStart ts = new ThreadStart(() => commenceRequestSend());
            Thread t = new Thread(ts);
            t.Start();
        }

        private void commenceRequestSend()
        {
            using (var client = new HttpClient())
            {
                try
                {
                    HttpResponseMessage resObj = client.PostAsync(_url, new StringContent(_reqObj.ToString(), System.Text.Encoding.UTF8, "application/json")).Result;
                    InvokeResponse(null, resObj.ToString());
                }
                catch (Exception ex)
                {
                    InvokeResponse(ex, null);
                }
            }
        }

        private void InvokeResponse(Exception e, string s)
        {
            Program.UIThread.Post((state) =>
            {
                _responseHandler(e, s);
            }, null);
        }
    }
}
