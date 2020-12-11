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
		LOG.info("GHConnect ��ü ���� ��");
		try {// ����� ��ü ����
			this.github = new GitHubBuilder().withOAuthToken(personalToken).build();
			LOG.info("�� ���� ���� ����");
		} catch (Exception e) {
			LOG.info("�� ���� ���� ����. �� ������ �ʿ��մϴ�.");
		}
	}
	
	public static GitHub getConnection() {
		return github;
	}
}
