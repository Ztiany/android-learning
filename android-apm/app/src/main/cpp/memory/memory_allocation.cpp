#include <jni.h>
#include <malloc.h>

#include "../common/native_util.h"

static void *gNativeMemory = nullptr;

extern "C" JNIEXPORT void JNICALL Java_me_ztiany_apm_scene_memory_MemoryAllocation_allocateNativeMemory(
        JNIEnv *env,
        jobject caller
) {
    if (!gNativeMemory) {
        gNativeMemory = malloc(20 * 1024 * 1024);
        LOG("allocateNativeMemory is called");
    } else {
        LOG("gNativeMemory is already allocated");
    }
}

extern "C" JNIEXPORT void JNICALL Java_me_ztiany_apm_scene_memory_MemoryAllocation_freeNativeMemory(
        JNIEnv *env,
        jobject caller
) {
    if (gNativeMemory != nullptr) {
        free(gNativeMemory);
        gNativeMemory = nullptr;
        LOG("freeNativeMemory is called");
    } else {
        LOG("gNativeMemory is already null");
    }
}