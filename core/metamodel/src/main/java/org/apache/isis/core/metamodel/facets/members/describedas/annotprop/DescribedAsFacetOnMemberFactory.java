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

package org.apache.isis.core.metamodel.facets.members.describedas.annotprop;

import java.util.Properties;

import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.ContributeeMemberFacetFactory;
import org.apache.isis.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.isis.core.metamodel.facets.all.describedas.DescribedAsFacet;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;

public class DescribedAsFacetOnMemberFactory extends FacetFactoryAbstract implements ContributeeMemberFacetFactory {


    public DescribedAsFacetOnMemberFactory() {
        super(FeatureType.MEMBERS);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {

        DescribedAsFacet facet = createFromMetadataPropertiesIfPossible(processMethodContext);
        
        if (facet == null) {
            facet = createFromAnnotationOnReturnTypeIfPossible(processMethodContext);
        }

        // no-op if null
        FacetUtil.addFacet(facet);
    }

    @Override
    public void process(ProcessContributeeMemberContext processMemberContext) {
        DescribedAsFacet facet = createFromMetadataPropertiesIfPossible(processMemberContext);
        // no-op if null
        FacetUtil.addFacet(facet);
    }
    
    private static DescribedAsFacet createFromMetadataPropertiesIfPossible(
            final ProcessContextWithMetadataProperties<? extends FacetHolder> pcwmp) {
        
        final FacetHolder holder = pcwmp.getFacetHolder();
        
        final Properties properties = pcwmp.metadataProperties("describedAs");
        return properties != null ? new DescribedAsFacetOnMemberFromProperties(properties, holder) : null;
    }
    

    private DescribedAsFacet createFromAnnotationOnReturnTypeIfPossible(final ProcessMethodContext processMethodContext) {
        final Class<?> returnType = processMethodContext.getMethod().getReturnType();
        final DescribedAsFacet returnTypeDescribedAsFacet = getDescribedAsFacet(returnType);
        return returnTypeDescribedAsFacet != null ? new DescribedAsFacetOnMemberDerivedFromType(returnTypeDescribedAsFacet, processMethodContext.getFacetHolder()) : null;
    }
    
    private DescribedAsFacet getDescribedAsFacet(final Class<?> type) {
        final ObjectSpecification paramTypeSpec = getSpecificationLoader().loadSpecification(type);
        return paramTypeSpec.getFacet(DescribedAsFacet.class);
    }



}
