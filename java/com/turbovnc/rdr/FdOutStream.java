/* Copyright (C) 2002-2005 RealVNC Ltd.  All Rights Reserved.
 * Copyright 2011 Pierre Ossman for Cendio AB
 * Copyright (C) 2012, 2014 Brian P. Hinz
 * Copyright (C) 2012, 2018 D. R. Commander.  All Rights Reserved.
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

package com.turbovnc.rdr;

import com.turbovnc.network.*;
import java.nio.channels.SelectionKey;

public class FdOutStream extends OutStream {

  static final int DEFAULT_BUF_SIZE = 16384;
  static final int MIN_BULK_SIZE = 1024;

  public FdOutStream(FileDescriptor fd_, boolean blocking_, int timeoutms_,
                     int bufSize_) {
    fd = fd_;  blocking = blocking_;  timeoutms = timeoutms_;
    bufSize = ((bufSize_ > 0) ? bufSize_ : DEFAULT_BUF_SIZE);
    b = new byte[bufSize];
    offset = 0;
    ptr = sentUpTo = start = 0;
    end = start + bufSize;
  }

  public FdOutStream(FileDescriptor fd_) { this(fd_, true, -1, 0); }

  public void setTimeout(int timeoutms_) {
    timeoutms = timeoutms_;
  }

  public void setBlocking(boolean blocking_) {
    blocking = blocking_;
  }

  public int length() {
    return offset + ptr - sentUpTo;
  }

  public void flush() {
    int timeoutms_;

    if (blocking)
      timeoutms_ = timeoutms;
    else
      timeoutms_ = 0;

    while (sentUpTo < ptr) {
      int n = writeWithTimeout(b, sentUpTo, ptr - sentUpTo, timeoutms_);

      // Timeout?
      if (n == 0) {
        // If non-blocking then we're done here
        if (!blocking)
          break;

        // Otherwise try blocking (with possible timeout)
        if ((timeoutms_ == 0) && (timeoutms != 0)) {
          timeoutms_ = timeoutms;
          break;
        }

        // Proper timeout
        throw new TimedOut();
      }

      sentUpTo += n;
      offset += n;
    }

    // Managed to flush everything?
    if (sentUpTo == ptr)
      ptr = sentUpTo = start;
  }

  private int writeWithTimeout(byte[] data, int dataPtr, int length,
                               int timeoutms_) {
    int n;

    do {

      Integer tv;
      if (timeoutms_ != -1) {
        tv = Integer.valueOf(timeoutms_);
      } else {
        tv = null;
      }

      n = fd.select(SelectionKey.OP_WRITE, tv);

    } while (n < 0);

    if (n == 0) return 0;

    n = fd.write(data, dataPtr, length);

    return n;
  }

  protected int overrun(int itemSize, int nItems) {
    if (itemSize > bufSize)
      throw new ErrorException("FdOutStream overrun: max itemSize exceeded");

    // First try to get rid of the data we have
    flush();

    // Still not enough space?
    if (itemSize > end - ptr) {
      // Can we shuffle things around?
      // (don't do this if it gains us less than 25%)
      if ((sentUpTo - start > bufSize / 4) &&
          (itemSize < bufSize - (ptr - sentUpTo))) {
        System.arraycopy(b, ptr, b, start, ptr - sentUpTo);
        ptr = start + (ptr - sentUpTo);
        sentUpTo = start;
      } else {
        // Have to get rid of more data, so turn off non-blocking
        // for a bit...
        boolean realBlocking;

        realBlocking = blocking;
        blocking = true;
        flush();
        blocking = realBlocking;
      }
    }

    // Can we fit all the items asked for?
    if (itemSize * nItems > end - ptr)
      nItems = (end - ptr) / itemSize;

    return nItems;
  }

  public FileDescriptor getFd() {
    return fd;
  }

  public void setFd(FileDescriptor fd_) {
    fd = fd_;
  }

  public int getBufSize() {
    return bufSize;
  }

  protected FileDescriptor fd;
  protected boolean blocking;
  protected int timeoutms;
  protected int start;
  protected int sentUpTo;
  protected int offset;
  protected int bufSize;
}
