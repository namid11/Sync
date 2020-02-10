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

namespace Sync {
    public partial class SettingForm : Form {

        ConnectManager connectManager = new ConnectManager();
        OperateRequestManager operateRequestManager = new OperateRequestManager();
        NotifyIcon icon;

        /// <summary>
        /// Form constructor 
        /// </summary>
        public SettingForm() {
            InitializeComponent();

            // タスクバーには表示させない
            //this.ShowInTaskbar = false;

            // meta.json初期化
            AppDataManager.init();

            // IPAddressListboxの項目の追加
            foreach (IPAddress addr in AddressManager.findMyAddress()) {
                ipAddressListBox.Items.Add(addr.ToString());
            }

            // タスクトレイにアイコンを表示
            icon = new NotifyIcon();
            icon.Icon = Properties.Resources.Sync_Icon_Transparent;
            icon.Visible = true;
            icon.Text = "Sync";

            // 右クリックメニュー
            icon.ContextMenuStrip = mainContextMenuStrip;

            // メニューボタン
            finishMenuButton.Click += new EventHandler(Exit);
            manualConnectionButton.Click += new EventHandler(manualConnect);
            manualConnectionMenuItem.Click += new EventHandler(manualConnect);
            disconnectMenuItem.Click += new EventHandler(disconnect);
            settingMenuItem.Click += new EventHandler(setting);

            // 接続スタータスの表示設定
            var latestData = AppDataManager.getLatestData();
            if (AppDataManager.autoStartFlag() && latestData != null) {
                // 以前の接続先データがあるなら表示
                dispConnectingView(latestData.device, latestData.ipAddress);
            } else {
                dispDisconnectingView();
            }


            // 起動時の自動接続開始
            if (AppDataManager.autoStartFlag()) {
                string ipAddress = AppDataManager.getSavedAddress();
                int port = AppDataManager.getSavedPort();
                operateRequestManager.connect(ipAddress, port);
                autoStartCheckBox.Checked = true;
                this.Visible = false;
            } else {
                // 起動時は表示？
                this.Visible = true;
            }

            startAutoConnectOperation();
        }

        private void manualConnect(object sender, EventArgs e) {
            string ipAddr = AppDataManager.getSavedAddress();
            int port = AppDataManager.getSavedPort();
            if (ipAddr == "") {
                showConnetError("「設定」からIPアドレスを選択してください");
                return;
            }
            operateRequestManager.connect(ipAddr, port);
            // Update View Component
            dispConnectingView("None", ipAddr);
        }

        // finish receiving UDP Sockets.
        private void disconnect(object sender, EventArgs e) {
            ToolStripMenuItem menu_item = (ToolStripMenuItem)sender;
            operateRequestManager.disconnect();
            dispDisconnectingView();
        }

        // 自動接続オペレーション
        private void startAutoConnectOperation() {
            // ローディング開始
            //AutoConnectingForm autoConnectingForm = new AutoConnectingForm();
            //autoConnectingForm.Show();

            // 事前に利用中のUDP通信を切断（自動接続のポート番号変えたのでいらない）
            //operateRequestManager.disconnect();

            // スマホ側から自動接続確認メッセージを受け取る
            connectManager.getConnectingMsg((CONNECTSTATUS status, dynamic rcvData) => {
                IPAddress iPAddress = connectManager.SameNetworkAddress;
                if (iPAddress == null) {
                    // Not found IP address on same network.
                    //showConnetError("接続に失敗しました。\nスマホとパソコンが同じネットワーク上にあるか確認してください");
                } else {
                    // successed connecting
                    AppDataManager.putAddress(iPAddress.ToString());
                    this.Invoke(new Action(() => {
                        // 接続完了したら、View更新
                        if (status == CONNECTSTATUS.OK) {
                            dispConnectingView(rcvData.device, rcvData.ip);
                        } else if (status == CONNECTSTATUS.ERROR) {
                            dispFailedConnectingView();
                        }

                        if (rcvData.IsDefined("autoStart")) {
                            autoStartCheckBox.Checked = true;
                        }

                        //autoConnectingForm.Close();
                    }));

                    DialogResult dialogResult = MessageBox.Show(
                        "接続が完了しました。\n次回から自動的にこのデバイスと接続しますか？\nデバイス名：" + rcvData.device, "接続成功",
                        MessageBoxButtons.OKCancel);
                    if (dialogResult == DialogResult.OK) {
                        AppDataManager.putAutoStartFlag(true);
                        AppDataManager.putLatestData(rcvData.device, rcvData.ip);
                    }
                    operateRequestManager.connect(iPAddress.ToString(), AppDataManager.getSavedPort());
                }
            },  
            () => {
                // reject
                //this.Invoke(new Action(() => {
                    //autoConnectingForm.Close();
                //}));
            });
        }

        private void setting(object sender, EventArgs e) {
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

        // 終了イベンド
        private void Exit(object sender, EventArgs e) {
            icon.Visible = false;
            operateRequestManager.disconnect();
            connectManager.cancelUdpReceiving();
            Application.Exit();
        }

        // ipアドレス選択時
        private void ipAddressListBox_SelectedIndexChanged(object sender, EventArgs e) {
            ComboBox comboBox = (ComboBox)sender;
            AppDataManager.putAddress(comboBox.SelectedItem.ToString());
        }

        // ポート番号入力制限（数値のみ）
        private void portNumberTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            TextBox textBox = (TextBox)sender;
            if ((e.KeyChar < '0' || '9' < e.KeyChar) && e.KeyChar != '\b') {
                e.Handled = true;
                int value = int.Parse(textBox.Text);
                AppDataManager.putPortNumber(value);
            }
        }

        private void manualSettingSwitch_CheckedChanged(object sender, EventArgs e) {
            CheckBox checkBox = (CheckBox)sender;
            manualConnectionMenuItem.Enabled = checkBox.Checked;
            ipAddressListBox.Enabled = checkBox.Checked;
            portNumberTextBox.Enabled = checkBox.Checked;
            manualConnectionButton.Enabled = checkBox.Checked;
        }

        private void autoStartCheckBox_CheckedChanged(object sender, EventArgs e) {
            CheckBox checkBox = (CheckBox)sender;
            Thread thread = new Thread(new ThreadStart(() => {
                AppDataManager.putAutoStartFlag(checkBox.Checked);
            }));
            thread.Start();
        }

        private void dispConnectingView(string connectedDevice, string connectedAddress) {
            connectStatusMenuItem.Text = "接続中";
            connectStatusMenuItem.ForeColor = Color.Blue;
            disconnectMenuItem.Enabled = true;

            connectingStateLabel.Text = "接続中";
            connectingDeviceNameLabel.Text = connectedDevice;
            connectingDeviceAddressLabel.Text = connectedAddress;
        }

        private void dispDisconnectingView() {
            connectStatusMenuItem.Text = "未接続";
            connectStatusMenuItem.ForeColor = Color.Gray;
            disconnectMenuItem.Enabled = false;

            connectingStateLabel.Text = "未接続";
            connectingDeviceNameLabel.Text = "未接続";
            connectingDeviceAddressLabel.Text = "未接続";
        }

        private void dispFailedConnectingView() {
            connectStatusMenuItem.Text = "エラー";
            connectStatusMenuItem.ForeColor = Color.Red;
        }


        private void showConnetError(string msg) {
            DialogResult dialogResult = MessageBox.Show(
                msg,
                "Failed",
                MessageBoxButtons.OK,
                MessageBoxIcon.Error);
        }
    }
}
