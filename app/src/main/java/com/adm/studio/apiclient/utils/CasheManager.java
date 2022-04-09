package com.adm.studio.apiclient.utils;

import android.graphics.Bitmap;
import android.content.Context;
import java.io.File;
import android.graphics.BitmapFactory;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.util.LruCache;

public class CasheManager {

	public static final String TAG = "CasheImageManager";
	public static final String IMAGE = "image/";

	public static Bitmap getImage(Context context, String imageName) throws Exception {
		log("getImage");

		String basePath = context.getCacheDir() + "/images/" + imageName;
		Bitmap bitmap = null;

		try {

			File file = new File(basePath);
			if (!file.exists())return null;

			bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

		} catch (Exception e) {
			log("getImage1 :" + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return bitmap;
	}


	public static boolean putImage(Context context, Bitmap btimap, String imageName) {
		log("putImage");

		boolean result = false;
		String basePath = context.getCacheDir() + "/images";

		FileOutputStream outputStream = null;

		try {

			File filePath = new File(basePath);
			if (!filePath.exists()) {
				filePath.mkdir();
			}

			filePath = new File(basePath + "/");
			File file = File.createTempFile(imageName, imageName, filePath);

			outputStream = new FileOutputStream(file);
			result = btimap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

			filePath = new File(basePath + "/" + imageName);
			file.renameTo(filePath);

		} catch (Exception e) {
			log("putImage1 : " + e.getMessage());
			e.printStackTrace();

		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					log("putImage2 : " + e.getMessage());
					e.printStackTrace();

				}
			}
		}

		return result;
	}


	private static void log(Object ob) {
		Log.d(Consent.TAG, Consent.TAG_CACHE_IMAGE_MANAGER + " : " + ob.toString());
	}



}

