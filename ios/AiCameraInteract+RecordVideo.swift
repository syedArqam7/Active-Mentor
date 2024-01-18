//
//  AiCameraInteract+RecordVideo.swift
//  jogo_test_flow
//
//  Created by Mac Book on 09/07/2020.
//

import Foundation
import AVFoundation
import VideoScreenRecorder

extension AiCameraInteract {
  public func startScreenRecording(fileName:String){
    ScreenRecorder.shared.startRecording(with: fileName, windowsToSkip: nil) { url, error in
      DispatchQueue.main.async {
        if let error = error {
          Utility.main.showAlert(message: error.localizedDescription, title: Strings.ALERT.text)
        }
        // use url
      }
    }
  }
  public func stopScreenRecording(){
    ScreenRecorder.shared.stopRecording { url, error in
      DispatchQueue.main.async {
        if let error = error {
          Utility.main.showAlert(message: error.localizedDescription, title: Strings.ALERT.text)
        }
        // use url
        if let url = url {
          UISaveVideoAtPathToSavedPhotosAlbum(url.path,self,#selector(self.video(_:didFinishSavingWithError:contextInfo:)),nil)
        }
      }
    }
  }
  @objc func video(_ videoPath: String, didFinishSavingWithError error: Error?, contextInfo info: AnyObject) {
      let title = (error == nil) ? "Success" : "Error"
      let message = (error == nil) ? "Video was saved" : "Video failed to save"

    Utility.main.showAlert(message: message, title: title)
  }
}



