//
//  MenuItemManager.swift
//  Mouse_Test
//
//  Created by NH on 2020/02/10.
//  Copyright © 2020 Namid. All rights reserved.
//

import Cocoa

class MenuManager {
    let menu: NSMenu!
    let separeteItem = NSMenuItem.separator()
    let deviceItem = NSMenuItem()
    let ipItem = NSMenuItem()
    
    var CONNECT_FLAG = false
    
    init(menu: NSMenu) {
        self.menu = menu
        startUp()
    }
    
    private func startUp() -> Void {
        // アプリ終了ボタンを作成
        let quitItem = NSMenuItem()
        // 終了ボタンのテキスト
        quitItem.title = "Quit Application"
        // 終了ボタンをクリックした時の動作
        quitItem.action = #selector(AppDelegate.quit(_:))
        // 作成したボタンを追加
        menu.addItem(quitItem)
        
        let sendItem = NSMenuItem()
        sendItem.title = "Send"
        sendItem.action = #selector(AppDelegate.send(_:))
        menu.insertItem(sendItem, at: 1)
    }
    
    /// 接続先のデバイス情報を表示 ( on Menubar )
    func showConnectedDevice(reqData: JSONRequestData) -> Void {
        if CONNECT_FLAG {
            // すでに接続デバイスを表示してれば、一旦削除
            hiddenConnectedDevice()
        }
        CONNECT_FLAG = true
        
        self.menu.insertItem(separeteItem, at: 0)
        
        deviceItem.isEnabled = false
        deviceItem.title = "Device:" + reqData.device
        self.menu.insertItem(deviceItem, at: 0)
        
        ipItem.isEnabled = false
        ipItem.title = "IP:" + reqData.ip
        self.menu.insertItem(ipItem, at: 0)
    }
    
    
    /// 接続先のデバイス情報を削除
    func hiddenConnectedDevice() -> Void {
        CONNECT_FLAG = false
        
        self.menu.removeItem(separeteItem)
        self.menu.removeItem(deviceItem)
        self.menu.removeItem(ipItem)
    }
    
    
    
    @objc private func send(_ sender: Any) {
//        var value: Int = 10
//        let socket = socketManager.createSocket(callback: false)
//        var address = SocketManager.createAddress(port: PORT_NUMBER, ip: inet_addr(IP_ADDRESS))
//        SocketManager.send(sock: socket, socketAddress: &address, data: UnsafeMutablePointer<Int>(&value))
    }
    
    @objc private func quit(_ sender: Any) {
        // アプリケーションの終了
        NSApplication.shared.terminate(self)
    }
}
