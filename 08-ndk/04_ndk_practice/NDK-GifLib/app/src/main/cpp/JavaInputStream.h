#ifndef GIFLIBDEMO_JAVAINPUTSTREAM_H
#define GIFLIBDEMO_JAVAINPUTSTREAM_H

#include <jni.h>

class JavaInputStream {
public:
    JavaInputStream(JNIEnv* env,jobject inputStream,jbyteArray byteArray);
    size_t read(void* buffer,size_t size);
private:
    JNIEnv* mEnv;
    const jobject  mInputStream;
    const jbyteArray mByteArray;
    const size_t mByteArrayLength;

public:
    static jmethodID  readMethodId;
};


#endif //GIFLIBDEMO_JAVAINPUTSTREAM_H
