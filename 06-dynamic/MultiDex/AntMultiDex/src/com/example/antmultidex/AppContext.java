package com.example.antmultidex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

public class AppContext extends Application{
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		dexTool();
	}	
	
	@SuppressLint("NewApi")
	private void dexTool() {

		File dexDir = new File(getFilesDir(), "dlibs");
		dexDir.mkdir();
		File dexFile = new File(dexDir, "libs.apk");
		
		File dexOpt = getCacheDir();
		
		//把Assets下的libs.apk写到dlibs目录下
		try {
			InputStream ins = getAssets().open("libs.apk");
			if (dexFile.length() != ins.available()) {
				FileOutputStream fos = new FileOutputStream(dexFile);
				byte[] buf = new byte[4096];
				int l;
				while ((l = ins.read(buf)) != -1) {
					fos.write(buf, 0, l);
				}
				fos.close();
			}
			ins.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//获取ClassLoader
		ClassLoader cl = getClassLoader();
		ApplicationInfo ai = getApplicationInfo();
		String nativeLibraryDir = null;
		//设置so路径
		if (Build.VERSION.SDK_INT > 8) {
			nativeLibraryDir = ai.nativeLibraryDir;
		} else {
			nativeLibraryDir = "/data/data/" + ai.packageName + "/lib/";
		}
		//够建一个新的DexClassLoader
		DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(),dexOpt.getAbsolutePath(), nativeLibraryDir, cl.getParent());
		//把DexClassLoader插入到类加载器委托机制的中间，这样就可以加载libs.apk中的类了
		try {
			Field f = ClassLoader.class.getDeclaredField("parent");
			f.setAccessible(true);
			f.set(cl, dcl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
