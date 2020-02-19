//
//  PresentationOperateManager.swift
//  Mouse_Test
//
//  Created by NH on 2020/02/11.
//  Copyright © 2020 Namid. All rights reserved.
//

import Cocoa

class PresentationOperateManager {
    
    let pointerWindow: NSWindow = NSWindow.init(
        contentRect: NSMakeRect(0, 0, 30, 30),
        styleMask: NSWindow.StyleMask.borderless,
        backing: NSWindow.BackingStoreType.buffered,
        defer: true
    )
    let laserImageView = NSImageView(image: NSImage(named: NSImage.Name("LaserPointer")) ?? NSImage())
    
    
    init() {
        // show Pointer
        pointerWindow.isOpaque = false
        pointerWindow.backgroundColor = NSColor(cgColor: CGColor(red: 1, green: 1, blue: 1, alpha: 0))
        laserImageView.imageFrameStyle = .none
        laserImageView.frame = pointerWindow.frame
        laserImageView.imageScaling = .scaleAxesIndependently
        pointerWindow.contentView?.addSubview(laserImageView)
        
//        pointerWindow.setIsVisible(false)
    }
    
    // レーザーポインターを移動
    func move(point: CGPoint) {
        pointerWindow.setFrameOrigin(revercePoint(point: point))
    }
    
    /// show laserpointer
    func show() {
        pointerWindow.orderFrontRegardless()
        pointerWindow.level = .popUpMenu
        pointerWindow.setFrameOrigin(NSEvent.mouseLocation)
        // カーソル隠す
        NSCursor.hide()
    }
    
    /// hide laserpointer
    func hide() {
        pointerWindow.orderOut(NSWorkspace().frontmostApplication!)
        // カーソル表示
        NSCursor.unhide()
    }
    
    func isVisible() -> Bool {
        return pointerWindow.isVisible
    }
    
    func getPoint() -> CGPoint {
        return pointerWindow.frame.origin
    }
    
    /// y軸がViewにおいて逆なので、変換用の関数
    private func revercePoint(point: CGPoint) -> CGPoint {
        return CGPoint(x: point.x, y: point.y)
    }
}
