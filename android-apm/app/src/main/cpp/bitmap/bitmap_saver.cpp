#include "bitmap_saver.h"
#include <cstdio>
#include <cstring>
#include <cerrno>
#include "../common/native_util.h"

const int FILE_HEADER_SIZE = 14;
const int INFO_HEADER_SIZE = 40;

/**
 * BMP 采用的是 BGR 格式，这里把 RGBA 转换为 BGRA。这里假设 image 是一个 RGBA 格式的图片。如果是其他格式，需要修改这个函数。
 */
void convert_to_bgra(unsigned char *image, unsigned int height, unsigned int width, unsigned int bytes_per_pixel) {
    unsigned int width_in_bytes = width * bytes_per_pixel;
    for (int i = 0; i < height; i++) {
        unsigned char *line_begin_address = image + (i * width_in_bytes);
        for (int j = 0; j < width_in_bytes; j += bytes_per_pixel) {
            unsigned char *line = line_begin_address + j;
            unsigned char red = *line;
            unsigned char green = *(line + 1);
            unsigned char blue = *(line + 2);
            unsigned char alpha = *(line + 3);

            (*line) = blue;
            *(line + 1) = green;
            *(line + 2) = red;
            *(line + 3) = alpha;
        }
    }
}

void convert_bgra_to_rgba(unsigned char *image, unsigned int height, unsigned int width, unsigned int bytes_per_pixel) {
    unsigned int width_in_bytes = width * bytes_per_pixel;
    for (int i = 0; i < height; i++) {
        unsigned char *line_begin_address = image + (i * width_in_bytes);

        for (int j = 0; j < width_in_bytes; j += bytes_per_pixel) {
            unsigned char *line = line_begin_address + j;
            unsigned char blue = *line;
            unsigned char green = *(line + 1);
            unsigned char red = *(line + 2);
            unsigned char alpha = *(line + 3);

            (*line) = red;
            *(line + 1) = green;
            *(line + 2) = blue;
            *(line + 3) = alpha;
        }
    }
}

static unsigned char *create_bitmap_file_header(int height, int stride) {

    int file_size = FILE_HEADER_SIZE + INFO_HEADER_SIZE + (stride * height);

    static unsigned char file_header[] = {
            0, 0,     /// signature
            0, 0, 0, 0, /// image file size in bytes
            0, 0, 0, 0, /// reserved
            0, 0, 0, 0, /// start of pixel array
    };

    file_header[0] = (unsigned char) ('B');
    file_header[1] = (unsigned char) ('M');
    file_header[2] = (unsigned char) (file_size);
    file_header[3] = (unsigned char) (file_size >> 8);
    file_header[4] = (unsigned char) (file_size >> 16);
    file_header[5] = (unsigned char) (file_size >> 24);
    file_header[10] = (unsigned char) (FILE_HEADER_SIZE + INFO_HEADER_SIZE);

    return file_header;
}

static unsigned char *create_bitmap_info_header(int height, int width, int bits_per_pixel) {

    static unsigned char info_header[] = {
            0, 0, 0, 0, /// header size
            0, 0, 0, 0, /// image width
            0, 0, 0, 0, /// image height
            0, 0,     /// number of color planes
            0, 0,     /// bits per pixel
            0, 0, 0, 0, /// compression
            0, 0, 0, 0, /// image size
            0, 0, 0, 0, /// horizontal resolution
            0, 0, 0, 0, /// vertical resolution
            0, 0, 0, 0, /// colors in color table
            0, 0, 0, 0, /// important color count
    };

    info_header[0] = (unsigned char) (INFO_HEADER_SIZE);
    info_header[4] = (unsigned char) (width);
    info_header[5] = (unsigned char) (width >> 8);
    info_header[6] = (unsigned char) (width >> 16);
    info_header[7] = (unsigned char) (width >> 24);

    info_header[8] = (unsigned char) (height);
    info_header[9] = (unsigned char) (height >> 8);
    info_header[10] = (unsigned char) (height >> 16);
    info_header[11] = (unsigned char) (height >> 24);
    info_header[12] = (unsigned char) (1);
    info_header[14] = (unsigned char) (bits_per_pixel * 8);

    return info_header;
}

void write_bitmap_to_file(
        unsigned char *pixels,
        unsigned int height,
        unsigned int width,
        const char *file_full_path,
        unsigned int bytes_per_pixel
) {
    int width_in_bytes = width * bytes_per_pixel;

    unsigned char padding[3] = {0, 0, 0};

    // BMP（Bitmap）文件格式要求每行的像素数据在存储时必须对齐到 4 字节（32位）边界。补齐每行数据为 4 的倍数。
    int padding_size = (4 - (width_in_bytes) % 4) % 4;

    int stride = (width_in_bytes) + padding_size;

    log_i("file_full_path: %s", file_full_path);
    FILE *image_file = fopen(file_full_path, "wb");
    log_i("image_file: %p", image_file);

    if (image_file != nullptr) {
        unsigned char *file_header = create_bitmap_file_header(height, stride);
        fwrite(file_header, 1, FILE_HEADER_SIZE, image_file);

        unsigned char *info_header = create_bitmap_info_header(height, width, bytes_per_pixel);
        fwrite(info_header, 1, INFO_HEADER_SIZE, image_file);

        int i;
        for (i = 0; i < height; i++) {
            /*
             * height 正数的话，倒着写入：BMP 文件格式中，图像数据（像素数组）通常是从图像的左下角开始，向上逐行存储（即文件中第一行数据实际对应图像的最底行）。
             * 这与许多图像处理库和显示系统采用的从上到下的像素排列顺序不同。“height 正数的话，倒着写入”的意思是按照 BMP 格式的要求，把 Bitmap 的像素数据从最
             * 底行开始向文件写入，直到最顶行。这就需要在写入文件时逆序遍历像素行，确保数据与 BMP 格式要求一致。
             */
            fwrite(pixels + ((height - i - 1) * width_in_bytes), bytes_per_pixel, width, image_file);
            fwrite(padding, 1, padding_size, image_file);
        }
        fclose(image_file);
    } else {
        log_e("Failed to open pixels file: %s, errno: %d, strerror: %s", file_full_path, errno, strerror(errno));
    }
}