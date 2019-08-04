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
 * TODO:下载多个文件
 */
public class FtpDownloads extends FTPWork {

    public FtpDownloads(FTPParams.Builder params, OnFtpCallBack callBack) {
        super(params, callBack);
    }

    @Override
    protected void executor(OnFtpCallBack callBack) throws Exception {
        File localFile = new File(params.localPath);
        if (!localFile.exists()) {
            boolean mkdirs = localFile.mkdirs();
        }

        if (params.list == null || params.list.isEmpty()) {
            callBack.onFail("下载列表为空");
            return;
        }

        for (String downloadFileName : params.list) {
            String serviceAllPath = params.servicePath + File.separator + downloadFileName;
            FTPFile[] ftpFile = ftpClient.listFiles(serviceAllPath);

            if (ftpFile == null || ftpFile.length == 0) {
                callBack.onFail(downloadFileName + "未找到！");
            } else {
                FTPFile serviceFile = ftpFile[0];
                long serverSize = serviceFile.getSize();
                //文件完整路径
                String fileAllLocalPath = params.localPath + File.separator + downloadFileName;
                File fileAllLocalPathFile = new File(fileAllLocalPath);
                if (fileAllLocalPathFile.exists()) {
                    boolean delete = fileAllLocalPathFile.delete();
                    boolean newFile = fileAllLocalPathFile.createNewFile();
                }
                //开始准备下载文件，不支持断点
                OutputStream out = new FileOutputStream(fileAllLocalPathFile);
                InputStream inputStream = ftpClient.retrieveFileStream(params.servicePath + File.separator + downloadFileName);

                byte[] data = new byte[1024];
                int len;
                while ((len = inputStream.read(data)) != -1) {
                    out.write(data, 0, len);
                }
                out.flush();
                out.close();
                inputStream.close();
                if (ftpClient.completePendingCommand() && fileAllLocalPathFile.length() == serverSize) {
                    callBack.onSuccess(fileAllLocalPath);
                } else if (fileAllLocalPathFile.length() != serverSize) {
                    callBack.onFail("文件长度校验错误");
                } else {
                    callBack.onFail("下载失败");
                }
            }
        }
    }
}
