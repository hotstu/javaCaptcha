package hotst.github.javacaptcha.model;

import hotst.github.javacaptcha.common.Constants;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class BinaryMatrix {
	
	private final boolean[][] array;
	private final int w;
	private final int h;
	
	private BinaryMatrix(boolean[][] array) {
		this.array = array;
		this.w = array[0].length;
		this.h = array.length;
	}
	
	/**
	 * 检查长宽，像素数是否大于最小长宽，像素数
	 * @return
	 */
	public boolean isValid() {
		if (w < Constants.MIN_RECT_WIDTH || h < Constants.MIN_RECT_HEGITH)
			return false;
		int count = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (array[i][j])
					count++;
			}
		}
		if (count < Constants.MIN_RECT_PIXELS)
			return false;
		return true;
	}
	
	public int getWidth() {
		return this.w;
	}
	
	public int getHeight() {
		return this.h;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @return 超出边境将会抛出异常
	 */
	public boolean getValue(int x, int y) {
		return array[y][x];
	}
	
	public void setValue(int x, int y, boolean value) {
		array[y][x] = value;
	}
	
	public void setTrue(int x, int y) {
		array[y][x] = true;
	}
	
	public void setFalse(int x, int y) {
		array[y][x] = false;
	}
	
	public BinaryMatrix scaleTo(int width, int height) {		
		float fy = (float) height / h;
		float fx = (float) width / w;
		System.out.printf("fx:%f fy:%f\n", fx, fy);
		
		boolean[][] res = new boolean[height][width];
		
		for (int i = 0; i < h; i++) {
			int scaledi = (int)(i * fy);
			for (int j = 0; j < w; j++) {
				int scaledj = (int)(j * fx);
				res[scaledi][scaledj] = array[i][j];
				for (int k = scaledj + 1; k < (int)((j+1) * fx); k++) {
					res[scaledi][k] = res[scaledi][k - 1];
				}
			}
			for (int m = scaledi + 1; m < (int)((i+1) * fy); m++) {
				for (int j = 0; j < width; j++) {
					res[m][j] = res[m - 1][j];
				}
			}
		}
		
		return new BinaryMatrix(res);
	}
	
	/**
	 * 去除上下左右多余的白色区域
	 * @return 生成的新矩阵 将返回null 如果本身或者生成的矩阵is not Valid
	 * @see {@link #isValid()}
	 */
	public BinaryMatrix trim() {
		int left = 0;
		int right = 0;
		int top = 0;
		int bottom = 0;
		
		if (w <= Constants.MIN_RECT_WIDTH || h <= Constants.MIN_RECT_HEGITH) {
			System.out.printf("1.BinaryMatrix#trim:丢弃小分割：(%d, %d) < (%d, %d)\n", w, 
					h, Constants.MIN_RECT_WIDTH, Constants.MIN_RECT_HEGITH);
			return null;
		}
		
		boolean shouldConti = true;
		for (int i = 0; i < w && shouldConti; i++) {
			for (int j = 0; j < h; j++) {
				if(array[j][i]) {
					left = i;
					shouldConti = false;
					break;
				}
			}
		}
		
		shouldConti = true;
		for (int i = w - 1; i >= 0 && shouldConti; i--) {
			for (int j = 0; j < h; j++) {
				if(array[j][i]) {
					right = i;
					shouldConti = false;
					break;
				}
			}
			
		}
		
		shouldConti = true;
		for (int i = 0; i < h && shouldConti; i++) {
			for (int j = 0; j < w; j++) {
				if(array[i][j]) {
					top = i;
					shouldConti = false;
					break;
				}
			}
		}
		
		shouldConti = true;
		for (int i = h - 1; i >= 0 && shouldConti; i--) {
			for (int j = 0; j < w; j++) {
				if(array[i][j]) {
					bottom = i;
					shouldConti = false;
					break;
				}
			}
		}
		
		int width = right - left + 1;
		int height = bottom - top + 1;
		if (width <= Constants.MIN_RECT_WIDTH || height <= Constants.MIN_RECT_HEGITH) {
			System.out.printf("BinaryMatrix#trim:丢弃小分割：(%d, %d) < (%d, %d)\n", width, 
					height, Constants.MIN_RECT_WIDTH, Constants.MIN_RECT_HEGITH);
			return null;
		}
		//System.out.printf("\n==%d %d %d %d w:%d,h:%d==\n", left, right, top, bottom, w, h);
		boolean[][] res = new boolean[height][width];
		int PointCount = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (array[top+i][left+j]) {
					res[i][j] = true;
					PointCount++;
				}
				else
					res[i][j] = false;
			}
		}
		if (PointCount < Constants.MIN_RECT_PIXELS) {
			//丢弃像素点小于100的图片（噪点）
			System.out.printf("BinaryMatrix#trim:丢弃小分割：%d < %d (pixels)\n", PointCount, Constants.MIN_RECT_PIXELS);
			return null;
		}
		return new BinaryMatrix(res);
	}
	
	/**
	 * 转换为bitmap
	 * @param dst
	 * @throws IOException
	 */
	public void dump2bitmap(File dst) throws IOException {
		BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (array[i][j])
					im.setRGB(j, i, Constants.COLOR_BLACK);
				else
					im.setRGB(j, i, Constants.COLOR_WHITE);
			}
		}
		ImageIO.write(im, "png", dst);
		
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(h + "X" + w);
		sb.append('\n');
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (array[i][j])
					sb.append("1 ");
				else
					sb.append("0 ");
			}
			sb.append('\n');
		}
		return sb.toString();
	};
	
	
	public static BinaryMatrix fromArray(boolean[][] input) {
		return new BinaryMatrix(input);
	}
	
	/**
	 * 从图片中载入，注意只有颜色值为0xff000000的会被判断为黑点
	 * @param input
	 * @return
	 */
	public static BinaryMatrix fromImage(BufferedImage input) {
		int width = input.getWidth();
		int height = input.getHeight();
		
		boolean[][] res = new boolean[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (input.getRGB(j, i) == Constants.COLOR_BLACK)
					res[i][j] = true;
			}
		}
		return new BinaryMatrix(res);
	}
	
	/**
	 * 生成空白矩阵
	 * @param width
	 * @param height
	 * @return
	 */
	public static BinaryMatrix fromBlank(int width, int height) {
		boolean[][] res = new boolean[height][width];
		return new BinaryMatrix(res);
	}

}











