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

package com.peergreen.shelbie.mbeans.internal.converter;

import javax.management.ObjectName;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.StaticServiceProperty;
import org.apache.felix.service.command.Converter;

/**
 * User: guillaume
 * Date: 15/03/13
 * Time: 22:06
 */
@Component
@Instantiate
@Provides(
        properties = @StaticServiceProperty(
                name = Converter.CONVERTER_CLASSES,
                value = "javax.management.ObjectName",
                type = "java.lang.String")
)
public class ObjectNameConverter implements Converter {
    @Override
    public Object convert(Class<?> desiredType, Object in) throws Exception {
        if (ObjectName.class.equals(desiredType)) {
            if (in instanceof String) {
                String name = (String) in;
                return ObjectName.getInstance(name);
            }
        }
        return null;
    }

    @Override
    public CharSequence format(Object target, int level, Converter escape) throws Exception {
        if (target instanceof ObjectName) {
            ObjectName objectName = (ObjectName) target;
            switch (level) {
                // TODO Provides a more detailed inspect format
                case Converter.PART:
                case Converter.LINE:
                case Converter.INSPECT:
                    return objectName.toString();
            }
        }
        return null;
    }
}
