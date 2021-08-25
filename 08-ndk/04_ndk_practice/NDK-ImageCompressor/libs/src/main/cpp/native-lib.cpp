#include <jni.h>
#include <string>
#include <malloc.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <jpeglib.h>

#define LOG_TAG "Native"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

void write_jpeg_file(uint8_t *data, int w, int h, jint q, const char *path);

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_imagecompressor_ImageCompressor_nativeCompress(JNIEnv *env, jclass clazz, jobject bitmap, jint q, jstring path) {

    const char *savePath = env->GetStringUTFChars(path, 0);
    LOGI("开始压缩：%s", savePath);

    //从bitmap获取argb数据
    AndroidBitmapInfo info;
    //获取里面的信息
    AndroidBitmap_getInfo(env, bitmap, &info);
    //得到图片中的像素信息，uint8_t 其实就是一个 char，占一个字节。
    uint8_t *pixels;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &pixels);
    //jpeg argb 中去掉其中的a ===>rgb
    int w = info.width;
    int h = info.height;
    int color;
    //开一块内存用来存入rgb信息
    uint8_t *data = (uint8_t *) malloc(w * h * 3);//每个像素三个字节
    uint8_t *temp = data;//用于记住原始角标
    uint8_t r, g, b;//每个像素的的颜色值

    //默认一个像素占四个字节(argb)，现在抽离 rgb 三色，舍弃透明度 a。
    for (int i = 0; i < h; ++i) {
        for (int j = 0; j < w; ++j) {
            color = *(int *) pixels;
            //获取三个颜色，一个像素为：#FFAABBCC
            r = (color >> 16) & 0xFF;
            g = (color >> 8) & 0xFF;
            b = (color) & 0xFF;
            //存放到data中，libjpeg 中，每个像素带你的颜色顺序是bgr
            *data = b;
            *(data + 1) = g;
            *(data + 2) = r;
            //移动到下一个像素点
            data += 3;
            pixels += 4;
        }
    }
    //把得到的新的图片的信息存入一个新文件中
    write_jpeg_file(temp, w, h, q, savePath);

    //释放资源
    free(temp);
    AndroidBitmap_unlockPixels(env, bitmap);
    env->ReleaseStringUTFChars(path, savePath);

    LOGI("压缩完成");
}

void write_jpeg_file(uint8_t *data, int w, int h, jint q, const char *path) {
//    3.1、创建jpeg压缩对象
    jpeg_compress_struct jcs;
    //错误回调
    jpeg_error_mgr error;
    jcs.err = jpeg_std_error(&error);
    //创建压缩对象
    jpeg_create_compress(&jcs);
    //3.2、指定存储文件  write binary
    FILE *f = fopen(path, "wb");
    jpeg_stdio_dest(&jcs, f);
    //3.3、设置压缩参数
    jcs.image_width = w;
    jcs.image_height = h;
    //bgr
    jcs.input_components = 3;
    jcs.in_color_space = JCS_RGB;
    jpeg_set_defaults(&jcs);
    //开启哈夫曼功能
    jcs.optimize_coding = true;
    jpeg_set_quality(&jcs, q, 1);
    //3.4、开始压缩
    jpeg_start_compress(&jcs, 1);
    //3.5、循环写入每一行数据
    int row_stride = w * 3;//一行的字节数
    JSAMPROW row[1];
    while (jcs.next_scanline < jcs.image_height) {
        //取一行数据
        uint8_t *pixels = data + jcs.next_scanline * row_stride;
        row[0] = pixels;
        jpeg_write_scanlines(&jcs, row, 1);
    }
    //3.6、压缩完成
    jpeg_finish_compress(&jcs);
    //3.7、释放jpeg对象
    fclose(f);
    jpeg_destroy_compress(&jcs);
}