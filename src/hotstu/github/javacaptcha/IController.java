package hotstu.github.javacaptcha;

import hotst.github.javacaptcha.model.BinaryMatrix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public interface IController {
	
	public File download();
	
	public BinaryMatrix preprocess(BufferedImage im);
	
	public List<BinaryMatrix> split(BinaryMatrix im); 
	
	public String predict(File f);
	
	

}
