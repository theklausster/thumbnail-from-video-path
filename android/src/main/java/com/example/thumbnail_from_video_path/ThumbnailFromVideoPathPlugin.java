package com.example.thumbnail_from_video_path;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import static io.flutter.util.PathUtils.getFilesDir;

/** ThumbnailFromVideoPathPlugin */
public class ThumbnailFromVideoPathPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "thumbnail_from_video_path");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("generateThumbnail")) {
      final Map<String, Object> args = call.arguments();
      log("onMethodCall: called with args: " + args);
      int width = (int) args.get("width");
      int height = (int) args.get("height");
      String path = (String) args.get("path");

      log("onMethodCall: width: "  + width);
      log("onMethodCall: height: "  + height);
      log("onMethodCall: path: "  + path);

      if (call.arguments == null) {
        result.error("error", "path: " + path + ", height: " + height + "and width: " + width, "");
        return;
      }

      String thumbnailPath = generateThumbnail(path, height, width);
      if (thumbnailPath == null) {
        result.error("thumbnail error", "For some reason thumbnail could not be generated", "");
        return;
      }

      log("thumbnail path is: " + thumbnailPath);
      result.success("path");
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private String generateThumbnail(String path, int heightFromFlutter, int widthFromFlutter) {
    log("generateThumbnail: from file path: " + path + ", height: " + heightFromFlutter + ", width: " + widthFromFlutter);

    Bitmap bitmap = generateBitmap(path, heightFromFlutter, widthFromFlutter);
    if (bitmap == null) {
      log("generateThumbnail: bitmap error");
      return null;
    }

    byte[] bytes = readBitmap(bitmap);
    if (bytes.length == 0) {
      log("generateThumbnail: bytes error");
      return null;
    }

    String thumbnailPath = writeToFile(path, bytes);
    if (thumbnailPath == null) {
      log("generateThumbnail: file error");
      return null;
    }

    return thumbnailPath;
  }

  private Bitmap generateBitmap(String path, int heightFromFlutter, int widthFromFlutter) {

    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    retriever.setDataSource(path);

    try {
      log("trying to create bitmap");
      Bitmap bitmap = retriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_NEXT_SYNC);
      log("done creating bitmap");
      return bitmap;
    } catch (RuntimeException e) {
      log("generateBitmap: Error while creating bitmap " + e);
      return null;
    }

  }
  private byte[] readBitmap(Bitmap bitmap) {
    log("readBitmap: trying to read bitmap");
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
    bitmap.recycle();
    return stream.toByteArray();
  }

  private String writeToFile(String path, byte[] bytes) {
    log("writeToFile: trying to write to file: " + path);

    String pathFromSplit = path.split("mp4")[0];
    String fileType = "png";
    log("writeToFile: first: " + pathFromSplit);
    log("writeToFile: fileType: " + fileType);
    String thumbailPath = pathFromSplit + fileType;
    log("writeToFile: thumbnailPath: " + thumbailPath);
    try {
      log("writeToFile: trying to write to file");
      FileOutputStream f = new FileOutputStream(thumbailPath);
      f.write(bytes);
      f.close();
      log("writeToFile: success. Path is: " + thumbailPath);
    } catch (java.io.IOException e) {
      log("writeToFile: error:" +  e);
      return null;
    }
    return thumbailPath;
  }

  void log(String log) {
    Log.d("1337", log);
  }
}
