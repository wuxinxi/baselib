package com.szxb.base.io.ftp;

import com.szxb.base.handler.HandlerDelivery;
import com.szxb.base.listener.OnFtpCallBack;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者: TangRen on 2019/8/4
 * 包名：com.szxb.base.io.ftp
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public abstract class FTPWork extends Thread {
    public FTPParams.Builder params;
    public OnFtpCallBack callBack;
    public FTPClient ftpClient;

    public FTPWork(FTPParams.Builder params, OnFtpCallBack callBack) {
        this.params = params;
        this.callBack = callBack;
    }

    /**
     * 执行具体任务
     *
     * @param callBack .
     * @throws Exception .
     */
    protected abstract void executor(OnFtpCallBack callBack) throws Exception;

    @Override
    public void run() {
        super.run();
        OnCallBack onCallBack = new OnCallBack(callBack);
        if (login()) {
            if (ftpClient == null) {
                onCallBack.onFail("登录失败");
                onCallBack.onFinish();
                return;
            }
            try {
                executor(onCallBack);
            } catch (Exception e) {
                e.printStackTrace();
                onCallBack.onFail(e.getClass().getName());
            } finally {
                if (ftpClient != null && ftpClient.isConnected()) {
                    try {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            onCallBack.onFail("登陆失败");
        }
        onCallBack.onFinish();
    }


    private boolean login() {
        int reply;
        try {
            ftpClient = new FTPClient();
            // 连接FTP服务器
            ftpClient.connect(params.ip, params.port);
            // 登录
            ftpClient.login(params.userName, params.userPsw);
            //连接的状态码
            reply = ftpClient.getReplyCode();
            ftpClient.setDefaultTimeout(params.timeOut);
            //判断是否连接上ftp
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return false;
            }

            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("GBK");
            if (params.isMainModel) {
                ftpClient.enterLocalActiveMode();
            } else {
                ftpClient.enterLocalPassiveMode();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * io处理
     *
     * @param available   大小
     * @param localSize   本地文件大小
     * @param inputStream 输入流
     * @param out         输出流
     * @throws Exception .
     */
    public void ioWork(long available, long localSize, InputStream inputStream, OutputStream out, OnFtpCallBack callBack) throws Exception {
        byte[] data = new byte[1024];
        int len;
        long step = available / 100;
        long process = 0;
        long currentSize = localSize;
        int maxFilter = available >= 20 * 1024 * 1024 ? 1 : 2;
        while ((len = inputStream.read(data)) != -1) {
            if (currentThread().isInterrupted()) {
                throw new InterruptedException("停止");
            }
            out.write(data, 0, len);
            currentSize = currentSize + len;
            if (currentSize / step != process) {
                process = currentSize / step;
                if (process % maxFilter == 0) {
                    if (callBack != null) {
                        callBack.onProgress((int) process);
                    }
                }
            }
        }
        out.flush();
        out.close();
        inputStream.close();
    }

    class OnCallBack implements OnFtpCallBack {
        private final OnFtpCallBack callBack;

        public OnCallBack(OnFtpCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onSuccess(final String savePath) {
            HandlerDelivery.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (callBack != null) {
                        callBack.onSuccess(savePath);
                    }
                }
            });
        }

        @Override
        public void onProgress(final int progress) {
            HandlerDelivery.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (callBack != null) {
                        callBack.onProgress(progress);
                    }
                }
            });
        }

        @Override
        public void onFail(final String msg) {
            HandlerDelivery.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (callBack != null) {
                        callBack.onFail(msg);
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            HandlerDelivery.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (callBack != null) {
                        callBack.onFinish();
                    }
                }
            });
        }
    }


    /**
     * 终止线程
     * 退出任务
     */
    public void cancel() {
        interrupt();
    }

}
