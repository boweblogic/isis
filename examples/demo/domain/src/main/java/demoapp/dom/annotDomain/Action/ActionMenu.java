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
package demoapp.dom.annotDomain.Action;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import lombok.val;
import lombok.extern.log4j.Log4j2;

import demoapp.dom.annotDomain.Action.associateWith.ActionAssociateWithVm;
import demoapp.dom.annotDomain.Action.associateWith.child.ActionAssociateWithChildVm;
import demoapp.dom.annotDomain.Action.domainEvent.ActionDomainEventVm;
import demoapp.dom.types.Samples;

@DomainService(nature=NatureOfService.VIEW, objectType = "demo.ActionMenu")
@Log4j2
public class ActionMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-asterisk", describedAs = "Semantic relationship between actions and other properties or collections")
    public ActionAssociateWithVm associateWith(){
        val associateWithVm = new ActionAssociateWithVm("value");
        val children = associateWithVm.getChildren();
        samples.stream()
                .map(ActionAssociateWithChildVm::new)
                .forEach(children::add);
        return associateWithVm;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-asterisk", describedAs = "Decouples interaction of actions")
    public ActionDomainEventVm domainEvent(){
        return new ActionDomainEventVm("change me");
    }


    public List<ActionAssociateWithChildVm> getChildren() {
        return samples.stream()
                .map(ActionAssociateWithChildVm::new)
                .collect(Collectors.toList());
    }

    @Inject
    Samples<String> samples;

}
