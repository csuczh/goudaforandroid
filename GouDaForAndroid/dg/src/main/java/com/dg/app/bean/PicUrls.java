package com.dg.app.bean;

import android.text.TextUtils;


public class PicUrls extends Base{
	// 中等质量图片url前缀
	private static final String BMIDDLE_URL = "http://ww3.sinaimg.cn/bmiddle/";
	// 原质量图片url前缀
	private static final String ORIGINAL_URL = "http://ww3.sinaimg.cn/large/";
	
	private String thumbnail_pic;
	private String bmiddle_pic;
	private String original_pic;
	
	private boolean showOriImag;

	/**
	 * 从缩略图url中截取末尾的图片id,用于和拼接成其他质量图片url
	 */
	public String getImageId() {
		int indexOf = thumbnail_pic.lastIndexOf("/") + 1;
		return thumbnail_pic.substring(indexOf);
	}

	/**
	 * @return 返回默认图像
 	 */
	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	/**
	 * @return  返回中等图像
	 */
	public String getBmiddle_pic() {
		return TextUtils.isEmpty(bmiddle_pic) ? BMIDDLE_URL + getImageId() : bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	/**
	 * @return 返回原图
	 */
	public String getOriginal_pic() {
		return TextUtils.isEmpty(original_pic) ? ORIGINAL_URL + getImageId() : original_pic;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}

	/**
	 * @return 是否显示原图
	 */
	public boolean isShowOriImag() {
		return showOriImag;
	}

	public void setShowOriImag(boolean showOriImag) {
		this.showOriImag = showOriImag;
	}
	
}