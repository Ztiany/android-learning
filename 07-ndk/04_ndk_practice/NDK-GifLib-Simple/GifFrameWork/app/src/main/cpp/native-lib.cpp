#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <android/log.h>

extern "C" {
#include "gif_lib.h"
};

#define  LOG_TAG    "CLOG"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  dispose(ext) (((ext)->Bytes[0] & 0x1c) >> 2)
#define  trans_index(ext) ((ext)->Bytes[3])
#define  transparency(ext) ((ext)->Bytes[0] & 1)

struct GifBean {
    int current_frame;
    int total_frame;
};

#define  argb(a, r, g, b) ( ((a) & 0xff) << 24 ) | ( ((b) & 0xff) << 16 ) | ( ((g) & 0xff) << 8 ) | ((r) & 0xff)

extern "C"
JNIEXPORT jlong JNICALL
Java_com_maniu_gifframework_GifHandler_loadGif(JNIEnv *env, jclass clazz, jstring path_) {
    //JNI：Java 字符串转换
    const char *path = env->GetStringUTFChars(path_, 0);

    //打开失败还是成功
    int Error;
    // Open a new GIF file
    GifFileType *gifFileType = DGifOpenFileName(path, &Error);
    //初始化缓冲区
    DGifSlurp(gifFileType);

    GifBean *gifBean = static_cast<GifBean *>(malloc(sizeof(GifBean)));
    memset(gifBean, 0, sizeof(GifBean));
    gifFileType->UserData = gifBean;

    //获取帧信息
    gifBean->current_frame = 0;//当前帧
    gifBean->total_frame = gifFileType->ImageCount;//总帧数

    //JNI：释放字符串
    env->ReleaseStringUTFChars(path_, path);
    return (jlong) (gifFileType);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_maniu_gifframework_GifHandler_getWidth(JNIEnv *env, jobject obj, jlong gif_handler) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_handler);
    return gifFileType->SWidth;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_maniu_gifframework_GifHandler_getHeight(JNIEnv *env, jobject obj, jlong gif_handler) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_handler);
    return gifFileType->SHeight;
}

/**
 * 处理了图片的各种处理情况。
 */
