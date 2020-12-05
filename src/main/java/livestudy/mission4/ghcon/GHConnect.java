package livestudy.mission4.ghcon;

import java.util.logging.Logger;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class GHConnect {
//	private static final String personalToken = "Insert your personal token";
	private static final String personalToken = "a3d6b51b10bfc360d4005c03bc8c9125c1a78422";
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
