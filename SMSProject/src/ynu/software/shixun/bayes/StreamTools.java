package ynu.software.shixun.bayes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {

	/**
	 * 把输入流转化为字符串
	 * @param is
	 * @return
	 * @throws Exception 
	 */
	public static String readInputStream(InputStream is) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer))!=-1) {
			bos.write(buffer, 0, len);
		}
		is.close();
		bos.close();
		String text =  new String(bos.toByteArray(),"gb2312");
		return text;
	}
}













