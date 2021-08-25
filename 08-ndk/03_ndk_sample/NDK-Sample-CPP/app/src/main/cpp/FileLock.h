//--------------------------------------------------------
// 仿写 MMKV 中的文件锁封装，使其支持递归锁
//--------------------------------------------------------

#ifndef NDK_SAMPLE_CPP_FILELOCK_H
#define NDK_SAMPLE_CPP_FILELOCK_H

#include <sys/file.h>
#include "Utils.h"
#include  <unistd.h>

enum LockType {
    ///共享锁
    Shared,
    ///独占锁
    Exclusive
};

class FileLock {
private:
    int m_fd;
    size_t m_sharedLockCount;
    size_t m_exclusiveLockCount;

    bool doLock(LockType, bool wait);

    bool isFileLockValid() { return m_fd >= 0; }

    static void printInfo(const char *message) {
        int pid = getpid();
        LOGD("进程 %d：%s", pid, message);
    }

    void printCount(const char *message) {
        int pid = getpid();
        LOGD("进程 %d：%s, shareCount = %d, exclusiveCount = %d",
             pid,
             message,
             m_sharedLockCount,
             m_exclusiveLockCount
        );
    }

public:
    FileLock(const FileLock &other) = delete;

    FileLock &operator=(const FileLock &other) = delete;

    FileLock(int fd) : m_fd(fd), m_sharedLockCount(0), m_exclusiveLockCount(0) {

    }

    bool lock(LockType lockType);

    bool tryLock(LockType lockType);

    bool unLock(LockType lockType);

};

#endif //NDK_SAMPLE_CPP_FILELOCK_H
