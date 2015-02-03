package ynu.software.shixun.blacklistfilter;

public class DataUtil {
	public static String handleNumber(String number){
		System.out.println("here?");
		String newnumber = number.replace("-", "");
		System.out.println("really?"+number);
		return newnumber;
	}
}
