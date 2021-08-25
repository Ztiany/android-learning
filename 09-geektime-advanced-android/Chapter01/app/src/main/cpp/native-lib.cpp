#include <jni.h>
#include <string>


extern "C" JNIEXPORT void JNICALL
Java_com_dodola_breakpad_MainActivity_makeCrash(JNIEnv *env, jobject /* this */) {
    int *a;
    *a = 3;
}