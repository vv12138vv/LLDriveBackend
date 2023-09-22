package com.lldrive.service;

import com.lldrive.service.impl.TokenServiceImpl;

public interface TokenService {

    enum Type{
        Regsiter,
        Login,
        Reset
    }
    void addToken(Type type, String key, Object value);

    Object getToken(Type type,String key);

    boolean updateToekn(Type type,String key);

}
