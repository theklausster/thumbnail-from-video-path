import Flutter
import UIKit

public class SwiftThumbnailFromVideoPathPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "thumbnail_from_video_path", binaryMessenger: registrar.messenger())
    let instance = SwiftThumbnailFromVideoPathPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
