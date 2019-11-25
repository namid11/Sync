
using Codeplex.Data;
using System.Text;

namespace My_ShareMouse_alpha {

    class MetaManager {
        /// <summary>
        /// JSONデータ取得
        /// </summary>
        /// <returns>パース済みJSONデータ</returns>
        public dynamic getMetaJson() {
            byte[] binary_json_data = Properties.Resources.meta;
            string json_data = Encoding.UTF8.GetString(binary_json_data);
            dynamic obj = DynamicJson.Parse(json_data);

            return obj;
        }

        /// <summary>
        /// IPアドレス取得
        /// </summary>
        /// <returns>IPアドレス</returns>
        public string getIPAddress() {
            dynamic obj = getMetaJson();
            string ip_address = (string)obj.ip_address;
            return ip_address;
        }

        /// <summary>
        /// ポート番号取得
        /// </summary>
        /// <returns>ポート番号</returns>
        public string getPortNum() {
            dynamic obj = getMetaJson();
            string port_num = (string)obj.port_number;
            return port_num;
        }


        public void setIPAddress(string ip_addr) {
            dynamic obj = getMetaJson();
            obj.ip_address = ip_addr;
            var json_str = DynamicJson.Serialize(obj);
            //System.Resources.IResourceWriter writer = new System.Resources.Res
        } 
    }
}