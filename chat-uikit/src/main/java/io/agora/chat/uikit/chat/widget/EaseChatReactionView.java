package io.agora.chat.uikit.chat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.agora.chat.uikit.R;
import io.agora.chat.uikit.adapter.ReactionGridAdapter;
import io.agora.chat.uikit.interfaces.OnItemClickListener;
import io.agora.chat.uikit.interfaces.OnItemLongClickListener;
import io.agora.chat.uikit.menu.EaseMessageMenuHelper;
import io.agora.chat.uikit.menu.EaseMessageMenuPopupWindow;
import io.agora.chat.uikit.menu.EaseMessageReactionHelper;
import io.agora.chat.uikit.models.EaseReactionEmojiconEntity;
import io.agora.chat.uikit.widget.EaseRecyclerView;
import io.agora.chat.uikit.widget.chatextend.RecyclerViewFlowLayoutManager;

public class EaseChatReactionView extends LinearLayout implements OnItemClickListener, OnItemLongClickListener {
    private final static String TAG = EaseChatReactionView.class.getSimpleName();
    private final ReactionGridAdapter mListAdapter;
    private List<EaseReactionEmojiconEntity> mData;
    private OnReactionItemListener mListener;
    private static final int MAX_REACTION_SHOW = 4;
    private String mMsgId;

    public EaseChatReactionView(Context context) {
        this(context, null);
    }

    public EaseChatReactionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public EaseChatReactionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.ease_widget_reaction_layout, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EaseChatReactionView);
        boolean isSender = typedArray.getBoolean(R.styleable.EaseChatReactionView_ease_chat_item_sender, false);
        typedArray.recycle();

        EaseRecyclerView reactionList;
        if (isSender) {
            reactionList = findViewById(R.id.rv_list_sender);
        } else {
            reactionList = findViewById(R.id.rv_list_received);
        }
        reactionList.setVisibility(View.VISIBLE);

        RecyclerViewFlowLayoutManager ms = new RecyclerViewFlowLayoutManager();
        reactionList.setLayoutManager(ms);

        mListAdapter = new ReactionGridAdapter();
        mListAdapter.setOnItemLongClickListener(this);
        mListAdapter.setOnItemClickListener(this);
        reactionList.setAdapter(mListAdapter);
        reactionList.addItemDecoration(new ReactionSpacesItemDecoration((int) EaseMessageMenuHelper.dip2px(context, 5)));

        reactionList.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    showMessageReaction();
                }
                return true;
            }
        });


        reactionList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // true: consume touch event
                // false: dispatch touch event
                return true;
            }
        });
    }


    private void showMessageReaction() {
        EaseMessageReactionHelper reactionHelper = new EaseMessageReactionHelper();
        reactionHelper.setReactionData(mData, mMsgId);
        reactionHelper.init(getContext(), new OnReactionItemListener() {
            @Override
            public void removeReaction(EaseReactionEmojiconEntity reactionEntity) {
                if (null != mListener) {
                    mListener.removeReaction(reactionEntity);
                }
            }

            @Override
            public void addReaction(EaseReactionEmojiconEntity reactionEntity) {
                if (null != mListener) {
                    mListener.addReaction(reactionEntity);
                }
            }
        });
        reactionHelper.setOutsideTouchable(true);
        reactionHelper.setOnPopupMenuDismissListener(new EaseMessageMenuPopupWindow.OnPopupWindowDismissListener() {
            @Override
            public void onDismiss(PopupWindow menu) {

            }
        });
        reactionHelper.show(this, this);
    }

    public void updateData(List<EaseReactionEmojiconEntity> data, String msgId) {
        mData = new ArrayList<>(data);
        mMsgId = msgId;
        int totalNum = 0;
        for (EaseReactionEmojiconEntity entity : mData) {
            totalNum += entity.getCount();
        }
        if (totalNum > MAX_REACTION_SHOW) {
            if (mData.size() > 99) {
                ReactionGridAdapter.setMoreTxt("... 99+");
            } else {
                ReactionGridAdapter.setMoreTxt("...");
            }
            List<EaseReactionEmojiconEntity> dataList = new ArrayList<>(MAX_REACTION_SHOW + 1);
            int length = mData.size();
            if (length > MAX_REACTION_SHOW) {
                length = MAX_REACTION_SHOW + 1;
            }
            for (int i = 0; i < length; i++) {
                dataList.add(mData.get(i));
            }
            mListAdapter.setData(dataList);
        } else {
            mListAdapter.setData(mData);
        }
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        return false;
    }

    public void setOnReactionItemListener(OnReactionItemListener onReactionItemListener) {
        mListener = onReactionItemListener;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public interface OnReactionItemListener {
        void removeReaction(EaseReactionEmojiconEntity reactionEntity);

        void addReaction(EaseReactionEmojiconEntity reactionEntity);
    }

    private static class ReactionSpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public ReactionSpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   RecyclerView parent, @NonNull RecyclerView.State state) {
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = 0;
            } else {
                outRect.left = space;
            }
        }
    }
}

