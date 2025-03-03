//
//  Strings.swift
//  NATIVE_AI
//
//  Created by Mac Book on 11/07/2020.
//

import Foundation
enum Strings: String{
  //SingleMethod For the Usage of Localization.
  var text: String { return NSLocalizedString( self.rawValue, comment: "") }
  
  case ALERT = "Alert"
  case OK = "Ok"

}
