package com.szxb.base.io.ftp;

import com.szxb.base.listener.OnFtpCallBack;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者: TangRen on 2019/8/4
 * 包名：com.szxb.base.io.ftp
 * 邮箱：996489865@qq.com
 * TODO:下载单个文件
 */
public class FtpDownload extends FTPWork {

    public FtpDownload(FTPParams.Builder params, OnFtpCallBack callBack) {
        super(params, callBack);
    }

    @Override
    protected void executor(OnFtpCallBack callBack) throws Exception {
        String serviceAllPath = params.servicePath + File.separator + params.fileName;
        FTPFile[] ftpFile = ftpClient.listFiles(serviceAllPath);
        if (ftpFile == null || ftpFile.length == 0) {
            callBack.onFail("文件不存在");
            return;
        }

        File localFile = new File(params.localPath);
        if (!localFile.exists()) {
            boolean mkdirs = localFile.mkdirs();
        }
        //文件完整路径
        String fileAllLocalPath = params.localPath + File.separator + params.fileName;
        File fileAllLocalPathFile = new File(fileAllLocalPath);
        //获取远程服务器文件大小
        long serverSize = ftpFile[0].getSize();
        long localSize = 0;
        if (fileAllLocalPathFile.exists()) {
            //如果文件存在
            if (params.isDelOld) {
                //删除旧文件
                boolean delete = fileAllLocalPathFile.delete();
                boolean newFile = fileAllLocalPathFile.createNewFile();
            } else {
                localSize = fileAllLocalPathFile.length();
                if (localSize >= serverSize) {
                    boolean delete = fileAllLocalPathFile.delete();
                    boolean newFile = fileAllLocalPathFile.createNewFile();
                    localSize = 0;
                }
            }
        } else {
            boolean newFile = fileAllLocalPathFile.createNewFile();
        }

        //开始准备下载文件，允许断点
        OutputStream out = new FileOutputStream(fileAllLocalPathFile, true);
        //设置追加的位置
        ftpClient.setRestartOffset(localSize);
        InputStream inputStream = ftpClient.retrieveFileStream(params.servicePath + File.separator + params.fileName);
        ioWork(serverSize, localSize, inputStream, out, callBack);
        if (ftpClient.completePendingCommand() && fileAllLocalPathFile.length() == serverSize) {
            callBack.onSuccess(fileAllLocalPath);
        } else if (fileAllLocalPathFile.length() != serverSize) {
            callBack.onFail("文件长度校验错误");
        } else {
            callBack.onFail("下载失败");
        }
    }
}
