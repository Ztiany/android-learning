/*******************************************************
 * Copyright 2010 - 2012 OPPO Mobile Comm Corp., Ltd.
 * All rights reserved.
 * <p>
 * Description    :
 * History      :
 * (ID, Date, Author, Description)
 *******************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;

import androidx.annotation.Nullable;


import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OppoEnvironment {
    private static final String TAG = "OppoEnvironment";


    public final static String DIR_DCIM = Environment.DIRECTORY_DCIM;
    public final static String DIR_PICTURES = Environment.DIRECTORY_PICTURES;
    public final static String DIR_MOVIES = Environment.DIRECTORY_MOVIES;
    public final static String DIR_DOCUMENTS = Environment.DIRECTORY_DOCUMENTS;
    public final static String DIR_DOWNLOADS = Environment.DIRECTORY_DOWNLOADS;
    public final static String DIR_ANDROID_DATA = "Android/data";
    public final static String DIR_DATA_DATA = "data/data";
    public final static String EMULATED = "emulated";

    /**
     * FIXME: Source Type
     */
    private static final Uri FILES_URI = (Files.getContentUri("external")).buildUpon()
            .appendQueryParameter("limit", 0 + "," + 1).build();
    private static final String[] PROJECTION = {Files.FileColumns.DATA};
    private static final String EXTERNAL = "external";
    private static String sInternalPath = null;
    private static String sExternalPath = null;
    private static String sMultiAppPath = null;
    private static boolean sFirstLoad = true;

    private static final Pattern PATTERN_RELATIVE_PATH = Pattern.compile(
            "(?i)^/storage/(?:emulated/[0-9]+/|[^/]+/)(Android/sandbox/([^/]+)/)?");

    private static final Pattern PATTERN_VOLUME_NAME = Pattern.compile("(?i)^/storage/([^/]+)");
    private static final Pattern PATTERN_TOP_LEVEL_DIR = Pattern.compile(
            "(?i)^/storage/[^/]+/[0-9]+/([^/]+)(/.*)?");


    public static String getRelativePath(@Nullable String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            return extractRelativePathForDirectory(filePath);
        } else {
            return extractRelativePathFile(filePath);
        }
    }

    private static String extractRelativePathFile(@Nullable String filePath) {
        if (filePath == null) return null;
        final Matcher matcher = PATTERN_RELATIVE_PATH.matcher(filePath);
        if (matcher.find()) {
            final int lastSlash = filePath.lastIndexOf('/');
            if (lastSlash == -1 || lastSlash < matcher.end()) {
                // This is a file in the top-level directory, so relative path is "/"
                // which is different than null, which means unknown path
                return "";
            } else {
                return filePath.substring(matcher.end(), lastSlash + 1);
            }
        } else {
            return null;
        }
    }

    /**
     * Returns relative path for the directory.
     */

    private static String extractRelativePathForDirectory(@Nullable String directoryPath) {
        if (directoryPath == null) {
            return null;
        }
        final Matcher matcher = PATTERN_RELATIVE_PATH.matcher(directoryPath);
        if (matcher.find()) {
            if (matcher.end() == directoryPath.length() - 1) {
                return "";
            }
            return directoryPath.substring(matcher.end()) + "/";
        }
        return null;
    }

    public static String getVolumeName(@Nullable String filePath) {
        if (filePath == null) {
            return null;
        }
        final Matcher matcher = PATTERN_VOLUME_NAME.matcher(filePath);
        if (matcher.find()) {
            final String volumeName = matcher.group(1);
            if (EMULATED.equals(volumeName)) {
                return MediaStore.VOLUME_EXTERNAL_PRIMARY;
            } else {
                return normalizeUuid(volumeName);
            }
        }
        return MediaStore.VOLUME_INTERNAL;
    }

    private static String normalizeUuid(@Nullable String fsUuid) {
        return fsUuid != null ? fsUuid.toLowerCase(Locale.US) : null;
    }

    public static String getFileName(@Nullable String filePath) {
        if (filePath == null) {
            return null;
        }
        filePath = getDisplayName(filePath);
        if (filePath == null) {
            return null;
        }
        final int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1) {
            return filePath;
        } else {
            return filePath.substring(0, lastDot);
        }
    }

    public static String getDisplayName(@Nullable String filePath) {
        if (filePath == null) {
            return null;
        }
        if (filePath.indexOf('/') == -1) {
            return filePath;
        }
        if (filePath.endsWith("/")) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        return filePath.substring(filePath.lastIndexOf('/') + 1);
    }

    public static boolean isDCIMDir(String filePath) {
        String relative = getRelativePath(filePath);
        if (relative == null) {
            return false;
        }
        return isRelativeDCIMDir(relative);
    }

    public static boolean isRelativeDCIMDir(String relative) {
        return relative.startsWith(DIR_DCIM);
    }

    public static boolean isPicturesDir(String filePath) {
        String relative = getRelativePath(filePath);
        if (relative == null) {
            return false;
        }
        return isRelativePicturesDir(relative);
    }

    public static boolean isRelativePicturesDir(String relative) {
        return relative.startsWith(DIR_PICTURES);
    }


    public static boolean isMoviesDir(String filePath) {
        String relative = getRelativePath(filePath);
        if (relative == null) {
            return false;
        }
        return isRelativeMoviesDir(relative);
    }

    public static boolean isRelativeMoviesDir(String relative) {
        return relative.startsWith(DIR_MOVIES);
    }

    public static boolean isDownloadsDir(String filePath) {
        String relative = getRelativePath(filePath);
        if (relative == null) {
            return false;
        }
        return isRelativeDownloadsDir(relative);
    }

    public static boolean isRelativeDownloadsDir(String relative) {
        return relative.startsWith(DIR_DOWNLOADS);
    }

    public static boolean isDocumentsDir(String filePath) {
        String relative = getRelativePath(filePath);
        if (relative == null) {
            return false;
        }
        return isRelativeDocumentsDir(relative);
    }

    public static boolean isRelativeDocumentsDir(String relative) {
        return relative.startsWith(DIR_DOCUMENTS);
    }

    public static boolean isPrivateDir(String filePath) {
        String relative = getRelativePath(filePath);
        if (relative == null) {
            return false;
        }
        return isRelativePrivateDir(relative);
    }

    public static boolean isRelativePrivateDir(String relative) {
        return relative.startsWith(DIR_ANDROID_DATA)
                || relative.startsWith(DIR_DATA_DATA);
    }

    public static boolean isPublicDir(String filePath) {
        String relative = getRelativePath(filePath);
        return isRelativePublicDir(relative);

    }

    public static boolean isRelativePublicDir(String relative) {
        return isRelativeDCIMDir(relative)
                || isRelativePicturesDir(relative)
                || isRelativeMoviesDir(relative)
                || isRelativeDownloadsDir(relative)
                || isRelativeDocumentsDir(relative);

    }

    public static boolean isOtherDir(String filePath) {
        return (!isPublicDir(filePath) && !isPrivateDir(filePath));
    }

    public static boolean isImageRelativeDir(String relative) {
        return isRelativeDCIMDir(relative)
                || isRelativePicturesDir(relative)
                || isRelativeDownloadsDir(relative)
                || isRelativeDocumentsDir(relative);
    }

    public static boolean isImageDir(String filePath) {
        String relative = getRelativePath(filePath);
        return isImageRelativeDir(relative);
    }

    public static boolean isVideoDir(String filePath) {
        String relative = getRelativePath(filePath);
        return isVideoRelativeDir(relative);
    }

    public static boolean isVideoRelativeDir(String relative) {
        return isRelativeDCIMDir(relative)
                || isRelativeMoviesDir(relative)
                || isRelativeDownloadsDir(relative)
                || isRelativeDocumentsDir(relative);
    }

}
