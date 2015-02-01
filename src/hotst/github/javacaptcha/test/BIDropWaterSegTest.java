package hotst.github.javacaptcha.test;

import static org.junit.Assert.*;
import hotst.github.javacaptcha.model.BinaryMatrix;
import hotstu.github.javacaptcha.imgprocessor.GenericPreprocessor;
import hotstu.github.javacaptcha.imgseg.BIDropWaterSeg;
import hotstu.github.javacaptcha.imgseg.ColorFillSeg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

public class BIDropWaterSegTest {
	private List<BinaryMatrix> lb;

	@Before
	public void setUp() throws Exception {
		File f = new File("download/58.jpg");

		BufferedImage img = null;
		img = ImageIO.read(f);
		GenericPreprocessor g = new GenericPreprocessor();
		BinaryMatrix b = g.preprocess(img);
		lb = ColorFillSeg.cfs(b);
		
	}
	
	@Test
	public void testTrim() {
		for (BinaryMatrix b : lb) {
			for (BinaryMatrix item : BIDropWaterSeg.seg(b)) {
				System.out.println(item.trim());
			}
		}
	}
	
//	@Test
//	public void testFind() {
//		for (BinaryMatrix b : lb) {
//			int[] starts = BIDropWaterSeg.find(b);
//			for (int i : starts) {
//				System.out.println(i+" ");
//			}
//			System.out.println();
//		}
//	}
//
//	@Test
//	public void testDrop() {
//		for (BinaryMatrix b : lb) {
//			int[] starts = BIDropWaterSeg.find(b);
//			for (int i : starts) {
//				int[] xs = BIDropWaterSeg.drop(i, b);
//				for (int j : xs) {
//					System.out.println(j+" ");
//				}
//				System.out.println();
//			}
//		}
//	}
	
	@Test
	public void testSeg() {
		for (BinaryMatrix b : lb) {
			for (BinaryMatrix item : BIDropWaterSeg.seg(b)) {
				System.out.println(item);
			}
		}
	}


}
