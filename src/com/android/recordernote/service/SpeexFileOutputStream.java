package com.android.recordernote.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.recordernote.jni.Speex;

public class SpeexFileOutputStream extends FileOutputStream {

	public SpeexFileOutputStream(File file) throws FileNotFoundException {
		super(file);
		Speex.init();
	}

	public void write(short[] bufferin) throws IOException {
		byte[] bufferout = new byte[160];
		Speex.encode(bufferin, bufferout);
		super.write(bufferout);
	}

	@Override
	public void close() throws IOException {
		Speex.release();
		super.close();
	}
}
