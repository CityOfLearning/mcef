// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.misc;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Vector;

/**
 * Class representing print settings.
 */
public abstract class CefPrintSettings {

	/**
	 * Print job color mode values.
	 */
	public enum ColorModel {
		COLOR_MODEL_UNKNOWN, COLOR_MODEL_GRAY, COLOR_MODEL_COLOR, COLOR_MODEL_CMYK, COLOR_MODEL_CMY, COLOR_MODEL_KCMY, COLOR_MODEL_CMY_K, // !<
																																			// CMY_K
																																			// represents
																																			// CMY+K.
		COLOR_MODEL_BLACK, COLOR_MODEL_GRAYSCALE, COLOR_MODEL_RGB, COLOR_MODEL_RGB16, COLOR_MODEL_RGBA, COLOR_MODEL_COLORMODE_COLOR, // !<
																																		// Used
																																		// in
																																		// samsung
																																		// printer
																																		// ppds.
		COLOR_MODEL_COLORMODE_MONOCHROME, // !< Used in samsung printer ppds.
		COLOR_MODEL_HP_COLOR_COLOR, // !< Used in HP color printer ppds.
		COLOR_MODEL_HP_COLOR_BLACK, // !< Used in HP color printer ppds.
		COLOR_MODEL_PRINTOUTMODE_NORMAL, // !< Used in foomatic ppds.
		COLOR_MODEL_PRINTOUTMODE_NORMAL_GRAY, // !< Used in foomatic ppds.
		COLOR_MODEL_PROCESSCOLORMODEL_CMYK, // !< Used in canon printer ppds.
		COLOR_MODEL_PROCESSCOLORMODEL_GREYSCALE, // !< Used in canon printer
													// ppds.
		COLOR_MODEL_PROCESSCOLORMODEL_RGB, // !< Used in canon printer ppds
	}

	/**
	 * Print job duplex mode values.
	 */
	public enum DuplexMode {
		DUPLEX_MODE_UNKNOWN, DUPLEX_MODE_SIMPLEX, DUPLEX_MODE_LONG_EDGE, DUPLEX_MODE_SHORT_EDGE,
	}

	/**
	 * Create a new CefPrintSettings object.
	 */
	public static final CefPrintSettings create() {
		return CefPrintSettings_N.createNative();
	}

	// This CTOR can't be called directly. Call method create() instead.
	CefPrintSettings() {
	}

	/**
	 * Returns a writable copy of this object.
	 */
	public abstract CefPrintSettings copy();

	/**
	 * Get the color model.
	 */
	public abstract ColorModel getColorModel();

	/**
	 * Get the number of copies.
	 */
	public abstract int getCopies();

	/**
	 * Get the device name.
	 */
	public abstract String getDeviceName();

	/**
	 * Get the DPI (dots per inch).
	 */
	public abstract int getDPI();

	/**
	 * Get the duplex mode.
	 */
	public abstract DuplexMode getDuplexMode();

	/**
	 * Retrieve the page ranges.
	 */
	public abstract void getPageRanges(Vector<CefPageRange> ranges);

	/**
	 * Returns the number of page ranges that currently exist.
	 */
	public abstract int getPageRangesCount();

	/**
	 * Returns true if the orientation is landscape.
	 *
	 */
	public abstract boolean isLandscape();

	/**
	 * Returns true if the values of this object are read-only. Some APIs may
	 * expose read-only objects.
	 */
	public abstract boolean isReadOnly();

	/**
	 * Returns true if only the selection will be printed.
	 */
	public abstract boolean isSelectionOnly();

	/**
	 * Returns true if this object is valid. Do not call any other methods if
	 * this function returns false.
	 */
	public abstract boolean isValid();

	/**
	 * Set whether pages will be collated.
	 */
	public abstract void setCollate(boolean collate);

	/**
	 * Set the color model.
	 */
	public abstract void setColorModel(ColorModel model);

	/**
	 * Set the number of copies.
	 */
	public abstract void setCopies(int copies);

	/**
	 * Set the device name.
	 */
	public abstract void setDeviceName(String name);

	/**
	 * Set the DPI (dots per inch).
	 */
	public abstract void setDPI(int dpi);

	/**
	 * Set the duplex mode.
	 */
	public abstract void setDuplexMode(DuplexMode mode);

	/**
	 * Set the page orientation.
	 */
	public abstract void setOrientation(boolean landscape);

	/**
	 * Set the page ranges.
	 */
	public abstract void setPageRanges(Vector<CefPageRange> ranges);

	/**
	 * Set the printer printable area in device units. Some platforms already
	 * provide flipped area. Set |landscape_needs_flip| to false on those
	 * platforms to avoid double flipping.
	 */
	public abstract void setPrinterPrintableArea(Dimension physical_size_device_units,
			Rectangle printable_area_device_units, boolean landscape_needs_flip);

	/**
	 * Set whether only the selection will be printed.
	 */
	public abstract void setSelectionOnly(boolean selection_only);

	/**
	 * Returns true if pages will be collated.
	 */
	public abstract boolean willCollate();
}
