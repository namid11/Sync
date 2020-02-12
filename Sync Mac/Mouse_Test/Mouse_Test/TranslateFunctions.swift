//
//  TranslateFunctions.swift
//  Mouse_Test
//
//  Created by N2WH on 2018/12/16.
//  Copyright © 2018 Namid. All rights reserved.
//

import Foundation


func cursorPointTrance(cursor: NSPoint, win: NSPoint) -> NSPoint {
    return NSPoint(x: win.x + cursor.x, y: win.y + cursor.y)
}
