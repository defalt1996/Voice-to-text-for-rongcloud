package com.example.rongcloud_android_imdemo_voicetotext;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.text.TextUtilsCompat;

import com.example.rongcloud_android_imdemo_voicetotext.util.AudioDecode;
import com.example.rongcloud_android_imdemo_voicetotext.util.IflytekHandle;
import com.iflytek.cloud.SpeechRecognizer;

import java.util.List;
import java.util.Locale;

import io.rong.imkit.conversation.messgelist.provider.BaseMessageItemProvider;
import io.rong.imkit.conversation.messgelist.provider.MessageClickType;
import io.rong.imkit.manager.AudioRecordManager;
import io.rong.imkit.model.State;
import io.rong.imkit.model.UiMessage;
import io.rong.imkit.widget.adapter.IViewProviderListener;
import io.rong.imkit.widget.adapter.ViewHolder;
import io.rong.imkit.widget.dialog.OptionsPopupDialog;
import io.rong.imlib.common.NetUtils;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.HQVoiceMessage;

public class MyHQVoiceMessageItemProvider extends BaseMessageItemProvider<HQVoiceMessage> {
    //转换后显示文字
    private TextView textView;
    private SpeechRecognizer mIat;
    private AudioDecode audioDecode;
    private SharedPreferences mSharedPreferences;
    private Toast mToast;
    private View view;

    public MyHQVoiceMessageItemProvider() {
        mConfig.showReadState = true;
        mConfig.showContentBubble = false;
    }

    @Override
    protected ViewHolder onCreateMessageContentViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_hq_voice_message, parent, false);
        return new ViewHolder(parent.getContext(), view);
    }

    @Override
    protected void bindMessageContentViewHolder(ViewHolder holder, ViewHolder parentHolder, HQVoiceMessage message, UiMessage uiMessage, int position, List<UiMessage> list, IViewProviderListener<UiMessage> listener) {
        boolean isSender = uiMessage.getMessage().getMessageDirection().equals(Message.MessageDirection.SEND);
        holder.setBackgroundRes(io.rong.imkit.R.id.rc_voice_bg, isSender ? io.rong.imkit.R.drawable.rc_ic_bubble_right : io.rong.imkit.R.drawable.rc_ic_bubble_left);
        int minWidth = 70, maxWidth = 204;
        float scale = holder.getContext().getResources().getDisplayMetrics().density;
        minWidth = (int) (minWidth * scale + 0.5f);
        maxWidth = (int) (maxWidth * scale + 0.5f);

        int duration = AudioRecordManager.getInstance().getMaxVoiceDuration();
        ;
        holder.getView(io.rong.imkit.R.id.rc_voice_bg).getLayoutParams().width = minWidth + (maxWidth - minWidth) / duration * message.getDuration();
        if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL) {
            holder.setText(io.rong.imkit.R.id.rc_duration, String.format("\"%s", message.getDuration()));
        } else {
            holder.setText(io.rong.imkit.R.id.rc_duration, String.format("%s\"", message.getDuration()));
        }

        if (uiMessage.getMessage().getMessageDirection() == Message.MessageDirection.SEND) {
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.getContext().getResources().getDrawable(io.rong.imkit.R.drawable.rc_an_voice_send);
            holder.setVisible(io.rong.imkit.R.id.rc_voice, false);
            holder.setVisible(io.rong.imkit.R.id.rc_voice_send, true);
            ((TextView) holder.getView(io.rong.imkit.R.id.rc_duration)).setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.getView(io.rong.imkit.R.id.rc_duration).getLayoutParams();
            lp.setMarginEnd(12);
            holder.getView(io.rong.imkit.R.id.rc_duration).setLayoutParams(lp);
            if (uiMessage.isPlaying()) {
                holder.setImageDrawable(io.rong.imkit.R.id.rc_voice_send, animationDrawable);
                if (animationDrawable != null)
                    animationDrawable.start();
            } else {
                holder.setImageResource(io.rong.imkit.R.id.rc_voice_send, io.rong.imkit.R.drawable.rc_voice_send_play3);
            }
            holder.setVisible(io.rong.imkit.R.id.rc_voice_unread, false);
            holder.setVisible(io.rong.imkit.R.id.rc_voice_download_error, false);
            holder.setVisible(io.rong.imkit.R.id.rc_download_progress, false);
        } else {
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.getContext().getResources().getDrawable(io.rong.imkit.R.drawable.rc_an_voice_receive);
            holder.setVisible(io.rong.imkit.R.id.rc_voice, true);
            holder.setVisible(io.rong.imkit.R.id.rc_voice_send, false);
            ((TextView) holder.getView(io.rong.imkit.R.id.rc_duration)).setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.getView(io.rong.imkit.R.id.rc_duration).getLayoutParams();
            lp.setMarginStart(12);
            holder.getView(io.rong.imkit.R.id.rc_duration).setLayoutParams(lp);
            if (uiMessage.isPlaying()) {
                holder.setImageDrawable(io.rong.imkit.R.id.rc_voice, animationDrawable);
                if (animationDrawable != null)
                    animationDrawable.start();
            } else {
                holder.setImageResource(io.rong.imkit.R.id.rc_voice, io.rong.imkit.R.drawable.rc_voice_receive_play3);
            }

            if (message.getLocalPath() != null) {
                holder.setVisible(io.rong.imkit.R.id.rc_voice_download_error, false);
                holder.setVisible(io.rong.imkit.R.id.rc_download_progress, false);
                holder.setVisible(io.rong.imkit.R.id.rc_voice_unread, !uiMessage.getMessage().getReceivedStatus().isListened());
            } else {
                if (uiMessage.getState() == State.ERROR || !NetUtils.isNetWorkAvailable(holder.getContext())) {
                    holder.setVisible(io.rong.imkit.R.id.rc_voice_unread, false);
                    holder.setVisible(io.rong.imkit.R.id.rc_voice_download_error, true);
                    holder.setVisible(io.rong.imkit.R.id.rc_download_progress, false);
                } else if (uiMessage.getState() == State.PROGRESS) {
                    holder.setVisible(io.rong.imkit.R.id.rc_voice_unread, false);
                    holder.setVisible(io.rong.imkit.R.id.rc_voice_download_error, false);
                    holder.setVisible(io.rong.imkit.R.id.rc_download_progress, true);
                } else {
                    holder.setVisible(io.rong.imkit.R.id.rc_voice_unread, true);
                    holder.setVisible(io.rong.imkit.R.id.rc_voice_download_error, false);
                    holder.setVisible(io.rong.imkit.R.id.rc_download_progress, false);
                }
            }
        }
    }


    @Override
    protected boolean onItemClick(ViewHolder holder, HQVoiceMessage message, UiMessage uiMessage, int position, List<UiMessage> list, IViewProviderListener<UiMessage> listener) {
        if (listener != null) {
            listener.onViewClick(MessageClickType.AUDIO_CLICK, uiMessage);

            return true;
        }
        return false;
    }

