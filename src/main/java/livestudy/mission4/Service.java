package livestudy.mission4;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class Service {
	private Dao dao;
	private Logger LOG;

	public Service() throws IOException {
		this.dao = new Dao();
		this.dao.getLog();
	}

	public boolean setRepo(String repo) throws IOException {
		return this.dao.setRepo(repo);
	}

	public void findByName(String name) {
		Double rate = this.dao.getAttendenceRateByName(name);
		if (rate == 0) return;
		System.out.println(name + " : " + String.format("%.2f", rate) + "%" + "\n");
	}

	public void findAll() {
		Map<String, Double> allRate = this.dao.getAllAttendenceRate();
		int idx = 1;
		for (String id : allRate.keySet()) {
			Double rate = allRate.get(id);
			System.out.print(idx + ".");
			String r = String.format("%20s", id);
			System.out.println(r + " -> " + rate + "%");
			idx += 1;
		}
		System.out.println("\n");
	}
}
