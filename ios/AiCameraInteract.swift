//
//  AiCameraInteract.swift
//  jogo_test_flow
//
//  Created by Mac Book on 06/07/2020.
//

import UIKit
import AVFoundation

class AiCameraInteract: UIViewController {
    
    @IBOutlet weak var cameraView: UIView!
    @IBOutlet weak var lblTime: UILabel!
    
  
  
    //  Camera Capture required properties
    var videoDataOutput: AVCaptureVideoDataOutput!
    var videoDataOutputQueue: DispatchQueue!
    var previewLayer:AVCaptureVideoPreviewLayer!
    var captureDevice : AVCaptureDevice!
    let session = AVCaptureSession()
    var currentFrame: CIImage!
    var doneSettingUp = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.setupAVCapture()
    }
    override func viewWillAppear(_ animated: Bool) {
        
    }
    
    @IBAction func onBtnStop(_ sender: UIButton) {
      self.stopScreenRecording()
    }
}

extension AiCameraInteract{
    private func dismissToReactNative(){
        if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
            appDelegate.dismissToReactNative()
        }
    }
  private func startCameraSession(){
    if !self.doneSettingUp {
      self.session.startRunning()
    }
  }
}
