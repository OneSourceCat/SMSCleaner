package com.example.bayes;

import java.util.Set;
import java.util.TreeSet;

/**
 * 这个类来去掉短信文本中的数字和字母
 * @author Administrator
 *
 */
public class DataUtil {

	/**
	 * 去掉text中的字母数字
	 * @param text
	 * @return
	 */
	public static String isData(String text){
		String pattern="[0-9a-zA-Z]";
		String newstr = text.replaceAll(pattern, "");
		return newstr;
	}
	
	/**
	 * 去掉字符数组中的重复的词语
	 * @param text
	 * @return
	 */
	public static String[] delreword(String[] text){
	    Set set=new TreeSet();
	    for(int i=0;i<text.length;i++){
	    	set.add(text[i]);
	    }
	    String[] newstr = (String[]) set.toArray(new String[0]);
	    return newstr;
	}
	
	
	public static void main(String[] args) {
//		String testStr="我是你的12的77三个人AS呢fafd";
//		String newstr= DataUtil.isData(testStr);
//	    System.out.println(newstr);
		
		String[] teststr={"哈哈","额","你好","我爱你","你好"};
		for(int i=0;i<teststr.length;i++){
			System.out.print(teststr[i]);
		}
		String[] newstr = DataUtil.delreword(teststr);
		System.out.println("\n\n");
		for(int j=0;j<newstr.length;j++){
			System.out.print(newstr[j]);
		}
	}
}
























