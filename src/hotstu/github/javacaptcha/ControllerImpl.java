package hotstu.github.javacaptcha;

import hotst.github.javacaptcha.common.Constants;
import hotst.github.javacaptcha.model.BinaryMatrix;
import hotst.github.javacaptcha.svm.RobustPredict;
import hotstu.github.javacaptcha.imgprocessor.GenericPreprocessor;
import hotstu.github.javacaptcha.imgprocessor.ICaptchaPreprocessor;
import hotstu.github.javacaptcha.imgseg.GenericSegment;
import hotstu.github.javacaptcha.imgseg.ISegment;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class ControllerImpl implements IController{
	
	private ICaptchaPreprocessor preProcessor;
	private ISegment segProcessor;
	
	public ControllerImpl() {
		this.preProcessor = new GenericPreprocessor();
		this.segProcessor = new GenericSegment();
	}

	@Override
	public String predict(File f) {
		BufferedReader reader = null;
		String result = "";
		try {
			BufferedImage sourceImage = ImageIO.read(f);
			BinaryMatrix im = preprocess(sourceImage);
			List<BinaryMatrix> interList = split(im);
			
			RobustPredict.predict(interList);

			reader = new BufferedReader(new FileReader(new File(
						Constants.SVM_RESULT_FILE)));
			String buff = null;
				while ((buff = reader.readLine()) != null) {
					float tmp = Float.valueOf(buff);
					int value = (int)tmp;
					if (value >= 0 && value <=9){
						result += value + "";
					}
					else {
						char c = (char) (value + 87);
						result += c;
					}
					
				}
			System.out.println(result);
		} catch (IOException e) {
			result = e.getMessage();
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ignored) {
				}
			}
		}
		return result;
	}


	@Override
	public File download() {
		return null;
	}

	@Override
	public BinaryMatrix preprocess(BufferedImage im) {
		return preProcessor.preprocess(im);
	}

	@Override
	public List<BinaryMatrix> split(BinaryMatrix im) {
		return segProcessor.seg2file(im, null);
	}

}
