//
//  MouseHock.swift
//  Mouse_Test
//
//  Created by N2WH on 2018/12/24.
//  Copyright © 2018 Namid. All rights reserved.
//

import Foundation
import Cocoa


//let event = NSEvent.mouseEvent(with: .mouseMoved,
//                               location: NSPoint(x: 100, y: 100),
//                               modifierFlags: [],
//                               timestamp: 10000,
//                               windowNumber: NSApplication.shared.mainWindow!.windowNumber,
//                               context: nil,
//                               eventNumber: 215,
//                               clickCount: 1,
//                               pressure: 0.0)

func hock() -> Void {
    // マウスの位置を常に取得
    NSEvent.addGlobalMonitorForEvents(matching: [.keyDown, .keyUp, .flagsChanged, .scrollWheel]) { (e) in
        //print(e)
    }
    
    NSEvent.addLocalMonitorForEvents(matching: .mouseMoved) { (e:NSEvent) -> NSEvent in
        return e
    }
}



