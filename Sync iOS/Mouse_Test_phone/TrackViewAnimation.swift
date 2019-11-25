//
//  TrackViewAnimation.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2019/07/29.
//  Copyright © 2019 Namid. All rights reserved.
//

import Foundation
import UIKit

// return line path instance.
func createLinePath(x: CGFloat, y: CGFloat, toX: CGFloat, toY: CGFloat) -> UIBezierPath {
    let bezierPath = UIBezierPath()
    bezierPath.move(to: CGPoint(x: x, y: y))
    bezierPath.addLine(to: CGPoint(x: toX, y: toY))
    bezierPath.lineCapStyle = .round
    return bezierPath
}
