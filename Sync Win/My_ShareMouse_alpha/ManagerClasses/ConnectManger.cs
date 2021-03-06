﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Windows.Forms;
using Codeplex.Data;
using System.Diagnostics;

namespace Sync {

    enum CONNECTSTATUS {
        OK, ERROR
    }

    delegate void resolve(CONNECTSTATUS status, dynamic rcvData);
    delegate void reject();
    delegate void safeCallDelegate();

    class ConnectManager {

        private int portUdp = 8081;
        private int portTcp = 3333;
        private IPEndPoint localEP = null;
        private UdpClient udpReceivingClient = null;
        private Thread udpReceivingThread = null;

        private IPAddress sameNetworkAddress = null;
        public IPAddress SameNetworkAddress {
            get {
                return sameNetworkAddress;
            }
        }

        public ConnectManager() {
        }

        private void getListen(resolve resolve, reject reject) {
            try {
                localEP = new IPEndPoint(IPAddress.Any, portUdp);   // IPAddress.Any is to try connecting all available ip address.
                udpReceivingClient = new UdpClient(localEP);
                //udpReceivingClient.Client.ReceiveTimeout = 20000;

                udpReceivingThread = new Thread(new ThreadStart(() => {
                    while (true) {
                        IPEndPoint remoteEP = null;
                        try {
                            byte[] rcvBytes = udpReceivingClient.Receive(ref remoteEP);
                            //データを文字列に変換する
                            string rcvMsg = Encoding.UTF8.GetString(rcvBytes);
                            var rcvJson = DynamicJson.Parse(rcvMsg);

                            //受信したデータと送信者の情報を表示する
                            Console.WriteLine("受信したデータ:{0}", rcvMsg);
                            Console.WriteLine("送信元アドレス:{0}\nデバイス:{1}", rcvJson.ip, rcvJson.device);

                            // 同一ネットワークのIPアドレスを取得し、クライアントに送る
                            sameNetworkAddress = findSameNetwork(rcvJson.ip);
                            if (sameNetworkAddress != null) {
                                Console.WriteLine("サーバのIPアドレスは{0}です", sameNetworkAddress.ToString());
                                var sendObj = new {
                                    ip = sameNetworkAddress.ToString(),
                                    machineName = Environment.MachineName
                                };
                                sendConnectDoneMsg(rcvJson.ip, DynamicJson.Serialize(sendObj));
                                resolve(CONNECTSTATUS.OK, rcvJson);
                            } else {
                                Console.WriteLine("同じネットワークのアドレスがありません");
                            }
                        } catch (SocketException e) {
                            Console.WriteLine("[Scoket Timeout] {0}", e.Message);
                            MessageBox.Show("接続先が見つかりませんでした", "接続エラー", MessageBoxButtons.OK, MessageBoxIcon.Error);
                            reject();
                            break;
                        } catch (Exception e) {
                            Console.WriteLine("[ERROR] {0}", e.Message);
                            break;
                        }
                    }
                    udpReceivingClient.Close();
                }));
                udpReceivingThread.Start();

            } catch (Exception e) {
                Console.WriteLine("[ERROR] {0}", e.Message);
            }
        }

        private void sendConnectDoneMsg(string ip, string sendMsg) {
            try {
                TcpClient tcpClient = new TcpClient(ip, portTcp);
                NetworkStream ns = tcpClient.GetStream();
                ns.WriteTimeout = 10000;
                byte[] sendBytes = Encoding.UTF8.GetBytes(sendMsg);
                ns.Write(sendBytes, 0, sendBytes.Length);
                ns.Close();
                tcpClient.Close();
            } catch(Exception e) {
                Console.WriteLine(e.Message);
            }
        }

        public void cancelUdpReceiving() {
            if (udpReceivingClient != null) {
                udpReceivingThread.Abort();
            }
        }

        public string getConnectingMsg(resolve resolve, reject reject) {
            getListen(resolve, reject);
            return "";
        }

        private IPAddress findSameNetwork(string ip) {
            IPAddress sameNetworkAddress = null;
            foreach (IPAddress ipAddr in AddressManager.findMyAddress()) {
                byte[] clientAddress = AddressManager.ipStringToBytes(ip);
                byte[] subnetmask = AddressManager.ipStringToBytes(AppDataManager.getSavedSubnetMask());
                bool addressFlag = false;
                for (int i = 0; i < subnetmask.Length; i++) {
                    if ((clientAddress[i] & subnetmask[i]) == (ipAddr.GetAddressBytes()[i] & subnetmask[i])) {
                        if (i == subnetmask.Length - 1) {
                            addressFlag = true;
                        } else {
                            continue;
                        }
                    } else {
                        break;
                    }
                }
                if (addressFlag) {
                    sameNetworkAddress = ipAddr;
                }
            }
            return sameNetworkAddress;
        }
    }
}
