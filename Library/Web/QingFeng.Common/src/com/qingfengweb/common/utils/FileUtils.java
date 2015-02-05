package com.qingfengweb.common.utils;

public class FileUtils {
	/**
	 * 获取扩展名
	 * @param filename 文件名
	 * @return 返回获取的扩展名
	 */
	public static String getExtension(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int index = filename.lastIndexOf('.');   
            if ((index >-1) && (index < (filename.length() - 1))) {   
                return filename.substring(index);   
            }   
        }   
        return filename;   
    }
}
