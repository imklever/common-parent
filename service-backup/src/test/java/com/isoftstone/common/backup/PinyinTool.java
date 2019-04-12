package com.isoftstone.common.backup;

import com.alibaba.fastjson.JSONObject;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinTool {
	public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
		String str = "柳瑞川";
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		 format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		String py = "";
		String szm="";
		String temp = "";
		String spera = "";
		String[] t;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((int) c <= 128) {
				py += c;
			}
			else {
				t = PinyinHelper.toHanyuPinyinStringArray(c, format);
				if (t == null)
					py += c;
				else {
					temp = t[0];
					temp=temp.substring(0, temp.length()-1);
					py += temp + (i == str.length() - 1 ? "" : spera);
					szm+=temp.substring(0, 1);
				}
			}
		}
		System.out.println(py);
		System.out.println(szm);

	}

	public void toPinYin(String str, String spera) {

	}
}
