/**
 * The FreeBSD Copyright
 * Copyright 1994-2008 The FreeBSD Project. All rights reserved.
 * Copyright (C) 2013-2016 Philip Helger philip[at]helger[dot]com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE FREEBSD PROJECT ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE FREEBSD PROJECT OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation
 * are those of the authors and should not be interpreted as representing
 * official policies, either expressed or implied, of the FreeBSD Project.
 */
package com.helger.as2lib.util.http;

import java.io.File;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.as2lib.message.IBaseMessage;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.string.ToStringGenerator;

/**
 * Directory based outgoing HTTP dumper.
 *
 * @author Philip Helger
 */
public class HTTPOutgoingDumperDirectoryBased implements IHTTPOutgoingDumper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (HTTPOutgoingDumperDirectoryBased.class);

  private final File m_aDumpDirectory;

  public HTTPOutgoingDumperDirectoryBased (@Nonnull final File aDumpDirectory)
  {
    ValueEnforcer.notNull (aDumpDirectory, "DumpDirectory");
    ValueEnforcer.isTrue (FileHelper.existsDir (aDumpDirectory),
                          () -> "DumpDirectory " + aDumpDirectory + " does not exist!");
    m_aDumpDirectory = aDumpDirectory;
  }

  @Nonnull
  public File getDumpDirectory ()
  {
    return m_aDumpDirectory;
  }

  /**
   * The filename to be used to store the request in the folder provided in the
   * constructor.
   *
   * @param aMsg
   *        The message to be send.
   * @return The local filename without any path. May not be <code>null</code>.
   */
  @Nonnull
  protected String getStoreFilename (@Nonnull final IBaseMessage aMsg)
  {
    return "as2-outgoing-" + Long.toString (System.currentTimeMillis ()) + ".http";
  }

  @Nullable
  public OutputStream dumpOutgoingRequest (@Nonnull final IBaseMessage aMsg)
  {
    final File aDestinationFile = new File (m_aDumpDirectory, getStoreFilename (aMsg));
    s_aLogger.info ("Dumping outgoing HTTP request to file " + aDestinationFile.getAbsolutePath ());
    return StreamHelper.getBuffered (FileHelper.getOutputStream (aDestinationFile));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("DumpDirectory", m_aDumpDirectory).toString ();
  }
}