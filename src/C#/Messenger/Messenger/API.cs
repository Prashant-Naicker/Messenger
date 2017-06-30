using Newtonsoft.Json.Linq;

namespace Messenger
{
    class API
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
    }
}
