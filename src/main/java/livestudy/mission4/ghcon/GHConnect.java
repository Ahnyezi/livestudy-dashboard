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
		try {// ����� ��ü ����
			this.github = new GitHubBuilder().withOAuthToken(personalToken).build();
			LOG.info("�� ���� ���� ����");
		} catch (Exception e) {
			LOG.info("�� ���� ���� ����. �� ������ �ʿ��մϴ�.");
		}
	}
	public GitHub getConnection() {
		return github;
	}

	public Logger getLog() {
		return LOG;
	}
}
