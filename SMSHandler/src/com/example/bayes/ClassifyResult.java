package com.example.bayes;

/**
* 分类结果
*/
public class ClassifyResult 
{
	public double probility;//分类后的概率
	public String classification;//分类结果
	public double proportion;  //类别概率的比例
	public ClassifyResult()//构造函数中的变量初始化
	{
		this.probility = 0;
		this.classification = null;
		this.proportion=0.0;
	}
}
