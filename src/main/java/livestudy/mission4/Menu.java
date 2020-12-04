package livestudy.mission4;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
	Service service;
	Scanner sc;

	public Menu(Scanner sc) throws IOException {
		this.service = new Service();
		this.sc = sc;
	}

	public void run() throws IOException {
		String repo;
		System.out.println("\n==================== Welcome to live-study dashboard ====================\n");

		do {

			System.out.print("Please enter the repository name>>");
			repo = sc.nextLine();

		} while (!this.service.setRepo(repo.trim()));
		
		run2();

	}

	public void run2() {

		System.out.println("\n=============================== Menu ===================================\n");
		String op;
		boolean flag = true;

		do {

			System.out.println("1.출석랭킹		2.내 출석율		3.종료");
			System.out.print("번호 입력 >>");
			op = sc.nextLine();

			switch (op.trim()) {

			case "1":
				System.out.println("\n=============================== Rank ==================================\n");
				service.findAll();
				break;

			case "2":
				System.out.print("\n아이디를 입력하세요 >>");
				String name = sc.nextLine();
				service.findByName(name.trim());
				break;

			case "3":
				flag = false;
				break;

			default:
				System.out.println("잘못된 번호");
			}

		} while (flag);
	}
}
