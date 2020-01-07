//
//  JSONManager.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2018/12/27.
//  Copyright © 2018 Namid. All rights reserved.
//

import Foundation

func createJSON(key: String, x: Float? = nil, y: Float? = nil) -> Data? {
    var data_dic = Dictionary<String, Any>()
    data_dic["key"] = key
    
    if let _ = x, let _ = y {
        // x, y が指定されている場合
        do {
            data_dic["context"] = ["x": x, "y": y]
            let json_data: Data = try JSONSerialization.data(withJSONObject: data_dic, options: [])
            return json_data
        } catch let e {
            print(e)
        }
    } else if let _ = x {
        // xのみの場合
        do {
            data_dic["context"] = ["value": x]
            let json_data: Data = try JSONSerialization.data(withJSONObject: data_dic, options: [])
            return json_data
        } catch let e {
            print(e)
        }
    } else {
        // x, y が指定されていない場合
        do {
            let json_data: Data = try JSONSerialization.data(withJSONObject: data_dic, options: [])
            return json_data
        } catch let e {
            print(e)
        }
    }
    
    return nil
}


struct Section: Codable {
    var GroupNum: Int
    var IPEndPoint: S_IPEndPoint
    var Control: S_Control
}

struct S_IPEndPoint: Codable {
    var ip_address: String?
    var post_number: Int?
}

struct S_Control: Codable {
    var sensitivity: Float?
}
