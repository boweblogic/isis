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

import static org.apache.isis.core.runtime.runner.Constants.APP_MANIFEST_LONG_OPT;
import static org.apache.isis.core.runtime.runner.Constants.APP_MANIFEST_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.isis.core.commons.configbuilder.IsisConfigurationBuilder;
import org.apache.isis.core.runtime.optionhandler.BootPrinter;
import org.apache.isis.core.runtime.optionhandler.OptionHandlerAbstract;
import org.apache.isis.core.runtime.runner.Constants;
import org.apache.isis.core.runtime.system.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionHandlerAppManifest extends OptionHandlerAbstract {

    private static final Logger LOG = LoggerFactory.getLogger(OptionHandlerAppManifest.class);
    private String appManifestClassName;

    public OptionHandlerAppManifest() {
        super();
    }

    @Override
    public void addOption(final Options options) {
    	
    	Option option = Option.builder(APP_MANIFEST_OPT)
    	.argName("app manifest")
    	.hasArg()
    	.longOpt(APP_MANIFEST_LONG_OPT)
    	.desc("fully qualified AppManifest class")
    	.build();
    	
        options.addOption(option);
    }

    @Override
    public boolean handle(final CommandLine commandLine, final BootPrinter bootPrinter, final Options options) {
        appManifestClassName = commandLine.getOptionValue(Constants.APP_MANIFEST_OPT);
        return true;
    }

    @Override
    public void prime(final IsisConfigurationBuilder isisConfigurationBuilder) {
        if (appManifestClassName == null) {
            return;
        }
        prime(isisConfigurationBuilder, SystemConstants.APP_MANIFEST_KEY, appManifestClassName);
    }

    static void prime(IsisConfigurationBuilder isisConfigurationBuilder, String key, String value) {
        LOG.info("priming: {}={}", key, value);
        isisConfigurationBuilder.add(key, value);
    }


}
