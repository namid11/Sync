//
//  ViewController.swift
//  Mouse_Test_phone
//
//  Created by N2WH on 2018/12/25.
//  Copyright © 2018 Namid. All rights reserved.
//

import UIKit

class ViewController: UIViewController, CAAnimationDelegate {
    
    // ------------------------------- プロパティ群 -------------------------------- //
    
    let PORT_NUMBER: UInt16 = UInt16(8080)      // ポート番号
    let IP_ADDRESS: String = "127.0.0.1"    // IPアドレス
    
    var buffer_pos: CGPoint = CGPoint(x: 0, y: 0)
    
    var socket: CFSocket = createSocket()
    var address: sockaddr_in = sockaddr_in()
    
    var point_sensitivity = 1.0
    var scroll_sensitivity = 1.0
    var swipe_sensitivity = 1.0
    var sensitivities: [Float] = [1.0, 1.0, 1.0]
    
    var stack_point: CGPoint = CGPoint(x: 0, y:0)
    var first_point: CGPoint = CGPoint(x: 0, y: 0)
    var stack_path: UIBezierPath = UIBezierPath()
    
    @IBOutlet weak var TrackingView: UIView!
    
    // --------------------------------------------------------------------------- //
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        if let port_t = UserDefaults.standard.string(forKey: "port_num"),
            let ip = UserDefaults.standard.string(forKey: "ip_address"),
            let port = UInt16(port_t) {
            address = createAddress(port: port, ip: ip)
        } else {
            address = createAddress(port: PORT_NUMBER, ip: IP_ADDRESS)
        }
        
        // UserDefaultsの初期設定
        for (_,key) in USERDEFAULTS_KEYS.userDefaults_keys.enumerated() {
            if UserDefaults.standard.float(forKey: key) == 0 {
                UserDefaults.standard.set(1.0, forKey: key)
            }
        }

    }
    
    // --------------------------------------------------------------------------- //
    
    
    /*
     Called: 画面ロード時
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let doubleTap = UITapGestureRecognizer(target: self, action: #selector(tapDouble(sender:)))
        doubleTap.numberOfTapsRequired = 2
        TrackingView.addGestureRecognizer(doubleTap)
        
        let singleTap = UITapGestureRecognizer(target: self, action: #selector(tapSingle(sender:)))
        singleTap.numberOfTapsRequired = 1
        singleTap.require(toFail: doubleTap)
        TrackingView.addGestureRecognizer(singleTap)
        
        let twoFingersSingleTap = UITapGestureRecognizer(target: self, action: #selector(tapTwoFingersSingle(sender:)))
        twoFingersSingleTap.numberOfTapsRequired = 1
        twoFingersSingleTap.numberOfTouchesRequired = 2
        TrackingView.addGestureRecognizer(twoFingersSingleTap)
        
        let pinchGesture = UIPinchGestureRecognizer(target: self, action: #selector(pinchView(sender:)))
        TrackingView.addGestureRecognizer(pinchGesture)
        
        let panGesture = UIPanGestureRecognizer(target: self, action: #selector(panView(sender:)))
        panGesture.maximumNumberOfTouches = 1
        TrackingView.addGestureRecognizer(panGesture)
        
        let doublePanGesture = UIPanGestureRecognizer(target: self, action: #selector(panView_Double(sender:)))
        doublePanGesture.maximumNumberOfTouches = 3
        doublePanGesture.minimumNumberOfTouches = 2
        TrackingView.addGestureRecognizer(doublePanGesture)
        
        let leftDoubleSwipe = UISwipeGestureRecognizer(target: self, action: #selector(leftSwipe_double(sender:)))
        leftDoubleSwipe.direction = .left
        leftDoubleSwipe.numberOfTouchesRequired = 2
        //leftDoubleSwipe.require(toFail: pinchGesture)
        leftDoubleSwipe.require(toFail: doublePanGesture)
        TrackingView.addGestureRecognizer(leftDoubleSwipe)
        
        let rightDoubleSwipe = UISwipeGestureRecognizer(target: self, action: #selector(rightSwipe_double(sender:)))
        rightDoubleSwipe.direction = .right
        rightDoubleSwipe.numberOfTouchesRequired = 2
        //rightDoubleSwipe.require(toFail: pinchGesture)
        rightDoubleSwipe.require(toFail: doublePanGesture)
        TrackingView.addGestureRecognizer(rightDoubleSwipe)
        
        let app: UIApplication = UIApplication.shared
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(load),
                                               name: UIApplication.willEnterForegroundNotification,
                                               object: app)
    }
    
    /*
     ViewAppear
     */
    override func viewDidAppear(_ animated: Bool) {
        // 各モーションの感度を読み込み
        for (i,key) in USERDEFAULTS_KEYS.userDefaults_keys.enumerated() {
            sensitivities[i] = UserDefaults.standard.float(forKey: key)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        
    }
    
    // ステータスバーの文字色を白に
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return UIStatusBarStyle.lightContent
    }
    
    /*
     Called: アニメーション開始時
     */
    func animationDidStart(_ anim: CAAnimation) {
        
    }
    
    /*
     Called: アニメーション終了時
     */
    func animationDidStop(_ anim: CAAnimation, finished flag: Bool) {
        if flag, let layer = anim.value(forKey: "layer") as? CALayer {
            layer.removeFromSuperlayer()
        }
    }
    
    /*
     Called: タッチ開始時
     */
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let point: CGPoint = touches.first!.location(in: TrackingView)
        stack_point = point
        first_point = point
        
        // 初回タッチ時に送信
        if let json_data = createJSON(key: "first") {
            sendPacket(sock: socket, address: &address, data: json_data)
            print("touchesBegan")
        } else {
            print("Failed to create JSON file")
        }

    }
    
    /*
     Called: タッチ終了時
     */
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
//        super.touchesEnded(touches, with: event)
//        print("touchesEnded")
    }
    
    override func pressesEnded(_ presses: Set<UIPress>, with event: UIPressesEvent?) {
        print("pressesEnded")
    }
    
    /*
     Called: タッチ移動時
     */
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        let _: CGPoint = touches.first!.location(in: TrackingView)
        
