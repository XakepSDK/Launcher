package net.sashok724.launcher.server.texture;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.sashok724.launcher.client.LauncherAPI;
import net.sashok724.launcher.client.client.PlayerProfile;
import net.sashok724.launcher.client.helper.VerifyHelper;
import net.sashok724.launcher.client.serialize.config.ConfigObject;
import net.sashok724.launcher.client.serialize.config.entry.BlockConfigEntry;

public abstract class TextureProvider extends ConfigObject implements AutoCloseable {
	private static final Map<String, Adapter<TextureProvider>> TEXTURE_PROVIDERS = new ConcurrentHashMap<>(2);

	@LauncherAPI
	protected TextureProvider(BlockConfigEntry block) {
		super(block);
	}

	@LauncherAPI
	public abstract PlayerProfile.Texture getCloakTexture(UUID uuid, String username) throws IOException;

	@LauncherAPI
	public abstract PlayerProfile.Texture getSkinTexture(UUID uuid, String username) throws IOException;

	@LauncherAPI
	public static TextureProvider newProvider(String name, BlockConfigEntry block) {
		VerifyHelper.verifyIDName(name);
		Adapter<TextureProvider> authHandlerAdapter = VerifyHelper.getMapValue(TEXTURE_PROVIDERS, name,
			String.format("Unknown texture provider: '%s'", name));
		return authHandlerAdapter.convert(block);
	}

	@Override
	public abstract void close() throws IOException;

	@LauncherAPI
	public static void registerProvider(String name, Adapter<TextureProvider> adapter) {
		VerifyHelper.putIfAbsent(TEXTURE_PROVIDERS, name, Objects.requireNonNull(adapter, "adapter"),
			String.format("Texture provider has been already registered: '%s'", name));
	}

	static {
		registerProvider("null", NullTextureProvider::new);
		registerProvider("void", VoidTextureProvider::new);

		// Auth providers that doesn't do nothing :D
		registerProvider("request", RequestTextureProvider::new);
	}
}
