package ynu.software.shixun.bayes;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKWordSplit {
	
	public static String splitWithIKAnalyzer(String sInput,String splitSeperator)
	{
		String sOutput = "";
		StringReader re = new StringReader(sInput);
		IKSegmenter ik = new IKSegmenter(re,true);
		Lexeme lex = null;
		try {
			while((lex=ik.next())!=null){
				//System.out.print(lex.getLexemeText()+"  ");
				sOutput = sOutput + lex.getLexemeText() + splitSeperator;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(sOutput);
		return sOutput;
	}
	
	
	public static void main(String[] args) {
		String s = "��������ʱ���ɶ�������Ϊ���＾������ȫ��չ������ӭ�����������ӻԾ����Ӧ������ѯ�绰��087164771507��ʡ��������";
		String str = IKWordSplit.splitWithIKAnalyzer(s, "|");
		System.out.println(str);
	}
}































