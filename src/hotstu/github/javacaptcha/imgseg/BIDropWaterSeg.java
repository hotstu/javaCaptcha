package hotstu.github.javacaptcha.imgseg;

import hotst.github.javacaptcha.common.Constants;
import hotst.github.javacaptcha.model.BinaryMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * BIDF Big Inertia Drop Fall algorithm implemented programly <br>
 * 使用程序员的思路实现大惯性水滴算法
 * 
 * @see http://www.intjit.org/cms/journal/volume/12/4/124_5.pdf
 * @author foo
 *
 */
public class BIDropWaterSeg {

	/**
	 * 大水滴的宽度 2*B+1,取0或者1效果最好,取0退化为IDF
	 */
	private static final int B = 0;
	
	public static int[] find(BinaryMatrix im ) {
		return find(im, Constants.AVER_CHAR_WIDTH, Constants.CHAR_RANG_WINDOW);
	}
	
	
	public static int[] find(BinaryMatrix im, int meanWidth, int window) {
		int w = im.getWidth();
		int h = im.getHeight();
		final int num = (int) ((float)w / meanWidth + 0.5f);
		System.out.printf("图片宽度:%d, 平均宽度:%d, 估测包含字符数:%d\n", w, meanWidth, num);
		if (num <= 1){
			return new int[]{};
		}
		int[] vericalCount = new int[w];

		//int sum = 0; // sum of black points
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (im.getValue(i, j)) {
					vericalCount[i]++;
				}
			}
			//sum += vericalCount[i];
		}
		

		// ___|```|____|````|____|````
		int[] points = new int[num - 1];
		for (int i = 1; i < num; i++) {
			int tempMin = vericalCount[i];
			int tempIndex = meanWidth*i - window/2;
			for (int j = meanWidth*i - window/2; j < meanWidth*i + window/2; j++) {
				if (tempMin > vericalCount[j]) {
					tempIndex = j;
					tempMin = vericalCount[j];
				}
			}
			points[i-1] = tempIndex;
		}
		return points;
	}
	
	
	public static List<BinaryMatrix> seg(final BinaryMatrix source) {
		int[] startpoints = find(source);
		final int w = source.getWidth();
		final int h = source.getHeight();
		List<BinaryMatrix> res = new ArrayList<>();
		
		if (startpoints.length == 0) {
			res.add(source);
			return res;
		}
		
		int[] left = new int[h];
		for (int i = 0; i < startpoints.length; i++) {
			int[] right = drop(startpoints[i], source);

			BinaryMatrix im = BinaryMatrix.fromBlank(w, h);

			for (int y = 0; y < h; y++) {
				int start = left[y];
				int end = right[y];
				for (int x = start; x < end; x++) {
						im.setValue(x, y, source.getValue(x, y));
				}
			}
			res.add(im);
			left = right;
		}
		int[] right = new int[h];
		for (int i = 0; i < left.length; i++) {
			right[i] = w;
		}
		BinaryMatrix im = BinaryMatrix.fromBlank(w, h);

		for (int y = 0; y < h; y++) {
			int start = left[y];
			int end = right[y];
			for (int x = start; x < end; x++) {
					im.setValue(x, y, source.getValue(x, y));
			}
		}
		res.add(im);
		return res;
	}
	
	
	
	public static int[] drop(final int startX, final BinaryMatrix source) {
		final int w = source.getWidth();
		final int h = source.getHeight();
		int leftLimit = 0;
		int rightLimit = w;
		int[] resX = new int[h];
		int lastX = startX;
		int lastY = 0;
		int curX = startX;
		int curY = 0;
		resX[curY] = curX;
		int dx = 0;
		int dy = 0;
		while (curY < h - 1) {
			int adj = getadj(curX, curY, source);
			// !判断查询的顺序代表了优先级
			// n5 n0 n4 <br>
			// n1 n2 n3 <br>
			if ((adj & 0b00100) == 0) {
				// n3 is white
				dx = 1;
				dy = 1;
			} else if ((adj & 0b00010) == 0) {
				// n2 is white
				dx = 0;
				dy = 1;
			} else if ((adj & 0b00001) == 0) {
				// n1 is white
				dx = -1;
				dy = 1;
			} else if ((adj & 0b01000) == 0) {
				// n4 is white
				dx = 1;
				dy = 0;
			} else if ((adj & 0b10000) == 0) {
				// n5 is white
				dx = -1;
				dy = 0;
			} else {
				// all is black
				if (curX > lastX) {
					// 具有向右的惯性
					dx = 1;
					dy = 1;
				} else {
					dx = 0;
					dy = 1;
				}
			}
			// ===end of if-else
			if (lastX == (curX + dx) && lastY == (curY + dy)) {
				// 出现了往复运动
				lastX = curX;
				lastY = curY;
				curY += 1;
			} else {
				lastX = curX;
				lastY = curY;
				curX = Math.min(Math.max(leftLimit, curX + dx), rightLimit);
				curY += dy;
			}
			resX[curY] = curX;
		}

		return resX;
	}
	
	
	
	/**
	 * 获得大水滴中心点周围的像素值<br>
	 * n5 n0 n4 <BR>
	 * n1 n2 n3 <br>
	 * 
	 * @param x
	 * @param y
	 *            需要保证y+1 <= height -1
	 * @return n0 - n5 中的不同位上1表示black，0表示white
	 */
	private static int getadj(int x, int y, BinaryMatrix im) {
		int res = 0;
		int w = im.getWidth();
		int h = im.getHeight();

		int right = x + B + 1;
		right = Math.min(w - 1, right);
		int n4 = im.getValue(right, y) ? 0b01000 : 0;

		int left = Math.max(0, x - B - 1);
		int n5 = im.getValue(left, y) ? 0b10000 : 0;

		int n1 = im.getValue(left, y + 1) ? 0b00001 : 0;

		int n3 = im.getValue(right, y + 1) ? 0b00100 : 0;

		// 如果 j == 2, 则判断下方[x-B,X+B]的区域， 只要有一个黑点，则当做黑点，
		int n2 = 0;
		for (int i = left + 1; i < right; i++) {
			if (im.getValue(i, y + 1)) {
				n2 = 0b00010;
				break;
			}
		}

		res |= n1;
		res |= n2;
		res |= n3;
		res |= n4;
		res |= n5;
		return res;
	}
	
	

	/**
	 * 返回最大值所在的下标
	 * 
	 * @param array
	 * @return
	 */
	public static int maximum(int[] array) {
		int res = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[res]) {
				res = i;
			}
		}
		return res;
	}

	/**
	 * 返回最小值所在的下标
	 * 
	 * @param array
	 * @return
	 */
	public static int minmum(int[] array) {
		int res = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] < array[res]) {
				res = i;
			}
		}
		return res;
	}

	public static void main(String[] args) {

	}

}
