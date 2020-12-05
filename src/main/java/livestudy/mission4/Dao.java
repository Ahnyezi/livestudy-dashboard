package livestudy.mission4;

import java.io.FileNotFoundException;
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
	private Map<String, Integer> participants; // userid�� ����Ƚ��
	private int total; 						   // �̽� �� ����

	public Dao() throws IOException {
		GHConnect con = new GHConnect();
		this.github = con.getConnection(); 	   // GHConnect�κ��� ����� ��ü ��������
		this.LOG = con.getLog(); 			   // GHConnect�κ��� �α� ��ü ��������
		this.participants = new HashMap<String, Integer>();
	}
	
	public Logger getLog() {
		return this.LOG;
	}

	// �������丮 ����
	public boolean setRepo(String repo) throws IOException {
		boolean flag = false;
		try {
			// ����ڷκ��� �Է¹��� repository �̸��� �̿��Ͽ� GHRepository ��ü ����
			this.repo = github.getRepository(repo).getSource();
			LOG.info("'" + repo + "' �������丮 ���� ����");
			LOG.info("������ �а� �ֽ��ϴ�. ��ø� ��ٷ��ֽʽÿ�.");
			setAttendance();
			flag = true;
		} catch (FileNotFoundException e) {
			LOG.info("'" + repo + "' �������丮�� �������� �ʽ��ϴ�.");
			flag = false;
		} catch (NullPointerException e){
			LOG.info("null pointer exception");
			flag = false;			
		}finally {
			return flag;
		}
	}

	// ���õ� repository�� ������ �⼮���� ����
	public void setAttendance() throws IOException {
		List<GHIssue> allTheIssues = repo.getIssues(GHIssueState.ALL);  	// ���õ� �������丮�� ��ü issue
		Set<String> nameList = new HashSet<String>(); 				    	// �ϳ��� issue�� �ڸ�Ʈ�� ���� user id�� ��� ���� �ӽ� set (�ߺ����� ����)
		this.total = allTheIssues.size(); 							    	// �⼮�� ����� ���� �̽� �� ����
		
		for (GHIssue issueForAWeek : allTheIssues) {				    	// �̽��� 1���ھ� �����ͼ�
			for (GHIssueComment comment : issueForAWeek.getComments()) {	// �ش� �̽��� ��ü �ڸ�Ʈ ��������
				nameList.add(comment.getUser().getLogin()); 				// �ڸ�Ʈ�� user id�� namelist(�ӽ� set)�� ����
			}
			insertNames(nameList);											// map<id, count>�� value(�⼮Ƚ��) ������Ű��
			nameList.clear();
		}
	}

	// ������� �ӽ� set���� map�� count(�⼮Ƚ��) ������Ű��
	public void insertNames(Set<String> nameList) {
		nameList.forEach((name) -> {
			if (this.participants.containsKey(name)) {						// �̹� map�� �����ϴ� id�� ���
				this.participants.put(name, participants.get(name) + 1);	
			} else {														// map�� �������� �ʴ� id�� ���
				this.participants.put(name, 1);
			}
		});
	}

	// userid�� �⼮Ƚ�� �˻�
	public Double getAttendenceRateByName(String name) {
		int count = 1;
		try {
			count = this.participants.get(name);
			return (double) ((count * 100) / this.total);
		} catch (NullPointerException e) {
			LOG.info("�������� �ʴ� ���̵��Դϴ�.");
		}
		return 0.0;
	}
	
	
	// ��� �������� �⼮Ƚ�� �˻�
	public Map<String, Double> getAllAttendenceRate() {
		Map<String, Double> allRate = new HashMap<String, Double>();
		this.participants.forEach((name, count) -> {
			allRate.put(name, (double) ((count * 100) / this.total));
		});
		return sortMapByValue(allRate); // �⼮���� �������� �������� ����
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
