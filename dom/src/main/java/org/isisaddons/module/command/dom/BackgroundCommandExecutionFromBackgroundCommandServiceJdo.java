/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.command.dom;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.isis.applib.services.command.Command;
import org.apache.isis.core.runtime.services.background.BackgroundCommandExecution;

/**
 * If used, ensure that <code>org.apache.isis.module:isis-module-background</code> is also included on classpath.
 */
public final class BackgroundCommandExecutionFromBackgroundCommandServiceJdo extends BackgroundCommandExecution {

    @SuppressWarnings("unused")
    private final static Logger LOG = LoggerFactory.getLogger(BackgroundCommandExecutionFromBackgroundCommandServiceJdo.class);

    @Override
    protected List<? extends Command> findBackgroundCommandsToExecute() {
        final List<CommandJdo> commands = backgroundCommandRepository.findBackgroundCommandsNotYetStarted();
        LOG.debug("Found " + commands.size() + " to execute");
        return commands; 
    }
    

    @javax.inject.Inject
    private BackgroundCommandServiceJdoRepository backgroundCommandRepository;
}