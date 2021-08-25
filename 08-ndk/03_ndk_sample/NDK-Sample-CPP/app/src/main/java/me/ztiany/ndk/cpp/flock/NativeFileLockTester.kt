package me.ztiany.ndk.cpp.flock

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-21 14:17
 */
class NativeFileLockTester {

    external fun nativeFileLockTest(fileName: String);

    external fun nativeFileLockInit(fileName: String);

    external fun nativeFileLockDoLock(isExclusive: Boolean, wait: Boolean);

    external fun nativeFileLockUnlock(isExclusive: Boolean);

    external fun nativeFileLockDestroy();

}