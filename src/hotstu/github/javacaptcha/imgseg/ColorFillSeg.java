package hotstu.github.javacaptcha.imgseg;

import hotst.github.javacaptcha.common.Constants;
import hotst.github.javacaptcha.model.BinaryMatrix;
import hotst.github.javacaptcha.model.Point;
import hotst.github.javacaptcha.model.SubRect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ColorFillSeg {
	
	/**
	 * cfs进行分割,返回分割后的数组
	 * @param sourceImage
	 * @return
	 */
	public static List<BinaryMatrix> cfs(BinaryMatrix im){
		int w = im.getWidth();
		int h = im.getHeight();
		
		ArrayList<SubRect> subImgList = new ArrayList<SubRect>(); 				//保存子图像
		boolean[][] bfsLookup = new boolean[w][h]; 
		char[][] groupLookup = new char[w][h];
		char currentGroup = 0;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				//如果不是黑色，或者已经被访问过，则跳过cfg
				if (!im.getValue(x, y) || bfsLookup[x][y]) {
					continue;
				}
				currentGroup ++;									//因为char默认为0，这里从1开始，以区分白色的区域
				//System.out.println((int)currentGourp);
				//如果黑色，且没有访问，则以此点开始进行连通域探索
				SubRect rect = new SubRect(currentGroup);			//保存当前字符块的坐标点
				LinkedList<Point> queue = new LinkedList<Point>();	//保存当前字符块的访问队列
				queue.add(new Point(x, y));
				bfsLookup[x][y] = true;
				groupLookup[x][y] = (char)currentGroup;
				rect.left = x;
				rect.top = y;
				rect.right = x;
				rect.bottom = y;
				int[] directionX = new int[]{-1, 0, 1};
				int[] directionY = new int[]{-1, 0, 1};
				while(!queue.isEmpty()){
					Point p = queue.pop();
					
					//搜寻目标的八个方向				
					for (int i : directionX) {
						
						for (int j: directionY) {
							if (i == 0 && j == 0) {
								continue;
							}
							int tx = Math.min(Math.max(p.x + i, 0), w - 1);
							int ty = Math.min(Math.max(p.y + j, 0), h - 1);
							if (im.getValue(tx, ty) && !bfsLookup[tx][ty]) {
								queue.add(new Point(tx, ty));
								bfsLookup[tx][ty] = true;
								groupLookup[tx][ty] = (char)currentGroup;
								//更新边界区域
								rect.left = Math.min(rect.left, tx);
								rect.top = Math.min(rect.top, ty);
								rect.right = Math.max(rect.right, tx);
								rect.bottom = Math.max(rect.bottom, ty);
							}
						}
					}
				}//end of while
				
				subImgList.add(rect);
			}
		}
		
		List<BinaryMatrix> cfgList = new ArrayList<>();
		for (SubRect r : subImgList) {
			if (r.getWidth() < Constants.MIN_RECT_WIDTH || r.getHeight() < Constants.MIN_RECT_HEGITH) {
				System.out.printf("ColorFillSeg:丢弃小分割：(%d, %d) < (%d, %d)\n", r.getWidth(), 
						r.getHeight(), Constants.MIN_RECT_WIDTH, Constants.MIN_RECT_HEGITH);
				continue;
			}
			BinaryMatrix image = BinaryMatrix.fromBlank(r.getWidth(), r.getHeight());
			int sumOfBlack = 0;
			for (int i = 0; i < r.getWidth(); i++) {
				for (int j = 0; j < r.getHeight(); j++) {
					if (groupLookup[r.left+i][r.top+j] == r.getNumber()) {
						sumOfBlack ++;
						image.setTrue(i, j);
					}
					else
						image.setFalse(i, j);
				}
			}
			if (sumOfBlack < Constants.MIN_RECT_PIXELS) {
				System.out.printf("ColorFillSeg:丢弃小分割：%d < %d (pixels)\n", sumOfBlack, Constants.MIN_RECT_PIXELS);
				continue;
			}
			cfgList.add(image);		//将切割的中间图片加入到cfgList中
		}
		return cfgList;
		
	}
	
	public static void main(String[] args) {

	}

}
