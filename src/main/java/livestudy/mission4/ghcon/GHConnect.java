package livestudy.mission4.ghcon;

import java.util.logging.Logger;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class GHConnect {
	private static final String personalToken = "Insert your personal token";
	private static GHConnect con = new GHConnect();
	private Logger LOG = Logger.getGlobal();
	private static GitHub github;

	private GHConnect() {
		LOG.info("GHConnect 객체 생성 중");
		try {// 깃허브 객체 생성
			this.github = new GitHubBuilder().withOAuthToken(personalToken).build();
			LOG.info("깃 계정 연결 성공");
		} catch (Exception e) {
			LOG.info("깃 계정 연결 실패. 재 연결이 필요합니다.");
		}
	}
	
	public static GitHub getConnection() {
		return github;
	}
}
