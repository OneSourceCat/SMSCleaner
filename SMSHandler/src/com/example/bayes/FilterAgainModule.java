package com.example.bayes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �����ʵ���ٹ���ģ�� 
 * 
 * 1.�����ŵĳ���
 * 2.���������Ƿ�����ַ
 * 3.���������Ƿ��е绰����
 * 4.�������еı������Ƿ����3��
 * 5.���������Ƿ���Ҫ��ظ�����Ϣ
 * 6.����Ϊİ������
 * @author chongrui
 *
 */
public class FilterAgainModule {

	/**
	 * �����ŵĳ���
	 * @param text
	 * @return
	 */
	private int getLength(String text){
		return text.length();
	}
	/**
	 * �ɶ���������ų��Ⱥ��weightֵ
	 * @param text    ���ŵ�content
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
	 * �������е�URL
	 * @param text
	 */
	public int getUrlWeight(String text){
		int weight = 0;
		//��URL������ƥ��
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
	 * �������еĵ绰����
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
	 *  ����ı���ʹ�õı����ŵĸ���������Ȩֵ
	 */
	public int getPunctuationWeight(String text){
		int weight = 0;
		int count=0;//������ 
		String punctuations[]={"(","��",",","��","��","��","��",
				"��","��",",",".","/","\\",";",":","!","��","[","��","%","*","'","��"};
		for(int i=0;i<punctuations.length;i++){
			if(text.indexOf(punctuations[i])>=0){
				count++;
			}
		}
		//��������ͬ�ı��
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
		//������    ����Ҫ����ѵ������Ķ������
		String[] flags = {"��ѯ","�绰","�ظ�","ѯ","����","��ϵ","����","��ѯ","��ѯ","��ϵ��","����"};
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
		String text="����ô�簡���Ȼ�ϴ����";
		FilterAgainModule filterAgainModule= new FilterAgainModule();
		int weight = filterAgainModule.getFinalWeight(text);
		//System.out.println(weight);
	}
	
}













































