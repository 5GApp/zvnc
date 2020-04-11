/* Copyright (C) 2002-2005 RealVNC Ltd.  All Rights Reserved.
 * Copyright (C) 2017 D. R. Commander.  All Rights Reserved.
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

//
// CSecurity - class on the client side for handling security handshaking.  A
// derived class for a particular security type overrides the processMsg()
// method.  processMsg() is called first when the security type has been
// decided on, and will keep being called whenever there is data to read from
// the server until either it returns 0, indicating authentication/security
// failure, or it returns 1, to indicate success.  A return value of 2
// (actually anything other than 0 or 1) indicates that it should be called
// back when there is more data to read.
//
// Note that the first time processMsg() is called, there is no guarantee that
// there is any data to read from the CConnection's InStream, but subsequent
// calls guarantee there is at least one byte which can be read without
// blocking.

package com.turbovnc.rfb;

public abstract class CSecurity {
  public abstract boolean processMsg(CConnection cc);
  public abstract int getType();
  public abstract String getDescription();
  public abstract String getProtocol();

  /*
   * Use variable directly instead of dumb get/set methods.
   * It MUST be set by viewer.
   */
  static UserPasswdGetter upg;
}
