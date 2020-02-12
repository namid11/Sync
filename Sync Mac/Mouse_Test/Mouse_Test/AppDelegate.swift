//
//  AppDelegate.swift
//  Mouse_Test
//
//  Created by N2WH on 2018/12/16.
//  Copyright © 2018 Namid. All rights reserved.
//


import Cocoa
import SystemConfiguration


@NSApplicationMain
class AppDelegate: NSObject, NSApplicationDelegate, NSGestureRecognizerDelegate {

    @IBOutlet weak var menu: NSMenu!

    let PORT_NUMBER: UInt16 = UInt16(8080)
    let IP_ADDRESS: String = "127.0.0.1"
    let socketManager = SocketManager()
    let connectionManager = ConnectionManager()
    let operateRequestManager: OperateReuqestmanager = OperateReuqestmanager()
    var menuManager:MenuManager!
    
    // メニューバーに表示されるアプリケーションを作成
    let statusItem = NSStatusBar.system.statusItem(withLength: NSStatusItem.variableLength)

    func applicationDidFinishLaunching(_ aNotification: Notification) {
        // Insert code here to initialize your application
        
        // メニューバーに表示されるアプリ。今回は文字列で設定
        //self.statusItem.button?.title = "Sample"
        // メニューのハイライトモードの設定
        self.statusItem.button?.highlight(false)
        self.statusItem.button?.image = NSImage(named: NSImage.Name("MenuIcon"))
        self.statusItem.button?.imagePosition = .imageOverlaps
        self.statusItem.button?.imageScaling = .scaleProportionallyDown
        self.statusItem.button?.imageHugsTitle = true
        
        // メニューの指定
        self.statusItem.menu = menu
        
        menuManager = MenuManager(menu: self.menu)
        
        
        // アクセシビリティをチェック
        if !AXIsProcessTrusted() {
            let alert = NSAlert.init()
            alert.alertStyle = .warning
            alert.addButton(withTitle: "OK")
            alert.window.title = "Warning"
            alert.messageText = "アクセシビリティを許可してください"
            alert.runModal()
        }
        
        
        // グローバルホック&ローカルホックをセット
        hock()
        
        // --ソケット作成--
        let socket = socketManager.createReceiveSocket(callback: { (data: CFData) in
            DispatchQueue.main.async {   // mainスレッド実行
                // コールバック処理
                self.operateRequestManager.operate(data: data as Data)
            }
        })
        var address = SocketManager.createAddress(port: PORT_NUMBER)
        SocketManager.setAddress(sock: socket, socketAddress: &address)
        SocketManager.runLoopSocket(sock: socket)
        
        
        // -- 接続受付オペレーション --
        connectionManager.receiveConnectionRequest { (reqData:JSONRequestData) in
            // - データ取得時のコールバック -
            self.menuManager.showConnectedDevice(reqData: reqData)  // menuItemを更新
            // アラート
            let alert = NSAlert.init()
            alert.alertStyle = .informational
            alert.addButton(withTitle: "OK")
            alert.window.title = "Success Connecting"
            alert.messageText = "'\(reqData.device)'と接続されました"
            alert.runModal()
        }
        
    }

    
    
    
    @IBAction func settingMenuButton(_ sender: Any) {
//        let event = CGEvent(mouseEventSource: nil, mouseType: CGEventType.mouseMoved, mouseCursorPosition: CGPoint(x: 234, y: 133), mouseButton: CGMouseButton(rawValue: UInt32(3))!)
//        event!.post(tap: CGEventTapLocation.cghidEventTap)
//        let event = CGEvent(scrollWheelEvent2Source: nil, units: .pixel, wheelCount: UInt32(30), wheel1: Int32(10), wheel2: Int32(10), wheel3: Int32(10))
//        event?.post(tap: .cghidEventTap)
        let event = CGEvent(keyboardEventSource: nil, virtualKey: CGKeyCode.init(bitPattern: 55), keyDown: true)
        event?.post(tap: .cghidEventTap)
        
    }
    
    @objc func send(_ sender: Any) {
//        var value: Int = 10
//        let socket = socketManager.createSocket(callback: false)
//        var address = SocketManager.createAddress(port: PORT_NUMBER, ip: inet_addr(IP_ADDRESS))
//        SocketManager.send(sock: socket, socketAddress: &address, data: UnsafeMutablePointer<Int>(&value))
    }
    
    @objc func quit(_ sender: Any) {
        // アプリケーションの終了
        NSApplication.shared.terminate(self)
    }
    
    func applicationWillTerminate(_ aNotification: Notification) {
        // Insert code here to tear down your application
    }
    
    
    func gestureRecognizer(_ gestureRecognizer: NSGestureRecognizer, shouldAttemptToRecognizeWith event: NSEvent) -> Bool {
        print(event)
        return true
    }
    
}

    
