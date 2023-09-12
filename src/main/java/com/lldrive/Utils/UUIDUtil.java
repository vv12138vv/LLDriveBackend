package com.lldrive.Utils;

import java.util.UUID;

public class UUIDUtil {

    public static String generate(int num){
        UUID uuid=UUID.randomUUID();
        String uuidStr=uuid.toString().replaceAll("-","");
        String resUUID=uuidStr.substring(0,num);
        return resUUID;
    }
}
