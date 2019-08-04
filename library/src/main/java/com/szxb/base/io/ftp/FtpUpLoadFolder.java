package com.szxb.base.io.ftp;

import com.szxb.base.listener.OnFtpCallBack;

import org.apache.commons.net.ftp.FTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 作者: TangRen on 2019/8/4
 * 包名：com.szxb.base.io.ftp
 * 邮箱：996489865@qq.com
 * TODO:上传文件夹
 */
public class FtpUpLoadFolder extends FTPWork {

    public FtpUpLoadFolder(FTPParams.Builder params, OnFtpCallBack callBack) {
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

        if (params.list == null || params.list.size() == 0) {
            callBack.onFail("数据为空");
            return;
        }

        int available = params.list.size();
        int currentSize = 0;
        int process;

        for (String path : params.list) {
            String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
            InputStream inputStream = new FileInputStream(path);
            boolean result = false;
            try {
                result = ftpClient.storeFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), inputStream);
                System.out.println("FTPManage.upLoadFolder 上传结果" + result + ",fileName=" + fileName );
            } catch (Exception e) {
                e.printStackTrace();
            }
            inputStream.close();
            currentSize++;
            process = (int) (((float) currentSize / available) * 100);
            if (result) {
                callBack.onProgress(process);
            } else {
                callBack.onFail(fileName + "失败");
            }
        }
    }
}
