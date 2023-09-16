package com.lldrive.Utils;

public class FileUtil {

    public static String getFileExtension(String fileName){
        int dotIndex=fileName.lastIndexOf('.');
        if(dotIndex>=0&&dotIndex<fileName.length()-1){
            return fileName.substring(dotIndex+1);
        }else{
            return null;
        }
    }
}