int drawFrameCompletely(GifFileType *gif, AndroidBitmapInfo info, void *pixels, bool force_dispose) {
    //背景颜色【RGB】
    GifColorType *bg;
    //图片颜色【RGB】
    GifColorType *color;
    //每一帧图片
    SavedImage *frame;
    //扩展块
    ExtensionBlock *ext = 0;
    //这一帧的描述信息
    GifImageDesc *frameInfo;
    //颜色表
    ColorMapObject *colorMap;

    int *line;
    int width, height, x, y, j, loc, n, inc, p;
    void *px;

    //获取当前帧图片信息
    GifBean *gifBean = static_cast<GifBean *>(gif->UserData);
    width = gif->SWidth;
    height = gif->SHeight;

    //获取当前帧数据
    frame = &(gif->SavedImages[gifBean->current_frame]);
    //这一帧相关的描述文件
    frameInfo = &(frame->ImageDesc);

    //颜色表【优先取当前帧的颜色表，没有则使用全局颜色表】
    if (frameInfo->ColorMap) {
        colorMap = frameInfo->ColorMap;
    } else {
        colorMap = gif->SColorMap;
    }

    //得到背景色
    bg = &colorMap->Colors[gif->SBackGroundColor];

    //遍历这一帧的扩展块，找到具有 GRAPHICS_EXT_FUNC_CODE 标志位的，这个扩展快存放着对该帧图片的
    //处置方法，是不处理还是其他。
    for (j = 0; j < frame->ExtensionBlockCount; j++) {
        if (frame->ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
            ext = &(frame->ExtensionBlocks[j]);
            break;
        }
    }

    //ext 为各种值时处理当前帧的方法。
    // 0 - 不使用处置方法
    // 1 - 不处置图形，把图形从当前位置移去【For dispose = 1, we assume its been drawn】
    // 2 - 恢复到背景色
    // 3 - 恢复到先前状态
    // 4-7 - 自定义
    px = pixels;
    if (ext && dispose(ext) == 1 && force_dispose && gifBean->current_frame > 0) {
        //不处置图形
        LOGE("drawFrame1--ext = %d, dispose(ext) = %d, force_dispose = %b", ext, dispose(ext), force_dispose);
        gifBean->current_frame = gifBean->current_frame - 1;
        drawFrameCompletely(gif, info, pixels, true);
    } else if (ext && dispose(ext) == 2 && bg) {
        //恢复到背景色
        LOGE("drawFrame2--ext = %d, dispose(ext) = %d, has bg = %b", ext, dispose(ext), (bg != NULL));
        for (y = 0; y < height; y++) {
            line = (int *) px;
            for (x = 0; x < width; x++) {
                line[x] = argb(255, bg->Red, bg->Green, bg->Blue);
            }
            px = (int *) ((char *) px + info.stride);
        }
    } else if (ext && dispose(ext) == 3 && gifBean->current_frame > 1) {
        LOGE("drawFrame3--ext = %d, dispose(ext) = %d", ext, dispose(ext));
        //回复到先前状态
        gifBean->current_frame = gifBean->current_frame - 2;
        drawFrameCompletely(gif, info, pixels, true);
    }

    px = pixels;

    //交织模式排列
    if (frameInfo->Interlace) {
        LOGE("drawFrame4");

        n = 0;
        inc = 8;
        p = 0;

        px = (int *) ((char *) px + info.stride * frameInfo->Top);

        for (y = frameInfo->Top; y < frameInfo->Top + frameInfo->Height; y++) {
            for (x = frameInfo->Left; x < frameInfo->Left + frameInfo->Width; x++) {
                loc = (y - frameInfo->Top) * frameInfo->Width + (x - frameInfo->Left);
                if (ext && frame->RasterBits[loc] == trans_index(ext) && transparency(ext)) {
                    continue;
                }

                color = (ext && frame->RasterBits[loc] == trans_index(ext)) ? bg : &colorMap->Colors[frame->RasterBits[loc]];

                if (color) {
                    line[x] = argb(255, color->Red, color->Green, color->Blue);
                }
            }

            px = (int *) ((char *) px + info.stride * inc);
            n += inc;

            if (n >= frameInfo->Height) {
                n = 0;
                switch (p) {
                    case 0:
                        px = (int *) ((char *) pixels + info.stride * (4 + frameInfo->Top));
                        inc = 8;
                        p++;
                        break;
                    case 1:
                        px = (int *) ((char *) pixels + info.stride * (2 + frameInfo->Top));
                        inc = 4;
                        p++;
                        break;
                    case 2:
                        px = (int *) ((char *) pixels + info.stride * (1 + frameInfo->Top));
                        inc = 2;
                        p++;
                }
            }
        }
    } else {//顺序模式排列

        LOGE("drawFrame5");

        px = (int *) ((char *) px + info.stride * frameInfo->Top);

        //遍历行
        for (y = frameInfo->Top; y < frameInfo->Top + frameInfo->Height; y++) {
            line = (int *) px;
            //遍历列
            for (x = frameInfo->Left; x < frameInfo->Left + frameInfo->Width; x++) {
                //当前点
                loc = (y - frameInfo->Top) * frameInfo->Width + (x - frameInfo->Left);
                if (ext && frame->RasterBits[loc] == trans_index(ext) && transparency(ext)) {
                    continue;
                }
                color = (ext && frame->RasterBits[loc] == trans_index(ext)) ? bg : &colorMap->Colors[frame->RasterBits[loc]];
                if (color) {
                    line[x] = argb(255, color->Red, color->Green, color->Blue);
                }
            }
            px = (int *) ((char *) px + info.stride);
        }
    }

    GraphicsControlBlock gcb;
    DGifSavedExtensionToGCB(gif, gifBean->current_frame, &gcb);
    int delay = gcb.DelayTime * 10;
    LOGE("delay %d", delay);
    return delay;
}

