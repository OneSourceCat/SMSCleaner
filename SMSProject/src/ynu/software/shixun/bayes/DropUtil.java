package ynu.software.shixun.bayes;

import java.util.regex.Pattern;


public class DropUtil {
	public static String drop(String text){
		String urlReg="(((http://)?([A-Za-z0-9]+[.])|(www.))\\w+[.]([A-Za-z0-9]{2,4})"+
				"?[[.]([A-Za-z0-9]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.]"+
				"[A-Za-z0-9]{2,4}+|/?)|(\\w*))";   //ȥ����ַ 
//		for(int i=0;i<text.length;i++){
//			text[i] = text[i].replaceAll(urlReg, "");
//		}
		return text.replaceAll(urlReg, "");
	}
	
	public static void main(String[] args) {
		String text = "�𾴵��û���������̽���ƽ̨���Ա��������������ֻ������ѻ���״�����ȫ���ٰ���������˶��Ƚ�����������ɡ��Ա����������ͳ���100000Ԫ��RMB���Լ�ƻ���ʼǱ�����һ̨����ʹ�õ��Ե�¼�վ��www.tianmaosecur.com���գ���ȡ�����֤��9588��ע�����˻���Ϣй¶��������ð�콱��Ų����𣡣��˴λ�ܷ��ɱ�����������Դ���";
		String[] textStr = ChineseSpliter.split(text, " ").split(" ");
		System.out.println(DropUtil.drop(text));
//		for(int i=0;i<textStr.length;i++){
//			System.out.print(textStr[i].trim()+"|");
//		}
	}
	
}
