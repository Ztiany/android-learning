package com.ztiany.androidipc.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {

    public static final int TABLE_NAME_BOOK = 1;
    public static final int TABLE_NAME_USER = 2;
    public static final String AUTHORITY = "com.ztiany.androidipc.provider";
    public static final String TABLE_BOOK = "book";
    public static final String TABLE_USER = "user";

    private final static Uri mBookUri = Uri.parse("content://" + AUTHORITY + "/book");

    private final static Uri mUserUri = Uri.parse("content://" + AUTHORITY + "/user");

    private final static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTHORITY, "book", TABLE_NAME_BOOK);
        mUriMatcher.addURI(AUTHORITY, "user", TABLE_NAME_USER);
    }

    @Override
    public boolean onCreate() {
        Log.d("BookProvider", "onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if (uri == null) {
            throw new IllegalArgumentException("Unsupported uri");
        }
        return null;
    }

    /**
     * @param uri uri
     * @return 返回一个Uri请求所对应的MIME类型，
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        String typeName = null;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case TABLE_NAME_BOOK:
                typeName = "text/plain";
                break;
            case TABLE_NAME_USER:
                typeName = "image/jpeg";
                break;
        }
        return typeName;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public String getTableName(Uri uri) {
        String name = null;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case TABLE_NAME_BOOK:
                name = TABLE_BOOK;
                break;
            case TABLE_NAME_USER:
                name = TABLE_USER;
                break;
        }
        return name;
    }

}
