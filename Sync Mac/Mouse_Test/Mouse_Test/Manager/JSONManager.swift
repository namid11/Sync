//
//  JSONManager.swift
//  Mouse_Test
//
//  Created by N2WH on 2018/12/27.
//  Copyright © 2018 Namid. All rights reserved.
//

import Foundation

struct JSONOperateData: Codable {
    var key: String
    var context: JSONOperateContext?
}

struct JSONOperateContext: Codable {
    var x: Float
    var y: Float
}

struct JSONRequestData: Codable {
    var ip: String
    var device: String
}

struct JSONResponseData: Codable {
    var ip: String
    var machineName: String
}
