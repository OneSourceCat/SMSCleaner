package com.example.bayes;
/**
 *  ���������ʼ���
		����������
		
		P(xj|cj)=( N(X=xi, C=cj )+1 ) / ( N(C=cj)+M+V ) 
		���У�N(X=xi, C=cj����ʾ���cj�а�������x i��ѵ���ı�������
		N(C=cj)��ʾ���cj�е�ѵ���ı�������
		Mֵ���ڱ��� N(X=xi, C=cj����С�����������⣻
		V��ʾ����������
		
		��������
		���� ��A, B�������¼�����P(A)>0 ��
		P(B�OA)=P(AB)/P(A)
 * @author Administrator
 *
 */

public class ClassConditionalProbability 
{
	private static TrainingDataManager tdm = new TrainingDataManager();
	private static final float M = 0F;
	
	/**
	* ��������������
	* @param x �������ı�����
	* @param c �����ķ���
	* @return ���������µ�����������
	*/
	public static float calculatePxc(String x, String c) 
	{
		float ret = 0F;
		//���ظ��������а����ؼ��֣��ʵ�ѵ���ı�����Ŀ
		float Nxc = tdm.getCountContainKeyOfClassification(c, x);
		//����ѵ���ı������ڸ��������µ�ѵ���ı���Ŀ
		float Nc = tdm.getTrainingFileCountOfClassification(c);
		//����ѵ���ı�������������Ŀ¼��
		float V = tdm.getTraningClassifications().length;
		ret = (Nxc + 1) / (Nc + M + 2);
		return ret;
	}
}