//        if let e = event {
//            print("EventType:", e)
//        }
//        print("Positon:", point)
//        print("------------------")
        
        
    }
    
    /*
     レイヤー作成、描画、アニメーション
     */
//    func setLayer(p: CGPoint) {
//        let customLayer = CustomCALayer(color: UIColor(red: 90/255,
//                                                       green: 194/255,
//                                                       blue: 198/255,
//                                                       alpha: 1).cgColor,
//                                        rect: CGRect(origin: p, size: CGSize(width: 25, height: 25)))
//        // animation.delegateで取得できるように値をセットする
//        animation.setValue(customLayer, forKey: "layer")
//        TrackingView.layer.addSublayer(customLayer)
//
//        customLayer.add(self.animation, forKey: nil)
//
//        // 1秒後にやる処理
////        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
////
////
////        }
//    }
    
    func pathAnimation(path: UIBezierPath) -> CAShapeLayer {
        let ani_group = CAAnimationGroup();
        let animation = CABasicAnimation();
        animation.fillMode = .forwards
        animation.isRemovedOnCompletion = false
        animation.duration = 2.0
        animation.keyPath = "strokeEnd"
        animation.fromValue = 0.0
        animation.toValue = 1.0
        let animation2 = CABasicAnimation()
        animation2.keyPath = "opacity"
        animation2.beginTime = CACurrentMediaTime()+2.0
        animation2.fillMode = .forwards
        animation2.isRemovedOnCompletion = false
        animation2.duration = 1.0
        animation2.fromValue = 1.0
        animation2.toValue = 0.0
        ani_group.animations = [animation, animation2]
        
        let layer = CAShapeLayer()
        layer.frame = CGRect(origin: CGPoint(x: 0, y: 0), size: TrackingView.frame.size)
        layer.path = path.cgPath;
        layer.strokeColor = UIColor.cyan.cgColor
        layer.fillColor = UIColor.cyan.cgColor
        layer.lineWidth = 15
        layer.lineCap = .round
        layer.add(animation2, forKey: nil)
        return layer
    }
    
    /*
     Called: シングルタップ時
     */
    @objc func tapSingle(sender: UITapGestureRecognizer) {
        print("Single Tap")
        if let json_data = createJSON(key: "click") {
            sendPacket(sock: socket, address: &address, data: json_data)
        } else {
            print("Failed to create JSON file")
        }
    }
    
    /*
     Called: タブルタップ時
     */
    @objc func tapDouble(sender: UITapGestureRecognizer) {
        print("Double Tap")
        if let json_data = createJSON(key: "double") {
            sendPacket(sock: socket, address: &address, data: json_data)
        } else {
            print("Failed to create JSON file")
        }
    }
    
    /*
     Called: 二本指タップ
     */
    @objc func tapTwoFingersSingle(sender: UITapGestureRecognizer) {
        print("Single tap on two fingers.")
        if let json_data = createJSON(key: "two_fingers_click") {
            sendPacket(sock: socket, address: &address, data: json_data)
        } else {
            print("Failed to create JSON file")
        }
    }
    
    /*
     Called: ピンチ時(拡大、縮小等)
     */
    @objc func pinchView(sender: UIPinchGestureRecognizer) {
        print("pinch")
        // ピンチイン・ピンチアウトの拡大縮小率, 1秒あたりのピンチの速度(read-only)
        print("scale: \(sender.scale), velocity: \(sender.velocity)")
        
        if sender.state == UIGestureRecognizer.State.ended {
            sender.scale = -1.0
            print("pinch ended")
        }
        if let json_data = createJSON(key: "pinch", x: Float(sender.scale)) {
            sendPacket(sock: socket, address: &address, data: json_data)
        } else {
            print("Failed to create JSON file")
        }
    }
    
    /*
     Called: パン時(移動時)
     */
    @objc func panView(sender: UIPanGestureRecognizer) {
        let translation: CGPoint = sender.translation(in: TrackingView) // first point からの座標
        //let location = sender.location(in: TrackingView)                // viewからの絶対座標
        if let json_data = createJSON(key: "moved",
                                      x: Float(translation.x) * sensitivities[USERDEFAULTS_KEYS.POINT],
                                      y: Float(translation.y) * sensitivities[USERDEFAULTS_KEYS.POINT]) {
            sendPacket(sock: socket, address: &address, data: json_data)
        } else {
            print("Failed to create JSON file")
        }
        
        print("Event: Pan")
        print(String(format: "%f, %f | %f, %f", Float(stack_point.x), Float(stack_point.y), Float(first_point.x + translation.x), Float(first_point.y + translation.y)))
        let path: UIBezierPath = createLinePath(x: stack_point.x,
                                                y: stack_point.y,
                                                toX: first_point.x + translation.x,
                                                toY: first_point.y + translation.y)
        let ani_layer = pathAnimation(path: path)
        TrackingView.layer.addSublayer(ani_layer)
        
        stack_point = CGPoint(x: first_point.x + translation.x, y: first_point.y + translation.y)
    }
    
    // 2本指移動（スクロール）
    @objc func panView_Double(sender: UIPanGestureRecognizer) {
        print("double pan",
              sender.numberOfTouches,
              "translation:",
              sender.translation(in: TrackingView),
              "location:",
              sender.location(in: TrackingView),
              "velocity:",
              sender.velocity(in: TrackingView))
        
        var send_key: DoublePan = DoublePan.Scroll
        
        let velocity = sender.velocity(in: TrackingView)
        if abs(velocity.x) > abs(velocity.y) {
            /*
             xベクトルの速度のほうが大きい場合
             */
            if velocity.x * CGFloat(sensitivities[USERDEFAULTS_KEYS.SWIPE]) > 2000.0 {
                /*
                 一定値より大きければブラウザバックとして判断
                 */
                send_key = DoublePan.BrowserBack
            } else if velocity.x * CGFloat(sensitivities[USERDEFAULTS_KEYS.SWIPE]) < -2000.0 {
                /*
                 一定値より小さければブラウザフォワードとして判断
                 */
                send_key = DoublePan.BrowserForward
            } else {
                
            }
        }
        if let json_data = createJSON(key: "scroll",
                                      x: Float(sender.velocity(in: TrackingView).x) * sensitivities[USERDEFAULTS_KEYS.SCROLL],
                                      y: Float(sender.velocity(in: TrackingView).y) * sensitivities[USERDEFAULTS_KEYS.SCROLL]) {
            sendPacket(sock: socket, address: &address, data: json_data)
        } else {
            print("Failed to create JSON file")
        }
        
        if sender.state == UIGestureRecognizer.State.ended {
            /*
             指が離れた時にブラウザバック(フォワード)のフラグが立っていたか確認
             */
            var key_text = ""
            switch send_key {
            case DoublePan.Scroll:
                break
            case DoublePan.BrowserBack:
                key_text = "browser_back"
            case DoublePan.BrowserForward:
                key_text = "browser_forward"
            }
            
            if let json_data = createJSON(key: key_text) {
                sendPacket(sock: socket, address: &address, data: json_data)
            } else {
                print("Failed to create JSON file")
            }
        }
    }
    
    
    /*
     Called: レフトスワイプ時
     */
    @objc func leftSwipe_double(sender: UISwipeGestureRecognizer) {
        print("leftSwipe_double")
    }
    
    /*
     Called: ライトスワイプ時
     */
    @objc func rightSwipe_double(sender: UISwipeGestureRecognizer) {
        print("rightSwipe_double")
    }
    
    /*
     Called: セッティングボタンイベント
     */
    @IBAction func putSetting(_ sender: Any) {
        
    }
    
    
    @objc func load() {
        socket = createSocket()
    }
}


