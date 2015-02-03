package com.example.bayes;

import java.util.Set;
import java.util.TreeSet;

/**
 * �������ȥ�������ı��е����ֺ���ĸ
 * @author Administrator
 *
 */
public class DataUtil {

	/**
	 * ȥ��text�е���ĸ����
	 * @param text
	 * @return
	 */
	public static String isData(String text){
		String pattern="[0-9a-zA-Z]";
		String newstr = text.replaceAll(pattern, "");
		return newstr;
	}
	
	/**
	 * ȥ���ַ������е��ظ��Ĵ���
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
//		String testStr="�������12��77������AS��fafd";
//		String newstr= DataUtil.isData(testStr);
//	    System.out.println(newstr);
		
		String[] teststr={"����","��","���","�Ұ���","���"};
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
























