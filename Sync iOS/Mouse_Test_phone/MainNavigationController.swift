//
//  MainNavigationController.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2019/01/09.
//  Copyright © 2019 Namid. All rights reserved.
//

import UIKit

class MainNavigationController: UINavigationController, UINavigationControllerDelegate {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.delegate = self
        // Do any additional setup after loading the view.
    }


    /*
     NVによる画面遷移をホック
     */
    func navigationController(_ navigationController: UINavigationController, didShow viewController: UIViewController, animated: Bool) {
        // 遷移先がmainVCの場合のみaddress値を更新
        if let vc = viewController as? ViewController,
            let port_tmp = UserDefaults.standard.string(forKey: "port_num"),
            let ip = UserDefaults.standard.string(forKey: "ip_address"),
            let port = UInt16(port_tmp) {
            vc.address = createAddress(port: port, ip: ip)
        }
    }
    
}
