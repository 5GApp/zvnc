/*
 * Copyright (C) 2005-2006 Martin Koegler
 * Copyright (C) 2006 OCCAM Financial Technology
 * Copyright (C) 2010 TigerVNC Team
 * Copyright (C) 2011 Brian P. Hinz
 * Copyright (C) 2012, 2017-2018 D. R. Commander.  All Rights Reserved.
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

import java.util.*;

import com.turbovnc.rdr.*;

public class CSecurityVeNCrypt extends CSecurity {

  public CSecurityVeNCrypt(SecurityClient sec) {
    haveRecvdMajorVersion = false;
    haveRecvdMinorVersion = false;
    haveSentVersion = false;
    haveAgreedVersion = false;
    haveListOfTypes = false;
    haveNumberOfTypes = false;
    haveChosenType = false;
    majorVersion = 0;
    minorVersion = 0;
    chosenType = RFB.SECTYPE_VENCRYPT;
    nAvailableTypes = 0;
    availableTypes = null;
    iAvailableType = 0;
    security = sec;
  }

  public boolean processMsg(CConnection cc) {
    InStream is = cc.getInStream();
    OutStream os = cc.getOutStream();

    /* get major, minor versions, send what we can support (or 0.0 for can't
       support it) */
    if (!haveRecvdMajorVersion) {
      majorVersion = is.readU8();
      haveRecvdMajorVersion = true;

      return false;
    }

    if (!haveRecvdMinorVersion) {
      minorVersion = is.readU8();
      haveRecvdMinorVersion = true;
    }

    /* major version in upper 8 bits and minor version in lower 8 bits */
    int version = (majorVersion << 8) | minorVersion;

    if (!haveSentVersion) {
      /* Currently we don't support former VeNCrypt 0.1 */
      if (version >= 0x0002) {
        majorVersion = 0;
        minorVersion = 2;
        os.writeU8(majorVersion);
        os.writeU8(minorVersion);
        os.flush();
      } else {
        /* Send 0.0 to indicate no support */
        majorVersion = 0;
        minorVersion = 0;
        os.writeU8(majorVersion);
        os.writeU8(minorVersion);
        os.flush();
        throw new ErrorException("Server reported an unsupported VeNCrypt version");
      }

      haveSentVersion = true;
      return false;
    }

    /* Check that the server is OK */
    if (!haveAgreedVersion) {
      if (is.readU8() != 0)
        throw new ErrorException("Server reported it could not support the VeNCrypt version");

      haveAgreedVersion = true;
      return false;
    }

    /* get a number of types */
    if (!haveNumberOfTypes) {
      nAvailableTypes = is.readU8();
      iAvailableType = 0;

      if (nAvailableTypes <= 0)
        throw new ErrorException("Server reported no VeNCrypt sub-types");

      availableTypes = new int[nAvailableTypes];
      haveNumberOfTypes = true;
      return false;
    }

    if (nAvailableTypes > 0) {
      /* read in the types possible */
      if (!haveListOfTypes) {
        if (is.checkNoWait(4)) {
          availableTypes[iAvailableType++] = is.readU32();
          haveListOfTypes = (iAvailableType >= nAvailableTypes);
          vlog.debug("Server offers security type " +
            RFB.secTypeName(availableTypes[iAvailableType - 1]) + " (" +
                            availableTypes[iAvailableType - 1] + ")");

          if (!haveListOfTypes)
            return false;

        } else
          return false;
      }

      /* make a choice and send it to the server, meanwhile set up the stack */
      if (!haveChosenType) {
        chosenType = RFB.SECTYPE_INVALID;
        int i;
        Iterator<Integer> j;
        List<Integer> secTypes = new ArrayList<Integer>();

        secTypes = Security.getEnabledExtSecTypes();

        /* Honor server's security type order */
        for (i = 0; i < nAvailableTypes; i++) {
          for (j = secTypes.iterator(); j.hasNext();) {
            int refType = (Integer)j.next();
            if (refType == availableTypes[i]) {
              chosenType = refType;
              break;
            }
          }

          if (chosenType != RFB.SECTYPE_INVALID)
            break;
        }

        vlog.debug("Choosing security type " +
                   RFB.secTypeName(chosenType) + " (" + chosenType + ")");

        /* Set up the stack according to the chosen type: */
        if (chosenType == RFB.SECTYPE_INVALID ||
            chosenType == RFB.SECTYPE_VENCRYPT)
          throw new AuthFailureException("No valid VeNCrypt sub-type");

        csecurity = security.getCSecurity(chosenType);

        /* send chosen type to server */
        os.writeU32(chosenType);
        os.flush();

        haveChosenType = true;
      }
    } else {
      /*
       * Server told us that there are 0 types it can support - this should not
       * happen, since if the server supports 0 sub-types, it doesn't support
       * this security type
       */
      throw new AuthFailureException("The server reported 0 VeNCrypt sub-types");
    }

    return csecurity.processMsg(cc);
  }

  public final int getType() { return chosenType; }

  public final String getDescription() {
    return RFB.secTypeName(chosenType);
  }

  public final String getProtocol() {
    return (csecurity != null ? csecurity.getProtocol() : "Not initialized");
  }

  static StringParameter secTypesStr;

  private CSecurity csecurity;
  SecurityClient security;
  private boolean haveRecvdMajorVersion;
  private boolean haveRecvdMinorVersion;
  private boolean haveSentVersion;
  private boolean haveAgreedVersion;
  private boolean haveListOfTypes;
  private boolean haveNumberOfTypes;
  private boolean haveChosenType;
  private int majorVersion, minorVersion;
  private int chosenType;
  private int nAvailableTypes;
  private int[] availableTypes;
  private int iAvailableType;
  //private final String desc;

  static LogWriter vlog = new LogWriter("CSecurityVeNCrypt");
}
