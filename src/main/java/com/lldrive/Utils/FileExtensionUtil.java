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
        //文档
        extensionMap.put("pdf",4);
        extensionMap.put("md",4);
        //特殊文档
        extensionMap.put("doc",5);
        extensionMap.put("docx",5);
        extensionMap.put("xls",6);
        extensionMap.put("xlsx",6);
        extensionMap.put("txt",7);
        //程序
        extensionMap.put("c",8);
        extensionMap.put("py",8);
        extensionMap.put("cpp",8);
        extensionMap.put("java",8);
        //压缩包
        extensionMap.put("zip",9);
        extensionMap.put("7z",9);
        extensionMap.put("rar",9);
    }

    public static int getMappingValue(String extension) {
        // 获取给定后缀名的映射值，如果找不到映射关系，则返回默认值或抛出异常，根据需求决定
        return extensionMap.getOrDefault(extension.toLowerCase(), 10);
    }
}
