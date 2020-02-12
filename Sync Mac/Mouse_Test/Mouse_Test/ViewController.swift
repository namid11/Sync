//
//  ViewController.swift
//  Mouse_Test
//
//  Created by N2WH on 2018/12/16.
//  Copyright © 2018 Namid. All rights reserved.
//

import Cocoa

class ViewController: NSViewController {

    /*
     プロパティ群
     */
    @IBOutlet weak var debug_label: NSTextField!
    
    
    /*
     初回ロード
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NSEvent.addLocalMonitorForEvents(matching: .swipe) { (e) -> NSEvent? in
            print(e)
            return e
        }
    
        
    }
    
    
    public func setLabel(point: NSPoint) -> Void {
        debug_label.stringValue = String(format: "x:%3f | y:%3f", point.x, point.y)
    }
    
    
    override func viewWillDisappear() {
        
    }
    
    override var representedObject: Any? {
        didSet {
        
        }
    }

    

}

