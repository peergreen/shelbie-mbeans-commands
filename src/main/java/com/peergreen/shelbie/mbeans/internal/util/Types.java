/*
 * Copyright 2013 Peergreen S.A.S.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.shelbie.mbeans.internal.util;

/**
 * User: guillaume
 * Date: 18/03/13
 * Time: 13:31
 */
public class Types {
    private Types() {}

    public static Class<?> getPrimitiveType(String type) {
        if ("boolean".equals(type)) {
            return Boolean.TYPE;
        }
        if ("int".equals(type)) {
            return Integer.TYPE;
        }
        if ("short".equals(type)) {
            return Short.TYPE;
        }
        if ("long".equals(type)) {
            return Long.TYPE;
        }
        if ("double".equals(type)) {
            return Double.TYPE;
        }
        if ("float".equals(type)) {
            return Float.TYPE;
        }
        if ("byte".equals(type)) {
            return Byte.TYPE;
        }
        if ("char".equals(type)) {
            return Character.TYPE;
        }
        return null;
    }


}
