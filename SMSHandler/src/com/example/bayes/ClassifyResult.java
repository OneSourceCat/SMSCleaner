package com.example.bayes;

/**
* ������
*/
public class ClassifyResult 
{
	public double probility;//�����ĸ���
	public String classification;//������
	public double proportion;  //�����ʵı���
	public ClassifyResult()//���캯���еı�����ʼ��
	{
		this.probility = 0;
		this.classification = null;
		this.proportion=0.0;
	}
}
