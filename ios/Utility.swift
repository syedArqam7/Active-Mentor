//
//  Utility.swift
//  NATIVE_AI
//
//  Created by Mac Book on 10/07/2020.
//

import Foundation

@objc class Utility: NSObject{
  static let main = Utility()
  fileprivate override init() {}
}

extension Utility{
  func randomString(length: Int) -> String {
    let letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return String((0..<length).map{ _ in letters.randomElement()! })
  }
}
extension Utility{
  func digitalClockTime(seconds: Int)->String{
    if seconds >= 0{
      let (_, m, s) = self.secondsToHoursMinutesSeconds(seconds: seconds)
      let min = m > 9 ? "\(m)" : "0\(m)"
      let sec = s > 9 ? "\(s)" : "0\(s)"
      return "\(min):\(sec)"
    }
    return "00:00"
  }
  private func secondsToHoursMinutesSeconds (seconds : Int) -> (Int, Int, Int) {
    return (seconds / 3600, (seconds % 3600) / 60, (seconds % 3600) % 60)
  }
}
// MARK: Alert related functions
extension Utility{
  func showAlert(message:String,title:String){
    let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
    alertController.addAction(UIAlertAction(title: Strings.OK.text, style: .default, handler: nil))
    Utility.main.topViewController()?.present(alertController, animated: true, completion: nil)
  }
}
//MARK:- Top View Controller
extension Utility{
  func topViewController(base: UIViewController? = (UIApplication.shared.delegate as? AppDelegate)?.window?.rootViewController) -> UIViewController? {
    if let nav = base as? UINavigationController {
      return topViewController(base: nav.visibleViewController)
    }
    if let tab = base as? UITabBarController {
      if let selected = tab.selectedViewController {
        return topViewController(base: selected)
      }
    }
    if let presented = base?.presentedViewController {
      return topViewController(base: presented)
    }
    return base
  }
}
