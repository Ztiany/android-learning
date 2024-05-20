#include "FileLock.h"

static int lockType2FlockType(LockType lockType) {
    switch (lockType) {
        case Shared:
            return LOCK_SH; //共享锁，读
        case Exclusive:
            return LOCK_EX; //互斥锁，写
    }
    return LOCK_EX;
}

bool FileLock::doLock(LockType lockType, bool wait) {
    //判断文件的有效性
    if (!isFileLockValid()) {
        printInfo("文件无效");
        return false;
    }

    bool unlockFirstWhenUpgradeLock = false;

    if (lockType == Shared) {//共享锁
        m_sharedLockCount++;//计数加1
        //如果之前已经加过共享锁或者独占锁，则不需要再进一步操作了。
        if (m_sharedLockCount > 1 || m_exclusiveLockCount > 0) {
            printCount("加共享锁");
            return true;
        }
    } else {
        m_exclusiveLockCount++;
        //如果之前已经加过独占锁了，则不需要进一步操作
        if (m_exclusiveLockCount > 1) {
            printCount("加独占锁");
            return true;
        }
        //如果当前已经持有读锁，那么先尝试加写锁，如果在持有读锁的情况下尝试进行锁升级，可能会造成死锁，因为此时其他进程也可能会发起锁升级操作
        //这就会有一定几率出现两个进程相互登录对付释放读锁的情况。
        if (m_sharedLockCount > 0) {
            unlockFirstWhenUpgradeLock = true;
        }
    }

    int realLockType = lockType2FlockType(lockType);
    //LOCK_NB表示非阻塞式加锁
    int command = wait ? realLockType : (realLockType | LOCK_NB);

    if (unlockFirstWhenUpgradeLock) {
        //先尝试加锁
        auto ret = flock(m_fd, realLockType | LOCK_NB);
        if (ret == 0) {
            printCount("unlock First When Upgrade Lock");
            //加速成功
            return true;
        }
        //加锁失败则释放现在持有的锁
        flock(m_fd, F_ULOCK);
    }

    auto ret = flock(m_fd, command);

    if (ret == 0) {
        printCount("real doLock success");
    } else {
        printCount("real doLock failed");
    }

    return ret == 0;
}

bool FileLock::lock(LockType lockType) {
    return doLock(lockType, true);
}

bool FileLock::tryLock(LockType lockType) {
    return doLock(lockType, false);
}

bool FileLock::unLock(LockType lockType) {
    //判断文件的有效性
    if (!isFileLockValid()) {
        printInfo("文件无效");
        return false;
    }

    bool downgradeToShareWhenUnlock = false;

    if (lockType == Shared) {
        if (m_sharedLockCount == 0) {
            //未加锁
            printCount("未加锁");
            return false;
        }
        //计数减一
        m_sharedLockCount--;
        //如果计数减一之后，还存在共享锁或者独占锁，则不需要再进一步操作了。（有独占锁解共享锁不操作）
        if (m_sharedLockCount > 0 || m_exclusiveLockCount > 0) {
            printCount("解共享锁");
            return true;
        }
    } else {
        if (m_exclusiveLockCount == 0) {
            //未加锁
            printCount("未加锁");
            return false;
        }
        //计数减一
        m_exclusiveLockCount--;
        //如果计数减一之后，还存在独占锁，则不需要再进一步操作了。
        if (m_exclusiveLockCount > 0) {
            printCount("解独占锁");
            return true;
        }
        //到这里，说明是需要进行锁的降级，将独占锁降级为共享锁
        if (m_sharedLockCount > 0) {
            downgradeToShareWhenUnlock = true;
        }
    }

    int command = downgradeToShareWhenUnlock ? LOCK_SH/*降价*/ : LOCK_UN/*完全解锁*/;

    auto ret = flock(m_fd, command);

    if (ret == 0) {
        printCount("real unlock success");
    } else {
        printCount("real unlock failed");
    }

    return ret == 0;
}
