package hotstu.github.javacaptcha.imgseg;

import hotst.github.javacaptcha.common.Constants;
import hotst.github.javacaptcha.model.BinaryMatrix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.istack.internal.Nullable;

public class GenericSegment implements ISegment {

	@Override
	public List<BinaryMatrix> seg(BinaryMatrix im) {
		List<BinaryMatrix> interList = ColorFillSeg.cfs(im);
		List<BinaryMatrix>  res = new ArrayList<>();
		for (BinaryMatrix i : interList) {
			List<BinaryMatrix> tmpList = BIDropWaterSeg.seg(i);
			
			for (BinaryMatrix j : tmpList) {
				BinaryMatrix k = j.trim();
				if (k == null) {
					continue;
				}
				//ImageCommons.matrixPrint(k);
				BinaryMatrix m = k.scaleTo(Constants.DST_CHAR_WIDTH, Constants.DST_CHAR_HEIGHT);
				//ImageCommons.matrixPrint(m);
				res.add(m);
			}
		}
		System.out.println("++分割验证码++: " + res.size() + "块");
		return res;
	}
	
	@Override
	public List<BinaryMatrix> seg2file(BinaryMatrix im, @Nullable String prefix) {
		List<BinaryMatrix> targets = seg(im);
		if(prefix == null || prefix == "") {
			prefix = "temp";
		}
		
		int c = 0;
		for (BinaryMatrix bs : targets) {
			File f = new File("tmp/" + prefix + "-" + c + ".png");
			c++;
			try {
				bs.dump2bitmap(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return targets;
	}
	
	public List<BinaryMatrix> seg2file(BinaryMatrix im, String prefix, String dir) {
		List<BinaryMatrix> targets = seg(im);
		if(prefix == null || prefix == "") {
			throw new RuntimeException("prefix is illegal!");
		}
		
		int c = 0;
		for (BinaryMatrix bs : targets) {
			File f = new File(dir + prefix + "-" + c + ".png");
			try {
				bs.dump2bitmap(f);
				c++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return targets;
	}
	

}






