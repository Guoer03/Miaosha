package com.guoer03.redis;
public abstract class KeyPrefix {
    private int expireSeconds;
    private String prefix;
    public KeyPrefix(String prefix) {//0代表永不过期
        this(0, prefix);
    }
    public KeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }
    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":" + prefix;
    }
}
