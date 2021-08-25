//
// Created by Administrator on 2019/11/7.
//

#ifndef ENJOYMMKV_PBUTILITY_H
#define ENJOYMMKV_PBUTILITY_H

#include <stdint.h>

size_t computeInt32Size(int32_t value) {
    //负数的编码长度肯定是 10，为什么呢？以 -1 为例：
    //源码：1000 0000 0000 0001
    //反码：1111    1111   1111    1110（除符号位取反）
    //补码：1111    1111   1111    1111（加 1）
    //而 protobuf 为了兼容性处理，即使是 int32 也被按照 int64 处理，所以 -1 一共是 64 位 1，按照 protobuf 按照每 7 位存储数据，64 / 7 = 9 余 1，一共需要 10 字节来存储。
    if (value < 0){
        return 10;
    }

    //0xffffffff 表示 32位能表示的 最大值
    //<< 7 则低7位变成0 与上value
    //如果value只要7位就够了则=0，编码只需要一个字节，否则进入其他判断
    if ((value & (0xffffffff << 7)) == 0) {
        return 1;
    } else if ((value & (0xffffffff << 14)) == 0) {
        return 2;
    } else if ((value & (0xffffffff << 21)) == 0) {
        return 3;
    } else if ((value & (0xffffffff << 28)) == 0) {
        return 4;
    }
    return 5;
}


size_t computeInt64Size(int64_t value) {
    if ((value & (0xffffffffffffffffL << 7)) == 0) {
        return 1;
    } else if ((value & (0xffffffffffffffffL << 14)) == 0) {
        return 2;
    } else if ((value & (0xffffffffffffffffL << 21)) == 0) {
        return 3;
    } else if ((value & (0xffffffffffffffffL << 28)) == 0) {
        return 4;
    } else if ((value & (0xffffffffffffffffL << 35)) == 0) {
        return 5;
    } else if ((value & (0xffffffffffffffffL << 42)) == 0) {
        return 6;
    } else if ((value & (0xffffffffffffffffL << 49)) == 0) {
        return 7;
    } else if ((value & (0xffffffffffffffffL << 56)) == 0) {
        return 8;
    } else if ((value & (0xffffffffffffffffL << 63)) == 0) {
        return 9;
    }
    return 10;
}

size_t computeItemSize(std::string key, InputBuffer *value) {
    int32_t keyLength = key.length();
    // 保存key的长度与key数据需要的字节
    int32_t size = keyLength + computeInt32Size(keyLength);
    // 加上保存value的长度与value数据需要的字节
    size += value->length() + computeInt32Size(value->length());
    return size;
}

size_t computeMapSize(std::unordered_map<std::string, InputBuffer *> map) {
    auto iter = map.begin();
    int32_t size = 0;
    for (; iter != map.end(); iter++) {
        auto key = iter->first;
        auto value = iter->second;
        size += computeItemSize(key, value);
    }
    return size;
}

#endif //ENJOYMMKV_PBUTILITY_H