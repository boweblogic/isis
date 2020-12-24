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
package org.apache.isis.persistence.jdo.integration.persistence.queries;

import java.util.List;

import javax.jdo.listener.InstanceLifecycleEvent;

import org.datanucleus.enhancement.Persistable;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.commons.collections.Can;
import org.apache.isis.commons.internal.assertions._Assert;
import org.apache.isis.commons.internal.collections._Lists;
import org.apache.isis.core.metamodel.spec.ManagedObject;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;
import org.apache.isis.persistence.jdo.integration.lifecycles.IsisLifecycleListener;
import org.apache.isis.persistence.jdo.integration.persistence.query.PersistenceQuery;

import lombok.val;

abstract class _PersistenceQueryProcessorAbstract<T extends PersistenceQuery>
implements PersistenceQueryProcessor<T> {


    /**
     * Traversing the provided list causes (or should cause) the
     * {@link IsisLifecycleListener#postLoad(InstanceLifecycleEvent) {
     * to be called.
     */
    protected Can<ManagedObject> loadAdapters(final PersistenceQueryContext queryContext, final List<?> pojos) {
        val adapters = _Lists.<ManagedObject>newArrayList();
        for (val pojo : pojos) {
            // ought not to be necessary, however for some queries it seems that the
            // lifecycle listener is not called
            ManagedObject adapter;
            if(pojo instanceof Persistable) {
                // an entity
                adapter = queryContext.initializeEntityAfterFetched((Persistable) pojo);
                _Assert.assertNotNull(adapter);
            } else {
                // a value type
                adapter = queryContext.initializeValueAfterFetched(pojo);
                _Assert.assertNotNull(adapter);
            }
            adapters.add(adapter);
        }
        return Can.ofCollection(adapters);
    }

    // -- HELPER

    protected static IsisJdoSupport_v3_2 isisJdoSupport(ServiceRegistry serviceRegistry) { 
        return serviceRegistry.lookupServiceElseFail(IsisJdoSupport_v3_2.class);
    }


}