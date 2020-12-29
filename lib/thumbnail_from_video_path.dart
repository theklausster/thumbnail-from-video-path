
import 'dart:async';

import 'package:flutter/services.dart';

class ThumbnailFromVideoPath {
  static const MethodChannel _channel =
      const MethodChannel('thumbnail_from_video_path');

  static Future<String> thumbnailFilePath(String path, int width, int height) async {
    Map args = {};
    args["width"] = width;
    args["height"] = height;
    args["path"] = path;

    final String thumbnailFilePath = await _channel.invokeMethod('generateThumbnail', args);
    return thumbnailFilePath;
  }
}
