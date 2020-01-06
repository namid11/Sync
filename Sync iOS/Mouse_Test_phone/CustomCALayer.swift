//
//  CustomCALayer.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2018/12/25.
//  Copyright © 2018 Namid. All rights reserved.
//

import UIKit

class CustomCALayer: CALayer {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    init(color: CGColor, rect: CGRect) {
        super.init()
        self.backgroundColor = color
        self.frame = rect
        self.masksToBounds = true
        self.cornerRadius = rect.width / 2
    }
    
}
