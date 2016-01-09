package net.sashok724.launcher.client.request;

import java.io.IOException;

import net.sashok724.launcher.client.Launcher;
import net.sashok724.launcher.client.LauncherAPI;
import net.sashok724.launcher.client.serialize.HInput;
import net.sashok724.launcher.client.serialize.HOutput;

public final class PingRequest extends Request<Void> {
	@LauncherAPI public static final byte EXPECTED_BYTE = 0b01010101;

	@LauncherAPI
	public PingRequest(Launcher.Config config) {
		super(config);
	}

	@LauncherAPI
	public PingRequest() {
		this(null);
	}

	@Override
	public Type getType() {
		return Type.PING;
	}

	@Override
	protected Void requestDo(HInput input, HOutput output) throws IOException {
		byte pong = (byte) input.readUnsignedByte();
		if (pong != EXPECTED_BYTE) {
			throw new IOException("Illegal ping response: " + pong);
		}
		return null;
	}
}
