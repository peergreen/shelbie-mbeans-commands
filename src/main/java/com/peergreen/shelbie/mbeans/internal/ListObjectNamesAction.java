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
import java.util.Set;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.HandlerDeclaration;
import org.apache.felix.service.command.CommandSession;

/**
 * User: guillaume
 * Date: 15/03/13
 * Time: 22:03
 */
@Component
@Command(name = "list-objectnames",
         scope = "mbeans",
         description = "List registered MBean's ObjectNames.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class ListObjectNamesAction implements Action {

    private final MBeanServer mbeanServer;

    @Argument(name = "pattern",
              description = "ObjectName pattern")
    private ObjectName objectName;

    public ListObjectNamesAction() {
        mbeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        Set<ObjectName> objectNames = mbeanServer.queryNames(objectName, null);
        for (ObjectName name : objectNames) {
            System.out.printf("  %s%n", name.getCanonicalName());
        }
        return null;
    }
}
