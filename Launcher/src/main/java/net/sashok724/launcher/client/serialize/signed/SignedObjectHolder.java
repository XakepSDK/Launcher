package net.sashok724.launcher.client.serialize.signed;

import java.io.IOException;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import net.sashok724.launcher.client.LauncherAPI;
import net.sashok724.launcher.client.serialize.HInput;
import net.sashok724.launcher.client.serialize.stream.StreamObject;

public final class SignedObjectHolder<O extends StreamObject> extends SignedBytesHolder {
	@LauncherAPI public final O object;

	@LauncherAPI
	public SignedObjectHolder(HInput input, RSAPublicKey publicKey, Adapter<O> adapter) throws IOException, SignatureException {
		super(input, publicKey);
		object = newInstance(adapter);
	}

	@LauncherAPI
	public SignedObjectHolder(O object, RSAPrivateKey privateKey) throws IOException {
		super(object.write(), privateKey);
		this.object = object;
	}

	@LauncherAPI
	public O newInstance(Adapter<O> adapter) throws IOException {
		try (HInput input = new HInput(bytes)) {
			return adapter.convert(input);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SignedObjectHolder && object.equals(((SignedObjectHolder<?>) obj).object);
	}

	@Override
	public int hashCode() {
		return object.hashCode();
	}

	@Override
	public String toString() {
		return object.toString();
	}
}
