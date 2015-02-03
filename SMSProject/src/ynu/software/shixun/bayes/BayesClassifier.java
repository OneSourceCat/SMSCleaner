package ynu.software.shixun.bayes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;



/**
* 朴素贝叶斯分类器
*/
public class BayesClassifier 
{
	private TrainingDataManager tdm;//训练集管理器
	private static double zoomFactor = 10.0f;
	/**
	* 默认的构造器，初始化训练集
	*/
	public BayesClassifier() 
	{
		tdm =new TrainingDataManager();
	}

	/**
	* 计算给定的文本属性向量X在给定的分类Cj中的类条件概率
	* <code>ClassConditionalProbability</code>连乘值
	* @param X 给定的文本属性向量
	* @param Cj 给定的类别
	* @return 分类条件概率连乘值，即<br>
	*/
	float calcProd(String[] X, String Cj) 
	{
		float ret = 1.0F;
		// 类条件概率连乘
		for (int i = 0; i <X.length; i++)
		{
			String Xi = X[i];
			ret *=ClassConditionalProbability.calculatePxc(Xi, Cj)*zoomFactor;
			Xi=null;
		}
		// 再乘以先验概率
		ret *= PriorProbability.calculatePc(Cj);
		//int bitNum = MathUtil.getBitNum(ret);
		return ret;
	}
	/**
	* 去掉停用词
	* @param text 给定的文本
	* @return 去停用词后结果
	*/
	public String[] DropStopWords(String[] oldWords)
	{
		Vector<String> v1 = new Vector<String>();
		for(int i=0;i<oldWords.length;++i)
		{
			if(StopWordsHandler.IsStopWord(oldWords[i])==false)
			{//不是停用词
				v1.add(oldWords[i]);
			}
		}
		String[] newWords = new String[v1.size()];
		v1.toArray(newWords);
		v1=null;
		return newWords;
	}
	/**
	* 对给定的文本进行分类
	* @param text 给定的文本
	* @return 分类结果
	*/
	
	@SuppressWarnings("unchecked")
	public ClassifyResult badPro(String text){
		System.out.println("badPro start!!");
		String newtext = DropUtil.drop(text);
		//计算垃圾
		String[] terms= ChineseSpliter.split(newtext, " ").split(" ");//中文分词处理(分词后结果可能还包含有停用词）
		//String[] terms=IKWordSplit.splitWithIKAnalyzer(newtext, " ").split(" ");
		String[] newterms = DropStopWords(terms);//去掉停用词，以免影响分类
		System.out.println("getTraningClassifications()获取分类");
		String[] Classes = tdm.getTraningClassifications();//分类
		float probility = 0.0F;
		List<ClassifyResult> crs = new ArrayList<ClassifyResult>();//分类结果
		for (int i = 0; i <Classes.length; i++) 
		{
			String Ci = Classes[i];//第i个分类
			probility = calcProd(newterms, Ci);//计算给定的文本属性向量terms在给定的分类Ci中的分类条件概率
			System.out.println("calcProd()----win");
			//保存分类结果
			ClassifyResult cr = new ClassifyResult();
			cr.classification = Ci;//分类
			cr.probility = probility;//关键字在分类的条件概率
			//System.out.println("In process....");
			System.out.println(Ci + "：" + probility);
			crs.add(cr);
			Ci=null;
			cr=null;
		}
		
		//对最后概率结果进行排序
		java.util.Collections.sort(crs,new Comparator() 
		{
			public int compare(final Object o1,final Object o2) 
			{
				final ClassifyResult m1 = (ClassifyResult) o1;
				final ClassifyResult m2 = (ClassifyResult) o2;
				final double ret = m1.probility - m2.probility;
				if (ret < 0) 
				{
					System.out.println("两者的比例:"+m2.probility/m1.probility);
					m1.proportion=m2.probility/m1.probility;
					m2.proportion=m2.probility/m1.probility;
					return 1;
				} 
				else 
				{
					System.out.println("两者的比例:"+(m1.probility/m2.probility));
					m1.proportion=m1.probility/m2.probility;
					m2.proportion=m1.probility/m2.probility;
					return -1;
				}
			}
		});
		System.out.println("badPro end!!");
		return crs.get(0);
	}

}