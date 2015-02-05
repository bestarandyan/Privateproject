/**
 * 
 */
package com.boluomi.children.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author 刘星星
 * @createDate 2013/1/15
 * 汉字转化为拼音的类
 *
 */
public class ChineseToEnglish {
	public static String toEnglish(char chinese){
		HanyuPinyinOutputFormat hanyupinyin = new HanyuPinyinOutputFormat();
		hanyupinyin.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hanyupinyin.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hanyupinyin.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
		String[] pinyinArray = null;
		//是否在汉字范围内 
		if(chinese >= 0x4e00 && chinese <= 0x9fa5){
			try {
				pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chinese,hanyupinyin);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
			
		}else{
			return java.lang.Character.toString(chinese).substring(0,1);
		}
		if(pinyinArray == null){
			pinyinArray = new String[3];
			pinyinArray[0] = "1";
		}
		return pinyinArray[0];
	}
}
