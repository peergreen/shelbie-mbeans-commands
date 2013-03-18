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

import static java.lang.String.format;

import java.lang.management.ManagementFactory;
import java.util.Set;
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
@Command(name = "list-domains",
         scope = "mbeans",
         description = "List domains.")
@HandlerDeclaration("<sh:command xmlns:sh='org.ow2.shelbie'/>")
public class ListDomainsAction implements Action {

    private final MBeanServer mbeanServer;

    public ListDomainsAction() {
        mbeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        String defaultDomain = mbeanServer.getDefaultDomain();
        Ansi buffer = Ansi.ansi();
        for (String domain : mbeanServer.getDomains()) {
            if (defaultDomain.equals(domain)) {
                buffer.render("  @|bold %s|@ (default)", domain);
            } else {
                buffer.render("  %s", domain);
            }
            // Print number of MBeans in the domain
            Set<ObjectName> names = mbeanServer.queryNames(ObjectName.getInstance(domain + ":*"), null);
            buffer.render(" (%d MBeans)", names.size());
            buffer.newline();
        }
        System.out.print(buffer.toString());
        return null;
    }
}
