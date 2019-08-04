package com.szxb.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.szxb.base.io.ftp.FTPParams;
import com.szxb.base.io.ftp.FtpDownload;
import com.szxb.base.io.ftp.FtpDownloads;
import com.szxb.base.io.ftp.FtpUpLoad;
import com.szxb.base.io.ftp.FtpUpLoadFolder;
import com.szxb.base.listener.OnFtpCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar bar;
    private TextView errorMsg;
    private Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = findViewById(R.id.progress);
        errorMsg = findViewById(R.id.errorMsg);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    String localPath = Environment.getExternalStorageDirectory() + File.separator + "apk";
    FTPParams.Builder builder = new FTPParams
            .Builder("139.199.158.253", "ftpuser", "QW!@123qwe")
            .setMainModel(true)
            .setLocalPath(localPath)
            .setDeleteOld(false)
            .setServicePath("/");

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                startActivity(new Intent(MainActivity.this,TestActivity.class));
                if (true)return;
                builder.setFileName("cz_2.1.2.apk");
                errorMsg.append("开始下载：" + builder.fileName + "\n");
                new FtpDownload(builder, new OnFtpCallBack() {
                    @Override
                    public void onSuccess(String savePath) {
                        errorMsg.append(savePath + "\n");
                    }

                    @Override
                    public void onProgress(int progress) {
                        bar.setProgress(progress);
                        Log.e("MainActivity",
                            "onProgress(MainActivity.java:68)"+progress);
                    }

                    @Override
                    public void onFail(String msg) {
                        errorMsg.append("下载失败：" + builder.fileName + msg + "\n");
                    }

                    @Override
                    public void onFinish() {
                        errorMsg.append(builder.fileName + "下载结束\n\n");
                    }
                }).start();
                break;
            case R.id.btn2:
                List<String> listFileName = new ArrayList<>();
                listFileName.add("cz_2.1.2.apk");
                listFileName.add("zhaji3.apk");
                builder.setList(listFileName);
                new FtpDownloads(builder, new OnFtpCallBack() {
                    @Override
                    public void onSuccess(String savePath) {
                        errorMsg.append(savePath + "\n");
                    }

                    @Override
                    public void onProgress(int progress) {
                        bar.setProgress(progress);
                    }

                    @Override
                    public void onFail(String msg) {
                        errorMsg.append("下载失败：" + builder.fileName + msg + "\n");
                    }

                    @Override
                    public void onFinish() {
                        errorMsg.append(builder.fileName + "下载结束\n\n");
                    }
                }).start();
                break;
            case R.id.btn3:
                builder.setLocalPath(Environment.getExternalStorageDirectory() + "");
                builder.setServicePath("apk");
                builder.setFileName("qrcode.apk");
                new FtpUpLoad(builder, new OnFtpCallBack() {
                    @Override
                    public void onSuccess(String savePath) {
                        errorMsg.append(savePath + "\n");
                    }

                    @Override
                    public void onProgress(int progress) {
                        bar.setProgress(progress);
                    }

                    @Override
                    public void onFail(String msg) {
                        errorMsg.append("上传失败：" + builder.fileName + msg + "\n");
                    }

                    @Override
                    public void onFinish() {
                        errorMsg.append(builder.fileName + "上传结束\n\n");
                    }
                }).start();
                break;
            case R.id.btn4:
                List<String> listFilePath = new ArrayList<>();
                listFilePath.add(Environment.getExternalStorageDirectory() + File.separator + "apk" + File.separator + "cz_2.1.2.apk");
                listFilePath.add(Environment.getExternalStorageDirectory() + File.separator + "apk" + File.separator + "zhaji3.apk");
                builder.setList(listFilePath);
                builder.setServicePath("/apk/");
                new FtpUpLoadFolder(builder,null).start();
                if (true)return;
                new FtpUpLoadFolder(builder, new OnFtpCallBack() {
                    @Override
                    public void onSuccess(String savePath) {
                        errorMsg.append(savePath + "\n");
                    }

                    @Override
                    public void onProgress(int progress) {
                        bar.setProgress(progress);
                    }

                    @Override
                    public void onFail(String msg) {
                        errorMsg.append("上传失败：" + builder.fileName + msg + "\n");
                    }

                    @Override
                    public void onFinish() {
                        errorMsg.append(builder.fileName + "上传结束\n\n");
                    }
                }).start();
                break;
            default:

                break;
        }
    }
}
