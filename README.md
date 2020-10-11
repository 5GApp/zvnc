# zvnc 
TurboVNC enhancement on user-input and display

![C/C++ CI](https://github.com/5GApp/zvnc/workflows/C/C++%20CI/badge.svg)

### Features
* Support UTF-8 clipboard in combined with [peer-vnc](https://github.com/InstantWebP2P/peer-vnc) web VNC client.

### Install

* Build with Docker

   `
   ./apppkg
   `

### License

(The GPLv2 License)

Copyright (c) 2015-present Tom Zhou(appnet.link@gmail.com)


About TurboVNC
==============

Virtual Network Computing (VNC) is a remote display system that allows you to
view and interact with a desktop environment that is running on a remote
computer.  Using VNC, you can run graphical applications on a remote machine
and send only the pixels generated by those applications to your local machine.
VNC is platform-independent and supports a wide variety of operating systems
and architectures as both servers and clients.

TurboVNC is a high-speed version of VNC derived from TightVNC.  It contains
a variant of Tight encoding that is tuned to maximize performance for image-
intensive applications (such as VirtualGL, video applications, and image
editors) while still providing excellent performance for other types of
applications.  TurboVNC, in combination with VirtualGL, provides a complete
solution for remotely displaying 3D applications with interactive performance.

Refer to the TurboVNC User's Guide and man pages for a more thorough
description of the components that make up TurboVNC.


Legal
=====

TurboVNC is

    Copyright (C) 2009-2020 D. R. Commander
    Copyright (C) 2011-2016 Brian P. Hinz
    Copyright (C) 2010 University Corporation for Atmospheric Research
    Copyright (C) 2004-2008 Sun Microsystems, Inc.
    Copyright (C) 2004 Landmark Graphics Corporation

TurboVNC contains code from LibVNCServer, RealVNC, TigerVNC, and TightVNC,
which are

    Copyright (C) 2011 Gernot Tenchio
    Copyright (C) 2010 TigerVNC Team
    Copyright (C) 2010 m-privacy GmbH
    Copyright (C) 2009-2016 Pierre Ossman for Cendio AB
    Copyright (C) 2006 OCCAM Financial Technology
    Copyright (C) 2005-2011 Martin Koegler
    Copyright (C) 2004-2009 Red Hat, Inc.
    Copyright (C) 2004-2005 Cendio AB
    Copyright (C) 2003 Sun Microsystems, Inc.
    Copyright (C) 2003 Dennis Syrovatsky
    Copyright (C) 2002-2005 RealVNC Ltd.
    Copyright (C) 2000-2010 TightVNC Team
    Copyright (C) 2000-2006 Constantin Kaplinsky
    Copyright (C) 2000 Tridia Corp.
    Copyright (C) 1999 AT&T Laboratories Cambridge.
    All rights reserved.

This software is distributed under the GNU General Public Licence as published
by the Free Software Foundation.  See the file LICENSE.txt for the conditions
under which this software is made available.  TurboVNC also contains code from
other sources.  See the Acknowledgements section below, and the individual
source files, for details of the conditions under which they are made
available.


ACKNOWLEDGEMENTS
================

This distribution contains zlib compression software.  This is:

  Copyright (C) 1995-2005 Jean-loup Gailly and Mark Adler

  This software is provided 'as-is', without any express or implied
  warranty.  In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
     claim that you wrote the original software. If you use this software
     in a product, an acknowledgment in the product documentation would be
     appreciated but is not required.
  2. Altered source versions must be plainly marked as such, and must not be
     misrepresented as being the original software.
  3. This notice may not be removed or altered from any source distribution.

  Jean-loup Gailly        Mark Adler
  jloup@gzip.org          madler@alumni.caltech.edu

  The data format used by the zlib library is described by RFCs (Request for
  Comments) 1950 to 1952 in the files ftp://ds.internic.net/rfc/rfc1950.txt
  (zlib format), rfc1951.txt (deflate format) and rfc1952.txt (gzip format).


This distribution contains public domain DES software by Richard Outerbridge.
This is:

    Copyright (c) 1988,1989,1990,1991,1992 by Richard Outerbridge.
    (GEnie : OUTER; CIS : [71755,204]) Graven Imagery, 1992.


This distribution contains software from the X Window System.  This is:

 Copyright 1987, 1988, 1998  The Open Group

 Permission to use, copy, modify, distribute, and sell this software and its
 documentation for any purpose is hereby granted without fee, provided that
 the above copyright notice appear in all copies and that both that
 copyright notice and this permission notice appear in supporting
 documentation.

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 OPEN GROUP BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 Except as contained in this notice, the name of The Open Group shall not be
 used in advertising or otherwise to promote the sale, use or other dealings
 in this Software without prior written authorization from The Open Group.


 Copyright 1987, 1988 by Digital Equipment Corporation, Maynard, Massachusetts.

                         All Rights Reserved

 Permission to use, copy, modify, and distribute this software and its
 documentation for any purpose and without fee is hereby granted,
 provided that the above copyright notice appear in all copies and that
 both that copyright notice and this permission notice appear in
 supporting documentation, and that the name of Digital not be
 used in advertising or publicity pertaining to distribution of the
 software without specific, written prior permission.

 DIGITAL DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE, INCLUDING
 ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS, IN NO EVENT SHALL
 DIGITAL BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR
 ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
 ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS
 SOFTWARE.
