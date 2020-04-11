/* Copyright (C) 2002-2005 RealVNC Ltd.  All Rights Reserved.
 * Copyright (C) 2011-2012, 2015, 2018 D. R. Commander.  All Rights Reserved.
 * Copyright (C) 2012 Brian P. Hinz
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */

package com.turbovnc.rfb;

import com.turbovnc.rdr.*;

public class ConnParams {

  static LogWriter vlog = new LogWriter("ConnParams");

  public ConnParams() {
    majorVersion = 0;  minorVersion = 0;
    width = 0;  height = 0;
    supportsDesktopResize = false;  supportsExtendedDesktopSize = false;
    supportsDesktopRename = false;  supportsLastRect = false;
    supportsSetDesktopSize = false;  supportsFence = false;
    supportsContinuousUpdates = false;
    supportsClientRedirect = false;
    supportsGII = false;
    name = null;  nEncodings = 0;  encodings = null;
    verStrPos = 0;
    screenLayout = new ScreenSet();

    setName("");
  }

  public boolean readVersion(InStream is) {
    done = false;
    if (verStrPos >= 12) return false;
    verStr = new StringBuilder(13);
    while (is.checkNoWait(1) && verStrPos < 12) {
      verStr.insert(verStrPos++, (char)is.readU8());
    }

    if (verStrPos < 12) {
      done = false;
      return true;
    }
    done = true;
    verStr.insert(12, '0');
    verStrPos = 0;
    if (verStr.toString().matches("RFB \\d{3}\\.\\d{3}\\n0")) {
      majorVersion = Integer.parseInt(verStr.substring(4, 7));
      minorVersion = Integer.parseInt(verStr.substring(8, 11));
      return true;
    }
    return false;
  }

  public void writeVersion(OutStream os) {
    String str = String.format("RFB %03d.%03d\n", majorVersion, minorVersion);
    os.writeBytes(str.getBytes(), 0, 12);
    os.flush();
  }

  // CHECKSTYLE VisibilityModifier:OFF
  public int majorVersion;
  public int minorVersion;
  // CHECKSTYLE VisibilityModifier:ON

  public void setVersion(int major, int minor) {
    majorVersion = major;  minorVersion = minor;
  }

  public boolean isVersion(int major, int minor) {
    return majorVersion == major && minorVersion == minor;
  }

  public boolean beforeVersion(int major, int minor) {
    return (majorVersion < major ||
            (majorVersion == major && minorVersion < minor));
  }

  public boolean afterVersion(int major, int minor) {
    return !beforeVersion(major, minor + 1);
  }

  // CHECKSTYLE VisibilityModifier:OFF
  public int width;
  public int height;
  public ScreenSet screenLayout;
  // CHECKSTYLE VisibilityModifier:ON

  public PixelFormat pf() { return pf; }

  public void setPF(PixelFormat pf_) {
    pf = pf_;
    if (pf.bpp != 8 && pf.bpp != 16 && pf.bpp != 32) {
      throw new ErrorException("setPF(): not 8, 16, or 32 bpp?");
    }
  }

  public String name() { return name; }

  public void setName(String name_) {
    name = name_;
  }

  public int nEncodings() { return nEncodings; }

  public int[] encodings() { return encodings; }

  boolean done;

  // CHECKSTYLE VisibilityModifier:OFF
  public boolean supportsDesktopResize;
  public boolean supportsExtendedDesktopSize;
  public boolean supportsDesktopRename;
  public boolean supportsClientRedirect;
  public boolean supportsFence;
  public boolean supportsContinuousUpdates;
  public boolean supportsLastRect;
  public boolean supportsGII;

  public boolean supportsSetDesktopSize;
  // CHECKSTYLE VisibilityModifier:ON

  private PixelFormat pf;
  private String name;
  private int nEncodings;
  private int[] encodings;
  private StringBuilder verStr;
  private int verStrPos;
}
