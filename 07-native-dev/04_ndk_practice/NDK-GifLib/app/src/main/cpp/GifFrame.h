#ifndef GIFLIBDEMO_GIFFRAME_H
#define GIFLIBDEMO_GIFFRAME_H

#include <gif_lib.h>
#include "JavaInputStream.h"

class GifFrame {
public:
    GifFrame(JavaInputStream* stream);
    GifFrame(JNIEnv* env,jobject assetManager,const char* filename);

    ~GifFrame();

    int getWidth();
    int getHeight();
    int getFrameCount();

    long loadFrame(JNIEnv *env,jobject bitmap,int frameIndex);

private:
    GifFileType* mGif;

};


#endif //GIFLIBDEMO_GIFFRAME_H
