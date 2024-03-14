#include <jni.h>
#include <cstdlib>
#include <cstdio>
#include <cstring>
#include <unwind.h>
#include <dlfcn.h>
#include <map>
#include <string>

#include "bytehook.h"
#include "native_util.h"

using namespace std;

#define MAX_STACK_DEPTH         30

// =================================================================================
// Memory Allocation Statistics
// =================================================================================

map<string, size_t> soMallocMap;
map<string, size_t> soFreeMap;
map<void *, size_t> objMap;

void _recordMemoryAllocation(const char *name, size_t len) {
    auto size = soMallocMap.find(name);
    if (size != soMallocMap.end()) {
        soMallocMap[name] = len + size->second;
    } else {
        soMallocMap[name] = len;
    }
}


void _recordMemoryFree(const char *name, unsigned int second) {
    auto size = soFreeMap.find(name);
    if (size != soFreeMap.end()) {
        soFreeMap[name] = second + size->second;
    } else {
        soFreeMap[name] = second;
    }
}

// =================================================================================
// Call Stack Information
// =================================================================================

struct backtrace_stack {
    void **current;
    void **end;
};

static _Unwind_Reason_Code _unwind_callback(struct _Unwind_Context *context, void *data) {
    struct backtrace_stack *state = (struct backtrace_stack *) (data);
    uintptr_t pc = _Unwind_GetIP(context);  // 获取 pc 值，即绝对地址
    if (pc) {
        if (state->current == state->end) {
            return _URC_END_OF_STACK;
        } else {
            *state->current++ = (void *) (pc);
        }
    }
    return _URC_NO_REASON;
}

static size_t _fill_backtraces_buffer(void **buffer, size_t max) {
    struct backtrace_stack stack = {buffer, buffer + max};
    _Unwind_Backtrace(_unwind_callback, &stack);
    return stack.current - buffer;
}

static void _dumpBacktrace(void **buffer, size_t count) {
    for (int i = 0; i < count; ++i) {
        void *address = buffer[i];
        Dl_info info = {};
        if (dladdr(address, &info)) {
            const uintptr_t address_relative = (uintptr_t) address - (uintptr_t) info.dli_fbase;
            LOG(
                    "# %d : %p : %s(%p)(%s)(%p)",
                    i,
                    address,
                    info.dli_fname,
                    address_relative,
                    info.dli_sname,
                    info.dli_saddr
            );
        }
    }
}

static void _triggerBacktrace(size_t len) {
    LOG("_triggerBacktrace %zu", len);
    void *buffer[MAX_STACK_DEPTH];
    size_t count = _fill_backtraces_buffer(buffer, MAX_STACK_DEPTH);
    _dumpBacktrace(buffer, count);
}


// =================================================================================
// Memory Hook
// =================================================================================


void *malloc_proxy(size_t len) {
    BYTEHOOK_STACK_SCOPE();

    // 记录每次分配的内存大小
    Dl_info info = {};
    if (dladdr(__builtin_return_address(0), &info)) {
        _recordMemoryAllocation(info.dli_fname, len);
    }

    // 触发堆栈信息
    if (len > 10 * 1024 * 1024) {
        _triggerBacktrace(len);
    }

    void *object = BYTEHOOK_CALL_PREV(malloc_proxy, len);
    objMap[object] = len;

    return object;
}

void free_proxy(void *__ptr) {
    BYTEHOOK_STACK_SCOPE();

    // 记录每次释放的内存大小
    Dl_info info = {};
    if (dladdr(__builtin_return_address(0), &info)) {
        auto len = objMap.find(__ptr);
        _recordMemoryFree(info.dli_fname, len->second);
    }

    return BYTEHOOK_CALL_PREV(free_proxy, __ptr);
}

void hookMemory() {
    bytehook_hook_all(
            nullptr,
            "malloc",
            (void *) malloc_proxy,
            nullptr,
            nullptr
    );

    bytehook_hook_all(
            nullptr,
            "free",
            (void *) free_proxy,
            nullptr,
            nullptr
    );
}

// =================================================================================
// JNI Bridge
// =================================================================================

extern "C" JNIEXPORT void JNICALL Java_me_ztiany_apm_JNIBridge_hookMemoryAllocation(
        JNIEnv *env,
        jobject caller
) {
    hookMemory();
    LOG("hookMemoryAllocation is called");
}

extern "C" JNIEXPORT void JNICALL Java_me_ztiany_apm_JNIBridge_dumpMemoryAllocationInfo(
        JNIEnv *env,
        jobject caller
) {
    for (auto &item: soMallocMap) {
        LOG(".so %s allocated %zu bytes, freed %zu bytes", item.first.c_str(), item.second, soFreeMap[item.first]);
    }
}