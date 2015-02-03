package com.example.bayes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;


/**
* ���ر�Ҷ˹������
*/
public class BayesClassifier 
{
	private TrainingDataManager tdm;//ѵ����������
	private String trainnigDataPath;//ѵ����·��
	private static double zoomFactor = 100.0f;
	/**
	* Ĭ�ϵĹ���������ʼ��ѵ����
	*/
	public BayesClassifier() 
	{
		tdm =new TrainingDataManager();
	}

	/**
	* ����������ı���������X�ڸ����ķ���Cj�е�����������
	* <code>ClassConditionalProbability</code>����ֵ
	* @param X �������ı���������
	* @param Cj ���������
	* @return ����������������ֵ����<br>
	*/
	float calcProd(String[] X, String Cj) 
	{
		float ret = 1.0F;
		int count =0;
		// ��������������
		for (int i = 0; i <X.length; i++)
		{
			String Xi = X[i];
			ret *=ClassConditionalProbability.calculatePxc(Xi, Cj)*zoomFactor;
			count++;
		}
		// �ٳ����������
		ret *= PriorProbability.calculatePc(Cj);
		//int bitNum = MathUtil.getBitNum(ret);
		return ret;
	}
	/**
	* ȥ��ͣ�ô�
	* @param text �������ı�
	* @return ȥͣ�ôʺ���
	*/
	public String[] DropStopWords(String[] oldWords)
	{
		Vector<String> v1 = new Vector<String>();
		for(int i=0;i<oldWords.length;++i)
		{
			if(StopWordsHandler.IsStopWord(oldWords[i])==false)
			{//����ͣ�ô�
				v1.add(oldWords[i]);
			}
		}
		String[] newWords = new String[v1.size()];
		v1.toArray(newWords);
		return newWords;
	}
	/**
	* �Ը������ı����з���
	* @param text �������ı�
	* @return ������
	*/
	
	@SuppressWarnings("unchecked")
	public ClassifyResult badPro(String text){
		String newtext = DropUtil.drop(text);
		String[] terms=IKWordSplit.splitWithIKAnalyzer(newtext, " ").split(" ");
		String[] newterms = DropStopWords(terms);//ȥ��ͣ�ôʣ�����Ӱ�����
		for(int i=0;i<newterms.length;i++){
			System.out.print("|"+newterms[i]);
		}
		System.out.println("\n");
		String[] Classes = tdm.getTraningClassifications();//����
		float probility = 0.0F;
		List<ClassifyResult> crs = new ArrayList<ClassifyResult>();//������
		for (int i = 0; i <Classes.length; i++) 
		{
			System.out.println(i);
			String Ci = Classes[i];//��i������
			probility = calcProd(newterms, Ci);//����������ı���������terms�ڸ����ķ���Ci�еķ�����������
			//���������
			ClassifyResult cr = new ClassifyResult();
			cr.classification = Ci;//����
			cr.probility = probility;//�ؼ����ڷ������������
			System.out.println(Ci + "��" + probility);
			crs.add(cr);
		}
		
		//�������ʽ����������
		java.util.Collections.sort(crs,new Comparator() 
		{
			public int compare(final Object o1,final Object o2) 
			{
				//����
				final ClassifyResult m1 = (ClassifyResult) o1;
				//����
				final ClassifyResult m2 = (ClassifyResult) o2;
				final double ret = m1.probility - m2.probility;
				if (ret < 0) 
				{   //�����ĸ���С������
					System.out.println("���ߵı���:"+m2.probility/m1.probility);
					m1.proportion=m2.probility/m1.probility;
					m2.proportion=m2.probility/m1.probility;
					return 1;
				} 
				else 
				{	//�����ĸ��ʴ�������
					System.out.println("���ߵı���:"+(m1.probility/m2.probility));
					m1.proportion=m1.probility/m2.probility;
					m2.proportion=m1.probility/m2.probility;
					return -1;
				}
			}
		});
		return crs.get(0);
	}

}