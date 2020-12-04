package livestudy.mission4;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import livestudy.mission4.ghcon.GHConnect;

public class Dao {
	private Logger LOG;
	private GitHub github;
	private GHRepository repo;
	private Map<String, Integer> participants;
	private int total;

	public Dao() throws IOException {
		GHConnect con = new GHConnect();
		this.github = con.getConnection();
		this.LOG = con.getLog();
		this.participants = new HashMap<String, Integer>();
	}

	public Logger getLog() {
		return this.LOG;
	}

	public boolean setRepo(String repo) throws IOException {
		boolean flag = false;
		try {
			this.repo = github.getRepository(repo).getSource();
			LOG.info("'" + repo + "' 리포지토리 진입 성공");
			LOG.info("정보를 읽고 있습니다. 잠시만 기다려주십시오.");
			setAttendance();
			flag = true;
		} catch (java.io.FileNotFoundException e) {
			LOG.info("'" + repo + "' 리포지토리는 존재하지 않습니다.");
			flag = false;
		} finally {
			return flag;
		}
	}

	public void setAttendance() throws IOException {
		List<GHIssue> allTheIssues = repo.getIssues(GHIssueState.ALL);
		Set<String> nameList = new HashSet<String>();
		this.total = allTheIssues.size();

		for (GHIssue issueForAWeek : allTheIssues) {
			for (GHIssueComment comment : issueForAWeek.getComments()) {
				nameList.add(comment.getUser().getLogin());
			}
			insertNames(nameList);
			nameList.clear();
		}
	}

	public void insertNames(Set<String> nameList) {
		nameList.forEach((name) -> {
			if (this.participants.containsKey(name)) {
				this.participants.put(name, participants.get(name) + 1);
			} else {
				this.participants.put(name, 1);
			}
		});
	}

	public Double getAttendenceRateByName(String name) {
		int count = 1;
		try {
			count = this.participants.get(name);
			return (double) ((count * 100) / this.total);
		} catch (NullPointerException e) {
			LOG.info("존재하지 않는 아이디입니다.");
		}
		return 0.0;
	}

	public Map<String, Double> getAllAttendenceRate() {
		Map<String, Double> allRate = new HashMap<String, Double>();
		this.participants.forEach((name, count) -> {
			allRate.put(name, (double) ((count * 100) / this.total));
		});
		return sortMapByValue(allRate);
	}

	public static LinkedHashMap<String, Double> sortMapByValue(Map<String, Double> allRate) {
		List<Map.Entry<String, Double>> entries = new LinkedList<>(allRate.entrySet());
		Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

		LinkedHashMap<String, Double> result = new LinkedHashMap<>();
		for (Entry<String, Double> entry : entries) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