//   长按实现语音转文字功能
    @Override
    protected boolean onItemLongClick(ViewHolder holder, HQVoiceMessage hqVoiceMessage, UiMessage uiMessage, int position, List<UiMessage> list, IViewProviderListener<UiMessage> listener) {
        if (listener != null) {

            String[] items = new String[]{"语音转文字"};
            /**
             * newInstance() 初始化OptionsPopupDialog
             * @param items弹出菜单功能选项
             * setOptionsPopupDialogListener()设置点击弹出菜单的监听
             * @param which表示点击的哪一个菜单项,与items的顺序一致
             * show()显示pop dialog
             */
            OptionsPopupDialog.newInstance(view.getContext(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {

                @Override
                public void onOptionsItemClicked(int which) {
                    if (which == 0) {
//                        讯飞语音转文字
                        new IflytekHandle(hqVoiceMessage.getLocalPath().toString(), view.getContext()) {
                            @Override
                            public void returnWords(String words) {
                                holder.setVisible(R.id.ifly, true);
                                if (TextUtils.isEmpty(words)) {
                                    words = "暂无翻译内容";
                                }
                                holder.setText(R.id.ifly, words);
                            }
                        };

                    }
                }
            }).show();
            return true;
        }
        return false;
    }

    @Override
    protected boolean isMessageViewType(MessageContent messageContent) {
        return messageContent instanceof HQVoiceMessage && !messageContent.isDestruct();
    }

    @Override
    public Spannable getSummarySpannable(Context context, HQVoiceMessage hqVoiceMessage) {
        return new SpannableString(context.getString(io.rong.imkit.R.string.rc_conversation_summary_content_voice));
    }
}
