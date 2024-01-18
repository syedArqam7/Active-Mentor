//
//  AiCameraInteract+Extension.swift
//  jogo_test_flow
//
//  Created by Mac Book on 09/07/2020.
//

import Foundation
import AVFoundation

// AVCaptureVideoDataOutputSampleBufferDelegate protocol and related methods
extension AiCameraInteract:  AVCaptureVideoDataOutputSampleBufferDelegate{
  func setupAVCapture(){
    session.sessionPreset = AVCaptureSession.Preset.high
    guard let device = AVCaptureDevice.default(AVCaptureDevice.DeviceType.builtInWideAngleCamera,
                                               for: .video,
                                               position: AVCaptureDevice.Position.front) else{return}
    self.captureDevice = device
    self.beginSession()
    self.doneSettingUp = true
    
    let fileName = Utility.main.randomString(length: 10)
    self.startScreenRecording(fileName: fileName)
  }
  func beginSession(){
    var deviceInput: AVCaptureDeviceInput!
    do {
      deviceInput = try AVCaptureDeviceInput(device: captureDevice)
      guard deviceInput != nil else {
        print("error: cant get deviceInput")
        return
      }
      
      if self.session.canAddInput(deviceInput){
        self.session.addInput(deviceInput)
      }
      
      videoDataOutput = AVCaptureVideoDataOutput()
      videoDataOutput.alwaysDiscardsLateVideoFrames=true
      videoDataOutputQueue = DispatchQueue(label: "VideoDataOutputQueue")
      videoDataOutput.setSampleBufferDelegate(self, queue:self.videoDataOutputQueue)
      
      if session.canAddOutput(self.videoDataOutput){
        session.addOutput(self.videoDataOutput)
      }
      
      videoDataOutput.connection(with: AVMediaType.video)?.isEnabled = true
      
      self.previewLayer = AVCaptureVideoPreviewLayer(session: self.session)
      self.previewLayer.videoGravity = .resizeAspectFill
      
      let rootLayer: CALayer = self.cameraView.layer
      rootLayer.masksToBounds = true
      self.previewLayer.frame = self.view.frame//rootLayer.bounds
      rootLayer.addSublayer(self.previewLayer)
      session.startRunning()
    } catch let error as NSError {
      deviceInput = nil
      print("error: \(error.localizedDescription)")
    }
  }
  func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
    currentFrame =   self.convertImageFromCMSampleBufferRef(sampleBuffer)
  }
  // clean up AVCapture
  func stopCamera(){
    self.session.stopRunning()
    self.doneSettingUp = false
  }
  func convertImageFromCMSampleBufferRef(_ sampleBuffer:CMSampleBuffer) -> CIImage{
    let pixelBuffer: CVPixelBuffer = CMSampleBufferGetImageBuffer(sampleBuffer)!
    let ciImage:CIImage = CIImage(cvImageBuffer: pixelBuffer)
    return ciImage
  }
}
extension AiCameraInteract{
  private func recordVideo(){
    
  }
}
