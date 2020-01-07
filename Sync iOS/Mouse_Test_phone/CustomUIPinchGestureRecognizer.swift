//
//  CustomUIPinchGestureRecognizer.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2019/05/12.
//  Copyright © 2019 Namid. All rights reserved.
//

import UIKit

class CustomUIPinchGestureRecognizer: UIPinchGestureRecognizer {
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent) {
        print("touchesEnded_")
    }
}
