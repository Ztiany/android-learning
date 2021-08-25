#ifndef CFORJNI_FILEUTILS_H
#define CFORJNI_FILEUTILS_H

#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>

bool split_file(const char *path, const char *path_pattern, int file_num);

bool merge_file(const char *path_pattern, int file_num, const char *merge_path);

#endif
