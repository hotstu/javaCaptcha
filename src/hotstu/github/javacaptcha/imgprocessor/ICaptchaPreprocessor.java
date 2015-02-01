package hotstu.github.javacaptcha.imgprocessor;

import hotst.github.javacaptcha.model.BinaryMatrix;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 验证码二值化
 * @author foo
 *
 */
public interface ICaptchaPreprocessor {

	/**
	 * 将图片转换为二维数组，true表示黑点，false表示白点
	 * @param im
	 * @return
	 */
	public BinaryMatrix preprocess(BufferedImage im);
	
}




