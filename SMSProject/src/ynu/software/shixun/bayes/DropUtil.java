package ynu.software.shixun.bayes;

import java.util.regex.Pattern;


public class DropUtil {
	public static String drop(String text){
		String urlReg="(((http://)?([A-Za-z0-9]+[.])|(www.))\\w+[.]([A-Za-z0-9]{2,4})"+
				"?[[.]([A-Za-z0-9]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.]"+
				"[A-Za-z0-9]{2,4}+|/?)|(\\w*))";   //去掉网址 
//		for(int i=0;i<text.length;i++){
//			text[i] = text[i].replaceAll(urlReg, "");
//		}
		return text.replaceAll(urlReg, "");
	}
	
	public static void main(String[] args) {
		String text = "尊敬的用户，最大网商交易平台《淘宝网》提醒您的手机号码已获得首次面向全国举办的送礼活动幸运二等奖，您将获得由《淘宝网》基金送出的100000元（RMB）以及苹果笔记本电脑一台，请使用电脑登录活动站点www.tianmaosecur.com验收，获取编号验证码9588（注将个人获奖信息泄露导致他人冒领奖项，概不负责！）此次活动受法律保护，请认真对待。";
		String[] textStr = ChineseSpliter.split(text, " ").split(" ");
		System.out.println(DropUtil.drop(text));
//		for(int i=0;i<textStr.length;i++){
//			System.out.print(textStr[i].trim()+"|");
//		}
	}
	
}
