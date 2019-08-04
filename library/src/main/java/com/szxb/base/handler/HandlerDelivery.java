package com.szxb.base.handler;

import android.os.Handler;
import android.os.Looper;

/**
 * 作者: TangRen on 2019/8/4
 * 包名：com.szxb.base.handler
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public final class HandlerDelivery {
    private Handler handler;
    private volatile static HandlerDelivery instance = null;

    private HandlerDelivery(Handler handler) {
        this.handler = handler;
    }

    public static HandlerDelivery getInstance() {
        if (instance == null) {
            synchronized (HandlerDelivery.class) {
                if (instance == null) {
                    instance = new HandlerDelivery(new Handler(Looper.getMainLooper()));
                }
            }
        }
        return instance;
    }

    public boolean post(Runnable r) {
        return handler.post(r);
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        return handler.postDelayed(r, delayMillis);
    }


    public boolean postAtFrontOfQueue(Runnable r) {
        return handler.postAtFrontOfQueue(r);
    }

    public boolean postAtTime(Runnable r, long uptimeMillis) {
        return handler.postAtTime(r, uptimeMillis);
    }

    public boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return handler.postAtTime(r, token, uptimeMillis);
    }

    public void removeCallbacks(Runnable r) {
        handler.removeCallbacks(r);
    }

    public void removeCallbacks(Runnable r, Object token) {
        handler.removeCallbacks(r, token);
    }
}
