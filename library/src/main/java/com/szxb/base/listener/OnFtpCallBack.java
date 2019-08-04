package com.szxb.base.listener;

/**
 * 作者：Tangren on 2019-08-02
 * 包名：com.wxx.ftp
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public interface OnFtpCallBack {
    /**
     * 成功
     *
     * @param savePath 保存的完整路径
     */
    void onSuccess(String savePath);

    /**
     * 进度
     *
     * @param progress .
     */
    void onProgress(int progress);

    /**
     * 失败
     *
     * @param msg 失败原因
     */
    void onFail(String msg);

    /**
     * 结束
     */
    void onFinish();

}
