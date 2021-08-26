package com.imooc.lib_update.app;

import android.app.Activity;
import android.content.Context;
import com.imooc.lib_commin_ui.CommonDialog;
import com.imooc.lib_network.okhttp.CommonOkHttpClient;
import com.imooc.lib_network.okhttp.listener.DisposeDataHandle;
import com.imooc.lib_network.okhttp.listener.DisposeDataListener;
import com.imooc.lib_network.okhttp.request.CommonRequest;
import com.imooc.lib_network.okhttp.utils.ResponseEntityToModule;
import com.imooc.lib_update.R;
import com.imooc.lib_update.update.UpdateService;
import com.imooc.lib_update.update.constant.Constants;
import com.imooc.lib_update.update.model.UpdateModel;
import com.imooc.lib_update.update.utils.Utils;

public final class UpdateHelper {

  public static final String UPDATE_FILE_KEY = "apk";

  public static String UPDATE_ACTION;
  //SDK全局Context, 供子模块用
  private static Context mContext;

  public static void init(Context context) {
    mContext = context;
    UPDATE_ACTION = mContext.getPackageName() + ".INSTALL";
  }

  public static Context getContext() {
    return mContext;
  }

  //外部检查更新方法
  public static void checkUpdate(final Activity activity) {
    CommonOkHttpClient.get(CommonRequest.
            createGetRequest(Constants.CHECK_UPDATE, null),
        new DisposeDataHandle(new DisposeDataListener() {
          @Override public void onSuccess(Object responseObj) {
            final UpdateModel updateModel = (UpdateModel) responseObj;
            if (Utils.getVersionCode(mContext) < updateModel.data.currentVersion) {
              //说明有新版本,开始下载
              CommonDialog dialog =
                  new CommonDialog(activity, mContext.getString(R.string.update_new_version),
                      mContext.getString(R.string.update_title),
                      mContext.getString(R.string.update_install),
                      mContext.getString(R.string.cancel), new CommonDialog.DialogClickListener() {
                    @Override public void onDialogClick() {
                      UpdateService.startService(mContext);
                    }
                  });
              dialog.show();
            } else {
              //弹出一个toast提示当前已经是最新版本等处理
            }
          }

          @Override public void onFailure(Object reasonObj) {
            onSuccess(
                ResponseEntityToModule.parseJsonToModule(MockData.UPDATE_DATA, UpdateModel.class));
          }
        }, UpdateModel.class));
  }
}
