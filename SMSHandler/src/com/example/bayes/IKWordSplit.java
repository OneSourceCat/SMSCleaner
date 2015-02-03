package com.example.bayes;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKWordSplit {
	
	public static String splitWithIKAnalyzer(String sInput,String splitSeperator)
	{
		String sOutput = "";
		//将String类型转为reader类
		StringReader re = new StringReader(sInput);
		//创建IKSegmenter对象
		IKSegmenter ik = new IKSegmenter(re,true);
		Lexeme lex = null;
		try {
			while((lex=ik.next())!=null){
				//分词  以splitSeperator为标志分隔词汇
				sOutput = sOutput + lex.getLexemeText() + splitSeperator;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sOutput;
	}
	
	
	public static void main(String[] args) {
		String s = "今年征兵时间由冬季调整为夏秋季，现已全面展开，欢迎广大适龄青年踊跃报名应征。咨询电话：087164771507。省征兵办宣";
		String str = IKWordSplit.splitWithIKAnalyzer(s, "|");
		System.out.println(str);
	}
}
































