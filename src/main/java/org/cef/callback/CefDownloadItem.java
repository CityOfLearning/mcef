// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import java.util.Date;

/**
 * Class used to represent a download item.
 */
public interface CefDownloadItem {
	/**
	 * Returns the content disposition.
	 */
	String getContentDisposition();

	/**
	 * Returns a simple speed estimate in bytes/s.
	 */
	long getCurrentSpeed();

	/**
	 * Returns the time that the download ended.
	 */
	Date getEndTime();

	/**
	 * Returns the full path to the downloaded or downloading file.
	 */
	String getFullPath();

	/**
	 * Returns the unique identifier for this download.
	 */
	int getId();

	/**
	 * Returns the mime type.
	 */
	String getMimeType();

	/**
	 * Returns the rough percent complete or -1 if the receive total size is
	 * unknown.
	 */
	int getPercentComplete();

	/**
	 * Returns the number of received bytes.
	 */
	long getReceivedBytes();

	/**
	 * Returns the time that the download started.
	 */
	Date getStartTime();

	/**
	 * Returns the suggested file name.
	 */
	String getSuggestedFileName();

	/**
	 * Returns the total number of bytes.
	 */
	long getTotalBytes();

	/**
	 * Returns the URL.
	 */
	String getURL();

	/**
	 * Returns true if the download has been canceled or interrupted.
	 */
	boolean isCanceled();

	/**
	 * Returns true if the download is complete.
	 */
	boolean isComplete();

	/**
	 * Returns true if the download is in progress.
	 */
	boolean isInProgress();

	/**
	 * Returns true if this object is valid. Do not call any other methods if
	 * this function returns false.
	 */
	boolean isValid();
}
