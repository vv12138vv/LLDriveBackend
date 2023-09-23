package com.lldrive.service;

import com.lldrive.domain.entity.User;

public interface TokenService {

    enum Type{
        Regsiter,
        Login,
        Reset
    }
    String addToken(Type type, String key, Object value);

    Object getValue(Type type, String key);

    boolean updateToken(Type type,String key);

    User getUser(Type type, String key);
}
