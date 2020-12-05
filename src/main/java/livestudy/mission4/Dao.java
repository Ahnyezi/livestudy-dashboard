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
	private Map<String, Integer> participants; // userid와 참여횟수
	private int total; 						   // 이슈 총 개수

	public Dao() throws IOException {
		GHConnect con = new GHConnect();
		this.github = con.getConnection(); 	   // GHConnect로부터 깃허브 객체 가져오기
		this.LOG = con.getLog(); 			   // GHConnect로부터 로그 객체 가져오기
		this.participants = new HashMap<String, Integer>();
	}
	
	public Logger getLog() {
		return this.LOG;
	}

	// 리포지토리 세팅
	public boolean setRepo(String repo) throws IOException {
		boolean flag = false;
		try {
			// 사용자로부터 입력받은 repository 이름을 이용하여 GHRepository 객체 생성
			this.repo = github.getRepository(repo).getSource();
			LOG.info("'" + repo + "' 리포지토리 진입 성공");
			LOG.info("정보를 읽고 있습니다. 잠시만 기다려주십시오.");
			setAttendance();
			flag = true;
		} catch (FileNotFoundException e) {
			LOG.info("'" + repo + "' 리포지토리는 존재하지 않습니다.");
			flag = false;
		} catch (NullPointerException e){
			LOG.info("null pointer exception");
			flag = false;			
		}finally {
			return flag;
		}
	}

	// 세팅된 repository의 유저별 출석정보 세팅
	public void setAttendance() throws IOException {
		List<GHIssue> allTheIssues = repo.getIssues(GHIssueState.ALL);  	// 세팅된 리포지토리의 전체 issue
		Set<String> nameList = new HashSet<String>(); 				    	// 하나의 issue에 코멘트를 남긴 user id를 담기 위한 임시 set (중복제거 위함)
		this.total = allTheIssues.size(); 							    	// 출석율 계산을 위한 이슈 총 개수
		
		for (GHIssue issueForAWeek : allTheIssues) {				    	// 이슈를 1주자씩 가져와서
			for (GHIssueComment comment : issueForAWeek.getComments()) {	// 해당 이슈의 전체 코멘트 가져오기
				nameList.add(comment.getUser().getLogin()); 				// 코멘트의 user id를 namelist(임시 set)에 삽입
			}
			insertNames(nameList);											// map<id, count>의 value(출석횟수) 증가시키기
			nameList.clear();
		}
	}

	// 만들어진 임시 set으로 map의 count(출석횟수) 증가시키기
	public void insertNames(Set<String> nameList) {
		nameList.forEach((name) -> {
			if (this.participants.containsKey(name)) {						// 이미 map에 존재하는 id일 경우
				this.participants.put(name, participants.get(name) + 1);	
			} else {														// map에 존재하지 않는 id일 경우
				this.participants.put(name, 1);
			}
		});
	}

	// userid로 출석횟수 검색
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
	
	
	// 모든 참여자의 출석횟수 검색
	public Map<String, Double> getAllAttendenceRate() {
		Map<String, Double> allRate = new HashMap<String, Double>();
		this.participants.forEach((name, count) -> {
			allRate.put(name, (double) ((count * 100) / this.total));
		});
		return sortMapByValue(allRate); // 출석율을 기준으로 내림차순 정렬
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
