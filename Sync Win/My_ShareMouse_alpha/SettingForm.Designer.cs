namespace Sync {
    partial class SettingForm {
        /// <summary>
        /// 必要なデザイナー変数です。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 使用中のリソースをすべてクリーンアップします。
        /// </summary>
        /// <param name="disposing">マネージド リソースを破棄する場合は true を指定し、その他の場合は false を指定します。</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows フォーム デザイナーで生成されたコード

        /// <summary>
        /// デザイナー サポートに必要なメソッドです。このメソッドの内容を
        /// コード エディターで変更しないでください。
        /// </summary>
        private void InitializeComponent() {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(SettingForm));
            this.mainContextMenuStrip = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.connectStatusMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.connectRequestMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.autoConnectionMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.manualConnectionMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.disconnectMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.settingMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.finishMenuButton = new System.Windows.Forms.ToolStripMenuItem();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.portNumberTextBox = new System.Windows.Forms.TextBox();
            this.ipAddressListBox = new System.Windows.Forms.ComboBox();
            this.manualSettingSwitch = new System.Windows.Forms.CheckBox();
            this.autoStartCheckBox = new System.Windows.Forms.CheckBox();
            this.panel1 = new System.Windows.Forms.Panel();
            this.manualConnectionButton = new System.Windows.Forms.Button();
            this.connectingStateLabel = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.connectingDeviceNameLabel = new System.Windows.Forms.Label();
            this.connectingDeviceAddressLabel = new System.Windows.Forms.Label();
            this.autoConnectionButton = new System.Windows.Forms.Button();
            this.mainContextMenuStrip.SuspendLayout();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // mainContextMenuStrip
            // 
            this.mainContextMenuStrip.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("mainContextMenuStrip.BackgroundImage")));
            this.mainContextMenuStrip.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.mainContextMenuStrip.Font = new System.Drawing.Font("Yu Gothic UI", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.mainContextMenuStrip.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.connectStatusMenuItem,
            this.toolStripSeparator1,
            this.connectRequestMenuItem,
            this.settingMenuItem,
            this.finishMenuButton});
            this.mainContextMenuStrip.Name = "mainContextMenuStrip";
            this.mainContextMenuStrip.ShowImageMargin = false;
            this.mainContextMenuStrip.Size = new System.Drawing.Size(99, 106);
            this.mainContextMenuStrip.Text = "Sync";
            // 
            // connectStatusMenuItem
            // 
            this.connectStatusMenuItem.ForeColor = System.Drawing.Color.White;
            this.connectStatusMenuItem.Name = "connectStatusMenuItem";
            this.connectStatusMenuItem.Size = new System.Drawing.Size(98, 24);
            this.connectStatusMenuItem.Text = "未接続";
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(95, 6);
            // 
            // connectRequestMenuItem
            // 
            this.connectRequestMenuItem.BackColor = System.Drawing.SystemColors.Control;
            this.connectRequestMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.autoConnectionMenuItem,
            this.manualConnectionMenuItem,
            this.disconnectMenuItem});
            this.connectRequestMenuItem.ForeColor = System.Drawing.Color.White;
            this.connectRequestMenuItem.Name = "connectRequestMenuItem";
            this.connectRequestMenuItem.Size = new System.Drawing.Size(98, 24);
            this.connectRequestMenuItem.Text = "接続";
            // 
            // autoConnectionMenuItem
            // 
            this.autoConnectionMenuItem.Name = "autoConnectionMenuItem";
            this.autoConnectionMenuItem.Size = new System.Drawing.Size(138, 24);
            this.autoConnectionMenuItem.Text = "自動接続";
            // 
            // manualConnectionMenuItem
            // 
            this.manualConnectionMenuItem.Enabled = false;
            this.manualConnectionMenuItem.Name = "manualConnectionMenuItem";
            this.manualConnectionMenuItem.Size = new System.Drawing.Size(138, 24);
            this.manualConnectionMenuItem.Text = "手動接続";
            // 
            // disconnectMenuItem
            // 
            this.disconnectMenuItem.Enabled = false;
            this.disconnectMenuItem.Name = "disconnectMenuItem";
            this.disconnectMenuItem.Size = new System.Drawing.Size(138, 24);
            this.disconnectMenuItem.Text = "切断";
            // 
            // settingMenuItem
            // 
            this.settingMenuItem.ForeColor = System.Drawing.Color.White;
            this.settingMenuItem.Name = "settingMenuItem";
            this.settingMenuItem.Size = new System.Drawing.Size(98, 24);
            this.settingMenuItem.Text = "設定";
            // 
            // finishMenuButton
            // 
            this.finishMenuButton.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.finishMenuButton.ForeColor = System.Drawing.Color.White;
            this.finishMenuButton.Name = "finishMenuButton";
            this.finishMenuButton.Size = new System.Drawing.Size(98, 24);
            this.finishMenuButton.Text = "終了";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.Font = new System.Drawing.Font("Yu Gothic UI Semilight", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.label1.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(45)))), ((int)(((byte)(52)))), ((int)(((byte)(54)))));
            this.label1.Location = new System.Drawing.Point(80, 102);
            this.label1.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(66, 20);
            this.label1.TabIndex = 1;
            this.label1.Text = "IPアドレス";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.BackColor = System.Drawing.Color.Transparent;
            this.label2.Font = new System.Drawing.Font("Yu Gothic UI Semilight", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.label2.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(45)))), ((int)(((byte)(52)))), ((int)(((byte)(54)))));
            this.label2.Location = new System.Drawing.Point(80, 150);
            this.label2.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(72, 20);
            this.label2.TabIndex = 2;
            this.label2.Text = "ポート番号";
            // 
            // portNumberTextBox
            // 
            this.portNumberTextBox.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.portNumberTextBox.Enabled = false;
            this.portNumberTextBox.Font = new System.Drawing.Font("Yu Gothic UI", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.portNumberTextBox.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(45)))), ((int)(((byte)(52)))), ((int)(((byte)(54)))));
            this.portNumberTextBox.Location = new System.Drawing.Point(182, 149);
            this.portNumberTextBox.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.portNumberTextBox.Name = "portNumberTextBox";
            this.portNumberTextBox.Size = new System.Drawing.Size(160, 22);
            this.portNumberTextBox.TabIndex = 4;
            this.portNumberTextBox.Text = "8080";
            this.portNumberTextBox.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            // 
            // ipAddressListBox
            // 
            this.ipAddressListBox.Enabled = false;
            this.ipAddressListBox.Font = new System.Drawing.Font("Yu Gothic UI", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.ipAddressListBox.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(45)))), ((int)(((byte)(52)))), ((int)(((byte)(54)))));
            this.ipAddressListBox.FormattingEnabled = true;
            this.ipAddressListBox.Location = new System.Drawing.Point(182, 98);
            this.ipAddressListBox.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.ipAddressListBox.Name = "ipAddressListBox";
            this.ipAddressListBox.Size = new System.Drawing.Size(160, 29);
            this.ipAddressListBox.TabIndex = 7;
            this.ipAddressListBox.SelectedIndexChanged += new System.EventHandler(this.ipAddressListBox_SelectedIndexChanged);
            // 
            // manualSettingSwitch
            // 
            this.manualSettingSwitch.AutoSize = true;
            this.manualSettingSwitch.BackColor = System.Drawing.Color.Transparent;
            this.manualSettingSwitch.Font = new System.Drawing.Font("Yu Gothic UI", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.manualSettingSwitch.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(45)))), ((int)(((byte)(52)))), ((int)(((byte)(54)))));
            this.manualSettingSwitch.Location = new System.Drawing.Point(103, 42);
            this.manualSettingSwitch.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.manualSettingSwitch.Name = "manualSettingSwitch";
            this.manualSettingSwitch.Size = new System.Drawing.Size(210, 25);
            this.manualSettingSwitch.TabIndex = 8;
            this.manualSettingSwitch.Text = "手動接続にする（非推奨）";
            this.manualSettingSwitch.UseVisualStyleBackColor = false;
            this.manualSettingSwitch.CheckedChanged += new System.EventHandler(this.manualSettingSwitch_CheckedChanged);
            // 
            // autoStartCheckBox
            // 
            this.autoStartCheckBox.AutoSize = true;
            this.autoStartCheckBox.BackColor = System.Drawing.Color.White;
            this.autoStartCheckBox.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.autoStartCheckBox.Font = new System.Drawing.Font("Yu Gothic UI Semibold", 11.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.autoStartCheckBox.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(188)))), ((int)(((byte)(249)))));
            this.autoStartCheckBox.Location = new System.Drawing.Point(32, 91);
            this.autoStartCheckBox.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.autoStartCheckBox.Name = "autoStartCheckBox";
            this.autoStartCheckBox.Padding = new System.Windows.Forms.Padding(10, 5, 5, 5);
            this.autoStartCheckBox.Size = new System.Drawing.Size(206, 34);
            this.autoStartCheckBox.TabIndex = 9;
            this.autoStartCheckBox.Text = "次回から自動的に開始する";
            this.autoStartCheckBox.UseVisualStyleBackColor = false;
            this.autoStartCheckBox.CheckedChanged += new System.EventHandler(this.autoStartCheckBox_CheckedChanged);
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.Color.Transparent;
            this.panel1.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("panel1.BackgroundImage")));
            this.panel1.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.panel1.Controls.Add(this.manualConnectionButton);
            this.panel1.Controls.Add(this.portNumberTextBox);
            this.panel1.Controls.Add(this.label2);
            this.panel1.Controls.Add(this.ipAddressListBox);
            this.panel1.Controls.Add(this.label1);
            this.panel1.Controls.Add(this.manualSettingSwitch);
            this.panel1.Location = new System.Drawing.Point(-11, 130);
            this.panel1.Margin = new System.Windows.Forms.Padding(0);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(521, 227);
            this.panel1.TabIndex = 10;
            // 
            // manualConnectionButton
            // 
            this.manualConnectionButton.Enabled = false;
            this.manualConnectionButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.manualConnectionButton.Font = new System.Drawing.Font("Yu Gothic UI", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.manualConnectionButton.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(188)))), ((int)(((byte)(249)))));
            this.manualConnectionButton.Location = new System.Drawing.Point(403, 140);
            this.manualConnectionButton.Name = "manualConnectionButton";
            this.manualConnectionButton.Size = new System.Drawing.Size(95, 38);
            this.manualConnectionButton.TabIndex = 17;
            this.manualConnectionButton.Text = "接続";
            this.manualConnectionButton.UseVisualStyleBackColor = true;
            // 
            // connectingStateLabel
            // 
            this.connectingStateLabel.AutoSize = true;
            this.connectingStateLabel.BackColor = System.Drawing.Color.Transparent;
            this.connectingStateLabel.Font = new System.Drawing.Font("Yu Gothic UI", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.connectingStateLabel.ForeColor = System.Drawing.Color.White;
            this.connectingStateLabel.Location = new System.Drawing.Point(27, 33);
            this.connectingStateLabel.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.connectingStateLabel.Name = "connectingStateLabel";
            this.connectingStateLabel.Size = new System.Drawing.Size(50, 25);
            this.connectingStateLabel.TabIndex = 11;
            this.connectingStateLabel.Text = "待機";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.BackColor = System.Drawing.Color.Transparent;
            this.label4.Font = new System.Drawing.Font("Yu Gothic UI", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.label4.ForeColor = System.Drawing.Color.White;
            this.label4.Location = new System.Drawing.Point(112, 18);
            this.label4.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(75, 21);
            this.label4.TabIndex = 12;
            this.label4.Text = "デバイス名";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.BackColor = System.Drawing.Color.Transparent;
            this.label5.Font = new System.Drawing.Font("Yu Gothic UI", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.label5.ForeColor = System.Drawing.Color.White;
            this.label5.Location = new System.Drawing.Point(112, 53);
            this.label5.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(69, 21);
            this.label5.TabIndex = 13;
            this.label5.Text = "IPアドレス";
            // 
            // connectingDeviceNameLabel
            // 
            this.connectingDeviceNameLabel.AutoSize = true;
            this.connectingDeviceNameLabel.BackColor = System.Drawing.Color.Transparent;
            this.connectingDeviceNameLabel.Font = new System.Drawing.Font("Yu Gothic UI", 11F);
            this.connectingDeviceNameLabel.ForeColor = System.Drawing.Color.White;
            this.connectingDeviceNameLabel.Location = new System.Drawing.Point(193, 19);
            this.connectingDeviceNameLabel.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.connectingDeviceNameLabel.Name = "connectingDeviceNameLabel";
            this.connectingDeviceNameLabel.Size = new System.Drawing.Size(45, 20);
            this.connectingDeviceNameLabel.TabIndex = 14;
            this.connectingDeviceNameLabel.Text = "None";
            // 
            // connectingDeviceAddressLabel
            // 
            this.connectingDeviceAddressLabel.AutoSize = true;
            this.connectingDeviceAddressLabel.BackColor = System.Drawing.Color.Transparent;
            this.connectingDeviceAddressLabel.Font = new System.Drawing.Font("Yu Gothic UI", 11F);
            this.connectingDeviceAddressLabel.ForeColor = System.Drawing.Color.White;
            this.connectingDeviceAddressLabel.Location = new System.Drawing.Point(193, 54);
            this.connectingDeviceAddressLabel.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
            this.connectingDeviceAddressLabel.Name = "connectingDeviceAddressLabel";
            this.connectingDeviceAddressLabel.Size = new System.Drawing.Size(45, 20);
            this.connectingDeviceAddressLabel.TabIndex = 15;
            this.connectingDeviceAddressLabel.Text = "None";
            // 
            // autoConnectionButton
            // 
            this.autoConnectionButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.autoConnectionButton.Font = new System.Drawing.Font("Yu Gothic UI", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.autoConnectionButton.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(15)))), ((int)(((byte)(188)))), ((int)(((byte)(249)))));
            this.autoConnectionButton.Location = new System.Drawing.Point(392, 77);
            this.autoConnectionButton.Name = "autoConnectionButton";
            this.autoConnectionButton.Size = new System.Drawing.Size(95, 38);
            this.autoConnectionButton.TabIndex = 16;
            this.autoConnectionButton.Text = "自動接続";
            this.autoConnectionButton.UseVisualStyleBackColor = true;
            // 
            // SettingForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
            this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.ClientSize = new System.Drawing.Size(499, 349);
            this.Controls.Add(this.autoConnectionButton);
            this.Controls.Add(this.connectingDeviceAddressLabel);
            this.Controls.Add(this.connectingDeviceNameLabel);
            this.Controls.Add(this.autoStartCheckBox);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.connectingStateLabel);
            this.Controls.Add(this.panel1);
            this.DoubleBuffered = true;
            this.Font = new System.Drawing.Font("Yu Gothic UI", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(128)));
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.Name = "SettingForm";
            this.Text = "Setting";
            this.mainContextMenuStrip.ResumeLayout(false);
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ContextMenuStrip mainContextMenuStrip;
        private System.Windows.Forms.ToolStripMenuItem finishMenuButton;
        private System.Windows.Forms.ToolStripMenuItem connectRequestMenuItem;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox portNumberTextBox;
        private System.Windows.Forms.ToolStripMenuItem settingMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
        private System.Windows.Forms.ComboBox ipAddressListBox;
        private System.Windows.Forms.ToolStripMenuItem autoConnectionMenuItem;
        private System.Windows.Forms.ToolStripMenuItem manualConnectionMenuItem;
        private System.Windows.Forms.ToolStripMenuItem disconnectMenuItem;
        private System.Windows.Forms.CheckBox manualSettingSwitch;
        private System.Windows.Forms.CheckBox autoStartCheckBox;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Label connectingStateLabel;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label connectingDeviceNameLabel;
        private System.Windows.Forms.Label connectingDeviceAddressLabel;
        private System.Windows.Forms.ToolStripMenuItem connectStatusMenuItem;
        private System.Windows.Forms.Button manualConnectionButton;
        private System.Windows.Forms.Button autoConnectionButton;
    }
}

