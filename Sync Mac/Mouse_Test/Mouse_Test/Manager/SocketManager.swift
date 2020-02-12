//
//  SocketManager.swift
//  Mouse_Test
//
//  Created by N2WH on 2018/12/24.
//  Copyright © 2018 Namid. All rights reserved.
//

import Foundation
import Cocoa



class SocketContext { // info に渡すデータ構造(今回はクラス)
    var onData: ((CFData) -> Void)?
}


class SocketManager {
    
    //static var thread = Thread.init()
    var socketContext: SocketContext = SocketContext()
    
    init() {
        // ソケット情報のメソッド設定
//        socketContext.onData = { (data: CFData) in
//            self.operateRequestManager.operate(data: data as Data)
//
////            DispatchQueue.main.async {
////
////            }
//        }
    }
    
    
    /// ソケット作成メソッド ( for receive )
    func createReceiveSocket(callback: @escaping ((CFData) -> Void) = { (data: CFData) in return }) -> CFSocket {
//        if !callback {
//            return CFSocketCreate(nil, AF_INET, SOCK_DGRAM, IPPROTO_UDP, 0, nil, nil)
//        }
        
        socketContext.onData = callback
        
        var cfSocketContext = CFSocketContext(version: 0,
                                              info: &socketContext,
                                              retain: nil,
                                              release: nil,
                                              copyDescription: nil)
        
        func callout(sock: CFSocket?, callbackType: CFSocketCallBackType, address: CFData?, _data: UnsafeRawPointer?, _info: UnsafeMutableRawPointer?) -> Void {
            switch callbackType {
            case .dataCallBack:
                let info = _info!.assumingMemoryBound(to: SocketContext.self).pointee
                let data = Unmanaged<CFData>.fromOpaque(_data!).takeUnretainedValue()
                info.onData?(data)
                break;
            default:
                break;
            }
        }
        
        return CFSocketCreate(kCFAllocatorDefault, AF_INET, SOCK_DGRAM, IPPROTO_UDP,
                              CFSocketCallBackType.dataCallBack.rawValue, callout, &cfSocketContext)
    }
    
    /// ソケット作成メソッド ( for send )
    func createSendSocket() -> CFSocket {
        return CFSocketCreate(nil, AF_INET, SOCK_STREAM, IPPROTO_TCP, 0, nil, nil)
    }
    
    
    /// アドレス作成メソッド
    // INADDR_ANYをipに設定するとPC内の全てIPに対して受信する
    static func createAddress(port:UInt16, ip:in_addr_t = in_addr_t(INADDR_ANY)) -> sockaddr_in {
        var socketAddress = sockaddr_in()
        socketAddress.sin_len = __uint8_t(MemoryLayout<sockaddr_in>.size)
        socketAddress.sin_addr.s_addr = ip
        socketAddress.sin_port = in_port_t(port.bigEndian)
        socketAddress.sin_family = sa_family_t(AF_INET)
        return socketAddress
    }
    
    
    
    /// アドレス情報付与メソッド
    static func setAddress(sock:CFSocket , socketAddress: UnsafeMutablePointer<sockaddr_in>) {
        CFSocketSetAddress(sock,
                           NSData(bytes: socketAddress, length: MemoryLayout<sockaddr_in>.size) as CFData)
    }
    
    
    /// パケット送信
    static func send(sock: CFSocket, socketAddress: UnsafeMutablePointer<sockaddr_in>, data: Data) {
        CFSocketSendData(sock,
                         NSData(bytes: socketAddress, length: MemoryLayout<sockaddr_in>.size) as CFData,
                         NSData.init(data: data) as CFData,
                         0)
        CFSocketInvalidate(sock)
    }
    

    /// 受信処理実行
    static func runLoopSocket(sock:CFSocket) {
        let thread = Thread.init {
            CFRunLoopAddSource(CFRunLoopGetCurrent(),
                               CFSocketCreateRunLoopSource(nil, sock, 1),
                               .defaultMode)
            
            RunLoop.current.run()
        }
        
        thread.start()
    }
}


enum PROTOCOL {
    case TCP, UDP
}
