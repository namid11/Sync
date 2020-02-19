//
//  OperateRequestManager.swift
//  Mouse_Test
//
//  Created by NH on 2020/02/08.
//  Copyright © 2020 Namid. All rights reserved.
//

import Foundation
import Cocoa

class OperateReuqestmanager {
    
    var buffer_location: CGPoint = CGPoint(x: 0, y: 0)
    let display_w: CGFloat!
    let display_h: CGFloat!
    
    var presenOprtManager: PresentationOperateManager = PresentationOperateManager()
    
    init() {
        display_w = NSScreen.main?.frame.width ?? 0.0
        display_h = NSScreen.main?.frame.height ?? 0.0
    }
    
    func operate(data: Data) -> Void {
        do {
            let json_struct: JSONOperateData = try JSONDecoder().decode(JSONOperateData.self, from: data)
            
            switch json_struct.key {
            case "moved":
                print("recieve key: 'moved'")
                guard let context = json_struct.context else {
                    print("Not context data")
                    return
                }
                
                if (!presenOprtManager.isVisible()) {
                    // Trackpadモード
                    let point = cursorPos(now: self.buffer_location,
                    add: CGPoint(x: CGFloat(context.x),
                                 y: CGFloat(context.y)))
                    let event = CGEvent(mouseEventSource: nil,
                                        mouseType: CGEventType.mouseMoved,
                                        mouseCursorPosition: point,
                                        mouseButton: CGMouseButton(rawValue: UInt32(3))!)
                    if let e = event {
                        e.post(tap: CGEventTapLocation.cghidEventTap)
                    } else {
                        print("failed creating event")
                    }
                } else {
                    // Presentationモード
                    let point = cursorPos(now: self.buffer_location,
                                          add: CGPoint(x: CGFloat(context.x), y: CGFloat(-context.y)))
                    presenOprtManager.move(point: point)
                }

                break
                
            case "first":
                print("recieve key: 'first'")
                if (presenOprtManager.isVisible()) {
                    self.buffer_location = presenOprtManager.getPoint()
                } else {
                    self.buffer_location = CGPoint(x: NSEvent.mouseLocation.x, y: self.display_h - NSEvent.mouseLocation.y)
                }
                
                break
                
            case "click":
                print("recieve key: 'click'")
                let event = CGEvent(mouseEventSource: nil, mouseType: .leftMouseDown, mouseCursorPosition: self.buffer_location, mouseButton: CGMouseButton.left)
                let event2 = CGEvent(mouseEventSource: nil, mouseType: .leftMouseUp, mouseCursorPosition: self.buffer_location, mouseButton: CGMouseButton.left)
                if let e = event, let e2 = event2 {
                    e.post(tap: .cghidEventTap)
                    e2.post(tap: .cghidEventTap)
                } else {
                    print("failed creating event")
                }
                break
                
            case "two_fingers_click":
                print("recieve key: 'double'")
                let event = CGEvent(mouseEventSource: nil, mouseType: .rightMouseDown, mouseCursorPosition: self.buffer_location, mouseButton: CGMouseButton.right)
                let event2 = CGEvent(mouseEventSource: nil, mouseType: .rightMouseUp, mouseCursorPosition: self.buffer_location, mouseButton: CGMouseButton.right)
                if let e = event, let e2 = event2 {
                    e.post(tap: .cghidEventTap)
                    e2.post(tap: .cghidEventTap)
                } else {
                    print("failed creating event")
                }
                
                break
            case "shake":
                let process = Process()
                process.launchPath = "/usr/bin/open"
                process.arguments = ["/Applications/Safari.app"]
                process.launch()
//                let event_cmd = CGEvent(keyboardEventSource: nil, virtualKey: 0x37, keyDown: true)    // 'left control'
//                let event_forward = CGEvent(keyboardEventSource: nil, virtualKey: 0x7E, keyDown: true)   // 'up arrow'
//                let event_cmd_f = CGEvent(keyboardEventSource: nil, virtualKey: 0x37, keyDown: false)
//                let event_forward_f = CGEvent(keyboardEventSource: CGEventSource(event: event_cmd_f), virtualKey: 0x7E, keyDown: false)
//                if let e_cmd = event_cmd, let e_forward = event_forward, let e_cmd_f = event_cmd_f, let e_forward_f = event_forward_f {
//                    e_forward.flags = .maskControl
//                    e_forward.post(tap: CGEventTapLocation.cghidEventTap)
//                    e_cmd_f.post(tap: .cghidEventTap)
//                    event_forward_f?.post(tap: .cghidEventTap)
//                    print("shake")
//                }
                break
                
            case "scroll":
                print("recieve key: 'scroll'")
                
                guard let context = json_struct.context else {
                    print("Not context data")
                    return
                }
                // 1 for Y-only, 2 for Y-X, 3 for Y-X-Z
                let event = CGEvent(scrollWheelEvent2Source: nil,
                                    units: CGScrollEventUnit.pixel,
                                    wheelCount: CGWheelCount(2),
                                    wheel1: Int32(context.y / 10),
                                    wheel2: Int32(context.x / 10),
                                    wheel3: Int32(0))
                if let e = event {
                    e.post(tap: CGEventTapLocation.cghidEventTap)
                } else {
                    print("failed creating event")
                }
                break
                
                
            case "browser_back":
                let event_cmd = CGEvent(keyboardEventSource: nil, virtualKey: 0x37, keyDown: true)    // 'left cmd'
                let event_forward = CGEvent(keyboardEventSource: nil, virtualKey: 0x7B, keyDown: true)   // 'left arrow'
                let event_cmd_f = CGEvent(keyboardEventSource: nil, virtualKey: 0x37, keyDown: false)
                let event_forward_f = CGEvent(keyboardEventSource: CGEventSource(event: event_cmd_f), virtualKey: 0x7B, keyDown: false)
                if let e_cmd = event_cmd, let e_forward = event_forward, let e_cmd_f = event_cmd_f, let e_forward_f = event_forward_f {
                    e_forward.flags = .maskCommand
                    e_forward.post(tap: CGEventTapLocation.cghidEventTap)
                    e_cmd_f.post(tap: .cghidEventTap)
                    event_forward_f?.post(tap: .cghidEventTap)
                    print("browser_back")
                } else {
                    print("failed creating event")
                }
                break
                
            case "browser_forward":
                let event_cmd = CGEvent(keyboardEventSource: nil, virtualKey: 0x37, keyDown: true)    // 'left cmd'
//                event_cmd!.post(tap: CGEventTapLocation.cghidEventTap)
                let event_forward = CGEvent(keyboardEventSource: nil, virtualKey: 0x7C, keyDown: true)   // 'right arrow'
//                event_forward!.post(tap: .cghidEventTap)
                let event_cmd_f = CGEvent(keyboardEventSource: nil, virtualKey: 0x37, keyDown: false)
//                event_cmd_f!.post(tap: .cghidEventTap)
                let event_forward_f = CGEvent(keyboardEventSource: CGEventSource(event: event_cmd_f), virtualKey: 0x7C, keyDown: false)
//                event_forward_f!.post(tap: .cghidEventTap)
                if let e_cmd = event_cmd, let e_forward = event_forward, let e_cmd_f = event_cmd_f, let e_forward_f = event_forward_f {
                    e_forward.flags = .maskCommand
                    e_forward.post(tap: .cghidEventTap)
                
                    e_cmd_f.post(tap: .cghidEventTap)
                    e_forward_f.post(tap: .cghidEventTap)
                    print("browser_foward")
                } else {
                    print("failed creating event")
                }
                break
                
            case "pp_laser":
                if (presenOprtManager.isVisible()) {
                    presenOprtManager.hide()
                } else {
                    presenOprtManager.show()
                }

                break
                
            case "pp_next":
                let event_forward = CGEvent(keyboardEventSource: nil, virtualKey: 0x7C, keyDown: true)   // 'left arrow'
                let event_forward_f = CGEvent(keyboardEventSource: nil, virtualKey: 0x7C, keyDown: false)   // 'left arrow'
                if let e_forward = event_forward, let e_forward_f = event_forward_f {
                    e_forward.post(tap: CGEventTapLocation.cghidEventTap)
                    e_forward_f.post(tap: CGEventTapLocation.cghidEventTap)
                }
                break
                
            case "pp_back":
                let event_forward = CGEvent(keyboardEventSource: nil, virtualKey: 0x7B, keyDown: true)   // 'left arrow'
                let event_forward_f = CGEvent(keyboardEventSource: nil, virtualKey: 0x7B, keyDown: false)   // 'left arrow'
                if let e_forward = event_forward, let e_forward_f = event_forward_f {
                    e_forward.post(tap: CGEventTapLocation.cghidEventTap)
                    e_forward_f.post(tap: CGEventTapLocation.cghidEventTap)
                }
                break
                
            default:
                break
            }
        } catch let e {
            print(e)
        }
    }
    
    func cursorPos(now: CGPoint, add: CGPoint) -> CGPoint {
        var x = now.x + add.x
        var y = now.y + add.y
        if x < 0 {
            x = 0
        }
        if x > display_w {
            x = display_w
        }
        if y < 0 {
            y = 0
        }
        if y > display_h - 0.00390625 {
            y = display_h - 0.00390625
        }
        return CGPoint(x: x, y: y)
    }
}
