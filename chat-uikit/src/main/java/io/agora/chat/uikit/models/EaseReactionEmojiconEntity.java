package io.agora.chat.uikit.models;

import androidx.annotation.NonNull;

import java.util.List;

public class EaseReactionEmojiconEntity {
    private EaseEmojicon emojicon;
    private int count;
    private List<String> userList;
    private boolean state;

    public EaseEmojicon getEmojicon() {
        return emojicon;
    }

    public void setEmojicon(EaseEmojicon emojicon) {
        this.emojicon = emojicon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @NonNull
    @Override
    public String toString() {
        return "EaseReactionEmojiconEntity{" +
                "emojicon=" + emojicon +
                ", count=" + count +
                ", userList=" + userList +
                ", state=" + state +
                '}';
    }
}
