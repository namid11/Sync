using Codeplex.Data;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Sync {
    class OperateRequestManager {

        private const int MOUSEEVENT_LEFTDOWN = 0x2;
        private const int MOUSEEVENT_LEFTUP = 0x4;
        private const int MOUSEEVENT_RIGHTDOWN = 0x8;
        private const int MOUSEEVENT_RIGHTUP = 0x10;
        private const int MOUSEEVENTF_WHEEL = 0x800;
        private const int EVENT_ZOOM = 0xFB;
        private const int VK_WINDOWSKEY = 0x5B;
        private const int VK_CTRLKEY = 0xA2;
        private const int VK_ALT = 0x12;
        private const int VK_BROWSER_BACK = 0xA6;
        private const int VK_BROWSER_FORWARD = 0xA7;
        private const int VK_TABKEY = 0x09;
        private const int VK_L = 0x4C;
        private const int VK_M = 0x4D;
        private const int VK_O = 0x4F;
        private const int VK_F5 = 0x74;
        private const int VK_ESC = 0x1B;
        private const int VK_PAGEUP = 0x21;
        private const int VK_PAGEDOWN = 0x22;
        private const int KEYEVENT_KEYDOWN = 0x0000;
        private const int KEYEVENT_KEYUP = 0x0002;


        [DllImport("USER32.dll", CallingConvention = CallingConvention.StdCall)]
        static extern void SetCursorPos(int x, int y);
        [DllImport("USER32.dll", CallingConvention = CallingConvention.StdCall)]
        static extern void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);
        [DllImport("USER32.dll", CallingConvention = CallingConvention.StdCall)]
        static extern void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);


        Point buffer_location = new Point(0, 0);
        bool buffer_scale = false;

        private void startUDP(UdpClient udp_client) {
            Thread thread = new Thread(new ThreadStart(() => {
                while (true) {
                    IPEndPoint remote_ep = null;

                    try {
                        byte[] recieveBytes = udp_client.Receive(ref remote_ep);
                        string recieveStr = Encoding.UTF8.GetString(recieveBytes);
                        dynamic obj = DynamicJson.Parse(recieveStr);
                        if (!obj.IsDefined("key")) {
                            //メッセージボックスを表示する
                            DialogResult result = MessageBox.Show("不明な通信がされました。\n再接続してください。",
                                "不明な通信",
                                MessageBoxButtons.OK, 
                                MessageBoxIcon.Warning);
                        }
                        switch (obj.key) {
                            case "moved":
                                //Console.WriteLine("key is 'moved'");
                                int x = (int)obj.context.x + this.buffer_location.X;
                                int y = (int)obj.context.y + this.buffer_location.Y;
                                SetCursorPos(x, y);
                                break;
                            case "first":
                                //Console.WriteLine("key is 'first'");
                                this.buffer_location = new Point(Cursor.Position.X, Cursor.Position.Y);
                                break;
                            case "click":
                                //Console.WriteLine("key is 'click'");
                                mouse_event(MOUSEEVENT_LEFTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_LEFTUP, 0, 0, 0, 0);
                                break;
                            case "double":
                                //Console.WriteLine("key is 'double'");
                                mouse_event(MOUSEEVENT_LEFTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_LEFTUP, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_LEFTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_LEFTUP, 0, 0, 0, 0);
                                break;
                            case "two_fingers_click":
                                //Console.WriteLine("key is 'two_fingers_click'");
                                mouse_event(MOUSEEVENT_RIGHTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_RIGHTUP, 0, 0, 0, 0);
                                break;
                            case "triple_fingers_click":
                                //Console.WriteLine("key is 'triple_fingers_click'");
                                keybd_event(VK_WINDOWSKEY, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_TABKEY, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_TABKEY, 0, KEYEVENT_KEYUP, 0);
                                keybd_event(VK_WINDOWSKEY, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "scroll":
                                //Console.WriteLine("key is 'scroll'");
                                mouse_event(MOUSEEVENTF_WHEEL, 0, 0, (int)(obj.context.y / 20), (int)(obj.context.x / 20));
                                break;
                            case "pinch":
                                //Console.WriteLine("key is 'pinch'");
                                if (obj.context.value == -1.0) {
                                    keybd_event(VK_CTRLKEY, 0, KEYEVENT_KEYUP, 0);
                                    this.buffer_scale = false;
                                    Console.WriteLine("reset");
                                } else {
                                    if (!this.buffer_scale) {
                                        keybd_event(VK_CTRLKEY, 0, KEYEVENT_KEYDOWN, 0);
                                        this.buffer_scale = true;
                                        Console.WriteLine("set");
                                    }
                                    mouse_event(MOUSEEVENTF_WHEEL, 0, 0, (int)((obj.context.value - 1.0) * 300.0), 0);
                                }
                                break;
                            case "browser_back":
                                //Console.WriteLine("key is 'browser_back'");
                                keybd_event(VK_BROWSER_BACK, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_BROWSER_BACK, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "browser_forward":
                                //Console.WriteLine("key is 'browser_forward'");
                                keybd_event(VK_BROWSER_FORWARD, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_BROWSER_FORWARD, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "shake":
                                //Console.WriteLine("key is 'shake'");
                                var app = new ProcessStartInfo("chrome.exe");
                                //var app = new ProcessStartInfo("explorer.exe");
                                //app.FileName = "start";
                                //app.Arguments = "https://www.google.com";
                                app.WindowStyle = ProcessWindowStyle.Minimized;
                                app.ErrorDialog = true;

                                try {
                                    Process.Start(app);
                                } catch (Exception e) {
                                    Console.WriteLine(e.ToString());
                                }
                                break;
                            case "lock":
                                //Console.WriteLine("key is lock.");
                                keybd_event(VK_WINDOWSKEY, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_M, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_WINDOWSKEY, 0, KEYEVENT_KEYUP, 0);
                                keybd_event(VK_M, 0, KEYEVENT_KEYUP, 0);
                                break;

                            case "pp_next":
                                //Console.WriteLine("key is pp_next");
                                keybd_event(VK_PAGEDOWN, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_PAGEDOWN, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "pp_back":
                                //Console.WriteLine("key is pp_back");
                                keybd_event(VK_PAGEUP, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_PAGEUP, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "pp_laser":
                                //Console.WriteLine("key is pp_laser");
                                mouse_event(MOUSEEVENT_RIGHTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_RIGHTUP, 0, 0, 0, 0);
                                keybd_event(VK_O, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_O, 0, KEYEVENT_KEYUP, 0);
                                keybd_event(VK_L, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_L, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "pp_pen":
                                break;
                            case "pp_start":
                                //Console.WriteLine("key is pp_start");
                                keybd_event(VK_F5, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_F5, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "pp_finish":
                                //Console.WriteLine("key is pp_finish");
                                keybd_event(VK_ESC, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_ESC, 0, KEYEVENT_KEYUP, 0);
                                break;
                            default:
                                break;
                        }
                        //Console.WriteLine("受信したデータ:{0}", recieveStr);
                        //Console.WriteLine("送信元アドレス:{0}\r\nポート番号{1}", remote_ep.Address, remote_ep.Port);
                    } catch (ObjectDisposedException e) {
                        //MessageBox.Show("基になる Socket は閉じられています。\n" + e.Message, 
                        //    "警告", 
                        //    MessageBoxButtons.OK, 
                        //    MessageBoxIcon.Error);
                        Console.WriteLine(e.Message);
                        break;
                    } catch (SocketException e) {
                        //MessageBox.Show("ソケットへのアクセス中にエラーが発生しました。\n" + e.Message,
                        //    "警告",
                        //    MessageBoxButtons.OK,
                        //    MessageBoxIcon.Error);
                        Console.WriteLine(e.Message);
                        break;
                    }
                }

                udp_client.Close();
                bufUdpClient = null;
            }));
            thread.Start();

        }


        private UdpClient bufUdpClient = null;
        public bool connect(string ipAddr, int port) {
            try {
                if (bufUdpClient != null) bufUdpClient.Close();
                IPEndPoint iPEndPoint = new IPEndPoint(IPAddress.Parse(ipAddr), port);
                bufUdpClient = new UdpClient(iPEndPoint);
                startUDP(bufUdpClient);
                return true;
            } catch (Exception e) {
                Console.WriteLine(e.Message);
            }
            return false;
        }
        public void disconnect() {
            try {
                bufUdpClient.Close();
                bufUdpClient = null;
            } catch(Exception e) {
                Console.WriteLine(e.Message);
            }
        }
    }
}
