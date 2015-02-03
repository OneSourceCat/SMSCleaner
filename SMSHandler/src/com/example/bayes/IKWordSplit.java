package com.example.bayes;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKWordSplit {
	
	public static String splitWithIKAnalyzer(String sInput,String splitSeperator)
	{
		String sOutput = "";
		//��String����תΪreader��
		StringReader re = new StringReader(sInput);
		//����IKSegmenter����
		IKSegmenter ik = new IKSegmenter(re,true);
		Lexeme lex = null;
		try {
			while((lex=ik.next())!=null){
				//�ִ�  ��splitSeperatorΪ��־�ָ��ʻ�
				sOutput = sOutput + lex.getLexemeText() + splitSeperator;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sOutput;
	}
	
	
	public static void main(String[] args) {
		String s = "��������ʱ���ɶ�������Ϊ���＾������ȫ��չ������ӭ�����������ӻԾ����Ӧ������ѯ�绰��087164771507��ʡ��������";
		String str = IKWordSplit.splitWithIKAnalyzer(s, "|");
		System.out.println(str);
	}
}
































