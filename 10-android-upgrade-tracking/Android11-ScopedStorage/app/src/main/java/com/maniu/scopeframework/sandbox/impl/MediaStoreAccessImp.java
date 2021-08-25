package com.maniu.scopeframework.sandbox.impl;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.maniu.scopeframework.annotion.DbField;
import com.maniu.scopeframework.sandbox.BaseRequest;
import com.maniu.scopeframework.sandbox.FileResponce;
import com.maniu.scopeframework.sandbox.IFile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MediaStoreAccessImp implements IFile {

//    真正对文件的操作  分区

    HashMap<String, Uri> uriMap = new HashMap<>();
//    限制  内置目录   data/data/
    public static final String AUDIO = "Audio";
    public static final String VIDEO = "Video";
    public static final String IMAGE = "Pictures";
    public static final String DOWNLOADS = "Downloads";//什么都可以放
    private static MediaStoreAccessImp sInstance;

    public static MediaStoreAccessImp getInstance() {
        if (sInstance == null) {
            synchronized (MediaStoreAccessImp.class) {
                if (sInstance == null) {
                    sInstance = new MediaStoreAccessImp();
                }
            }
        }
        return sInstance;
    }



    private  MediaStoreAccessImp() {
//外置卡 对应图片的URI
        uriMap.put(AUDIO, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(VIDEO,  MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(IMAGE, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        uriMap.put(DOWNLOADS, MediaStore.Downloads.EXTERNAL_CONTENT_URI);

    }
    public    static    void  setup() {

    }
//Android R
    @Override
    public <T extends BaseRequest> FileResponce newCreateFile(Context context, T baseRequest) {
        Uri uri = uriMap.get(baseRequest.getType());
        ContentValues contentValues = objectConvertValues(baseRequest);
        Uri resultUri = context.getContentResolver().insert(uri, contentValues);
        if (resultUri != null) {
            Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show();
        }
        FileResponce fileResponse = new FileResponce();
        fileResponse.setUri(resultUri);
        return fileResponse;
    }
//  分区存储  性能上   比原先的  性能要低
    private <T extends BaseRequest> ContentValues objectConvertValues(T baseRequest) {
        ContentValues contentValues = new ContentValues();
        Field[] fields = baseRequest.getClass().getDeclaredFields();

        for (Field field : fields) {
            DbField dbField = field.getAnnotation(DbField.class);
            if (dbField == null) {
                continue;
            }
//
            String name = dbField.value();
            String value = null;
            String fieldName = field.getName();

            char firstLetter = Character.toUpperCase(fieldName.charAt(0));
            String theRest = fieldName.substring(1);
            String methodName = "get" + firstLetter + theRest;
            Log.i("tuch", "objectConvertValues: " + methodName);
            try {
               Method getMethod= baseRequest.getClass().getMethod(methodName);
                value= (String) getMethod.invoke(baseRequest);

            } catch ( Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                contentValues.put(name, value);
            }
        }
        return contentValues;
    }

    @Override
    public <T extends BaseRequest> FileResponce delete(Context context, T baseRequest) {
        Uri uri = query(context, baseRequest).getUri();
//        备注
//        Uri uri =uriMap.get(baseRequest);
        context.getContentResolver().delete(uri, null, null);
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce renameTo(Context context, T where, T request) {
        Uri uri = query(context, where).getUri();
        ContentValues contentValues = objectConvertValues(request);
        int code=context.getContentResolver().update(uri, contentValues, null, null);
        if (code > 0) {
            Toast.makeText(context, "更改成功", Toast.LENGTH_SHORT).show();
        }
        FileResponce fileResponse = new FileResponce();
        fileResponse.setUri(uri);
        return fileResponse;
    }
    @Override
    public <T extends BaseRequest> FileResponce copyFile(Context context, T baseRequest) {
        return null;
    }
    @Override
    public <T extends BaseRequest> FileResponce query(Context context, T baseRequest) {
        Uri uri = uriMap.get(baseRequest.getType());
        ContentValues contentValues = objectConvertValues(baseRequest);
        Condition condition = new Condition(contentValues);
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        Cursor cursor = context.getContentResolver().query(uri, projection, condition.whereCasue, condition.whereArgs, null);
        Uri queryUri = null;
        if (cursor != null && cursor.moveToFirst()) {
            queryUri = ContentUris.withAppendedId(uri, cursor.getLong(0));
            Toast.makeText(context, "查询成功"+queryUri, Toast.LENGTH_SHORT).show();
            cursor.close();
        }
        FileResponce fileResponse = new FileResponce();
        fileResponse.setUri(queryUri);
        return fileResponse;
    }
    private class Condition {
        private String whereCasue;  //image_url =?  AND   qq_number=?

        private String[] whereArgs;
        public Condition( ContentValues contentValues) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1=1");
            ArrayList list = new ArrayList();//whereArgs里面的内容存入list
            //取所有的字段名
            Iterator<Map.Entry<String, Object>> set= contentValues.valueSet().iterator() ;
            while (set.hasNext()) {
                Map.Entry<String, Object> entry=set.next();
                String key = entry.getKey();
                String value = (String) entry.getValue();
                if (value != null) {
                    stringBuilder.append(" and " + key + " =? ");
                    list.add(value);
                }
            }
            this.whereCasue = stringBuilder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }

    }
}
