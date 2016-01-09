package net.sashok724.launcher.server.command.handler;

import java.io.BufferedReader;
import java.io.IOException;

import net.sashok724.launcher.client.helper.IOHelper;
import net.sashok724.launcher.server.LaunchServer;

public final class StdCommandHandler extends CommandHandler {
	private final BufferedReader reader;

	public StdCommandHandler(LaunchServer server) {
		super(server);
		reader = IOHelper.newReader(System.in);
	}

	@Override
	public void bell() {
		// Do nothing, unsupported
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("clear terminal");
	}

	@Override
	public String readLine() throws IOException {
		return reader.readLine();
	}
}
