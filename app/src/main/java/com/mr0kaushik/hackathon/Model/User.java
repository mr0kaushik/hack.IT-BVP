package com.mr0kaushik.hackathon.Model;

public class User {
    private String email;
    private String name;
    private long reward_count;
    private int rejected_count;
    private int approved_count;
    private int pending_count;
    private String img_url;

    public User() { }

    public User(String email, String name, long reward_count, int rejected_count, int approved_count, int pending_count, String img_url) {
        this.email = email;
        this.name = name;
        this.reward_count = reward_count;
        this.rejected_count = rejected_count;
        this.approved_count = approved_count;
        this.pending_count = pending_count;
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getReward_count() {
        return reward_count;
    }

    public void setReward_count(long reward_count) {
        this.reward_count = reward_count;
    }

    public int getRejected_count() {
        return rejected_count;
    }

    public void setRejected_count(int rejected_count) {
        this.rejected_count = rejected_count;
    }

    public int getApproved_count() {
        return approved_count;
    }

    public void setApproved_count(int approved_count) {
        this.approved_count = approved_count;
    }

    public int getPending_count() {
        return pending_count;
    }

    public void setPending_count(int pending_count) {
        this.pending_count = pending_count;
    }
}
