package com.odious.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoadAndSave {

	public static final boolean serialize(Object obj, String fileName) {
		// write to disk
		FileOutputStream saveFile;
		try {
			saveFile = new FileOutputStream(System.getProperty("user.dir")
					+ "\\" + fileName);
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
			return false;
		}

		// write object to ObjectOutputStream
		ObjectOutputStream objOut;
		try {
			objOut = new ObjectOutputStream(saveFile);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

		try {
			objOut.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (objOut != null) {
					objOut.flush();
					objOut.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return true;
	}

	public static final Object load(String fileName) {
		Object obj = null;
		File test = new File(System.getProperty("user.dir") + "\\" + fileName);
		if (test.exists()) {
			FileInputStream fileIn;
			try {
				fileIn = new FileInputStream(System.getProperty("user.dir")
						+ "\\" + fileName);
			} catch (FileNotFoundException fnf) {
				fnf.printStackTrace();
				return null;
			}

			ObjectInputStream objIn;
			try {
				objIn = new ObjectInputStream(fileIn);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return null;
			}

			try {
				obj = objIn.readObject();

			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (objIn != null && fileIn != null) {
						objIn.close();
						System.out.println("objin closed!");
						fileIn.close();
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		return obj;
	}

}