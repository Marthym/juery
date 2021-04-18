/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ght1pc9kc.juery.core.common.lang3;

public class BooleanUtils {
    // String to Boolean methods
    //-----------------------------------------------------------------------

    /**
     * <p>Converts a String to a Boolean.</p>
     *
     * <p>{@code 'true'}, {@code 'on'}, {@code 'y'}, {@code 't'}, {@code 'yes'}
     * or {@code '1'} (case insensitive) will return {@code true}.
     * {@code 'false'}, {@code 'off'}, {@code 'n'}, {@code 'f'}, {@code 'no'}
     * or {@code '0'} (case insensitive) will return {@code false}.
     * Otherwise, {@code null} is returned.</p>
     *
     * <p>NOTE: This method may return {@code null} and may throw a {@code NullPointerException}
     * if unboxed to a {@code boolean}.</p>
     *
     * <pre>
     *   // N.B. case is not significant
     *   BooleanUtils.toBooleanObject(null)    = null
     *   BooleanUtils.toBooleanObject("true")  = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("T")     = Boolean.TRUE // i.e. T[RUE]
     *   BooleanUtils.toBooleanObject("false") = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("f")     = Boolean.FALSE // i.e. f[alse]
     *   BooleanUtils.toBooleanObject("No")    = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("n")     = Boolean.FALSE // i.e. n[o]
     *   BooleanUtils.toBooleanObject("on")    = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("ON")    = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("off")   = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("oFf")   = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("yes")   = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("Y")     = Boolean.TRUE // i.e. Y[ES]
     *   BooleanUtils.toBooleanObject("1")     = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("0")     = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("blue")  = null
     *   BooleanUtils.toBooleanObject("true ") = null // trailing space (too long)
     *   BooleanUtils.toBooleanObject("ono")   = null // does not match on or no
     * </pre>
     *
     * @param str the String to check; upper and lower case are treated as the same
     * @return the Boolean value of the string, {@code null} if no match or {@code null} input
     */
    public static Boolean toBooleanObject(final String str) {
        // Previously used equalsIgnoreCase, which was fast for interned 'true'.
        // Non interned 'true' matched 15 times slower.
        //
        // Optimisation provides same performance as before for interned 'true'.
        // Similar performance for null, 'false', and other strings not length 2/3/4.
        // 'true'/'TRUE' match 4 times slower, 'tRUE'/'True' 7 times slower.
        if (str == "true") {
            return Boolean.TRUE;
        }
        if (str == null) {
            return null;
        }
        switch (str.length()) {
            case 1: {
                final char ch0 = str.charAt(0);
                if (ch0 == 'y' || ch0 == 'Y' ||
                        ch0 == 't' || ch0 == 'T' ||
                        ch0 == '1') {
                    return Boolean.TRUE;
                }
                if (ch0 == 'n' || ch0 == 'N' ||
                        ch0 == 'f' || ch0 == 'F' ||
                        ch0 == '0') {
                    return Boolean.FALSE;
                }
                break;
            }
            case 2: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                if ((ch0 == 'o' || ch0 == 'O') &&
                        (ch1 == 'n' || ch1 == 'N')) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'n' || ch0 == 'N') &&
                        (ch1 == 'o' || ch1 == 'O')) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 3: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                if ((ch0 == 'y' || ch0 == 'Y') &&
                        (ch1 == 'e' || ch1 == 'E') &&
                        (ch2 == 's' || ch2 == 'S')) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'o' || ch0 == 'O') &&
                        (ch1 == 'f' || ch1 == 'F') &&
                        (ch2 == 'f' || ch2 == 'F')) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 4: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                if ((ch0 == 't' || ch0 == 'T') &&
                        (ch1 == 'r' || ch1 == 'R') &&
                        (ch2 == 'u' || ch2 == 'U') &&
                        (ch3 == 'e' || ch3 == 'E')) {
                    return Boolean.TRUE;
                }
                break;
            }
            case 5: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                final char ch4 = str.charAt(4);
                if ((ch0 == 'f' || ch0 == 'F') &&
                        (ch1 == 'a' || ch1 == 'A') &&
                        (ch2 == 'l' || ch2 == 'L') &&
                        (ch3 == 's' || ch3 == 'S') &&
                        (ch4 == 'e' || ch4 == 'E')) {
                    return Boolean.FALSE;
                }
                break;
            }
            default:
                break;
        }

        return null;
    }
}
