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

package com.peergreen.shelbie.mbeans.internal;

import static com.peergreen.shelbie.mbeans.internal.util.Types.getPrimitiveType;

import java.lang.management.ManagementFactory;
import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.service.command.CommandSession;

/**
 * User: guillaume
 * Date: 15/03/13
 * Time: 22:03
 */
@Component
@Command(name = "set-attribute",
         scope = "mbeans",
         description = "Assign a value to a MBean's attribute.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class SetAttributeAction implements Action {

    private final MBeanServer mbeanServer;

    @Argument(name = "object-name",
              index = 0,
              required = true,
              description = "Name of the MBean")
    private ObjectName objectName;

    @Argument(name = "attribute-name",
            index = 1,
            required = true,
            description = "MBean's attribute name")
    private String attribute;

    @Argument(name = "attribute-value",
              index = 2,
              required = true,
              description = "MBean's new attribute value")
    private Object value;

    public SetAttributeAction() {
        mbeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public Object execute(CommandSession session) throws Exception {

        MBeanInfo mBeanInfo = mbeanServer.getMBeanInfo(objectName);
        MBeanAttributeInfo attributeInfo = findAttribute(mBeanInfo);

        if (!attributeInfo.isWritable()) {
            throw new Exception(String.format("Attribute '%s' is not writable", attribute));
        }

        Class<?> type = getType(attributeInfo);
        Object typed;
        if (type.isInstance(value)) {
            typed = value;
        } else {
            // need to convert
            Object converted = session.convert(type, value);
            if (converted == null) {
                throw new Exception(String.format("Cannot convert value '%s' into a '%s'", value, type.getName()));
            }
            typed = converted;
        }

        mbeanServer.setAttribute(objectName, new Attribute(attribute, typed));

        return null;
    }

    private Class<?> getType(MBeanAttributeInfo info) throws Exception {
        Class<?> type = getPrimitiveType(info.getType());
        if (type == null) {
            // Not a primitive type
            ClassLoader loader = mbeanServer.getClassLoaderFor(objectName);
            if (loader == null) {
                loader = ClassLoader.getSystemClassLoader();
            }

            return loader.loadClass(info.getType());
        }

        return type;
    }

    private MBeanAttributeInfo findAttribute(MBeanInfo mBeanInfo) throws Exception {
        for (MBeanAttributeInfo attributeInfo : mBeanInfo.getAttributes()) {
            if (attribute.equals(attributeInfo.getName())) {
                return attributeInfo;
            }
        }
        throw new Exception(String.format("Attribute '%s' was not found in MBean '%s'", attribute, objectName));
    }
}
