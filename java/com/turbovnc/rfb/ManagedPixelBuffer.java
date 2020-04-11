/* Copyright (C) 2002-2005 RealVNC Ltd.  All Rights Reserved.
 * Copyright (C) 2012 D. R. Commander.  All Rights Reserved.
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

public class ManagedPixelBuffer extends PixelBuffer {
  public void setSize(int w, int h) {
    width = w;
    height = h;
    checkDataSize();
  }
  public void setPF(PixelFormat pf) {
    super.setPF(pf);
    checkDataSize();
  }

  public int dataLen() { return area(); }

  final void checkDataSize() {
    if (data == null || ((int[])data).length < dataLen())
      data = new int[dataLen()];
  }
}
