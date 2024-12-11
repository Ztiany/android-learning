#ifndef ANDROID_APM_BITMAP_SAVER_H
#define ANDROID_APM_BITMAP_SAVER_H

void convert_to_bgra(unsigned char *image, unsigned int height, unsigned int width, unsigned int bytes_per_pixel);

void convert_bgra_to_rgba(unsigned char *image, unsigned int height, unsigned int width, unsigned int bytes_per_pixel);

void write_bitmap_to_file(
        unsigned char *pixels,
        unsigned int height,
        unsigned int width,
        const char *file_full_path,
        unsigned int bytes_per_pixel
);

#endif
