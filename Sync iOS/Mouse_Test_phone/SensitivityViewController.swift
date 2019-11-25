//
//  SensitivityViewController.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2019/01/11.
//  Copyright © 2019 Namid. All rights reserved.
//

import UIKit

class SensitivityViewController: UIViewController {

    @IBOutlet weak var sensitivityLabel: UILabel!
    @IBOutlet weak var sensitivitySlider: UISlider!
    
    var key_name: String? = nil
    var bar_minimmum: Float = 1
    var bar_maximum: Float = 10
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let key = key_name {
            let value = UserDefaults.standard.float(forKey: key)
            sensitivitySlider.value = value
            sensitivitySlider.minimumValue = bar_minimmum
            sensitivitySlider.maximumValue = bar_maximum
            sensitivityLabel.text = String(format: "感度：%.1f", value)
        }

    }
    
    @IBAction func changeSlider(_ sender: Any) {
        sensitivityLabel.text = String(format: "感度：%.1f", sensitivitySlider.value)
    }
    
    @IBAction func pushDone(_ sender: Any) {
        if let key = key_name {
            UserDefaults.standard.set(sensitivitySlider.value, forKey: key)
            self.navigationController?.popViewController(animated: true)
        }
    }
    


}
