//
//  CursorWindowController.swift
//  Mouse_Test
//
//  Created by NH on 2020/02/11.
//  Copyright © 2020 Namid. All rights reserved.
//

import Cocoa

class CursorWindowController: NSWindowController {

    override func windowDidLoad() {
        super.windowDidLoad()
    
        // Implement this method to handle any initialization after your window controller's window has been loaded from its nib file.
        
        window?.isOpaque = false
        window?.backgroundColor = NSColor(calibratedHue: 1, saturation: 1, brightness: 1, alpha: 0)
        
    }

}
