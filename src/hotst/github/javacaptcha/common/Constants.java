package hotst.github.javacaptcha.common;

/**
 * 一些常数，根据不同验证码进行调整
 * @author foo
 *
 */
public class Constants {
	
	/**
	 * 一个字符切片的宽度最小像素，小于这个值将被视为噪点被抛弃
	 */
	public static final int MIN_RECT_WIDTH = 5;
	
	/**
	 * 一个字符切片的高度最小像素，小于这个值将被视为噪点被抛弃
	 */
	public static final int MIN_RECT_HEGITH = 8;
	
	/**
	 * 一个字符切片的矩形区域类最小黑色像素总数，小于这个值将被视为噪点被抛弃
	 */
	public static final int MIN_RECT_PIXELS = 30;
	
	/**
	 * 平均字符宽度
	 */
	public static final int AVER_CHAR_WIDTH = 12;
	
	/**
	 * 查找字符边境时的左右偏移量，例如，在[x-a,x+a]的地方查询交界点中的a
	 */
	public static final int CHAR_RANG_WINDOW = 8;
	
	public static final int COLOR_WHITE = 0xffffffff;
	
	public static final int COLOR_BLACK = 0xff000000;
	
	/**
	 * 用于缩放的目标单字符宽度
	 */
	public  static final int DST_CHAR_WIDTH = 24;
	
	/**
	 * 用于缩放的目标单字符长度
	 */
	public static final int DST_CHAR_HEIGHT = 24;
	
	public static final String SVM_TEST_FILE = "svm/svm.test2";
	
	public static final String SVM_RESULT_FILE = "svm/result.txt";
	
	public static final String SVM_MODEL_FILE = "svm/svm.model2";
	
	public static final String SVM_TRANS_FILE = "svm/svm.train2";

}



