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
package org.apache.isis.testdomain.model.interaction;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.extensions.modelannotation.applib.annotation.Model;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

@Action
@RequiredArgsConstructor
public class InteractionNpmDemo_biArgDisabled {

    @SuppressWarnings("unused")
    private final InteractionNpmDemo holder;
    
    @Value @Accessors(fluent = true)            
    public static class Parameters {      
        int a;
        int b;
    }
    
    @Model
    public int act(int a, int b) {
        return a + b;
    }
    
    //TODO[ISIS-2362] supporting methods (no arg): rename -> disable()
    @Model
    public String disableAct() {
        return "Disabled for demonstration.";
    }
    
    //TODO[ISIS-2362] supporting methods (no arg): rename -> validate()
    //TODO[ISIS-2362] supporting methods (all args): rename -> validate(params)
    //@Model //FIXME not recognized by method finders (metamodel)
    public String validateAct(Parameters params) {
        return "Never valid for demonstration.";
    }
}
