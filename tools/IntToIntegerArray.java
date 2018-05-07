package tools;

import java.util.Arrays;

public class IntToIntegerArray {

	public static Integer[] convert(int[] data) {
		return Arrays.stream(data).boxed().toArray(Integer[]::new);
	}
	
}
