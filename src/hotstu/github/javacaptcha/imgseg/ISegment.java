package hotstu.github.javacaptcha.imgseg;

import hotst.github.javacaptcha.model.BinaryMatrix;

import java.util.List;

import com.sun.istack.internal.Nullable;

public interface ISegment {
	
	public List<BinaryMatrix> seg(BinaryMatrix im);
	
	public List<BinaryMatrix> seg2file(BinaryMatrix im, @Nullable String prefix); 

}
