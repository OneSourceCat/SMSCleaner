package com.example.bayes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类实现再过滤模块 
 * 
 * 1.检测短信的长度
 * 2.检测短信中是否含有网址
 * 3.检测短信中是否含有电话号码
 * 4.检测短信中的标点符号是否多于3个
 * 5.检测短信中是否含有要求回复的信息
 * 6.号码为陌生号码
 * @author chongrui
 *
 */
public class FilterAgainModule {

	/**
	 * 检测短信的长度
	 * @param text
	 * @return
	 */
	private int getLength(String text){
		return text.length();
	}
	/**
	 * 由短信求检测短信长度后的weight值
	 * @param text    短信的content
	 */
	public int getLengthWeight(String text){
		int length=getLength(text);
		//System.out.println("length===="+length);
		int weight=0;
		if(length>=100){
			weight+=5;
		}else if(length>=70){
			weight+=4;
		}else if(length>=40){
			weight+=3;
		}
		//System.out.println("weight-len:"+weight);
		return weight;
	}
	/**
	 * 检测短信中的URL
	 * @param text
	 */
	public int getUrlWeight(String text){
		int weight = 0;
		//对URL的正则匹配
		String regex="((http://)?([A-Za-z0-9]+[.])|(www.))\\w+[.]([A-Za-z0-9]{2,4})"+
						"?[[.]([A-Za-z0-9]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.]"+
						"[A-Za-z0-9]{2,4}+|/?)";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if(matcher.find()){
			weight+=4;
		}
		//System.out.println("weight-url:"+weight);
		return weight;
	}
	/**
	 * 检测短信中的电话号码
	 * @param text
	 */
	public int getPhoneNumberWeight(String text){
		int weight = 0;
		String regexPhoneNumber="(1[3458]\\d{9})|(((\\d{3,4})?\\d{7,8}))";
		Pattern pattern=Pattern.compile(regexPhoneNumber);
		Matcher matcher=pattern.matcher(text);
		if(matcher.find()){
			weight+=3;
		}
		//System.out.println("weight-number:"+weight);
		return weight;
	}
	/**
	 *  检测文本中使用的标点符号的个数来增加权值
	 */
	public int getPunctuationWeight(String text){
		int weight = 0;
		int count=0;//计数器 
		String punctuations[]={"(","（",",","，","。","、","；",
				"：","！",",",".","/","\\",";",":","!","【","[","《","%","*","'","‘"};
		for(int i=0;i<punctuations.length;i++){
			if(text.indexOf(punctuations[i])>=0){
				count++;
			}
		}
		//有三个不同的标点
		if(count>=3){
			weight+=2;
		}
		return weight;
	}
	/**
	 * 
	 */
	public int getResponseFlagWeight(String text){
		int weight = 0;
		//待补充    这里要翻看训练集里的短信添加
		String[] flags = {"咨询","电话","回复","询","热线","联系","发送","详询","垂询","联系人","详情"};
		for(int i=0;i<flags.length;i++){
			
			if(text.indexOf(flags[i])>=0){
				weight+=3;
				break;
			}
		}
		return weight ; 
	}

	public int getFinalWeight(String text){
		int weight = 0;
		weight = getLengthWeight(text)+
				getPhoneNumberWeight(text)+
				getUrlWeight(text)+
				getResponseFlagWeight(text)+
				getPunctuationWeight(text);
		return weight;
	}
	

	public static void main(String[] args) {
		String text="起这么早啊，等会洗完脸";
		FilterAgainModule filterAgainModule= new FilterAgainModule();
		int weight = filterAgainModule.getFinalWeight(text);
		//System.out.println(weight);
	}
	
}













































