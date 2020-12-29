#import "ThumbnailFromVideoPathPlugin.h"
#if __has_include(<thumbnail_from_video_path/thumbnail_from_video_path-Swift.h>)
#import <thumbnail_from_video_path/thumbnail_from_video_path-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "thumbnail_from_video_path-Swift.h"
#endif

@implementation ThumbnailFromVideoPathPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftThumbnailFromVideoPathPlugin registerWithRegistrar:registrar];
}
@end
