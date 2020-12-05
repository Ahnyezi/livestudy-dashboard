package livestudy.mission4;

import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		Menu menu = new Menu(sc);
		menu.run();
		sc.close();
	}
}
