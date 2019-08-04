package com.szxb.base.io.ftp;

import java.util.List;

/**
 * 作者: TangRen on 2019/8/4
 * 包名：com.szxb.base.io.ftp
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class FTPParams {
    /**
     * IP
     */
    private String ip;
    /**
     * 端口
     */
    private int port;
    /**
     * 服务器文件路径
     */
    private String servicePath;
    /**
     * 文件本地保存路径
     */
    private String localPath;
    /**
     * 是否是主动模式
     */
    private boolean isMainModel;
    /**
     * 下载重试次数
     */
    private int retryCnt;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPsw;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 批量下载(文件名)|上传(本地文件完整路径)
     */
    private List<String> list;

    /**
     * 超时时间
     */
    private int timeOut;

    /**
     * 如果文件已存在是否删除
     * 不删除则默认支持增量
     */
    private boolean isDelOld;

   public static class Builder {
        public String ip;
        public String userName;
        public String userPsw;
        public String fileName;
        public int port = 21;
        public String localPath = "";
        public String servicePath = "/";
        public boolean isMainModel = true;
        public int retryCnt = 0;
        public boolean isDelOld = true;
        public List<String> list;
        public int timeOut = 15 * 1000;

        public Builder(String ip, String userName, String userPsw) {
            this.ip = ip;
            this.userName = userName;
            this.userPsw = userPsw;
        }

        public FTPParams.Builder setServicePath(String servicePath) {
            this.servicePath = servicePath;
            return this;
        }

        public FTPParams.Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public FTPParams.Builder setMainModel(boolean mainModel) {
            isMainModel = mainModel;
            return this;
        }

        public FTPParams.Builder setRetryCnt(int retryCnt) {
            this.retryCnt = retryCnt;
            return this;
        }

        public FTPParams.Builder setLocalPath(String localPath) {
            this.localPath = localPath;
            return this;
        }

        public FTPParams.Builder setDeleteOld(boolean isDelOld) {
            this.isDelOld = isDelOld;
            return this;
        }

        public FTPParams.Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public FTPParams.Builder setList(List<String> list) {
            this.list = list;
            return this;
        }

        public FTPParams.Builder setTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public FTPParams create() {
            return new FTPParams(this);
        }
    }

    public FTPParams(FTPParams.Builder builder) {
        ip = builder.ip;
        port = builder.port;
        userName = builder.userName;
        userPsw = builder.userPsw;
        localPath = builder.localPath;
        servicePath = builder.servicePath;
        isMainModel = builder.isMainModel;
        retryCnt = builder.retryCnt;
        fileName = builder.fileName;
        isDelOld = builder.isDelOld;
        list = builder.list;
        timeOut = builder.timeOut;
    }

}
