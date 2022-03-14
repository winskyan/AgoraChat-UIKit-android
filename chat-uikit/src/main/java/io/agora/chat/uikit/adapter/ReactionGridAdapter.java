package io.agora.chat.uikit.adapter;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import io.agora.chat.uikit.R;
import io.agora.chat.uikit.models.EaseReactionEmojiconEntity;


public class ReactionGridAdapter extends EaseBaseRecyclerViewAdapter<EaseReactionEmojiconEntity> {
    private static String mMoreTxt;


    @Override
    public ReactionViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ease_row_reaction, parent, false);
        return new ReactionViewHolder(view);
    }

    public static void setMoreTxt(String moreTxt) {
        mMoreTxt = moreTxt;
    }

    private static class ReactionViewHolder extends ViewHolder<EaseReactionEmojiconEntity> {
        private ImageView emojicon;
        private TextView count;

        public ReactionViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void initView(View itemView) {
            emojicon = findViewById(R.id.iv_emojicon);
            count = findViewById(R.id.tv_count);
        }

        @Override
        public void setData(EaseReactionEmojiconEntity item, int position) {
            if (position >= 4) {
                emojicon.setVisibility(View.GONE);
                SpannableString spanString = new SpannableString(mMoreTxt);
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                spanString.setSpan(span, 0, spanString.length(), Spannable.SPAN_COMPOSING);
                count.setText(spanString);
            } else {
                if (0 != item.getEmojicon().getIcon()) {
                    emojicon.setImageResource(item.getEmojicon().getIcon());
                }
                if (1 == item.getCount()) {
                    count.setVisibility(View.GONE);
                } else {
                    count.setVisibility(View.VISIBLE);
                    count.setText(String.valueOf(item.getCount()));
                }
            }
        }
    }
}
