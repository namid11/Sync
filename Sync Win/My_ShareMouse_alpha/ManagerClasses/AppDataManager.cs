using Codeplex.Data;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Sync {

    class AppDataManager {

        static public void init() {
            // meta.json の確認
            if (!File.Exists("./meta.json")) {
                reset();
            }
        }

        static private void reset() {
            // 初期データを作成する
            FileStream fs = new FileStream("./meta.json", FileMode.Create);
            var obj = new {
                version = 1.0,
                default_data = new {
                    port_number = 8080,
                    subnet_mask = "255.255.255.0"
                },
                saved = new {
                    port_number = 8080,
                    ip_address = "",
                    subnet_mask = "255.255.255.0"
                },
                local = new {
                    auto_start = false,
                    manual_connect = false,
                }
            };
            byte[] root = Encoding.UTF8.GetBytes(DynamicJson.Serialize(obj));
            fs.Write(root, 0, root.Length);
            fs.Close();
        }

        // meta.json書き出し
        static public void setMetaData(dynamic jsonData) {
            byte[] writeData = Encoding.UTF8.GetBytes(jsonData.ToString());
            FileStream fs = new FileStream("./meta.json", FileMode.Create);
            fs.Write(writeData, 0, writeData.Length);
            fs.Close();
        }

        // save IPAddress
        static public void putAddress(string ip) {
            var metaJson = getMetaData();
            metaJson.saved.ip_address = ip;
            setMetaData(metaJson);
        }

        static public void putPortNumber(int port) {
            var metaJson = getMetaData();
            metaJson.saved.port_number = port;
            setMetaData(metaJson);
        }

        static public void putAddressAndPort(string ip, int port) {
            var metaJson = getMetaData();
            metaJson.saved.ip_address = ip;
            metaJson.saved.port_number = port;
            setMetaData(metaJson);
        }

        static public void putManualConnectFlag(bool flag) {
            var metaJson = getMetaData();
            metaJson.local.manual_connect = flag;
        }

        static public void putAutoStartFlag(bool flag) {
            var metaJson = getMetaData();
            metaJson.local.auto_start = flag;
            setMetaData(metaJson);
        }
        
        static public void putLatestData(string device, string ipAddress) {
            var metaJson = getMetaData();
            if (!metaJson.IsDefined("latest_data")) metaJson.latest_data = new {};
            metaJson.latest_data.device = device;
            metaJson.latest_data.ipAddress = ipAddress;
            setMetaData(metaJson);
        }

        // meta.json読み込み
        static public dynamic getMetaData() {
            try {
                var json = DynamicJson.Parse(File.ReadAllText("./meta.json"));
                return json;
            } catch (Exception e) {
                Console.WriteLine(e.Message);
                return null;
            }
        }

        static public string getSavedAddress() {
            var metaJson = getMetaData();
            string ipAddress = metaJson.saved.ip_address;
            if (ipAddress == "") {
                return "192.168.0.1"; 
            }
            return ipAddress;
        }

        static public int getSavedPort() {
            var metaJson = getMetaData();
            return (int)metaJson.saved.port_number;
        }

        static public string getSavedSubnetMask() {
            var metaJson = getMetaData();
            return metaJson.saved.subnet_mask;
        }

        static public bool autoStartFlag() {
            var metaJson = getMetaData();
            if (metaJson.local.IsDefined("auto_start")) {
                return metaJson.local.auto_start;
            } else {
                putAutoStartFlag(false);
                return false;
            }
        }

        static public bool manualConnectingFlag() {
            var metaJson = getMetaData();
            if (metaJson.local.IsDefined("manual_connect")) {
                return metaJson.local.manual_connect;
            } else {
                putManualConnectFlag(false);
                return false;
            }
        }

        static public dynamic getLatestData() {
            var metaJson = getMetaData();
            if (!metaJson.IsDefined("latest_data")) return null;
            else return metaJson.latest_data;
        }
    }
}
