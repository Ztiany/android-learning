package com.ztiany.mediaselector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-09 10:50
 */
public class SysMediaSelector {

    private static final String TAG = SysMediaSelector.class.getSimpleName();

    private static final int REQUEST_CAMERA = 196;
    private static final int REQUEST_CROP = 197;
    private static final int REQUEST_ALBUM = 198;
    private static final int REQUEST_FILE = 199;

    private static final String POSTFIX = ".fileProvider";
    private String mAuthority;

    private final ContextWrapper mContextWrapper;
    private final MediaSelectorCallback mMediaSelectorCallback;

    private String mSavePhotoPath;
    private CropOptions mCropOptions;
    private final CropOptions mDefaultOptions = new CropOptions();
    private String mCropTitle;
    private boolean mNeedCrop;


    public SysMediaSelector(ContextWrapper contextWrapper, MediaSelectorCallback mediaSelectorCallback) {
        mContextWrapper = contextWrapper;
        mMediaSelectorCallback = mediaSelectorCallback;
        mAuthority = contextWrapper.getContext().getPackageName().concat(POSTFIX);
        mDefaultOptions.setAspectX(1);
        mDefaultOptions.setAspectY(1);
        mDefaultOptions.setOutputX(1000);
        mDefaultOptions.setOutputY(1000);
    }

    ///////////////////////////////////////////////////////////////////////////
    // setter
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 默认的authority 为"包名.fileProvider"
     *
     * @param authority 指定FileProvider的authority
     */
    public void setAuthority(String authority) {
        mAuthority = authority;
    }

    private CropOptions getCropOptions() {
        return mCropOptions == null ? mDefaultOptions : mCropOptions;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Take method
    ///////////////////////////////////////////////////////////////////////////

    public boolean takePhotoFromCamera(String savePath) {
        mSavePhotoPath = savePath;
        mNeedCrop = false;
        return toCamera();
    }

    /**
     * 为了保证裁裁剪图片不出问题，务必指定CropOptions中的各个参数(不要为0，比如魅族手机如果指定OutputX和OutputY为0，则只会裁减出一个像素)，否则可能出现问题
     */
    public boolean takePhotoFromCameraAndCrop(String savePath, CropOptions cropOptions, String cropTitle) {
        mSavePhotoPath = savePath;
        mNeedCrop = true;
        mCropOptions = cropOptions;
        mCropTitle = cropTitle;
        return toCamera();
    }

    public boolean takePhotoFormAlbum() {
        mNeedCrop = false;
        try {
            mContextWrapper.startActivityForResult(Utils.makeAlbumIntent(), REQUEST_ALBUM, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean takePhotoFormAlbumAndCrop(String savePhotoPath, CropOptions cropOptions, String cropTitle) {
        mNeedCrop = true;
        mSavePhotoPath = savePhotoPath;
        mCropOptions = cropOptions;
        mCropTitle = cropTitle;
        try {
            mContextWrapper.startActivityForResult(Utils.makeAlbumIntent(), REQUEST_ALBUM, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean toCamera() {
        if (!Utils.hasCamera(mContextWrapper.getContext())) {
            return false;
        }
        File targetFile = new File(mSavePhotoPath);
        Intent intent = Utils.makeCaptureIntent(mContextWrapper.getContext(), targetFile, mAuthority);
        try {
            mContextWrapper.startActivityForResult(intent, REQUEST_CAMERA, null);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "takePhotoFromCamera error", e);
        }
        return false;
    }

    private boolean toCrop() {
        File targetFile = new File(mSavePhotoPath);
        Intent intent = Utils.makeCropIntent(mContextWrapper.getContext(), targetFile, mAuthority, getCropOptions(), mCropTitle);
        try {
            mContextWrapper.startActivityForResult(intent, REQUEST_CROP, null);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "toCrop error", e);
        }
        return false;
    }

    private boolean toCrop(Uri uri) {
        File targetFile = new File(mSavePhotoPath);
        Intent intent = Utils.makeCropIntent(mContextWrapper.getContext(), uri, targetFile, mAuthority, getCropOptions(), mCropTitle);
        try {
            mContextWrapper.startActivityForResult(intent, REQUEST_CROP, null);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "toCrop error", e);
        }
        return false;
    }


    public boolean takeFile() {
        return takeFile(null);
    }

    public boolean takeFile(String mimeType) {
        Intent intent = Utils.makeFilesIntent(mimeType);
        try {
            mContextWrapper.startActivityForResult(intent, REQUEST_FILE, null);
        } catch (Exception e) {
            Log.e(TAG, "takeFile error", e);
            return false;
        }
        return true;
    }
    ///////////////////////////////////////////////////////////////////////////
    // Process Result
    ///////////////////////////////////////////////////////////////////////////

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            processCameraResult(resultCode, data);
        } else if (requestCode == REQUEST_CROP) {
            processCropResult(resultCode, data);
        } else if (requestCode == REQUEST_ALBUM) {
            processAlbumResult(resultCode, data);
        } else if (requestCode == REQUEST_FILE) {
            processFileResult(resultCode, data);
        }
    }

    private void processFileResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri == null) {
                mMediaSelectorCallback.onTakeFail();
            } else {
                String absolutePath = Utils.getAbsolutePath(mContextWrapper.getContext(), uri);
                mMediaSelectorCallback.onTakeSuccess(absolutePath);
            }
        } else {
            mMediaSelectorCallback.onTakeFail();
        }
    }

    private void processAlbumResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                if (mNeedCrop) {
                    boolean success = toCrop(uri);
                    if (!success) {
                        mMediaSelectorCallback.onTakeSuccess(Utils.getAbsolutePath(mContextWrapper.getContext(), uri));
                    }
                } else {
                    mMediaSelectorCallback.onTakeSuccess(Utils.getAbsolutePath(mContextWrapper.getContext(), uri));
                }
            } else {
                mMediaSelectorCallback.onTakeFail();
            }
        }
    }

    private void processCropResult(int resultCode, @SuppressWarnings("unused") Intent data) {
        //有时候，系统裁减的结果可能没有保存到我们指定的目录，而是通过data返回了
        if (resultCode == Activity.RESULT_OK) {
            if (new File(mSavePhotoPath).exists()) {
                mMediaSelectorCallback.onTakeSuccess(mSavePhotoPath);
            } else if (data != null && data.getData() != null) {
                String realPathFromURI = Utils.getAbsolutePath(mContextWrapper.getContext(), data.getData());
                if (TextUtils.isEmpty(realPathFromURI)) {
                    mMediaSelectorCallback.onTakeFail();
                } else {
                    mMediaSelectorCallback.onTakeSuccess(realPathFromURI);
                }
            } else {
                mMediaSelectorCallback.onTakeFail();
            }
        }
    }


    private void processCameraResult(int resultCode, @SuppressWarnings("unused") Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //检测图片是否被保存下来
            if (!new File(mSavePhotoPath).exists()) {
                mMediaSelectorCallback.onTakeFail();
                return;
            }
            //需要裁减，可以裁减则进行裁减，否则直接返回
            if (mNeedCrop) {
                boolean success = toCrop();
                if (!success) {
                    mMediaSelectorCallback.onTakeSuccess(mSavePhotoPath);
                }
            } else {
                mMediaSelectorCallback.onTakeSuccess(mSavePhotoPath);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Interface
    ///////////////////////////////////////////////////////////////////////////

    public interface MediaSelectorCallback {

        void onTakeSuccess(String path);

        void onTakeFail();
    }

}
