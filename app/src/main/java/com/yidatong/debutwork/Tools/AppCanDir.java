package com.yidatong.debutwork.Tools;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class AppCanDir {
    public  static String TAG ="AppCanDir";
    public  static  String getCanReadFilesDir(Context context){
        // 获取可读路径
        File fileDir = context.getFilesDir();
        String filePath = fileDir.getAbsolutePath();
        Log.d(TAG, "可读路径：" + filePath);
        return  filePath;
    }

    // 获取可写路径
    public  static  String getCanWriteCacheDir(Context context){
        File cacheDir = context.getCacheDir();
        String cachePath = cacheDir.getAbsolutePath();
        Log.d(TAG, "可写路径：" + cachePath);
        return  cachePath;
    }

}
