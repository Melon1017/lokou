package com.lokou.myweb.raft;
import java.util.HashMap;
import java.util.Map;
import com.lokou.common.LokouPropertis;
import com.lokou.common.Server;

public class Servers {
	Map<String, Server> servers;
	Server locoal;

	private Servers() {
		String serverstr = LokouPropertis.getLokouPropertis()
				.getProperty("servers").toString();
		servers = new HashMap<String, Server>();
		if (serverstr != null) {
			String[] serverArray = serverstr.split("^");
			for (String severinfoStr : serverArray) {
				Server server = new Server();
				server.setHost(severinfoStr.replaceAll(":.*", ""));
				server.setPort(Integer.parseInt(severinfoStr.replaceAll(".*:",
						"")));
				server.setId(severinfoStr);
				servers.put(server.getId(), server);
			}
		}
		String localServerStr = LokouPropertis.getLokouPropertis()
				.getProperty("servers").toString();
		if (localServerStr != null) {
			Server server = new Server();
			server.setHost(localServerStr.replaceAll(":.*", ""));
			server.setPort(Integer.parseInt(localServerStr
					.replaceAll(".*:", "")));
			server.setId(localServerStr);
			locoal = server;

		}
	}

	public Server getLocoal() {
		return locoal;
	}

	public void setLocoal(Server locoal) {
		this.locoal = locoal;
	}

	public Map<String, Server> getServers() {
		return servers;
	}

	public void setServers(Map<String, Server> servers) {
		this.servers = servers;
	}

	static class ServersHolder {
		public static Servers servers = new Servers();
	}

	public static Servers getInstance() {
		return ServersHolder.servers;
	}

}
