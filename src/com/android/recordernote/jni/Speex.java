package com.android.recordernote.jni;

public class Speex {
	static {
		System.loadLibrary("speex-jni");
	}

	public static native void init();

	public static native void release();

	public static native int encode(short[] in, byte[] out);

	public static native int decode(byte[] in, short[] out);
}
