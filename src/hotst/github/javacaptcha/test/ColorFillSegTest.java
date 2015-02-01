package hotst.github.javacaptcha.test;

import static org.junit.Assert.*;
import hotst.github.javacaptcha.model.BinaryMatrix;
import hotstu.github.javacaptcha.imgprocessor.GenericPreprocessor;
import hotstu.github.javacaptcha.imgseg.ColorFillSeg;
import hotstu.github.javacaptcha.imgseg.GenericSegment;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

public class ColorFillSegTest {

	@Test
	public void testCfsBinaryMatrix() {
		File f = new File("download/58.jpg");

		BufferedImage img = null;
		try {
			img = ImageIO.read(f);
		} catch (IOException e) {
			fail("载入失败");
		}
		GenericPreprocessor g = new GenericPreprocessor();
		BinaryMatrix b = g.preprocess(img);
		List<BinaryMatrix> lb = ColorFillSeg.cfs(b);
		
		for (BinaryMatrix item : lb) {
			System.out.println(item);
		}

	}

}
