package org.mycard.model.data;

import java.io.File;

import org.mycard.StaticApplication;
import org.mycard.common.Constants;

import android.text.TextUtils;


public final class ImageItemInfoHelper {
	final static String SUFFIX_THUMNAIL = "t";
	final static String SUFFIX_ORIGINAL = "o";
	
	final static String HTTP_PREFIX = "http://";
	final static String HTTPS_PREFIX = "https://";
	
	private static final String IMAGE_SUFFIX = ".jpg";
			
	
	public static boolean isThumnailExist(ImageItem item) {
		final String path = getThumnailPath(item);
		if (path == null)
			return false;
		
		File file = new File(path);
		if (file.exists() && file.isFile())
			return true;
		
		return false;
	}
	
	public static String getThumnailPath(ImageItem item) {
		if (item == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(StaticApplication.peekInstance().getCardImagePath()).append(Constants.THUMBNAIL_IMAGE_DIRECTORY);
		if (!new File(sb.toString()).exists()) {
			new File(sb.toString()).mkdirs();
		}
		sb.append(item.id).append(IMAGE_SUFFIX);
		return sb.toString();
	}
	
	public static String getThumnailUrl(ImageItem item) {
		String url = item.id;
		if (TextUtils.isEmpty(url))
			return null;
		
		if (url.startsWith(HTTP_PREFIX) || url.startsWith(HTTPS_PREFIX))
			return url;
		
		final String baseUrl = ResourcesConstants.THUMBNAIL_URL;
		url = TextUtils.isEmpty(baseUrl) ? null : baseUrl + url + IMAGE_SUFFIX;
		return url;
	}
	
	public static boolean isImageExist(ImageItem item) {
		final String path = getImagePath(item);
		if (path == null)
			return false;
		
		File file = new File(path);
		if (file.exists() && file.isFile())
			return true;
		
		return false;
	}
	
	public static String getImagePath(ImageItem item) {
		if (item == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(StaticApplication.peekInstance().getCardImagePath()).append(Constants.CARD_IMAGE_DIRECTORY);
		if (!new File(sb.toString()).exists()) {
			new File(sb.toString()).mkdirs();
		}
		sb.append(item.id).append(IMAGE_SUFFIX);
		return sb.toString();
	}
	
	public static String getImageUrl(ImageItem item) {
		String url = item.id;
		if (TextUtils.isEmpty(url))
			return null;
		
		if (url.startsWith(HTTP_PREFIX) || url.startsWith(HTTPS_PREFIX))
			return url;
		
		final String baseUrl = ResourcesConstants.IMAGE_URL;
		url = TextUtils.isEmpty(baseUrl) ? null : baseUrl + url + IMAGE_SUFFIX;
		return url;
	}
	
}
