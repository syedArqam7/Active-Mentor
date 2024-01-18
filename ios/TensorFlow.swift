//
//  TensorFlow.swift
//  NATIVE_AI
//
//  Created by Mac Book on 14/07/2020.
//

import UIKit

class TensorFlow: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func onBtnObjectDetectionTensorFlow(_ sender: UIButton) {
      self.presentObjectDetectionTensorFlow()
    }
    @IBAction func onBtnPoseEstimationTensorFlow(_ sender: UIButton) {
      self.presentPoseEstimationTensorFlow()
    }
}
//MARK:- Helper methods
extension TensorFlow{
  private func presentObjectDetectionTensorFlow(){
    let storyboard = UIStoryboard(name: "Main", bundle: nil)
    let controller = storyboard.instantiateViewController(withIdentifier: "TensorFlowObjectDetection") as! TensorFlowObjectDetection
    self.present(controller, animated: true, completion: nil)
  }
  private func presentPoseEstimationTensorFlow(){
    let storyboard = UIStoryboard(name: "Main", bundle: nil)
    let controller = storyboard.instantiateViewController(withIdentifier: "TensorFlowPoseEstimation") as! TensorFlowPoseEstimation
    self.present(controller, animated: true, completion: nil)
  }
}
