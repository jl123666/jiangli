package inputErr;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UsrInput {

	private static Scanner cin = new Scanner(System.in);

	public static int getInt() {
		while (true) {
			try {
				int res = cin.nextInt();
				
				return res;
			} catch (InputMismatchException err) {
				cin = newScanner(); //非法输入时创建新的Scanner，确保能正确输入
			}
		}
	}

	public static float getFloat() {
		while (true) {
			try {
				float res = cin.nextFloat();
				
				return res;
			} catch (InputMismatchException err) {
				cin = newScanner();
			}
		}
	}
	
	public static String getString() {
		while (true) {
			try {
				String res = cin.next();
				
				return res;
			} catch(InputMismatchException err) {
				cin = newScanner();
			}
		}
	}
	
	private static Scanner newScanner() {
		return new Scanner(System.in);
	}
}