package hotst.github.javacaptcha.model;

/**
 * 字符块图片
 *
 */
public class SubRect{
	public char number;		//代号，用于图片中不同分组的划分
	public int left;
	public int top;
	public int right;
	public int bottom;
	
	public SubRect(char number) {
		this.number = number;
		this.left = 0;
		this.top = 0;
		this.right = 0;
		this.bottom = 0;
	}
	
	public int getWidth() {
		return right - left;
	}
	public int getHeight() {
		return bottom - top;
	}
	public int getNumber() {
		return this.number;
	}
}
