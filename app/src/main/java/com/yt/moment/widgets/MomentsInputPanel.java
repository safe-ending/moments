//package com.yt.moment;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.text.Editable;
//import android.text.Selection;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentActivity;
//
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.OnTextChanged;
//
//public class MomentsInputPanel extends FrameLayout implements IEmotionSelectedListener {
//    @Bind(R.id.audioImageView)
//    ImageView audioImageView;
//    @Bind(R.id.audioButton)
//    TextView audioButton;
//    @Bind(R.id.editText)
//    EditText editText;
//    @Bind(R.id.emotionImageView)
//    ImageView emotionImageView;
//
//    @Bind(R.id.sendButton)
//    Button sendButton;
//
//    @Bind(R.id.emotionContainerFrameLayout)
//    KeyboardHeightFrameLayout emotionContainerFrameLayout;
//    @Bind(R.id.emotionLayout)
//    EmotionLayout emotionLayout;
//    @Bind(R.id.extContainerContainerLayout)
//    KeyboardHeightFrameLayout extContainerFrameLayout;
//
//    @Bind(R.id.inputPanelFrameLayout)
//    FrameLayout inputContainerFrameLayout;
//    @Bind(R.id.conversationExtViewPager)
//    ViewPagerFixed extViewPager;
//
//    ConversationExtension extension;
//    private InputAwareLayout rootLinearLayout;
//    private FragmentActivity activity;
//
//    private String draftString;
//    private static final int TYPING_INTERVAL_IN_SECOND = 10;
//    private SharedPreferences sharedPreferences;
//
//    private OnCommentsInputPanelStateChangeListener onCommentsInputPanelStateChangeListener;
//
//    public MomentsInputPanel(@NonNull Context context) {
//        super(context);
//    }
//
//    public MomentsInputPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//
//    }
//
//    public MomentsInputPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public MomentsInputPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
//
//    public void setOnCommentsInputPanelStateChangeListener(OnCommentsInputPanelStateChangeListener onConversationInputPanelStateChangeListener) {
//        this.onCommentsInputPanelStateChangeListener = onConversationInputPanelStateChangeListener;
//    }
//
//    public void bind(FragmentActivity activity, InputAwareLayout rootInputAwareLayout) {
//
//    }
//
//    public void setupConversation(String userId, String parentId) {
//        editText.requestFocus();
//        editText.setText("回复" + userId);
//        editText.setSelection(2 + (TextUtils.isEmpty(userId) ? 0 : userId.length()));
//        //TODO 保存草稿
////        ConversationInfo conversationInfo = conversationViewModel.getConversationInfo(conversation);
////        if (conversationInfo == null) {
////            return;
////        }
////        Draft draft = Draft.fromDraftJson(conversationInfo.draft);
////        draftString = draft == null ? "" : draft.getContent();
////        editText.setText(draftString);
//    }
//
//    public void init(FragmentActivity activity, InputAwareLayout rootInputAwareLayout) {
//        LayoutInflater.from(getContext()).inflate(R.layout.moments_input_panel, this, true);
//        ButterKnife.bind(this, this);
//
//        this.activity = activity;
//        this.rootLinearLayout = rootInputAwareLayout;
//
//        this.extension = new ConversationExtension(activity, this, extViewPager);
//
//
//        sharedPreferences = getContext().getSharedPreferences("sticker", Context.MODE_PRIVATE);
//
//        // emotion
//        emotionLayout.attachEditText(editText);
//        emotionLayout.setEmotionAddVisiable(false);
//        emotionLayout.setEmotionSettingVisiable(false);
//
//        editText.setOnKeyListener((v, keyCode, event) -> {
//            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
//                Editable buffer = ((EditText) v).getText();
//                // If the cursor is at the end of a MentionSpan then remove the whole span
//                int start = Selection.getSelectionStart(buffer);
//                int end = Selection.getSelectionEnd(buffer);
//                if (start == end) {
//                    MentionSpan[] mentions = buffer.getSpans(start, end, MentionSpan.class);
//                    if (mentions.length > 0) {
//                        buffer.replace(
//                                buffer.getSpanStart(mentions[0]),
//                                buffer.getSpanEnd(mentions[0]),
//                                ""
//                        );
//                        buffer.removeSpan(mentions[0]);
//                        return true;
//                    }
//                }
//                return false;
//            }
//            return false;
//        });
//
//
//        // emotion
//        emotionLayout.setEmotionSelectedListener(this);
//        emotionLayout.setEmotionExtClickListener(new IEmotionExtClickListener() {
//            @Override
//            public void onEmotionAddClick(View view) {
//                Toast.makeText(activity, "add", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onEmotionSettingClick(View view) {
//                Toast.makeText(activity, "setting", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @OnClick(R.id.emotionImageView)
//    void onEmotionImageViewClick() {
//        if (rootLinearLayout.getCurrentInput() == emotionContainerFrameLayout) {
//            hideEmotionLayout();
//        } else {
//            showEmotionLayout();
//            hideAudioButton();
//
//        }
//    }
//
//    private void showAudioButton() {
//        audioButton.setVisibility(View.VISIBLE);
//        editText.setVisibility(View.GONE);
//        sendButton.setVisibility(View.GONE);
//        audioImageView.setImageResource(R.mipmap.ic_cheat_keyboard);
//        rootLinearLayout.hideCurrentInput(editText);
//        rootLinearLayout.hideAttachedInput(true);
//    }
//
//
//    private void hideAudioButton() {
//        audioButton.setVisibility(View.GONE);
//        editText.setVisibility(View.VISIBLE);
//        if (TextUtils.isEmpty(editText.getText())) {
//            sendButton.setVisibility(View.GONE);
//        } else {
//            sendButton.setVisibility(View.VISIBLE);
//        }
//        rootLinearLayout.show(editText, emotionContainerFrameLayout);
//        audioImageView.setImageResource(R.mipmap.ic_cheat_voice);
//    }
//
//    @OnTextChanged(value = R.id.editText, callback = OnTextChanged.Callback.TEXT_CHANGED)
//    void onInputTextChanged(CharSequence s, int start, int before, int count) {
//        if (activity.getCurrentFocus() == editText) {
//
//        }
//    }
//
//
//    @OnTextChanged(value = R.id.editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    void afterInputTextChanged(Editable editable) {
//        if (editText.getText().toString().trim().length() > 0) {
//            sendButton.setVisibility(View.VISIBLE);
//        } else {
//            sendButton.setVisibility(View.GONE);
//        }
//    }
//
//    @OnClick(R.id.sendButton)
//    void sendMessage() {
//        Editable content = editText.getText();
//        if (TextUtils.isEmpty(content)) {
//            return;
//        }
//        if (onCommentsInputPanelStateChangeListener != null) {
//            onCommentsInputPanelStateChangeListener.sendMessage(content.toString().trim());
//            editText.setText("");
//            onCommentsInputPanelStateChangeListener.onInputPanelCollapsed();
//        }
//    }
//
//
//    private void showEmotionLayout() {
//        audioButton.setVisibility(View.GONE);
//        emotionImageView.setImageResource(R.mipmap.ic_cheat_keyboard);
//        if (onCommentsInputPanelStateChangeListener != null) {
//            onCommentsInputPanelStateChangeListener.onInputPanelExpanded();
//        }
//    }
//
//    private void hideEmotionLayout() {
//        emotionImageView.setImageResource(R.mipmap.ic_cheat_emo);
//        if (onCommentsInputPanelStateChangeListener != null) {
//            onCommentsInputPanelStateChangeListener.onInputPanelCollapsed();
//        }
//    }
//
//    void collapse() {
//        extension.reset();
//        emotionImageView.setImageResource(R.mipmap.ic_cheat_emo);
//        rootLinearLayout.hideAttachedInput(true);
//        rootLinearLayout.hideCurrentInput(editText);
//        if (onCommentsInputPanelStateChangeListener != null) {
//            onCommentsInputPanelStateChangeListener.onInputPanelCollapsed();
//        }
//    }
//
//
//    @Override
//    public void onEmojiSelected(String key) {
////        LogBox.e("onEmojiSelected : " + key);
//    }
//
//    @Override
//    public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
//
//    }
//
//
//    public interface OnCommentsInputPanelStateChangeListener {
//        /**
//         * 输入面板展开
//         */
//        void onInputPanelExpanded();
//
//        /**
//         * 输入面板关闭
//         */
//        void onInputPanelCollapsed();
//
//        /**
//         * 发送消息
//         */
//        void sendMessage(String text);
//    }
//}
