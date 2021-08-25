/***********************************************************
 ** Copyright (C), 2008-2017, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File:
 ** Description:
 ** Version:
 ** Date :
 ** Author:
 **
 ** ---------------------Revision History: ---------------------
 **  <author>    <data>    <version >    <desc>
****************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
    /**
     * Closes a <code>Closeable</code> unconditionally.
     * <p>
     * Equivalent to {@link Closeable#close()}, except any exceptions will be ignored. This is typically used in
     * finally blocks.
     * <p>
     * Example code:
     * </p>
     * <pre>
     * Closeable closeable = null;
     * try {
     *     closeable = new FileReader(&quot;foo.txt&quot;);
     *     // process closeable
     *     closeable.close();
     * } catch (Exception e) {
     *     // error handling
     * } finally {
     *     IOUtils.closeQuietly(closeable);
     * }
     * </pre>
     * <p>
     * Closing all streams:
     * </p>
     * <pre>
     * try {
     *     return IOUtils.copy(inputStream, outputStream);
     * } finally {
     *     IOUtils.closeQuietly(inputStream);
     *     IOUtils.closeQuietly(outputStream);
     * }
     * </pre>
     *
     * @param closeable the objects to close, may be null or already closed
     * @since 2.0
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }

    public static void closeQuietly(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (int i = 0; i < closeables.length; i++) {
            closeQuietly(closeables[i]);
        }
    }
}
