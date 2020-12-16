package com.guoer03.redis;

public class UserKey extends KeyPrefix{
    public UserKey(String prefix) {
        super(prefix);
    }
    public static UserKey getById=new UserKey("id");
    public static UserKey getByName=new UserKey("name");
}
