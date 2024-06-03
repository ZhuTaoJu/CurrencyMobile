package com.yidatong.debutwork.Tools;

import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;

import com.yidatong.debutwork.R;

import java.io.IOException;
import java.util.Objects;

public class MyWakeLock {
    static PowerManager.WakeLock mWakeLock;

    //申请设备电源锁
    @SuppressLint("InvalidWakeLockTag")
    public static void acquireWakeLock(Context context)
    {
        if (null == mWakeLock)
        {
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "WakeLock");
            if (null != mWakeLock)
            {
                mWakeLock.acquire(10*60*1000L /*10 minutes*/);
            }
        }
    }

    //释放设备电源锁
    public static void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    private  Context context=null;
    public MyWakeLock(Context context){
        this.context = context;
        registReceiver();
    }

    private void  registReceiver() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(mScreenStatusReceiver, mIntentFilter);
    }

    private void  unregistReceiver() {
        context.unregisterReceiver(mScreenStatusReceiver);
    }

    /* 保持屏幕熄灭后  cpu 还一直运行 */
    private  PowerManager.WakeLock wl = null;
    @SuppressLint("InvalidWakeLockTag")
    private void  acquire(Context context,String name) {
        try {
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            if (wl == null){
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, name);
            }
            wl.acquire(10 * 60 * 60 * 1000L /*10 minutes*/);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //清除唤醒
    private void stopAcquire() {
        try {
            if (wl.isHeld()){
                wl.release();
            }
            wl = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BroadcastReceiver mScreenStatusReceiver = new  BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {

            if(Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON))  {
                stopAcquire();
                return;
            }
            else if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF )) {
                acquire(context,"dded");
            }
        }
    };

    /**
     * 忽略电池优化
     */

    public void ignoreBatteryOptimization(Activity activity) {
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
        if (!hasIgnored) {
            @SuppressLint("BatteryLife") Intent intent = new Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            context.startActivity(intent);
        }
    }

    private MediaPlayer mMediaPlayer = null;

    // 开始播放无声音乐
    private void  startMediaPlayer() {
//        mMediaPlayer = MediaPlayer.create(context.getApplicationContext(),R.raw.silent_music);
        mMediaPlayer.isLooping();
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    private void stopMediaPlayer() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    // 在BackgroundMusicService中添加播放音乐的代码
    public void playSilentMusic() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = null;
//          afd = context.getResources().openRawResourceFd(R.raw.silent_music);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.setVolume(0f, 0f);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    4 提高app优先级
//    在application中提高app优先级, 申请大内存, 防止内存不足时候被清理
    // android:largeHeap="true" ,  android:priority="1000"


}
