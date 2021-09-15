package com.example.rongcloud_android_imdemo_voicetotext;


import android.app.Application;

//import com.example.rongcloud_android_imdemo_voicetotext.HQ.HQVoiceMessageItemProvider;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import io.rong.imkit.IMCenter;
import io.rong.imkit.RongIM;
import io.rong.imkit.config.ConversationClickListener;
import io.rong.imkit.config.RongConfigCenter;
import io.rong.imkit.conversation.messgelist.provider.HQVoiceMessageItemProvider;
import io.rong.imkit.conversation.messgelist.provider.VoiceMessageItemProvider;
import io.rong.imkit.utils.RouteUtils;
import io.rong.imlib.RongIMClient;
import io.rong.message.HQVoiceMessage;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //        融云初始化
        RongIM.init(this, "8luwapkv86del");
//        讯飞语音识别
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + "f0bd82b4");
//        设置语音消息类型为语音消息
        RongIM.getInstance().setVoiceMessageType(IMCenter.VoiceMessageType.HighQuality);
//        自定义语音消息
        RongConfigCenter.conversationConfig().replaceMessageProvider(VoiceMessageItemProvider.class, new MyVoiceMessageItemProvider());
//        自定义高质量语音消息
        RongConfigCenter.conversationConfig().replaceMessageProvider(HQVoiceMessageItemProvider.class, new MyHQVoiceMessageItemProvider());

    }
}

