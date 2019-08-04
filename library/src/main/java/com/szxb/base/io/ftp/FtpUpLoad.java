package com.szxb.base.io.ftp;

import com.szxb.base.listener.OnFtpCallBack;

import org.apache.commons.net.ftp.FTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者: TangRen on 2019/8/4
 * 包名：com.szxb.base.io.ftp
 * 邮箱：996489865@qq.com
 * TODO:上传单个文件
 */
public class FtpUpLoad extends FTPWork {

    public FtpUpLoad(FTPParams.Builder params, OnFtpCallBack callBack) {
        super(params, callBack);
    }

    @Override
    protected void executor(OnFtpCallBack callBack) throws Exception {
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        String workPath = ftpClient.printWorkingDirectory();
        if (!ftpClient.changeWorkingDirectory(workPath + params.servicePath)) {
            String[] dirs = params.servicePath.split("/");
            String tempPath = workPath;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) {
                    continue;
                }
                tempPath += "/" + dir;
                if (!ftpClient.changeWorkingDirectory(tempPath)) {
                    if (!ftpClient.makeDirectory(tempPath)) {
                        callBack.onFail("创建目录失败");
                        return;
                    } else {
                        ftpClient.changeWorkingDirectory(tempPath);
                    }
                }
            }
        }

        InputStream inputStream = new FileInputStream(params.localPath + File.separator + params.fileName);
        OutputStream out = ftpClient.storeFileStream(params.fileName);
        ioWork(inputStream.available(), 0, inputStream, out, callBack);
        callBack.onSuccess(params.servicePath);
    }
}
