package livestudy.mission4;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		Menu menu = new Menu(sc);
		menu.run();
		sc.close();
	}
}
