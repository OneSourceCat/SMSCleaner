package ynu.software.shixun.bayes;

/**
* ������
*/
public class ClassifyResult 
{
	public double probility;//����ĸ���
	public String classification;//����
	public double proportion;
	public ClassifyResult()
	{
		this.probility = 0;
		this.classification = null;
		this.proportion=0.0;
	}
}
