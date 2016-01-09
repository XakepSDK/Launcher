package net.sashok724.launcher.server.auth.provider;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import net.sashok724.launcher.client.LauncherAPI;
import net.sashok724.launcher.client.helper.VerifyHelper;
import net.sashok724.launcher.client.serialize.config.ConfigObject;
import net.sashok724.launcher.client.serialize.config.entry.BlockConfigEntry;

public abstract class AuthProvider extends ConfigObject implements AutoCloseable {
	private static final Map<String, Adapter<AuthProvider>> AUTH_PROVIDERS = new ConcurrentHashMap<>(8);

	@LauncherAPI
	protected AuthProvider(BlockConfigEntry block) {
		super(block);
	}

	@Override
	public abstract void close() throws IOException;

	@LauncherAPI
	public abstract String auth(String login, String password) throws Exception;

	@LauncherAPI
	public static AuthProvider newProvider(String name, BlockConfigEntry block) {
		VerifyHelper.verifyIDName(name);
		Adapter<AuthProvider> authHandlerAdapter = VerifyHelper.getMapValue(AUTH_PROVIDERS, name,
			String.format("Unknown auth provider: '%s'", name));
		return authHandlerAdapter.convert(block);
	}

	@LauncherAPI
	public static void registerProvider(String name, Adapter<AuthProvider> adapter) {
		VerifyHelper.putIfAbsent(AUTH_PROVIDERS, name, Objects.requireNonNull(adapter, "adapter"),
			String.format("Auth provider has been already registered: '%s'", name));
	}

	static {
		registerProvider("null", NullAuthProvider::new);
		registerProvider("accept", AcceptAuthProvider::new);
		registerProvider("reject", RejectAuthProvider::new);

		// Auth providers that doesn't do nothing :D
		registerProvider("mysql", MySQLAuthProvider::new);
		registerProvider("file", FileAuthProvider::new);
		registerProvider("request", RequestAuthProvider::new);
	}
}
