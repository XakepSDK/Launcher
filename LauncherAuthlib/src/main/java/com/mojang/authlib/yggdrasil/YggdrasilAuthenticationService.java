package com.mojang.authlib.yggdrasil;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.sashok724.launcher.client.helper.LogHelper;

public final class YggdrasilAuthenticationService implements AuthenticationService {
	@SuppressWarnings("UnusedParameters")
	public YggdrasilAuthenticationService(Proxy proxy, String clientToken) {
		LogHelper.debug("Patched AuthenticationService created: '%s'", clientToken);
	}

	@Override
	public MinecraftSessionService createMinecraftSessionService() {
		return new YggdrasilMinecraftSessionService(this);
	}

	@Override
	public GameProfileRepository createProfileRepository() {
		return new YggdrasilGameProfileRepository();
	}

	@Override
	public UserAuthentication createUserAuthentication(Agent agent) {
		throw new UnsupportedOperationException("createUserAuthentication is used only by Mojang Launcher");
	}
}
