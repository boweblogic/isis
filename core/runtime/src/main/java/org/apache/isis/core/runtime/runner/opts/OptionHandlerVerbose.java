/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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

package org.apache.isis.core.runtime.runner.opts;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import org.apache.isis.core.commons.config.IsisConfigurationBuilder;
import org.apache.isis.core.runtime.optionhandler.BootPrinter;
import org.apache.isis.core.runtime.optionhandler.OptionHandlerAbstract;

import static org.apache.isis.core.runtime.runner.Constants.VERBOSE_OPT;

/**
 * @deprecated - this seems to be a no-op; it is no longer registered in IsisRunner/IsisWebServer
 */
@Deprecated
public class OptionHandlerVerbose extends OptionHandlerAbstract {

    public OptionHandlerVerbose() {
        super();
    }

    @Override
    public void addOption(final Options options) {
        options.addOption(VERBOSE_OPT, false, "print information, warning and error messages");
    }

    @Override
    public boolean handle(final CommandLine commandLine, final BootPrinter bootPrinter, final Options options) {
        return true;
    }

    @Override
    public void primeConfigurationBuilder(final IsisConfigurationBuilder isisConfigurationBuilder) {
        // TODO need to do what, exactly???

    }

}
