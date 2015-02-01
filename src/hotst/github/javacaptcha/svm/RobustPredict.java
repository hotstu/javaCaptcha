package hotst.github.javacaptcha.svm;

import hotst.github.javacaptcha.common.Constants;
import hotst.github.javacaptcha.model.BinaryMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RobustPredict {

	/**
	 * 转换为svm的格式
	 * 
	 * @param imageList
	 * @throws FileNotFoundException 
	 */
	public static  void svmFormat(List<BinaryMatrix> imageList) {

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File(Constants.SVM_TEST_FILE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		for (BinaryMatrix im : imageList) {
			int w = im.getWidth();
			int h = im.getHeight();
			int count = 1;
			StringBuilder sb = new StringBuilder();
			sb.append("-1 "); // 默认无标号，则为-1
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					// 黑色点标记为1
					sb.append(count);
					sb.append(':');
					if (im.getValue(x, y)) {
						sb.append('1');
					} else {
						sb.append('0');
					}
					sb.append(' ');
					count++;
				}
			}
			sb.append("\r\n");
			writer.write(sb.toString());
		}
		writer.flush();
		writer.close();

	}

	/**
	 * 公共接口
	 * 
	 * @param imageList
	 * @throws IOException
	 */
	public static void predict(List<BinaryMatrix> imageList)
			throws IOException {
		RobustPredict.svmFormat(imageList);

		// predict参数
		String[] parg = { Constants.SVM_TEST_FILE,"not use", Constants.SVM_RESULT_FILE };

		System.out.println("预测开始");
		RobustSVMPredict.main(parg);
		System.out.println("预测结束");
	}

	public static void main(String[] args) {
	}

}
