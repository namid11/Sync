//
//  WindowController.swift
//  Mouse_Test
//
//  Created by N2WH on 2018/12/16.
//  Copyright © 2018 Namid. All rights reserved.
//

import Cocoa

class WindowController: NSWindowController {
    
    var vc: NSViewController?
    
    override func windowDidLoad() {
        super.windowDidLoad()
        
        vc = self.contentViewController
        
        // mouseMovedイベントのコールバック処理を許可
        window?.acceptsMouseMovedEvents = true
    
        NSEvent.addGlobalMonitorForEvents(matching: .mouseMoved, handler: {(e: NSEvent) -> Void in
            //print("GlobalMonitor")
            //print(e.locationInWindow)
        })
        
        NSEvent.addLocalMonitorForEvents(matching: [.swipe], handler: {(e:NSEvent) -> NSEvent in
            print("LocalMonitor")
            return e
        })
        
        // Implement this method to handle any initialization after your window controller's window has been loaded from its nib file.
    }
    
    override func mouseMoved(with event: NSEvent) {
        if(event.type == NSEvent.EventType.mouseMoved) {
            if let window = event.window, let _ = vc {
                (vc as! ViewController).setLabel(point: cursorPointTrance(cursor: event.locationInWindow,
                                                                         win: NSPoint(x: window.frame.origin.x, y: window.frame.origin.y)))
            }
        }
    }
    
    override func swipe(with event: NSEvent) {
        print(event)
    }
}
