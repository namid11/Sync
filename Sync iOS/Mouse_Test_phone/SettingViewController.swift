//
//  SettingViewController.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2019/01/09.
//  Copyright © 2019 Namid. All rights reserved.
//

import UIKit

class SettingViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    let section_context: [String] = ["IP_END_POINT", "CONTROL"]
    let row_context: Dictionary<String, [String]> = ["IP_END_POINT":["IPアドレス", "ポート番号"],
                                                     "CONTROL": ["ポインタ",
                                                                 "スクロール",
                                                                 "スワイプ"]]
    
    /*
     セクションの数
     */
    func numberOfSections(in tableView: UITableView) -> Int {
        return section_context.count
    }
    
    /*
     セクション名
     */
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return section_context[section]
    }
    
    /*
     セルの数
     */
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return row_context[section_context[section]]?.count ?? 0
    }
    
    /*
     Cellの作成
     */
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "default")
        if let c = cell {
            c.textLabel?.text = row_context[section_context[indexPath.section]]?[indexPath.row] ?? ""
            return c;
        } else {
            return UITableViewCell(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
        }
    }
    
    /*
     セル選択時
     */
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch indexPath.section {
        case 0:
            switch indexPath.row {
            case 0:
                alertIPAddr()
                break
            case 1:
                alertPortNum()
                break
            default:
                break
            }
            
        case 1:
            // コントロール
            performSegue(withIdentifier: "Setting_to_Sensitivity",
                         sender: USERDEFAULTS_KEYS.userDefaults_keys[indexPath.row])
            
        default:
            break
        }
        
        tableView.cellForRow(at: indexPath)!.isSelected = false
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    /*
     遷移前編集メソッド
     */
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let vc: SensitivityViewController = segue.destination as! SensitivityViewController
        vc.key_name = (sender as? String) ?? "nil"
    }
    
    
    /*
     IPアドレス編集アラート表示
     */
    func alertIPAddr() {
        let alert = UIAlertController(title: "IPアドレスの変更",
                                      message: "リモートホストのIPアドレスを入力",
                                      preferredStyle: .alert)
        alert.addAction(UIAlertAction.init(title: "OK", style: .default, handler: { (aa:UIAlertAction) in
            // テキストフィールドのデータを保存
            if let tf = alert.textFields {
                let userDefaults = UserDefaults.standard
                userDefaults.set(tf[0].text, forKey: "ip_address")
                userDefaults.synchronize()
            }
        }))
        
        alert.addTextField { (tf: UITextField) in
            // テキストフィールドの初期設定
            if let ip_address = UserDefaults.standard.string(forKey: "ip_address") {
                tf.text = ip_address
            }
        }
        
        alert.addAction(UIAlertAction(title: "キャンセル", style: .cancel, handler: nil))
        
        self.present(alert, animated: true, completion: nil)
    }
    
    
    /*
     ポート番号編集アラート表示
     */
    func alertPortNum() {
        let alert = UIAlertController(title: "ポート番号の変更",
                                      message: "利用するポート番号を入力",
                                      preferredStyle: .alert)
        alert.addAction(UIAlertAction.init(title: "OK", style: .default, handler: { (aa:UIAlertAction) in
            // テキストフィールドのデータを保存
            if let tf = alert.textFields {
                let userDefaults = UserDefaults.standard
                userDefaults.set(tf[0].text, forKey: "port_num")
                userDefaults.synchronize()
            }
        }))
        
        alert.addTextField { (tf: UITextField) in
            // テキストフィールドの初期設定
            if let port = UserDefaults.standard.string(forKey: "port_num") {
                tf.text = port
            }
        }
        
        alert.addAction(UIAlertAction(title: "キャンセル", style: .cancel, handler: nil))
        
        self.present(alert, animated: true, completion: nil)
    }
}
