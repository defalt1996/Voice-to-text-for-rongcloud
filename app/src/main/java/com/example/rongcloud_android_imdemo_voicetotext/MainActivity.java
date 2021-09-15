package com.example.rongcloud_android_imdemo_voicetotext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//import com.tbruyelle.rxpermissions3.RxPermissions;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import io.rong.imkit.IMCenter;
import io.rong.imkit.RongIM;
import io.rong.imkit.config.RongConfigCenter;
import io.rong.imkit.conversation.messgelist.provider.HQVoiceMessageItemProvider;
import io.rong.imkit.conversation.messgelist.provider.VoiceMessageItemProvider;
import io.rong.imkit.utils.RouteUtils;
import io.rong.imlib.RongIMClient;

public class MainActivity extends AppCompatActivity {
    String token = "7tFyttugY+A3j5kxNEHpucxASFjGoX05cjQj+g2udTk=@9tsf.cn.rongnav.com;9tsf.cn.rongcfg.com";
    private static final int BAIDU_READ_PHONE_STATE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        获取语音转文字时需要的运行时权限

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        }
    }

    /**
     * 连接融云
     */
    private void connectRong() {

        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * 成功回调
             * @param userId 当前用户 ID
             */
            @Override
            public void onSuccess(String userId) {
                Toast.makeText(getApplicationContext(), "融云连接成功", Toast.LENGTH_SHORT).show();
//                打开融云会话列表
                RouteUtils.routeToConversationListActivity(MainActivity.this, "会话列表");

            }

            /**
             * 错误回调
             * @param errorCode 错误码
             */
            @Override
            public void onError(RongIMClient.ConnectionErrorCode errorCode) {
                Toast.makeText(getApplicationContext(), "融云连接失败" + errorCode, Toast.LENGTH_SHORT).show();
                if (errorCode.equals(RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT)) {
                    //从 APP 服务获取新 token，并重连
                } else {
                    //无法连接 IM 服务器，请根据相应的错误码作出对应处理
                }
            }

            /**
             *   本地消息数据库打开的回调
             * @param databaseOpenStatus
             */
            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {

            }
        });
    }


    /**
     * 获取运行时权限
     */
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            }, BAIDU_READ_PHONE_STATE);
        }
        connectRong();
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}