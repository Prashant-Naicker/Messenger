using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Net.Http;
using System.Threading;

namespace Messenger
{
    abstract class API
    {
        private static string _url;
        private JObject _reqObj;

        public API(string url)
        {
            _url = url;
        }

        // Request/Response.
        public void setRequestObject(JObject reqObj)
        {
            _reqObj = reqObj;
        }
        private void raiseResponse(Exception ex, string resObj)
        {
            onResponseCallback(ex, resObj);
        }
        public abstract void onResponseCallback(Exception ex, string resObj);

        // Send
        public void send()
        {
            ThreadStart ts = new ThreadStart(() => commenceRequestSend());
            Thread t = new Thread(ts);
            t.Start();
        }

        public void commenceRequestSend()
        {
           using (var client = new HttpClient())
            {
                try
                {
                    HttpResponseMessage resObj = client.PostAsync(_url, new StringContent(_reqObj.ToString(), System.Text.Encoding.UTF8, "application/json")).Result;
                    raiseResponse(null, JsonConvert.SerializeObject(resObj));
                }
                catch (Exception ex)
                {
                    raiseResponse(ex, null);
                }
            }
        }
    }
}
