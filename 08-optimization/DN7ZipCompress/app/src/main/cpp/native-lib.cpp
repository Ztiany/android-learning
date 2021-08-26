#include <jni.h>
#include <string>
#include <7zTypes.h>
#include <android/log.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "7zr",__VA_ARGS__);

//表示这个函数在别的地方实现
extern int MY_CDECL main
        (
#ifndef _WIN32
        int numArgs, char *args[]
#endif
);

void strArgs(const char *cmd, int &args, char pString[66][1024]);

extern "C"
JNIEXPORT jint JNICALL
Java_com_dn_lsn_112_1demo_ZipCode_exec(JNIEnv *env, jclass type, jstring cmd_) {
    const char *cmd = env->GetStringUTFChars(cmd_, 0);
    //7zr a /sdcard/7-Zip.7z /sdcard/7-Zip -mx=9
    int numArgs;
    char temp[66][1024] = {0};
    //分割字符串 将值填入变量
    strArgs(cmd, numArgs, temp);
    char *args[] = {0};
    for (int i = 0; i < numArgs; ++i) {
        args[i] = temp[i];
        LOGE("%s", args[i]);
    }
    env->ReleaseStringUTFChars(cmd_, cmd);
    return main(numArgs, args);
}

void strArgs(const char *cmd, int &numArgs, char argv[66][1024]) {
    //获得字符串长度
    int size = strlen(cmd);
    //argv的两个下标
    int a = 0, b = 0;
    //0 = false
    //记录是否进入空格
    //7zr         a /sdcard/7-Zip.7z /sdcard/7-Zip -mx=9
    // argv[0]="7zr\0"
    //argv[1]="a\0"
    //7zr\0
    int inspace = 0;
    for (int i = 0; i < size; ++i) {
        char c = cmd[i];
        switch (c) {
            case ' ':
            case '\t':
                if (inspace) {
                    //字符串结束符号
                    argv[a][b++] = '\0';
                    a++;
                    //加入下一个有效字符前 复原
                    b = 0;
                    inspace = 0;
                }
                break;
            default:
                //如果是字符
                inspace = 1;
                argv[a][b++] = c;
                break;
        }
    }
    //7zr a /sdcard/7-Zip.7z /sdcard/7-Zip -mx=9
    //如果最末尾不是空格 就不会进入  case ' ': case '\t': 补上最后一个结束符
    //f(inspace){}
    if (cmd[size - 1] != ' ' && cmd[size - 1] != '\t') {
        argv[a][b] = '\0';
        a++;
    }
    numArgs = a;
}
