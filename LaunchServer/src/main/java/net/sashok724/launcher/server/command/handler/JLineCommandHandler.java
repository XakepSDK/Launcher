package net.sashok724.launcher.server.command.handler;

import java.io.IOException;

import jline.console.ConsoleReader;
import net.sashok724.launcher.client.helper.LogHelper;
import net.sashok724.launcher.server.LaunchServer;

public final class JLineCommandHandler extends CommandHandler {
	private final ConsoleReader reader;

	public JLineCommandHandler(LaunchServer server) throws IOException {
		super(server);

		// Set reader
		reader = new ConsoleReader();
		reader.setExpandEvents(false);

		// Replace writer
		LogHelper.removeStdOutput();
		LogHelper.addOutput(new JLineOutput());
	}

	@Override
	public void bell() throws IOException {
		reader.beep();
	}

	@Override
	public void clear() throws IOException {
		reader.clearScreen();
	}

	@Override
	public String readLine() throws IOException {
		return reader.readLine();
	}

	private final class JLineOutput implements LogHelper.Output {
		@Override
		public void println(String message) {
			try {
				reader.println(ConsoleReader.RESET_LINE + message);
				reader.drawLine();
				reader.flush();
			} catch (IOException ignored) {
				// Ignored
			}
		}
	}
}
