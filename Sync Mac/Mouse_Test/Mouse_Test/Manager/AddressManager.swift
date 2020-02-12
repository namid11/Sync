//
//  AddressManager.swift
//  Mouse_Test
//
//  Created by NH on 2020/02/10.
//  Copyright © 2020 Namid. All rights reserved.
//

import Cocoa

func findUseAddress() -> Array<String> {
    var array = Array<String>()
    let addresses = Host.current().addresses
    for addr in addresses {
        if (addr.split(separator: ".").count != 1 && !addr.starts(with: "127.")) {
            array.append(addr)
        }
    }
    
    return array
}


func ipAddrToBytes(ip: String) -> Array<UInt8> {
    var vs = Array<UInt8>()
    let ipSplit = ip.components(separatedBy: ".")
    for i in 0..<ipSplit.count {
        vs.append(UInt8(Int(ipSplit[i])!))
    }
    
    return vs
}
