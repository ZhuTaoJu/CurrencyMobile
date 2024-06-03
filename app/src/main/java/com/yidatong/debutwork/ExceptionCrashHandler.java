package com.yidatong.debutwork;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 单例的设计模式的异常捕捉
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler{

    private  static  ExceptionCrashHandler mInstance;
    private  static  final  String TAG="ExceptionCrashHandler:";
    private  Thread.UncaughtExceptionHandler mDefaultException;
    ProgressDialog dialog;
    public  static  ExceptionCrashHandler getInstance(){
        if(mInstance==null){
            //用来解决多线程并发问题
            synchronized (ExceptionCrashHandler.class){
                mInstance=new ExceptionCrashHandler();
            }
        }
        return mInstance;
    }
    private Context mContext;

    public  void  init(Context context){
        this.mContext=context;
        //把全局的异常捕获设置为当前类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultException=Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        //全局异常捕获
        Log.d(TAG, "全局异常捕获:"+  getStackTrace(ex));
        Log.d(TAG, "-------");
        try{
            //让系统默认处理
            if(!handleExample(ex)&&mDefaultException!=null){
                mDefaultException.uncaughtException(thread,ex);
                return;
            }
//        else{

//            try {//Sleep 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序

//                Thread.sleep(3000);

//            } catch (Exception e1) {

//                e1.printStackTrace();

//            }

//        }
            String st=getStackTrace(ex);
            jump(st);

        }catch (Exception ex6){
            Log.d(TAG, "全局异常捕获:"+  getStackTrace(ex6));
        }
    }

    private  void  jump(String txt){
        // 跳转到崩溃提示Activity
        Intent intent = new Intent(mContext, SimpleLogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String chancel=getChannelInfo();
        Bundle bundle = new Bundle();
        //渠道信息
        bundle.putString("chancel", chancel);
        //错误信息
        bundle.putString("err", txt);
        intent.putExtras(bundle);
        //  Log.d(TAG, "jump: "+txt);
        mContext.startActivity(intent);
        // 关闭已奔溃的app进程
        System.exit(0);
    }

    private  String getChannelInfo(){
        ApplicationInfo info;
        String chancelInfo="";
        try{
            PackageManager pm = mContext.getPackageManager();
            info = pm.getApplicationInfo( mContext.getPackageName(), PackageManager.GET_META_DATA);
            chancelInfo=(String) info.metaData.get("appChannel");
            Log.d(TAG, "getChannelInfo: "+chancelInfo);
        }
        catch (Exception e){
            Log.d(TAG, "getChannelInfo: ");
            e.printStackTrace();
        }
        return chancelInfo;
    }

    /**

     * 自定义错误处理,收集错误信息 将异常信息保存 发送错误报告等操作均在此完成.

     * 但是无法再访问activity

     * @param ex

     * @return true:如果处理了该异常信息;否则返回false.

     */

    private boolean handleExample(Throwable ex) {
        // 如果已经处理过这个Exception,则让系统处理器进行后续关闭处理
        if (ex == null){
            return false;
        }
        new Thread(new Runnable(){
            @Override
            public void run() {
                // Toast 显示需要出现在一个线程的消息队列中
                Looper.prepare();
                //无法调用dialog，因为它依赖视图
//            initDialog();
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
        return true;
    }

    private void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static String getStackTrace(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally
        {
            pw.close();
        }
    }

    private  void  initDialog(){
        dialog=new ProgressDialog(mContext);
        dialog.setTitle("提示信息");
        dialog.setMessage("上传错误....");
        dialog.show();
    }
}

