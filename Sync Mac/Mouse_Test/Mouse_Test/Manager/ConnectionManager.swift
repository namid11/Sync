//
//  ConnectionManager.swift
//  Mouse_Test
//
//  Created by NH on 2020/02/10.
//  Copyright © 2020 Namid. All rights reserved.
//

import Cocoa

class ConnectionManager {
    
    let socketManager = SocketManager()
    
    func receiveConnectionRequest(rcvCallback: @escaping((JSONRequestData) -> Void)) -> Void {
        let socket = socketManager.createReceiveSocket(callback: {(data: CFData) in
            // --- callback --- //
            let nDeviceData: JSONRequestData? =  self.takeRequestParams(data: data as Data)
            if let deviceData = nDeviceData {
                // この段階で、受信処理は成功
                DispatchQueue.main.async {  // mainスレッド処理
                    rcvCallback(deviceData)
                }
                
                do {
                    // レスポンス処理（TCPでソケット通信）
                    let responseData = try JSONEncoder().encode(
                        JSONResponseData(
                            ip: self.findSameNetwork(targetIp: deviceData.ip) ?? "NULL",
                            machineName: Host.current().localizedName ?? "NULL"
                        )
                    )
                    let socket = self.socketManager.createSendSocket()
                    var address = SocketManager.createAddress(port: UInt16(3333), ip: inet_addr(deviceData.ip))
                    CFSocketConnectToAddress(socket, NSData(bytes: &address, length: MemoryLayout<sockaddr_in>.size) as CFData, 0)
                    print("same address:" + (self.findSameNetwork(targetIp: deviceData.ip) ?? "NULL"))
                    SocketManager.send(sock: socket, socketAddress: &address, data: responseData)
                } catch let e {
                    print(e)
                }
            }
            
            
        })
        var address = SocketManager.createAddress(port: 8081)
        SocketManager.setAddress(sock: socket, socketAddress: &address)
        SocketManager.runLoopSocket(sock: socket)
    }
    
    private func takeRequestParams(data: Data) -> JSONRequestData? {
        do {
            let jsonData: JSONRequestData = try JSONDecoder().decode(JSONRequestData.self, from: data)
            print("ip:" + jsonData.ip, "device:", jsonData.device)
            return jsonData
        } catch let e {
            print(e)
        }
    
        return nil
    }
    
    /// クライアントのipアドレスと同じネットワークのipアドレスを検索
    private func findSameNetwork(targetIp: String) -> String? {
        for ipAddr in findUseAddress() {
            let serverAddress: [UInt8] = ipAddrToBytes(ip: ipAddr)
            let clientAddress: [UInt8] = ipAddrToBytes(ip: targetIp)
            let subnetmask: [UInt8] = ipAddrToBytes(ip: "255.255.255.0")
            var addressFlag = false
            for i in 0..<subnetmask.count {
                if ((clientAddress[i] & subnetmask[i]) == (serverAddress[i] & subnetmask[i])) {
                    if (i == subnetmask.count - 1) {
                        addressFlag = true
                    } else {
                        continue
                    }
                    
                } else {
                    break
                }
            }
            
            if (addressFlag) {
                return ipAddr
            }
        }
        return nil
    }
}
