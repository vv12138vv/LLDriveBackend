package com.lldrive.Utils;

import java.util.HashMap;
import java.util.Map;

public class FileExtensionUtil {
    private static final Map<String, Integer> extensionMap;

    static {
        extensionMap=new HashMap<>();
        extensionMap.put("folder",0);
        //video
        extensionMap.put("mp4",1);
        extensionMap.put("avi",1);
        extensionMap.put("mkv",1);
        extensionMap.put("rmvb",1);
        extensionMap.put("mov",1);
        //music
        extensionMap.put("mp3",2);
        extensionMap.put("wav",2);
        extensionMap.put("wma",2);
        extensionMap.put("flac",2);
        //image
        extensionMap.put("jpg",3);
        extensionMap.put("png",3);
        extensionMap.put("jpeg",3);
        extensionMap.put("svg",3);
        extensionMap.put("gif",3);
        //doc
        extensionMap.put("pdf",4);
        extensionMap.put("doc",4);
        extensionMap.put("docx",4);
        extensionMap.put("xls",4);
        extensionMap.put("md",4);
        extensionMap.put("txt",4);
    }

    public static int getMappingValue(String extension) {
        // 获取给定后缀名的映射值，如果找不到映射关系，则返回默认值或抛出异常，根据需求决定
        return extensionMap.getOrDefault(extension.toLowerCase(), 0);
    }
}
