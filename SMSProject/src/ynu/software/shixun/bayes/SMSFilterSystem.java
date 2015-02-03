package ynu.software.shixun.bayes;
/**
 * ������һ��������
 * ʵ�ֶ��Ź��ˣ����а���������С���ղ��Ե�����bayes��Ȩ���ٹ���ģ��
 * 1.����ƥ��   С��15�Ķ���ֱ���ж�Ϊ����
 * 2.��Ҷ˹����ģ��---->���
 * 3.Ȩֵ�ٹ���ϵͳ
 * @author chongrui
 */
public class SMSFilterSystem {

	
	public String SMSFilter(String text){
		String classify="";
		//1.���ȼ���bayesģ���Ȩֵ�ٹ���ģ��
		ClassifyResult result= new ClassifyResult();
		BayesClassifier bayesClassifier= new BayesClassifier();
		//����С��15�Ķ����ж�Ϊ��������
		if(text.length()<=13){
			System.out.println("length==="+text.length());
			classify="good";
			return classify;
		}
		//2.����bayes�������
		result = bayesClassifier.badPro(text);
		System.out.println("��Ҷ˹���----"+result.classification);
		System.out.println("�������ı���----"+result.proportion);
		System.out.println("���ر�Ҷ˹�Ľ���---�ı����ڣ�["+result.classification+"]....����Ϊ��"+result.probility);
		//3.������С���ս����ж�  ������������ʱ�����ϵΪ3------>ֱ���ж�����������ڹ���ģ��
		
		  if(result.proportion>2){
		     System.out.println("��������ֱ���ж�!");
		     classify = result.classification;
		  }else{
		     //�ٹ���ģ��
		     //���������ԣ������ٹ���ģ��
				FilterAgainModule filterAgainModule= new FilterAgainModule();
				System.out.println(text);
				int weight = filterAgainModule.getFinalWeight(text);
				//Ȩֵ
				if(weight>=5){
					System.out.println("ȨֵΪ�� "+weight);
					classify="bad";
				}else{
					System.out.println("ȨֵΪ�� "+weight);
					classify="good";
				}
		  }
		return classify;
	}

	public String NaiveBayesClassify(String text){
		String classify="";
		//1.���ȼ���bayesģ���Ȩֵ�ٹ���ģ��
		ClassifyResult result= new ClassifyResult();
		BayesClassifier bayesClassifier= new BayesClassifier();
		//2.����bayes�������
		result = bayesClassifier.badPro(text);
		classify=result.classification;
		System.out.println("��Ҷ˹���----"+result.classification);
		System.out.println("�������ı���----"+result.proportion);
		System.out.println("���ر�Ҷ˹�Ľ���---�ı����ڣ�["+result.classification+"]....����Ϊ��"+result.probility);
		return classify;
	}
}














































