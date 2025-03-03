//
//  AiCameraInteract.m
//  jogo_test_flow
//
//  Created by Mac Book on 07/07/2020.
//

#import "React/RCTBridgeModule.h"
#import "AppDelegate.h"

@interface RCT_EXTERN_MODULE(SwiftApp,NSObject)
RCT_EXPORT_METHOD(goToNative) {
  NSLog(@"RN binding - Native View - Loading AiCameraInteract.swift");
  dispatch_async(dispatch_get_main_queue(), ^{
      AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
      [appDelegate presentTensorFlowExample];
  });
}
@end

