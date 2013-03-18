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
import java.util.Set;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

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
@Command(name = "mbean-info",
         scope = "mbeans",
         description = "Display structural information about a given MBean.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class MBeanInfoAction implements Action {

    private final MBeanServer mbeanServer;

    @Argument(name = "object-name",
              required = true,
              description = "ObjectName to be queried")
    private ObjectName objectName;

    public MBeanInfoAction() {
        mbeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        MBeanInfo beanInfo = mbeanServer.getMBeanInfo(objectName);
        Ansi buffer = Ansi.ansi();

        // MBeanInfo <object-name> (<class-name>)
        //   "<description>"
        buffer.render("----------------------------------------------------");
        buffer.newline();
        buffer.render("@|bold %s|@ (@|faint %s|@)%n", objectName, beanInfo.getClassName());
        buffer.render("  %s%n", beanInfo.getDescription());
        buffer.render("----------------------------------------------------");
        buffer.newline();

        if (beanInfo.getAttributes().length != 0) {
            buffer.render("Attributes:");
            buffer.newline();
            for (MBeanAttributeInfo attributeInfo : beanInfo.getAttributes()) {
                String rw = attributeInfo.isReadable() ? "r" : "-";
                rw += attributeInfo.isWritable() ? "w" : "-";
                buffer.render("  * %-30s [%S] %-40s %s%n", attributeInfo.getName(), rw, attributeInfo.getType(), attributeInfo.getDescription());
            }
        }

        if (beanInfo.getOperations().length != 0) {
            buffer.render("Operations:");
            buffer.newline();
            for (MBeanOperationInfo operationInfo : beanInfo.getOperations()) {
                Ansi parameters = Ansi.ansi();
                for (MBeanParameterInfo parameterInfo : operationInfo.getSignature()) {
                    if (!parameters.toString().isEmpty()) {
                        parameters.a(", ");
                    }
                    parameters.render("%s %s", parameterInfo.getType(), parameterInfo.getName());
                }
                buffer.render("  * %s(%s):%s %s %n", operationInfo.getName(), parameters, operationInfo.getReturnType(), operationInfo.getDescription());
            }
        }

        if (beanInfo.getNotifications().length != 0) {
            buffer.render("Notifications:");
            buffer.newline();
            for (MBeanNotificationInfo notificationInfo : beanInfo.getNotifications()) {
                buffer.render("  * %s %s%n", notificationInfo.getName(), notificationInfo.getDescription());
                for (String type : notificationInfo.getNotifTypes()) {
                    buffer.render("    + %s%n", type);
                }
            }
        }
        System.out.print(buffer.toString());
        return null;
    }
}
