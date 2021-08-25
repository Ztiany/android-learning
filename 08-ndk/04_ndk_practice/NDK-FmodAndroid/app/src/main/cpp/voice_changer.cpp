#include <fmod.hpp>
#include "cstdlib"
#include "android/log.h"
#include "common.h"
#include <unistd.h>
#include <jni.h>

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"jason",FORMAT,##__VA_ARGS__);

#define MODE_NORMAL 0
#define MODE_LUOLI 1
#define MODE_DASHU 2
#define MODE_JINGSONG 3
#define MODE_GAOGUAI 4
#define MODE_KONGLING 5

using namespace FMOD;

/*系统*/
System *fmodSystem = NULL;
/*声音*/
Sound *sound = NULL;
//声轨
Channel *channel = NULL;
//一个数字信号处理单元
DSP *dsp = NULL;
//声轨集合，此处没有使用
//ChannelGroup *mastergroup = NULL;
bool isExit;

void closeInner() {
    Common_Close();
    if (sound != NULL) {
        sound->release();
    }
    if (fmodSystem != NULL) {
        fmodSystem->close();
        fmodSystem->release();
    }
}


extern "C" JNIEXPORT void JNICALL
Java_com_ztiany_fmod_VoiceChangerActivity_close(JNIEnv *env, jobject thiz) {
    isExit = true;
}

extern "C" void JNICALL
Java_com_ztiany_fmod_VoiceChangerActivity_playVoice(JNIEnv *env, jobject thiz, jstring assetsName,
                                                    jint effect) {


    isExit = false;
    bool playing = true;
    float frequency = 0;

    //获取路径
    const char *assets = env->GetStringUTFChars(assetsName, NULL);
    const char *path_cstr = Common_MediaPath(assets);

    LOGI("%s, %s", assets, path_cstr);

    try {

        //初始化
        System_Create(&fmodSystem);
        
        //系统对象最大声轨为32
        fmodSystem->init(32, FMOD_INIT_NORMAL, NULL);

        //加载声音 如果过大建议用FMOD_CREATESTREAM 标志
        fmodSystem->createSound(path_cstr, FMOD_DEFAULT, NULL, &sound);

        //得到声音轨道集合
        //fmodSystem->getMasterChannelGroup(&mastergroup);

        switch (effect) {
            case MODE_NORMAL:
                //原生播放
                fmodSystem->playSound(sound, 0, false, &channel);
                LOGI("%s", "fix normal");
                break;
            case MODE_LUOLI:
                //萝莉
                //DSP digital signal process
                //dsp -> 音效 创建fmod中预定义好的音效
                //FMOD_DSP_TYPE_PITCHSHIFT dsp，提升或者降低音调用的一种音效
                fmodSystem->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                //设置音调的参数
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 2.5);
                //设置播放的音乐
                fmodSystem->playSound(sound, 0, false, &channel);
                //添加到channel
                channel->addDSP(0, dsp);
                LOGI("%s", "fix luoli");
                break;

            case MODE_JINGSONG:
                //惊悚：颤抖的声音
                fmodSystem->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_TREMOLO_SKEW, 0.5);
                fmodSystem->playSound(sound, 0, false, &channel);
                channel->addDSP(0, dsp);
                break;
            case MODE_DASHU:
                //大叔
                fmodSystem->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 0.8);
                fmodSystem->playSound(sound, 0, false, &channel);
                //添加到channel
                channel->addDSP(0, dsp);
                LOGI("%s", "fix dashu");
                break;
            case MODE_GAOGUAI:
                //搞怪：提高说话的速度
                fmodSystem->playSound(sound, 0, false, &channel);
                channel->getFrequency(&frequency);
                frequency = frequency * 1.6F;
                channel->setFrequency(frequency);
                LOGI("%s", "fix gaoguai");
                break;
            case MODE_KONGLING:
                //空灵
                fmodSystem->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                //回声时间
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 300);
                //回声次数
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 20);
                fmodSystem->playSound(sound, 0, false, &channel);
                channel->addDSP(0, dsp);
                LOGI("%s", "fix kongling");
                break;

            default:
                break;
        }
    } catch (...) {
        LOGE("%s", "发生异常");
        closeInner();
    }

    fmodSystem->update();

    //单位是微秒，每秒钟判断下是否在播放
    while (playing && !isExit) {
        channel->isPlaying(&playing);
        usleep(1000 * 1000);
    }

    closeInner();
    env->ReleaseStringUTFChars(assetsName, assets);
}
