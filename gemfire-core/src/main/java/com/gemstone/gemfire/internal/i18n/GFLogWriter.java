/*
 * Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.gemstone.gemfire.internal.i18n;

import com.gemstone.gemfire.i18n.StringId;

public interface GFLogWriter {

  boolean fineEnabled();

  boolean infoEnabled();

  boolean warningEnabled();

  boolean severeEnabled();


  void fine(String string);

  void fine(String string, Throwable thr);


  void info(StringId str);

  void info(StringId str, Object arg);

  void info(StringId str, Object[] args);

  void info(StringId str, Object arg, Throwable thr);

  void info(StringId str, Object[] args, Throwable thr);


  void warning(StringId str);

  void warning(StringId str, Object arg);

  void warning(StringId str, Object[] args);

  void warning(StringId str, Object arg, Throwable thr);

  void warning(StringId str, Object[] args, Throwable thr);


  void severe(StringId str);

  void severe(StringId str, Object arg);

  void severe(StringId str, Object[] args);

  void severe(StringId str, Object arg, Throwable thr);

  void severe(StringId str, Object[] args, Throwable thr);
}
