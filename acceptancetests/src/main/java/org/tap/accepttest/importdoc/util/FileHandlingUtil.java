/*
 * MIT License
 *
 * Copyright (c) 2016 Georgios Andreadakis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.tap.accepttest.importdoc.util;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for file handling based on the class {@link Path} from NIO 2.
 *
 * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
 */
public class FileHandlingUtil {

    public static Path readFromClasspath(String name) throws URISyntaxException {
        URL systemResource = ClassLoader.getSystemResource(name);
        if (systemResource == null) {

            String msg = "Could not find resource '" + name + "' in classpath";
            System.err.println(msg);
            ClasspathUtil.showClasspath();

            throw new IllegalStateException(msg);
        }
        //System.out.println("Accessing system resource: " + systemResource);
        //System.out.println("Accessing system resource by URI: " + systemResource.toURI());
        return Paths.get(systemResource.toURI());
    }

}
