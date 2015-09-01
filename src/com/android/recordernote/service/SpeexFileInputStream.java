package com.android.recordernote.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.android.recordernote.jni.Speex;

public class SpeexFileInputStream extends FileInputStream {

	public SpeexFileInputStream(File file) throws FileNotFoundException {
		super(file);
		Speex.init();
	}

	public void read(short[] bufferout) throws IOException {
		byte[] bufferin = new byte[160];
		super.read(bufferin);
		Speex.decode(bufferin, bufferout);
	}

	@Override
	public void close() throws IOException {
		Speex.release();
		super.close();
	}
}
