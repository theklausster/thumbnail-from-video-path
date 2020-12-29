import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:thumbnail_from_video_path/thumbnail_from_video_path.dart';

void main() {
  const MethodChannel channel = MethodChannel('thumbnail_from_video_path');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return 'hello.png';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await ThumbnailFromVideoPath.thumbnailFilePath, 'hello.png');
  });
}
