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
		try {
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
