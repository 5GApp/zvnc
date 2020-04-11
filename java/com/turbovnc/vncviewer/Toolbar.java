/* Copyright (C) 2011-2012 Brian P. Hinz
 * Copyright (C) 2012-2013, 2015, 2018 D. R. Commander.  All Rights Reserved.
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

package com.turbovnc.vncviewer;

import java.awt.event.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import javax.swing.*;
import javax.swing.border.*;

import com.turbovnc.rdr.*;
import com.turbovnc.rfb.*;

public class Toolbar extends JToolBar implements ActionListener {

  static final String[] BUTTONS = {
    "Connection options...", "Connection info...", "Full screen",
    "Request screen refresh", "Request lossless refresh",
    "Send Ctrl-Alt-Del", "Send Ctrl-Esc",
    "Send Ctrl key press/release", "Send Alt key press/release",
    "New Connection...", "Disconnect"
  };

  private final ClassLoader cl = getClass().getClassLoader();
  private final ImageIcon toolbarIcons =
    new ImageIcon(cl.getResource("com/turbovnc/vncviewer/toolbar.png"));
  private final Image toolbarImage = toolbarIcons.getImage();

  public Toolbar(CConn cc_) {
    super();
    cc = cc_;
    BufferedImage bi =
      new BufferedImage(176, 16, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = bi.createGraphics();
    g.drawImage(toolbarImage, 0, 0, 176, 16, null);
    setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    setAlignmentY(java.awt.Component.CENTER_ALIGNMENT);
    setFloatable(false);
    setBorder(new EmptyBorder(1, 2, 1, 0));
    for (int i = 0; i < 11; i++) {
      if (i >= 5 && i <= 8 && cc.opts.viewOnly)
        continue;
      if (i >= 9 && i <= 10 && VncViewer.noNewConn.getValue())
        continue;
      if (i >= 5 && i <= 6 && VncViewer.restricted.getValue())
        continue;
      ImageIcon icon = new ImageIcon(
        tk.createImage(bi.getSubimage(i * 16, 0, 16, 16).getSource()));
      AbstractButton button;
      switch (i) {
        case 7:
          // fallthrough
        case 8:
          button = new JToggleButton(icon);
          button.setBorder(BorderFactory.createLoweredBevelBorder());
          break;
        default:
          button = new JButton(icon);
          button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      }
      button.setName(BUTTONS[i]);
      button.setToolTipText(BUTTONS[i]);
      button.setBorderPainted(false);
      button.setFocusPainted(false);
      button.setFocusable(false);
      button.addActionListener(this);
      button.addMouseListener(new ButtonListener(button));
      button.setContentAreaFilled(false);
      add(button);
      add(Box.createHorizontalStrut(2));
      if (i == 1 ||
          (i == 4 && (!cc.opts.viewOnly || !VncViewer.noNewConn.getValue())) ||
          (i == 8 && !VncViewer.noNewConn.getValue())) {
        // ref http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4346610
        add(new JSeparator(JSeparator.VERTICAL) {
          public Dimension getMaximumSize() {
            return new Dimension(getPreferredSize().width,
                                 Integer.MAX_VALUE);
          }
        });
        add(Box.createHorizontalStrut(2));
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    Object s = e.getSource();
    if (((AbstractButton)s).getName() == BUTTONS[0]) {
      cc.options.showDialog(cc.viewport);
    } else if (((AbstractButton)s).getName() == BUTTONS[1]) {
      cc.showInfo();
    } else if (((AbstractButton)s).getName() == BUTTONS[2]) {
      cc.toggleFullScreen();
    } else if (((AbstractButton)s).getName() == BUTTONS[3]) {
      cc.refresh();
    } else if (((AbstractButton)s).getName() == BUTTONS[4]) {
      cc.losslessRefresh();
    } else if (((AbstractButton)s).getName() == BUTTONS[5] &&
               !cc.opts.viewOnly) {
      cc.writeKeyEvent(Keysyms.CONTROL_L, true);
      cc.writeKeyEvent(Keysyms.ALT_L, true);
      cc.writeKeyEvent(Keysyms.DELETE, true);
      cc.writeKeyEvent(Keysyms.DELETE, false);
      cc.writeKeyEvent(Keysyms.ALT_L, false);
      cc.writeKeyEvent(Keysyms.CONTROL_L, false);
    } else if (((AbstractButton)s).getName() == BUTTONS[6] &&
               !cc.opts.viewOnly) {
      cc.writeKeyEvent(Keysyms.CONTROL_L, true);
      cc.writeKeyEvent(Keysyms.ESCAPE, true);
      cc.writeKeyEvent(Keysyms.CONTROL_L, false);
      cc.writeKeyEvent(Keysyms.ESCAPE, false);
    } else if (((AbstractButton)s).getName() == BUTTONS[7] &&
               !cc.opts.viewOnly) {
      if (((AbstractButton)s).isSelected()) {
        cc.writeKeyEvent(Keysyms.CONTROL_L, true);
      } else {
        cc.writeKeyEvent(Keysyms.CONTROL_L, false);
      }
    } else if (((AbstractButton)s).getName() == BUTTONS[8] &&
               !cc.opts.viewOnly) {
      if (((AbstractButton)s).isSelected()) {
        cc.writeKeyEvent(Keysyms.ALT_L, true);
      } else {
        cc.writeKeyEvent(Keysyms.ALT_L, false);
      }
    } else if (((AbstractButton)s).getName() == BUTTONS[9]) {
      VncViewer.newViewer(cc.viewer);
    } else if (((AbstractButton)s).getName() == BUTTONS[10]) {
      cc.close();
    }
  }

  public class ButtonListener implements MouseListener {
    Border raised = new BevelBorder(BevelBorder.RAISED);
    Border lowered = new BevelBorder(BevelBorder.LOWERED);
    Border inactive = new EmptyBorder(2, 2, 2, 2);
    AbstractButton b;
    public ButtonListener(javax.swing.AbstractButton button) {
      b = button;
    }
    public void mousePressed(MouseEvent e) {
      if (!b.isEnabled()) return;
      if (b instanceof javax.swing.JToggleButton) {
        b.setBorder((b.isSelected() ? inactive : lowered));
        b.setBorderPainted((b.isSelected() ? false : true));
      } else {
        b.setBorder(lowered);
        b.setBorderPainted(true);
      }
    }

    public void mouseReleased(MouseEvent e) {
      if (!b.isEnabled()) return;
      if (b instanceof javax.swing.JButton) {
        b.setBorder(inactive);
        b.setBorderPainted(false);
      }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {
      if (!b.isEnabled()) return;
      if (b instanceof javax.swing.JToggleButton && b.isSelected())
        return;
      b.setBorder(raised);
      b.setBorderPainted(true);
    }

    public void mouseExited(MouseEvent e) {
      if (!b.isEnabled()) return;
      if (b instanceof javax.swing.JToggleButton && b.isSelected())
        return;
      b.setBorder(inactive);
      b.setBorderPainted(false);
    }
  }

  private CConn cc;
  static Toolkit tk = Toolkit.getDefaultToolkit();
  static LogWriter vlog = new LogWriter("Toolbar");
}
