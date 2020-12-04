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

			System.out.println("1.�⼮��ŷ		2.�� �⼮��		3.����");
			System.out.print("��ȣ �Է� >>");
			op = sc.nextLine();

			switch (op.trim()) {

			case "1":
				System.out.println("\n=============================== Rank ==================================\n");
				service.findAll();
				break;

			case "2":
				System.out.print("\n���̵� �Է��ϼ��� >>");
				String name = sc.nextLine();
				service.findByName(name.trim());
				break;

			case "3":
				flag = false;
				break;

			default:
				System.out.println("�߸��� ��ȣ");
			}

		} while (flag);
	}
}
