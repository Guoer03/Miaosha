package com.guoer03.redis;
public class OrderKey extends KeyPrefix {
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
