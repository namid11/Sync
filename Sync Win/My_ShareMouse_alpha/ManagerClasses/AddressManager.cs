using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Threading.Tasks;

namespace Sync {
    class AddressManager {
        static public byte[] ipStringToBytes(string ip) {
            byte[] vs = new byte[4];
            string[] ipSplit = ip.Split('.');
            for (int i = 0; i < ipSplit.Length; i++) {
                vs[i] = (byte)int.Parse(ipSplit[i]);
            }
            return vs;
        }

        static public List<IPAddress> findMyAddress() {
            // ipアドレス取得
            IPAddress[] iPs = Dns.GetHostAddresses(Dns.GetHostName());
            List<IPAddress> ipv4Address = new List<IPAddress>();
            foreach (IPAddress ipAdd in iPs) {
                // IPv6とローカルアドレスは除く
                if (ipAdd.ToString().IndexOf(".") > 0 && !ipAdd.ToString().StartsWith("127.")) {
                    ipv4Address.Add(ipAdd);
                }
            }
            return ipv4Address;
        }
    }
}
