package net.sashok724.launcher.client.request.auth;

import java.io.IOException;
import java.util.regex.Pattern;

import net.sashok724.launcher.client.Launcher;
import net.sashok724.launcher.client.LauncherAPI;
import net.sashok724.launcher.client.helper.SecurityHelper;
import net.sashok724.launcher.client.helper.VerifyHelper;
import net.sashok724.launcher.client.request.Request;
import net.sashok724.launcher.client.serialize.HInput;
import net.sashok724.launcher.client.serialize.HOutput;

public final class JoinServerRequest extends Request<Boolean> {
	private static final Pattern SERVERID_PATTERN = Pattern.compile("-?[0-9a-f]{1,40}");

	// Instance
	private final String username;
	private final String accessToken;
	private final String serverID;

	@LauncherAPI
	public JoinServerRequest(Launcher.Config config, String username, String accessToken, String serverID) {
		super(config);
		this.username = VerifyHelper.verifyUsername(username);
		this.accessToken = SecurityHelper.verifyToken(accessToken);
		this.serverID = verifyServerID(serverID);
	}

	@LauncherAPI
	public JoinServerRequest(String username, String accessToken, String serverID) {
		this(null, username, accessToken, serverID);
	}

	@Override
	public Type getType() {
		return Type.JOIN_SERVER;
	}

	@Override
	protected Boolean requestDo(HInput input, HOutput output) throws IOException {
		output.writeASCII(username, 16);
		output.writeASCII(accessToken, -SecurityHelper.TOKEN_STRING_LENGTH);
		output.writeASCII(serverID, 41); // 1 char for minus sign
		output.flush();

		// Read response
		readError(input);
		return input.readBoolean();
	}

	@LauncherAPI
	public static boolean isValidServerID(CharSequence serverID) {
		return SERVERID_PATTERN.matcher(serverID).matches();
	}

	@LauncherAPI
	public static String verifyServerID(String serverID) {
		return VerifyHelper.verify(serverID, JoinServerRequest::isValidServerID,
			String.format("Invalid server ID: '%s'", serverID));
	}
}
