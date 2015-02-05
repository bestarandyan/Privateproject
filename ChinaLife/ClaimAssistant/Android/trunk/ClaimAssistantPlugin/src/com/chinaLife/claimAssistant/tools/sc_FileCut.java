package com.chinaLife.claimAssistant.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

//文件分割类  
public class sc_FileCut {
	private OutputStream ou;
	private File file;
	private long fileLength;
	private byte[] buf;
	private RandomAccessFile saveFile;

	public sc_FileCut(String f) {
		file = new File(f);
		if (file.exists()) {
			fileLength = file.length();
		} else {
			fileLength = 0;
		}
	}

	public void Cut(int n, String fn, String tgt) throws IOException {
		File fe;
		int seeklen = 0;
		int tpLen = 0;
		int tpDe = 0;
		if (fileLength % n == 0) {
			tpLen = (int) (fileLength / n);
		} else {
			tpLen = (int) (fileLength / n);
			tpDe = (int) (fileLength % n);
		}

		saveFile = new RandomAccessFile(file, "r");
		saveFile.seek(0);

		for (int i = 1; i <= n; i++) {
			fe = new File(file.getParent() + "\\" + fn + "_" + i + "." + tgt);
			if (i < n) {
				if (tpLen != 0) {
					buf = new byte[tpLen];
					saveFile.read(buf);
					ou = new FileOutputStream(fe);
					ou.write(buf);
					seeklen += tpLen;
					saveFile.seek(seeklen);
					buf = null;
				}
			} else if (i == n) {
				if (tpDe != 0) {
					buf = new byte[tpLen + tpDe];
					saveFile.read(buf);
					ou = new FileOutputStream(fe);
					ou.write(buf);
					buf = null;
				}
			}
		}
		saveFile.close();
	}
}
