# livestudy-dashboard
출석률 체크를 위한 대시보드

## :pushpin: livestudy 대시보드 코드 작성

### :bulb:  Requirements
-   깃헙 이슈 1번부터 18번까지 댓글을 순회하며 댓글을 남긴 사용자를 체크 할 것.
-   참여율을 계산하세요. 총 18회에 중에 몇 %를 참여했는지 소숫점 두자리가지 보여줄 것.
-   [Github 자바 라이브러리](https://github-api.kohsuke.org/)를 사용하면 편리합니다.
-   깃헙 API를 익명으로 호출하는데 제한이 있기 때문에 본인의 깃헙 프로젝트에 이슈를 만들고 테스트를 하시면 더 자주 테스트할 수 있습니다.
<br>


### :question:   GitHub 자바 라이브러리

**터미널이나 웹을 통해서가 아니라,  프로그램 내에서 깃허브에 접근할 수 있게해주는 라이브러리이다.**<br>
<br>

![image](https://user-images.githubusercontent.com/62331803/101135457-2b32d500-364f-11eb-8827-ea9540f72c22.png)
<br>

`Introduction`을 보면, **객체지향적 관점에서, GitHub의 여러가지 객체에 접근할 수 있게 한 라이브러리**라고 적혀있다.<br>

쉽게 말해서 **내가 짠 프로그램에서 깃허브에 접속**하고, **여러 정보(repository 정보, issue 내용, comment 내용 등..)에 접근할 수 있게 해주는 API**인 것이다.<br>
<br>

**본격적으로 이 API를 이용해서 live-study 대시보드를 만들어보자!**<br>

### 1. 라이브러리 설치

나는 간단하게 `maven`과 `이클립스`를 이용하기로 했다.<br>
<br>

> Dependency Information 메뉴에서 pom.xml에 삽입할 태그 내용을 복사한다<br>


![image](https://user-images.githubusercontent.com/62331803/101136173-4520e780-3650-11eb-9684-80e21aa3eb02.png)
<br>

> 내가 만든 maven 프로젝트의 pom.xml에 붙여넣는다<br>

![image](https://user-images.githubusercontent.com/62331803/101136515-c8423d80-3650-11eb-8a2a-0eeace810af9.png)
<br>


> 설치완료<br>

![image](https://user-images.githubusercontent.com/62331803/101136611-ef990a80-3650-11eb-857a-f9782d58fb82.png)
<br>

> 이제 해당 라이브러리를 import해서 사용할 수 있다<br>

![image](https://user-images.githubusercontent.com/62331803/101136746-2111d600-3651-11eb-867b-a0b757e5f440.png)
<br>
<br>


### 2. 코드 짜기


**사실 다른 과제를 할 시간이 없을 거 같아서, 이 과제를 정성들여 하기로 마음 먹었다. 흫..**<br>

:bulb: **목표**
- `MVC 패턴으로 짜기`
- `리포지토리명 사용자에게 받아와서 리포지토리 객체 생성하기`
- `출석율 높은 순으로 정렬해서 출력하기`
- `아이디로 출석율 검색하기`

<BR>

**우선 결과물은 다음과 같다!**<br>

> **깃 계정 연결** <br>

![image](https://user-images.githubusercontent.com/62331803/101140681-a350c900-3656-11eb-8fe6-82b5869a3edd.png)
<br>

> **출석 랭킹**<br>

![image](https://user-images.githubusercontent.com/62331803/101140081-cdee5200-3655-11eb-8871-ed72f014bcf8.png)
<br>

> **이름으로 검색**<br>

**누구신지 모르겠지만... 아이디 좀 쓸게요...**<br>

![image](https://user-images.githubusercontent.com/62331803/101140165-eeb6a780-3655-11eb-80e2-024fc7203717.png)
<br>

> **존재하지 않는 아이디 입력할 경우**<br>

![image](https://user-images.githubusercontent.com/62331803/101140380-3a695100-3656-11eb-94fa-ac8828e1bd5a.png)
<br>
<br>



:point_right: **코드는 다음과 같이 구성했다.**<br>

![image](https://user-images.githubusercontent.com/62331803/101137250-e3617d00-3651-11eb-8663-395689f5cf1b.png)
<br>

- `GHConnect` : 깃허브 객체를 싱글톤을 만들어서 사용하기 위한 클래스
- `Dao` :  데이터 처리
- `Service` : 기능 출력
- `Menu` : 메뉴
- `Main` : 메인

<br><br>

:point_right: **1. GHConnect : 깃허브 객체 생성**<br> 

- 깃허브에 접속하기 위한 방법은 여러가지가 있다.<br>
  
![image](https://user-images.githubusercontent.com/62331803/101137608-75698580-3652-11eb-8194-ce919c44b053.png)
<br>

- 나는 `Personal access tokens`을 통해 접속하는 방식을 택했다. 
- [참고자료](https://calvinjmkim.tistory.com/19)
- 깃허브 객체와 로그 객체는 현재 클래스에서 하나만 생성해서 공유하도록 했다.


```java
package livestudy.mission4.ghcon;

import java.io.IOException;
import java.util.logging.Logger;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class GHConnect {
	private static final String personalToken = "Insert your personal token";//
	private static final Logger LOG = Logger.getGlobal();//로그 객체
	private GitHub github;

	public GHConnect() {
		try {
			// 깃허브 객체 생성
			this.github = new GitHubBuilder().withOAuthToken(personalToken).build();
		} catch (Exception e) {
			LOG.info("깃 계정 연결 실패");
		}
		LOG.info("깃 계정 연결 성공");
	}

	public GitHub getConnection() {
		return github;
	}

	public Logger getLog() {
		return LOG;
	}
}

```
<br>

**접속에 성공하면 다음과 같이 출력된다.**<br>

![image](https://user-images.githubusercontent.com/62331803/101139464-edd14600-3654-11eb-8607-9a5cfb561d70.png)
<br>

<br><br>

:point_right: **2. Dao : 데이터 관리**<br> 

- 사용자로부터 리포지토리 이름을 받아 `GHRepository` 객체를 생성한다.
- 해당 리포지토리 안에 issue를 순회하며 comment를 남긴 user id를 가져온다.
- `user id`를 key, `comment`개수를 value로 가지는 Map을 만들어, User마다 출석횟수를 저장한다.
- 

```java
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
		this.github = con.getConnection(); // 깃허브 객체 가져오기
		this.LOG = con.getLog(); // 로그 객체 가져오기
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
```

<br><br>

:point_right: **3. Service: 데이터 출력**<br> 

```java
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

	// 한 명
	public void findByName(String name) {
		Double rate = this.dao.getAttendenceRateByName(name);
		if (rate == 0) return;
		System.out.println(name + " : " + String.format("%.2f", rate) + "%" + "\n");
	}

	// 전체 유저
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

```
<br><br>

:point_right: **4. Menu: 메뉴**<br> 

```java
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

```


<br><br>

:point_right: **5. main: 메인**<br> 

```java
package livestudy.mission4;

import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		Menu menu = new Menu(sc);
		menu.run();
	}
}

```
<br>


