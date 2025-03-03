#import <React/RCTBridgeDelegate.h>
#import <UIKit/UIKit.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate, RCTBridgeDelegate>

@property (nonatomic, strong) UIWindow *window;

- (void) presentAiCameraInteract;
- (void) dismissToReactNative;

- (void) presentTensorFlowExample;

@end
