package net.sashok724.launcher.server.auth.handler;

import java.io.IOException;
import java.util.UUID;

import net.sashok724.launcher.client.LauncherAPI;
import net.sashok724.launcher.client.helper.VerifyHelper;
import net.sashok724.launcher.client.serialize.config.entry.BlockConfigEntry;

public final class NullAuthHandler extends AuthHandler {
	private volatile AuthHandler handler;

	public NullAuthHandler(BlockConfigEntry block) {
		super(block);
	}

	@Override
	public UUID auth(String username, String accessToken) throws IOException {
		return getHandler().auth(username, accessToken);
	}

	@Override
	public UUID checkServer(String username, String serverID) throws IOException {
		return getHandler().checkServer(username, serverID);
	}

	@Override
	public void close() throws IOException {
		AuthHandler handler = this.handler;
		if (handler != null) {
			handler.close();
		}
	}

	@Override
	public boolean joinServer(String username, String accessToken, String serverID) throws IOException {
		return getHandler().joinServer(username, accessToken, serverID);
	}

	@Override
	public UUID usernameToUUID(String username) throws IOException {
		return getHandler().usernameToUUID(username);
	}

	@Override
	public String uuidToUsername(UUID uuid) throws IOException {
		return getHandler().uuidToUsername(uuid);
	}

	@LauncherAPI
	public void setBackend(AuthHandler handler) {
		this.handler = handler;
	}

	private AuthHandler getHandler() {
		return VerifyHelper.verify(handler, a -> a != null, "Backend auth handler wasn't set");
	}
}
