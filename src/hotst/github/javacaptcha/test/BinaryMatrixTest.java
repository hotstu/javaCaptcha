package hotst.github.javacaptcha.test;

import static org.junit.Assert.*;
import hotst.github.javacaptcha.model.BinaryMatrix;

import org.junit.Before;
import org.junit.Test;

public class BinaryMatrixTest {
	private  BinaryMatrix matrix;
	
	@Before
	public void setUp() {
		boolean[][] array = new boolean[2][3];
		
		matrix = BinaryMatrix.fromArray(array);
	}

	@Test
	public void testIsValid() {
		assertTrue(matrix.isValid());
	}

	@Test
	public void testGetWidth() {
		assertArrayEquals(new int[]{3}, new int[]{matrix.getWidth()});
	}

	@Test
	public void testGetHeight() {
		assertArrayEquals(new int[]{2}, new int[]{matrix.getHeight()});
	}

	@Test
	public void testGetValue() {
		assertFalse(matrix.getValue(0, 0));
	}

	@Test
	public void testSetTrue() {
		matrix.setTrue(0, 0);
		assertTrue(matrix.getValue(0, 0));
	}

	@Test
	public void testSetFalse() {
		matrix.setFalse(0, 0);
		assertFalse(matrix.getValue(0, 0));
	}

	@Test
	public void testScaleTo() {
		BinaryMatrix tmp = matrix.scaleTo(10, 10);
		assertArrayEquals(new int[]{10,10}, new int[]{tmp.getHeight(), tmp.getWidth()});
	}

	@Test
	public void testDump2bitmap() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		System.out.println("====testToString====");
		System.out.println(matrix);
	}

	@Test
	public void testFromArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromBlank() {
		System.out.println("====testFromBlank====");
		BinaryMatrix b = BinaryMatrix.fromBlank(10, 10);
		System.out.println(b);
	}

}
