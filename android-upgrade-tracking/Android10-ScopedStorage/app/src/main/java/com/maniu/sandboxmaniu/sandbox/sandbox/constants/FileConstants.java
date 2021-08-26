
/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : FileConstants.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          FileConstants
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.constants;


import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class FileConstants {
    /**
     * 文件访问类型
     */
    @IntDef({FileType.TYPE_FILE, FileType.TYPE_MEDIA_STORE, FileType.TYPE_SAF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FileType {
        /**
         * 用file接口访问，非沙箱模式和私有目录下使用
         */
        int TYPE_FILE = 1;
        /**
         * 用接口访问 公共目录下使用
         */
        int TYPE_MEDIA_STORE = 2;
        /**
         * SAF接口访问，其他目录下使用
         */
        int TYPE_SAF = 3;
    }

    /**
     * 打卡文件的模 只读，只写，读写都可以
     */
    @IntDef({FileModeType.MODE_READ, FileModeType.MODE_WRITE, FileModeType.MODE_READ_AND_WRITE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FileModeType {
        int MODE_READ = 0;
        int MODE_WRITE = 1;
        int MODE_READ_AND_WRITE = 2;
    }

    @StringDef({FileMode.MODE_READ, FileMode.MODE_WRITE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FileMode {
        String MODE_READ = "r";
        String MODE_WRITE = "w";
    }


}
