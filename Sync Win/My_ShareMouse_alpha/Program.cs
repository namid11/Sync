using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace My_ShareMouse_alpha {
    static class Program {
        /// <summary>
        /// アプリケーションのメイン エントリ ポイントです。
        /// </summary>
        /// 

        static Form1 form = null;
        [STAThread]
        static void Main() {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            // Formを表示させないための処理
            if(form == null) {
                form = new Form1();   // Formのインスタンスは作っておくらしい
                Application.Run();          // Formインスタンスを渡さずにRun
            }
        }

        static public void display() {
            form.Show();
        }
    }
}
