
## :pushpin: (4주차 과제2) livestudy 대시보드 코드 작성

### :bulb:  Requirements
-   깃헙 이슈 1번부터 18번까지 댓글을 순회하며 댓글을 남긴 사용자를 체크 할 것.
-   참여율을 계산하세요. 총 18회에 중에 몇 %를 참여했는지 소숫점 두자리가지 보여줄 것.
-   [Github 자바 라이브러리](https://github-api.kohsuke.org/)를 사용하면 편리합니다.
-   깃헙 API를 익명으로 호출하는데 제한이 있기 때문에 본인의 깃헙 프로젝트에 이슈를 만들고 테스트를 하시면 더 자주 테스트할 수 있습니다.
<br><br>

**사실 다른 과제를 할 시간이 없을 거 같아서, 이 과제를 정성들여 하기로 마음 먹었다.. 흫..**<br>

:bulb:  **목표**  <br>
- `깃허브 API 이해`
- `MVC 패턴으로 짜기`
- `사용자에게 Repository name 받아와서 해당 repo 객체 생성하기`
- `메뉴 만들기`
   - `아이디별 출석율 검색`
   - `출석랭크 높은 순으로 정렬해서 출력`

<br>

 :point_right: **목차**   <br>
1. [GitHub 자바 라이브러리 ?](#1-GitHub-자바-라이브러리)

2. [코드 구성](#2-코드-구성)
      - 2.0. [결과물](#우선-결과물은-다음과-같다)
     -  2.1. [GHConnect : 깃허브 객체 생성](#21-GHConnect--깃허브-객체-생성)
     - 2.2. [Dao : 데이터 관리](#22--Dao--데이터-관리)
     - 2.3. [Service : 화면 출력](#23--Service--화면-출력)
     - 2.4. [Menu : 메뉴](#24--Menu--메뉴)
     - 2.5. [main : 메인](#25--main--메인)
     
3. [회고](#3-회고)
      - 3.1 [깃허브 자바 라이브러리를 사용하며](#31-깃허브-자바-라이브러리를-사용하며)
      - 3.2 [자바 람다식](#32-자바-람다식)
      - 3.3 [단위 테스트의 필요성](#33-단위-테스트의-필요성)


<br>


### 1. GitHub 자바 라이브러리


![image](https://user-images.githubusercontent.com/62331803/101235118-ed928280-3708-11eb-948c-8d15a1ad093f.png)
<br>

**과제 예시에 올려주신 GitHub 자바 라이브러리를 알아보았다.** <br>

- 해당 라이브러리는 GitHub API를 Object Oriented(객체지향)적인 관점에서 사용할 수 있도록 만들어진 자바 언어 기반의 라이브러리이다.
- 해당 라이브러리에는 `GHUser(깃허브유저)`, `GHRepository(깃허브리포지토리)`,`GHOrganization(깃허브그룹)` 등 깃허브에서 사용되는 각각의 도메인 모델들을 제어하기 위한 클래스가 존재하며, 각 클래스의 메서드를 통해서 깃허브 사이트에서 사용할 수 있는 다양한 기능을 프로그램 내에서 사용할 수 있다. 
   - 예를 들어, `GHUser` 클래스의 isMemberOf(GHOrganization) 메서드는 특정 깃허브 유저가 특정 깃허브 그룹의 멤버인지 참/거짓 여부를 반환한다.
   - 이와 같이 **객체** 형태로 깃허브 상의 정보를 제어하며 프로그램에 활용할 수 있는 것이다. 
<br>

**본격적으로 이 API를 이용해서 live-study 대시보드를 만들어보자!**<br>
<br>

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


### 2. 코드 구성

#### 우선 결과물은 다음과 같다!
<br>

> **깃 계정 연결** <br>

- 로그로 연결 성공 여부 띄우기

![image](https://user-images.githubusercontent.com/62331803/101228707-3255f380-36e0-11eb-9771-83a6651f670a.png)
<br>

> **사용자 리포지토리 입력**<br>

- 로그로 리포지토리 존재 여부 띄우기

![image](https://user-images.githubusercontent.com/62331803/101228773-73e69e80-36e0-11eb-8756-4f56845c978e.png)
<br>

> **이름으로 출석율 검색**

![image](https://user-images.githubusercontent.com/62331803/101228835-b90ad080-36e0-11eb-8ec9-f36a6db16f5c.png)
<br>

> **출석 랭킹**<br>

- 출석율이 높은 순으로 정렬해서 출력

![image](https://user-images.githubusercontent.com/62331803/101228858-d2ac1800-36e0-11eb-9992-03b96a14dba5.png)
<br>


<details>
<summary>전체 랭킹 보기</summary>

```java
=============================== Rank ==================================

1.           jymaeng95 -> 22.0%
2.             jongnan -> 22.0%
3.             nimkoes -> 22.0%
4.             roeniss -> 22.0%
5.            lee-maru -> 22.0%
6.              kjw217 -> 22.0%
7.             Lob-dev -> 22.0%
8.             geneaky -> 22.0%
9.           KilJaeeun -> 22.0%
10.            Jul-liet -> 22.0%
11.            pka0220z -> 22.0%
12.         SeungWoo-Oh -> 22.0%
13.     sejongdeveloper -> 22.0%
14.          Sungjun-HQ -> 22.0%
15.         sojintjdals -> 16.0%
16.       HyangKeunChoi -> 16.0%
17.          twowinsh87 -> 16.0%
18.           ufonetcom -> 16.0%
19.           sigriswil -> 16.0%
20.              catsbi -> 16.0%
21.           binghe819 -> 16.0%
22.            seovalue -> 16.0%
23.             kys4548 -> 16.0%
24.       KyungJae-Jang -> 16.0%
25.           kongduboo -> 16.0%
26.            ahyz0569 -> 16.0%
27.          loop-study -> 16.0%
28.            yskkkkkk -> 16.0%
29.           idiot2222 -> 16.0%
30.          limyeonsoo -> 16.0%
31.        Youngerjesus -> 16.0%
32.            gcha-kim -> 16.0%
33.           numuduwer -> 16.0%
34.             pej4303 -> 16.0%
35.                kyu9 -> 16.0%
36.      thisisyoungbin -> 16.0%
37.             Ryureka -> 16.0%
38.           plzprayme -> 16.0%
39.              sky7th -> 16.0%
40.            ejxzhn22 -> 16.0%
41.           YuSeungmo -> 16.0%
42.             sskim91 -> 16.0%
43.              DDOEUN -> 16.0%
44.             jjone36 -> 16.0%
45.             CODEMCD -> 16.0%
46.               sowjd -> 16.0%
47.        chaechae0322 -> 16.0%
48.            1031nice -> 16.0%
49.      JadenKim940105 -> 16.0%
50.          SooJungDev -> 16.0%
51.        dacapolife87 -> 16.0%
52.          ChoiGiSung -> 16.0%
53.        manOfBackend -> 16.0%
54.          jiwoo-choi -> 16.0%
55.             honux77 -> 16.0%
56.           rshak8912 -> 16.0%
57.               uHan2 -> 16.0%
58.          dmstjd1024 -> 16.0%
59.          asqwklop12 -> 16.0%
60.          Junhan0037 -> 16.0%
61.          hypernova1 -> 16.0%
62.         dev-jaekkim -> 16.0%
63.          dudqls5271 -> 16.0%
64.             abcdsds -> 16.0%
65.           hong918kr -> 16.0%
66.           jongyeans -> 16.0%
67.        WonYong-Jang -> 16.0%
68.           addadda15 -> 16.0%
69.              JsKim4 -> 16.0%
70.          sowhat9293 -> 16.0%
71.             jigmini -> 16.0%
72.        DevelopJKong -> 16.0%
73.         hongminpark -> 16.0%
74.        haemin-jeong -> 16.0%
75.           kingsubin -> 16.0%
76.            YeseulDo -> 16.0%
77.              372dev -> 16.0%
78.            LeeWoooo -> 16.0%
79.               yky03 -> 16.0%
80.          GunnwooKim -> 16.0%
81.          jwsims1995 -> 16.0%
82.               m3252 -> 16.0%
83.            wdEffort -> 16.0%
84.          rlatmd0829 -> 16.0%
85.            msmn1729 -> 16.0%
86.      sweetchinmusic -> 16.0%
87.         0417taehyun -> 16.0%
88.           MoonHKLee -> 16.0%
89.        devShLee7017 -> 16.0%
90.             ysmiler -> 16.0%
91.             hyeonic -> 16.0%
92.            ksundong -> 16.0%
93.            Yadon079 -> 16.0%
94.             9m1i9n1 -> 16.0%
95.                etff -> 16.0%
96.              devvip -> 16.0%
97.          koreas9408 -> 16.0%
98.           damho1104 -> 16.0%
99.          league3236 -> 16.0%
100.                gtpe -> 16.0%
101.          Chohongjae -> 16.0%
102.          ohjoohyung -> 16.0%
103.        sungpillhong -> 16.0%
104.         doyoung0205 -> 16.0%
105.           sunnynote -> 16.0%
106.           Jangilkyu -> 16.0%
107.            pond1029 -> 16.0%
108.         conyconydev -> 16.0%
109.           yeGenieee -> 16.0%
110.           garlickim -> 16.0%
111.           jikimee64 -> 16.0%
112.             mongzza -> 16.0%
113.           iseunghan -> 16.0%
114.               GGob2 -> 16.0%
115.               Yo0oN -> 16.0%
116.              yeo311 -> 16.0%
117.            giyeon95 -> 16.0%
118.             gblee87 -> 16.0%
119.          Jason-time -> 16.0%
120.          JoosJuliet -> 16.0%
121.             jessi68 -> 11.0%
122.          ohhhmycode -> 11.0%
123.             fpdjsns -> 11.0%
124.            jaxx2001 -> 11.0%
125.             zhaoSeo -> 11.0%
126.            star1606 -> 11.0%
127.           jaeyeon93 -> 11.0%
128.              Rebwon -> 11.0%
129.            younwony -> 11.0%
130.           whiteship -> 11.0%
131.         surfing2003 -> 11.0%
132.       Youngjin-KimY -> 11.0%
133.       accidentlywoo -> 11.0%
134.        cold-pumpkin -> 11.0%
135.         HyeonWuJeon -> 11.0%
136.            tbnsok40 -> 11.0%
137.             sjhello -> 11.0%
138.          sangwoobae -> 11.0%
139.             inhalin -> 11.0%
140.             keunyop -> 11.0%
141.          ByungJun25 -> 11.0%
142.           Dubidubab -> 11.0%
143.             2yeseul -> 11.0%
144.              DevRyu -> 11.0%
145.              JOYB28 -> 11.0%
146.             gintire -> 11.0%
147.      good-influence -> 11.0%
148.              elon09 -> 11.0%
149.          dongyeon94 -> 11.0%
150.              tocgic -> 11.0%
151.            ehdrhelr -> 11.0%
152.           gurumee92 -> 11.0%
153.           devksh930 -> 11.0%
154.          jaewon0913 -> 11.0%
155.         JeongJin984 -> 11.0%
156.            kksb0831 -> 11.0%
157.           sinchang1 -> 11.0%
158.      ShimSeoungChul -> 11.0%
159.             Gomding -> 11.0%
160.          ParkIlHoon -> 11.0%
161.            lee-jemu -> 11.0%
162.              sujl95 -> 11.0%
163.             fkfkfk9 -> 11.0%
164.       Kim-JunHyeong -> 11.0%
165.          JoongSeokD -> 11.0%
166.               s0w0s -> 11.0%
167.              mokaim -> 11.0%
168.        kimseungki94 -> 11.0%
169.            coldhoon -> 11.0%
170.     choiyoungkwon12 -> 11.0%
171.             ggomjae -> 5.0%
172.              hyngsk -> 5.0%
173.              metorg -> 5.0%
174.             cbw1030 -> 5.0%
175.           JuHyun419 -> 5.0%
176.     SnowisTargaryen -> 5.0%
177.         Lee-jaeyong -> 5.0%
178.              hyenny -> 5.0%
179.              yks095 -> 5.0%
180.           angelatto -> 5.0%
181.             TaeYing -> 5.0%
182.              cs7998 -> 5.0%
183.          yallyyally -> 5.0%
184.          monkeyDugi -> 5.0%
185.           sangw0804 -> 5.0%
186.          tjdqls1200 -> 5.0%
187.           zilzu4165 -> 5.0%
188.           jaehyunup -> 5.0%
189.              id6827 -> 5.0%
190.             Ahnyezi -> 5.0%
191.             ldw1220 -> 5.0%
192.              hanull -> 5.0%
193.              lleezz -> 5.0%
194.           goodzzong -> 5.0%
195.             kyunyan -> 5.0%
196.           EdwardJae -> 5.0%
197.              hwonny -> 5.0%
198.           batboy118 -> 5.0%
199.         riyenas0925 -> 5.0%
200.             jeeneee -> 5.0%
201.            ssayebee -> 5.0%
202.        castleCircle -> 5.0%
203.           kimmy100b -> 5.0%
204.            junhok82 -> 5.0%
205.             kdm8939 -> 5.0%
206.            kopokero -> 5.0%
207.           redbean88 -> 5.0%
208.         nekisse-lee -> 5.0%
209.              ghwann -> 5.0%
210.            oktopman -> 5.0%
211.         memoregoing -> 5.0%
212.             BaeJi77 -> 5.0%


```

</details>
<br><br>




:point_right: **코드는 다음과 같이 구성했다.**<br>

![image](https://user-images.githubusercontent.com/62331803/101137250-e3617d00-3651-11eb-8663-395689f5cf1b.png)
<br>

- `GHConnect` : 깃허브 객체를 싱글톤을 만들어서 사용하기 위한 클래스
- `Dao` :  데이터 처리
- `Service` : 기능 출력
- `Menu` : 메뉴
- `Main` : 메인

<br><br>

#### 2.1. GHConnect : 깃허브 객체 생성

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
	private static final String personalToken = "Insert your personal token";
	private static final Logger LOG = Logger.getGlobal();
	private GitHub github;

	public GHConnect() {
		try {// 깃허브 객체 생성
			this.github = new GitHubBuilder().withOAuthToken(personalToken).build();
			LOG.info("깃 계정 연결 성공");
		} catch (Exception e) {
			LOG.info("깃 계정 연결 실패. 재 연결이 필요합니다.");
		}
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

#### 2.2. Dao : 데이터 관리

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
	private Map<String, Integer> participants; // userid와 참여횟수
	private int total; // 이슈 총 개수

	public Dao() throws IOException {
		GHConnect con = new GHConnect();
		this.github = con.getConnection(); // GHConnect로부터 깃허브 객체 가져오기
		this.LOG = con.getLog(); // GHConnect로부터 로그 객체 가져오기
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

	// 세팅된 repository의 유저별 출석정보 세팅
	public void setAttendance() throws IOException {
		List<GHIssue> allTheIssues = repo.getIssues(GHIssueState.ALL); // 세팅된 리포지토리의 전체 issue
		Set<String> nameList = new HashSet<String>(); 		       // 하나의 issue에 코멘트를 남긴 user id를 담기 위한 임시 set (중복제거 위함)
		this.total = allTheIssues.size(); 			       // 출석율 계산을 위한 이슈 총 개수
		
		for (GHIssue issueForAWeek : allTheIssues) {				// 이슈를 1주자씩 가져와서
			for (GHIssueComment comment : issueForAWeek.getComments()) {	// 해당 이슈의 전체 코멘트 가져오기
				nameList.add(comment.getUser().getLogin()); 	        // 코멘트의 user id를 namelist(임시 set)에 삽입
			}
			insertNames(nameList);						// map<id, count>의 value(출석횟수) 증가시키기
			nameList.clear();
		}
	}

	// 만들어진 임시 set으로 map의 count(출석횟수) 증가시키기
	public void insertNames(Set<String> nameList) {
		nameList.forEach((name) -> {
			if (this.participants.containsKey(name)) {			// 이미 map에 존재하는 id일 경우
				this.participants.put(name, participants.get(name) + 1);	
			} else {						        // map에 존재하지 않는 id일 경우
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
```

<br><br>


#### 2.3. Service : 화면 출력

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

	// 리포지토리 세팅
	public boolean setRepo(String repo) throws IOException {
		return this.dao.setRepo(repo);
	}

	// userid로 출석율 검색
	public void findByName(String name) {
		Double rate = this.dao.getAttendenceRateByName(name);
		if (rate == 0) return;
		System.out.println(name + " : " + String.format("%.2f", rate) + "%" + "\n");
	}

	// 모든 유저의 출석율 검색, 출석율 높은 순으로 정렬
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

#### 2.4. Menu : 메뉴

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

#### 2.5. main : 메인

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

<br><br>



### 3. 회고

#### 3.1. 깃허브 자바 라이브러리를 사용하며

#### 3.2. 자바 람다식

#### 3.3. 단위 테스트의 필요성

