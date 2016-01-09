package net.sashok724.launcher.server.command.hash;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sashok724.launcher.client.client.ClientProfile;
import net.sashok724.launcher.client.helper.IOHelper;
import net.sashok724.launcher.client.helper.LogHelper;
import net.sashok724.launcher.server.LaunchServer;
import net.sashok724.launcher.server.command.Command;

public final class DownloadAssetCommand extends Command {
	private static final String ASSET_URL_MASK = "https://launcher.sashok724.net/download/assets/%s.zip";

	public DownloadAssetCommand(LaunchServer server) {
		super(server);
	}

	@Override
	public String getArgsDescription() {
		return "<version> <dir>";
	}

	@Override
	public String getUsageDescription() {
		return "Download asset dir";
	}

	@Override
	public void invoke(String... args) throws Exception {
		verifyArgs(args, 2);
		ClientProfile.Version version = ClientProfile.Version.byName(args[0]);
		String dirName = IOHelper.verifyFileName(args[1]);
		Path assetDir = LaunchServer.UPDATES_DIR.resolve(dirName);

		// Create asset dir
		LogHelper.subInfo("Creating asset dir: '%s'", dirName);
		Files.createDirectory(assetDir);

		// Download required asset
		LogHelper.subInfo("Downloading asset, it may take some time");
		unpack(new URL(String.format(ASSET_URL_MASK, IOHelper.urlEncode(version.name))), assetDir);

		// Finished
		server.syncUpdatesDir(Collections.singleton(dirName));
		LogHelper.subInfo("Asset successfully downloaded: '%s'", dirName);
	}

	public static void unpack(URL url, Path dir) throws IOException {
		try (ZipInputStream input = IOHelper.newZipInput(url)) {
			for (ZipEntry entry = input.getNextEntry(); entry != null; entry = input.getNextEntry()) {
				if (entry.isDirectory()) {
					continue; // Skip directories
				}

				// Unpack entry
				String name = entry.getName();
				LogHelper.subInfo("Downloading file: '%s'", name);
				IOHelper.transfer(input, dir.resolve(IOHelper.toPath(name)));
			}
		}
	}
}
