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

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.service.command.CommandSession;
import org.fusesource.jansi.Ansi;

/**
 * User: guillaume
 * Date: 15/03/13
 * Time: 22:03
 */
@Component
@Command(name = "get-attributes",
         scope = "mbeans",
         description = "Get MBean's attribute(s) value.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class GetAttributesAction implements Action {

    private final MBeanServer mbeanServer;

    @Argument(name = "object-name",
              index = 0,
              required = true,
              description = "ObjectName to be queried")
    private ObjectName objectName;

    @Argument(name = "attributes",
              index = 1,
              description = "Attribute names (if empty, all readable attributes are returned)")
    private String[] attributes;

    public GetAttributesAction() {
        mbeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        if (attributes == null) {
            List<String> names = new ArrayList<String>();
            MBeanInfo info = mbeanServer.getMBeanInfo(objectName);
            for (MBeanAttributeInfo attributeInfo : info.getAttributes()) {
                if (attributeInfo.isReadable()) {
                    names.add(attributeInfo.getName());
                }
            }
            attributes = names.toArray(new String[names.size()]);
        }
        AttributeList list = mbeanServer.getAttributes(objectName, attributes);

        Ansi buffer = Ansi.ansi();
        for (Attribute attribute : list.asList()) {
            buffer.render("  @|bold %s|@ ", attribute.getName());
            if (attribute.getValue() instanceof CompositeData) {
                CompositeData data = (CompositeData) attribute.getValue();
                buffer.a("Composite{");
                Ansi content = Ansi.ansi();
                for (String item : data.getCompositeType().keySet()) {
                    if (!content.toString().isEmpty()) {
                        content.a(", ");
                    }
                    content.render("@|italic %s|@=%s", item, data.get(item));
                }
                buffer.a(content.toString());
                buffer.a("}");
            } else {
                buffer.render("%s", attribute.getValue().toString());
            }
            buffer.newline();
        }

        System.out.print(buffer.toString());
        return null;
    }
}
