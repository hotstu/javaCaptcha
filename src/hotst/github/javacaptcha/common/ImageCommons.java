package hotst.github.javacaptcha.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.istack.internal.Nullable;

public class ImageCommons {
	
	/**
	 * 获得二值化图像
	 * 最大类间方差法
	 * @param gray
	 * @param width
	 * @param height
	 */
	public static  int getOstu(int[][] gray, int width, int height){
		int grayLevel = 256;
		int[] pixelNum = new int[grayLevel];
		//计算所有色阶的直方图
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int color = gray[x][y];
				pixelNum[color] ++;
			}
		}
		
		double sum = 0;
		int total = 0;
		for (int i = 0; i < grayLevel; i++) {
			sum += i*pixelNum[i]; //x*f(x)质量矩，也就是每个灰度的值乘以其点数（归一化后为概率），sum为其总和
			total += pixelNum[i]; //n为图象总的点数，归一化后就是累积概率
		}
		double sumB = 0;//前景色质量矩总和
		int threshold = 0;
		double wF = 0;//前景色权重
		double wB = 0;//背景色权重
		
		double maxFreq = -1.0;//最大类间方差
		
		for (int i = 0; i < grayLevel; i++) {
			wB += pixelNum[i]; //wB为在当前阈值背景图象的点数
			if (wB == 0) { //没有分出前景后景
				continue;
			}
			
			wF = total - wB; //wB为在当前阈值前景图象的点数
			if (wF == 0) {//全是前景图像，则可以直接break
				break;
			}
			
			sumB += (double)(i*pixelNum[i]);
			double meanB = sumB / wB;
			double meanF = (sum - sumB) / wF;
			//freq为类间方差
			double freq = (double)(wF)*(double)(wB)*(meanB - meanF)*(meanB - meanF);
			if (freq > maxFreq) {
				maxFreq = freq;
				threshold = i;
			}
		}
		
		return threshold;
	}
	
	
	
	public static void dump2Image(boolean[][] src, File dst) {
		int h = src.length;
		int w = src[0].length;
		BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (src[i][j])
					im.setRGB(j, i, 0xff000000);
				else
					im.setRGB(j, i, 0xffffffff);
			}
		}
		try {
			ImageIO.write(im, "png", dst);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean[][] fromImage(File src) {
		boolean[][] res = null;
		try {
			BufferedImage im = ImageIO.read(src);
			int w = im.getWidth();
			int h = im.getHeight();
			res = new boolean[h][w];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					//System.out.printf("%x\n", im.getRGB(i, j));
					if ( im.getRGB(x, y) == Constants.COLOR_BLACK)
						res[y][x] = true;
					else
						res[y][x] = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
    /**
     * 缩放矩阵
     * @param src
     * @param dstw 目标宽
     * @param dsth 目标高
     * @return
     */
	public static @Nullable boolean[][] matrixScale(boolean[][] src, int dstw, int dsth) {
		int h = src.length;
		int w = src[0].length;
		
		float fx = (float) dstw / h;
		float fy = (float) dsth / w;
		System.out.printf("fx:%f fy:%f\n", fx, fy);
		
		boolean[][] res = new boolean[dstw][dsth];
		
		for (int i = 0; i < h; i++) {
			int scaledi = (int)(i * fx);
			for (int j = 0; j < w; j++) {
				int scaledj = (int)(j * fy);
				res[scaledi][scaledj] = src[i][j];
				for (int k = scaledj + 1; k < (int)((j+1) * fy); k++) {
					res[scaledi][k] = res[scaledi][k - 1];
				}
			}
			for (int m = scaledi + 1; m < (int)((i+1) * fx); m++) {
				for (int j = 0; j < dsth; j++) {
					res[m][j] = res[m - 1][j];
				}
			}
		}
		
		return res;
	}
	
	public static void test() {
		boolean[][] src = new boolean[2][2];
		src[0][0] = true;
		src[0][1] = false;
		src[1][0] = false;
		src[1][1] = true;
		for (int i = 0; i < src.length; i++) {
			for (int j = 0; j < src[0].length; j++) {
				if (src[i][j])
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}
		System.out.println("=======scale========");
		boolean[][] dst = matrixScale(src, 10, 10);
		for (int i = 0; i < dst.length; i++) {
			for (int j = 0; j < dst[0].length; j++) {
				if (dst[i][j])
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}
		
		System.out.println("=======scale========");
		dst = matrixScale(dst, 3, 3);
		for (int i = 0; i < dst.length; i++) {
			for (int j = 0; j < dst[0].length; j++) {
				if (dst[i][j])
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}
	}
	
	
	public static void matrixPrint(boolean[][] input) {
		int h = input.length;
		int w = input[0].length;
		System.out.println(h + "*" + w + ":");
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (input[i][j])
				    System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		File f = new File("1_gray/46.jpg");
		boolean[][] b = fromImage(f);
		dump2Image(b, new File("tmp/ddddd.png"));
	}
	

}
