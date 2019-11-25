//
//  SocketManager.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2018/12/25.
//  Copyright © 2018 Namid. All rights reserved.
//

import Foundation


/*
 ソケット作成メソッド
 */
func createSocket() -> CFSocket {
    return CFSocketCreate(kCFAllocatorDefault, AF_INET, SOCK_DGRAM, IPPROTO_UDP, 0, nil, nil)
}


/*
 アドレス作成メソッド
 */
func createAddress(port: UInt16, ip: String) -> sockaddr_in {
    var address = sockaddr_in()
    address.sin_len = __uint8_t(MemoryLayout<sockaddr_in>.size)
    address.sin_family = sa_family_t(AF_INET)
    address.sin_addr.s_addr = inet_addr(ip)
    address.sin_port = in_port_t(port.bigEndian)
    return address
}


/*
 UDP送信メソッド
 */
func sendPacket(sock:CFSocket, address:UnsafeMutablePointer<sockaddr_in>, data: Data) {
    
    CFSocketSendData(sock,
                     NSData(bytes: address, length: MemoryLayout<sockaddr_in>.size) as CFData,
                     data as CFData,
                     5)
}
