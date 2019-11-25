using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Runtime.InteropServices;
using System.Runtime.Serialization;
using Codeplex.Data;
using System.Diagnostics;

namespace My_ShareMouse_alpha {
    public partial class Form1 : Form {

        private const int MOUSEEVENT_LEFTDOWN = 0x2;
        private const int MOUSEEVENT_LEFTUP = 0x4;
        private const int MOUSEEVENT_RIGHTDOWN = 0x8;
        private const int MOUSEEVENT_RIGHTUP = 0x10;
        private const int MOUSEEVENTF_WHEEL = 0x800;
        private const int EVENT_ZOOM = 0xFB;
        private const int VK_WINDOWSKEY = 0x5B;
        private const int VK_CTRLKEY = 0xA2;
        private const int VK_BROWSER_BACK = 0xA6;
        private const int VK_BROWSER_FORWARD = 0xA7;
        private const int KEYEVENT_KEYDOWN = 0x0000;
        private const int KEYEVENT_KEYUP = 0x0002;

        [DllImport("USER32.dll", CallingConvention = CallingConvention.StdCall)]
        static extern void SetCursorPos(int x, int y);
        [DllImport("USER32.dll", CallingConvention = CallingConvention.StdCall)]
        static extern void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);
        [DllImport("USER32.dll", CallingConvention = CallingConvention.StdCall)]
        static extern void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
        

        NotifyIcon icon;
        IPAddress ip_address;
        IPEndPoint ip_end_point;
        UdpClient udp_client = null;
        MetaManager metaManager = new MetaManager();
        //string ip_addr = "192.168.100.4";
        int port = 8080;
        Point buffer_location = new Point(0, 0);
        bool buffer_scale = false;

        /// <summary>
        /// Form constructor 
        /// </summary>
        public Form1() {
            InitializeComponent();

            // ipアドレス取得
            IPAddress[] iPs = Dns.GetHostAddresses(Dns.GetHostName());
            ip_address = iPs[iPs.Length - 1];
            Console.WriteLine("IPアドレス：{0}", ip_address.ToString());

            // タスクバーには表示させない
            this.ShowInTaskbar = false;

            // タスクトレイにアイコンを表示
            icon = new NotifyIcon();
            icon.Icon = Properties.Resources.NotifyIcon;
            icon.Visible = true;
            icon.Text = "My_ShereMouse";

            // 右クリックメニュー
            icon.ContextMenuStrip = mainContextMenuStrip;

            // メニューボタン
            finishMenuButton.Click += new EventHandler(Exit);
            connectSettingMenuItem.Click += new EventHandler(connected);
            settingMenuItem.Click += new EventHandler(setting);

            /* UDP通信の設定 */
            //ip_address = IPAddress.Parse(ip_addr);
            ip_end_point = new IPEndPoint(ip_address, port);

        }

        /// <summary>
        /// UDP受信 
        /// </summary>
        private void startUDP() {
            Thread thread = new Thread(new ThreadStart(() => {
                while(true) {
                    IPEndPoint remote_ep = null;

                    try {
                        byte[] recieveBytes = udp_client.Receive(ref remote_ep);
                        string recieveStr = Encoding.UTF8.GetString(recieveBytes);
                        dynamic obj = DynamicJson.Parse(recieveStr);
                        switch (obj.key) {
                            case "moved":
                                Console.WriteLine("key is 'moved'");
                                int x = (int)obj.context.x + this.buffer_location.X;
                                int y = (int)obj.context.y + this.buffer_location.Y;
                                SetCursorPos(x, y);
                                break;
                            case "first":
                                Console.WriteLine("key is 'first'");
                                this.buffer_location = new Point(Cursor.Position.X, Cursor.Position.Y);
                                break;
                            case "click":
                                Console.WriteLine("key is 'click'");
                                mouse_event(MOUSEEVENT_LEFTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_LEFTUP, 0, 0, 0, 0);
                                break;
                            case "double":
                                Console.WriteLine("key is 'double'");
                                mouse_event(MOUSEEVENT_LEFTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_LEFTUP, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_LEFTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_LEFTUP, 0, 0, 0, 0);
                                break;
                            case "two_fingers_click":
                                Console.WriteLine("key is 'two_fingers_click'");
                                mouse_event(MOUSEEVENT_RIGHTDOWN, 0, 0, 0, 0);
                                mouse_event(MOUSEEVENT_RIGHTUP, 0, 0, 0, 0);
                                break;
                            case "scroll":
                                Console.WriteLine("key is 'scroll'");
                                mouse_event(MOUSEEVENTF_WHEEL, 0, 0, (int)(obj.context.y / 20), (int)(obj.context.x / 20));
                                break;
                            case "pinch":
                                Console.WriteLine("key is 'pinch'");
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
                                    mouse_event(MOUSEEVENTF_WHEEL, 0, 0, (int)((obj.context.value-1.0) * 300.0), 0);
                                }
                                break;
                            case "browser_back":
                                Console.WriteLine("key is 'browser_back'");
                                keybd_event(VK_BROWSER_BACK, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_BROWSER_BACK, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "browser_forward":
                                Console.WriteLine("key is 'browser_forward'");
                                keybd_event(VK_BROWSER_FORWARD, 0, KEYEVENT_KEYDOWN, 0);
                                keybd_event(VK_BROWSER_FORWARD, 0, KEYEVENT_KEYUP, 0);
                                break;
                            case "shake":
                                Console.WriteLine("key is 'shake'");
                                var app = new ProcessStartInfo("chrome.exe");
                                //var app = new ProcessStartInfo("explorer.exe");
                                //app.FileName = "start";
                                //app.Arguments = "https://www.google.com";
                                app.WindowStyle = ProcessWindowStyle.Minimized;
                                app.ErrorDialog = true;

                                try
                                {
                                    Process.Start(app);
                                }
                                catch (Exception e)
                                {
                                    Console.WriteLine(e.ToString());
                                }
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
            }));
            thread.Start();
            
        }

        /// <summary>
        /// UDP接続 or UDP接続解除
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void connected(object sender, EventArgs e) {
            ToolStripMenuItem menu_item = (ToolStripMenuItem)sender;
            if(menu_item.Text == "接続") {
                menu_item.Text = "接続解除";
                udp_client = new UdpClient(ip_end_point);
                startUDP();
            } else {
                menu_item.Text = "接続";
                udp_client.Close();
            }

        }

        /* UDP接続解除 */
        private void release(object sender, EventArgs e) {
            
        }

        /// <summary>
        /// 設定画面表示
        /// </summary>
        /// <param name="sender">sender</param>
        /// <param name="e">event</param>
        private void setting(object sender, EventArgs e) {
            //MessageBox.Show("OK");
            this.Visible = true;
        }

        protected override void OnFormClosed(FormClosedEventArgs e) {
            base.OnFormClosed(e);
        }

        protected override void OnFormClosing(FormClosingEventArgs e) {
            if (e.CloseReason == CloseReason.UserClosing) {
                e.Cancel = true;        // アプリケーション終了シーケンスをキャンセル
                this.Visible = false;   // しかし、ウィンドウは閉じる
            } else {
                base.OnFormClosing(e);
            }
        }


        /* 
         * 終了メソッド 
         */
        private void Exit(object sender, EventArgs e) {
            icon.Visible = false;
            if(udp_client != null) {
                udp_client.Close();
            }
            Application.Exit();
        }

        private void Form1_Load(object sender, EventArgs e) {

        }

        private void Button1_Click(object sender, EventArgs e) {
            metaManager.getIPAddress();
            metaManager.getPortNum();
        }
    }
}
