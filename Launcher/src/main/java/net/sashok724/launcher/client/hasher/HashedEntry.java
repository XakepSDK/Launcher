package net.sashok724.launcher.client.hasher;

import java.io.IOException;

import net.sashok724.launcher.client.LauncherAPI;
import net.sashok724.launcher.client.serialize.HInput;
import net.sashok724.launcher.client.serialize.stream.EnumSerializer;
import net.sashok724.launcher.client.serialize.stream.StreamObject;

public abstract class HashedEntry extends StreamObject {
	@LauncherAPI public boolean flag; // For external usage

	@LauncherAPI
	public abstract Type getType();

	@LauncherAPI
	public abstract long size();

	@LauncherAPI
	public enum Type implements EnumSerializer.Itf {
		DIR(1), FILE(2);
		private static final EnumSerializer<Type> SERIALIZER = new EnumSerializer<>(Type.class);
		private final int n;

		Type(int n) {
			this.n = n;
		}

		@Override
		public int getNumber() {
			return n;
		}

		public static Type read(HInput input) throws IOException {
			return SERIALIZER.read(input);
		}
	}
}