/**
 * 没有处理特殊情况，比如：
 *
 * 1. 没有处理每种图片的处置方式。
 * 2. 没有处理交织方式排列的图片。
 */
int drawFrameSimple(GifFileType *gif, AndroidBitmapInfo *info, int *pixels) {
    //获取当前帧图片信息
    GifBean *gifBean = static_cast<GifBean *>(gif->UserData);
    int frame_no = gifBean->current_frame;

    //颜色
    GifColorType *color;
    //每一帧图片
    SavedImage *frame;
    //扩展快，定义一些行为
    ExtensionBlock *ext = 0;
    //描述文件
    GifImageDesc *frameInfo;
    //颜色表
    ColorMapObject *colorMap;

    int *line;
    int x, y, j, loc;
    int *px;

    //获取这一帧
    frame = &(gif->SavedImages[frame_no]);
    //这一帧相关的描述文件
    frameInfo = &(frame->ImageDesc);

    //这一帧的颜色列表
    if (frameInfo->ColorMap) {
        colorMap = frameInfo->ColorMap;
    } else {
        //没有的话就获取全局的颜色列表
        colorMap = gif->SColorMap;
    }

    //遍历这一帧的扩展块，找到具有 GRAPHICS_EXT_FUNC_CODE 标志位的，这个扩展快存放着对该帧图片的处置方法，是不处理还是其他。
    for (j = 0; j < frame->ExtensionBlockCount; ++j) {
        if (frame->ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
            ext = &(frame->ExtensionBlocks[j]);
            break;
        }
    }

    //这时候 px 是二维指针
    px = pixels;
    //frameInfo->Top：从哪个y坐标开始（距离顶部的top），乘以每一行的字节，就是我们需要开始遍历的字节
    px = (int *) ((char *) px + info->stride * frameInfo->Top);

    //遍历每一列
    for (y = frameInfo->Top; y < frameInfo->Top + frameInfo->Height; ++y) {
        //定位到列
        line = px;
        //遍历每一行
        for (x = frameInfo->Left; x < frameInfo->Left + frameInfo->Width; ++x) {
            //当前的这一点
            loc = (y - frameInfo->Top) * frameInfo->Width + (x - frameInfo->Left);
            //判断处置方法，拿到当前帧loc位置的字节，看是否等于扩展块中索引为3的字节，并且数值为1
            if (ext && frame->RasterBits[loc] == ext->Bytes[3] && ext->Bytes[0]) {
                continue;
            }
            color = &colorMap->Colors[frame->RasterBits[loc]];
            if (color) {
                //Bitmap 的颜色顺序是 ABGR，参考 https://github.com/AndroidDeveloperLB/AndroidJniBitmapOperations/issues/11。
                line[x] = ((255 & 0xff) << 24) | ((color->Blue & 0xff) << 16) | ((color->Green) << 8) | (color->Red);
            }
        }
        px = (int *) ((char *) px + info->stride);
    }

    GraphicsControlBlock gcb;
    DGifSavedExtensionToGCB(gif, gifBean->current_frame, &gcb);
    int delay = gcb.DelayTime * 10;
    LOGE("delay %d", delay);
    return delay;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_maniu_gifframework_GifHandler_updateFrame(JNIEnv *env, jobject obj, jlong gif_point, jobject bitmap) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_point);

    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);

    int *pixels = NULL;
    AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&pixels));
    //int delay = drawFrameCompletely(gifFileType, info, pixels, false);
    int delay = drawFrameSimple(gifFileType, &info, (int *) pixels);
    AndroidBitmap_unlockPixels(env, bitmap);

    //计算下一帧是第几帧
    GifBean *gifBean = static_cast<GifBean *>(gifFileType->UserData);
    gifBean->current_frame++;
    if (gifBean->current_frame > gifBean->total_frame - 1) {
        gifBean->current_frame = 0;
    }
    return delay;
}