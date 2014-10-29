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

package org.apache.isis.core.metamodel.app;

import java.util.*;
import com.google.common.collect.Lists;
import org.apache.isis.core.commons.components.ApplicationScopedComponent;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.config.IsisConfigurationDefault;
import org.apache.isis.core.metamodel.facetdecorator.FacetDecorator;
import org.apache.isis.core.metamodel.metamodelvalidator.dflt.MetaModelValidatorDefault;
import org.apache.isis.core.metamodel.progmodel.ProgrammingModel;
import org.apache.isis.core.metamodel.runtimecontext.RuntimeContext;
import org.apache.isis.core.metamodel.runtimecontext.ServicesInjector;
import org.apache.isis.core.metamodel.services.container.DomainObjectContainerDefault;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.SpecificationLoaderSpi;
import org.apache.isis.core.metamodel.specloader.ObjectReflectorDefault;
import org.apache.isis.core.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.apache.isis.core.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.apache.isis.core.metamodel.specloader.traverser.SpecificationTraverser;
import org.apache.isis.core.metamodel.specloader.validator.MetaModelValidator;
import org.apache.isis.core.metamodel.specloader.validator.ValidationFailures;

import static org.apache.isis.core.commons.ensure.Ensure.ensureThatArg;
import static org.apache.isis.core.commons.ensure.Ensure.ensureThatState;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Facade for the entire Isis metamodel and supporting components.
 * 
 * <p>
 * Currently this is <i>not</i> used by Isis proper, but is available for use by integration tests.
 * The intention is to factor it into <tt>IsisSystem</tt>.
 */
public class IsisMetaModel implements ApplicationScopedComponent {

    private static enum State {
        NOT_INITIALIZED, INITIALIZED, SHUTDOWN;
    }

    private final List<Object> services = Lists.newArrayList();

    private State state = State.NOT_INITIALIZED;

    private ObjectReflectorDefault reflector;
    private RuntimeContext runtimeContext;

    private IsisConfiguration configuration;
    private ClassSubstitutor classSubstitutor;
    private CollectionTypeRegistry collectionTypeRegistry;
    private ProgrammingModel programmingModel;
    private SpecificationTraverser specificationTraverser;
    private Set<FacetDecorator> facetDecorators;
    private MetaModelValidator metaModelValidator;

    private ValidationFailures validationFailures;

    
    public static class Builder {
        private final RuntimeContext runtimeContext;
        private final ProgrammingModel programmingModel;
        private List<Object> services = Lists.newArrayList();
        
        private Builder(RuntimeContext embeddedContext, ProgrammingModel programmingModel) {
            this.runtimeContext = embeddedContext;
            this.programmingModel = programmingModel;
        }
        
        public Builder withServices(Object... services) {
            return withServices(Arrays.asList(services));
        }
        
        public Builder withServices(List<Object> services) {
            this.services = services;
            return this;
        }
        
        public IsisMetaModel build() {
            return new IsisMetaModel(runtimeContext, programmingModel, services);
        }
    }
    
    public static Builder builder(RuntimeContext runtimeContext, ProgrammingModel programmingModel) {
        return new Builder(runtimeContext, programmingModel);
    }

    public IsisMetaModel(final RuntimeContext runtimeContext, ProgrammingModel programmingModel, final List<Object> services) {
        this(runtimeContext, programmingModel, services.toArray());
    }
    
    public IsisMetaModel(final RuntimeContext runtimeContext, ProgrammingModel programmingModel, final Object... services) {
        this.runtimeContext = runtimeContext;

        this.services.add(new DomainObjectContainerDefault());
        this.services.addAll(Arrays.asList(services));

        setConfiguration(new IsisConfigurationDefault());

        this.classSubstitutor = new ClassSubstitutor();

        this.collectionTypeRegistry = new CollectionTypeRegistry();
        this.specificationTraverser = new SpecificationTraverser();

        setFacetDecorators(new TreeSet<FacetDecorator>());
        setProgrammingModelFacets(programmingModel);

        setMetaModelValidator(new MetaModelValidatorDefault());
    }

    /**
     * The list of classes representing services.
     */
    public List<Object> getServices() {
        return Collections.unmodifiableList(services);
    }

    // ///////////////////////////////////////////////////////
    // init, shutdown
    // ///////////////////////////////////////////////////////

    @Override
    public void init() {
        ensureNotInitialized();
        reflector = new ObjectReflectorDefault(configuration, specificationTraverser, programmingModel, facetDecorators, metaModelValidator);

        reflector.setServices(services);
        
        runtimeContext.injectInto(reflector);
        reflector.injectInto(runtimeContext);

        validationFailures = reflector.initAndValidate();
        runtimeContext.init();

        for (final Object service : services) {
            final ObjectSpecification serviceSpec = reflector.loadSpecification(service.getClass());
            serviceSpec.markAsService();
        }

        state = State.INITIALIZED;
    }
    
    public ValidationFailures getValidationFailures() {
        return validationFailures;
    }

    @Override
    public void shutdown() {
        ensureInitialized();
        state = State.SHUTDOWN;
    }

    // ///////////////////////////////////////////////////////
    // SpecificationLoader
    // ///////////////////////////////////////////////////////

    /**
     * Available once {@link #init() initialized}.
     */
    public SpecificationLoaderSpi getSpecificationLoader() {
        return reflector;
    }


    // ///////////////////////////////////////////////////////
    // DependencyInjector
    // ///////////////////////////////////////////////////////

    /**
     * Available once {@link #init() initialized}.
     */
    public ServicesInjector getDependencyInjector() {
        ensureInitialized();
        return runtimeContext.getDependencyInjector();
    }

    // ///////////////////////////////////////////////////////
    // Override defaults
    // ///////////////////////////////////////////////////////

    /**
     * The {@link IsisConfiguration} in force, either defaulted or specified
     * {@link #setConfiguration(IsisConfiguration) explicitly.}
     */
    public IsisConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Optionally specify the {@link IsisConfiguration}.
     * 
     * <p>
     * Call prior to {@link #init()}.
     */
    public void setConfiguration(final IsisConfiguration configuration) {
        ensureNotInitialized();
        ensureThatArg(configuration, is(notNullValue()));
        this.configuration = configuration;
    }

    /**
     * The {@link ClassSubstitutor} in force, either defaulted or specified
     * {@link #setClassSubstitutor(ClassSubstitutor) explicitly}.
     */
    public ClassSubstitutor getClassSubstitutor() {
        return classSubstitutor;
    }

    /**
     * The {@link CollectionTypeRegistry} in force, either defaulted or
     * specified {@link #setCollectionTypeRegistry(CollectionTypeRegistry)
     * explicitly.}
     */
    public CollectionTypeRegistry getCollectionTypeRegistry() {
        return collectionTypeRegistry;
    }

    /**
     * Optionally specify the {@link CollectionTypeRegistry}.
     * 
     * <p>
     * Call prior to {@link #init()}.
     */
    public void setCollectionTypeRegistry(final CollectionTypeRegistry collectionTypeRegistry) {
        ensureNotInitialized();
        ensureThatArg(collectionTypeRegistry, is(notNullValue()));
        this.collectionTypeRegistry = collectionTypeRegistry;
    }

    /**
     * The {@link SpecificationTraverser} in force, either defaulted or
     * specified {@link #setSpecificationTraverser(SpecificationTraverser)
     * explicitly}.
     */
    public SpecificationTraverser getSpecificationTraverser() {
        return specificationTraverser;
    }

    /**
     * Optionally specify the {@link SpecificationTraverser}.
     */
    public void setSpecificationTraverser(final SpecificationTraverser specificationTraverser) {
        this.specificationTraverser = specificationTraverser;
    }

    /**
     * The {@link ProgrammingModel} in force, either defaulted or specified
     * {@link #setProgrammingModelFacets(ProgrammingModel) explicitly}.
     */
    public ProgrammingModel getProgrammingModelFacets() {
        return programmingModel;
    }

    /**
     * Optionally specify the {@link ProgrammingModel}.
     * 
     * <p>
     * Call prior to {@link #init()}.
     */
    public void setProgrammingModelFacets(final ProgrammingModel programmingModel) {
        ensureNotInitialized();
        ensureThatArg(programmingModel, is(notNullValue()));
        this.programmingModel = programmingModel;
    }

    /**
     * The {@link FacetDecorator}s in force, either defaulted or specified
     * {@link #setFacetDecorators(Set) explicitly}.
     */
    public Set<FacetDecorator> getFacetDecorators() {
        return Collections.unmodifiableSet(facetDecorators);
    }

    /**
     * Optionally specify the {@link FacetDecorator}s.
     * 
     * <p>
     * Call prior to {@link #init()}.
     */
    public void setFacetDecorators(final Set<FacetDecorator> facetDecorators) {
        ensureNotInitialized();
        ensureThatArg(facetDecorators, is(notNullValue()));
        this.facetDecorators = facetDecorators;
    }

    /**
     * The {@link MetaModelValidator} in force, either defaulted or specified
     * {@link #setMetaModelValidator(MetaModelValidator) explicitly}.
     */
    public MetaModelValidator getMetaModelValidator() {
        return metaModelValidator;
    }

    /**
     * Optionally specify the {@link MetaModelValidator}.
     */
    public void setMetaModelValidator(final MetaModelValidator metaModelValidator) {
        this.metaModelValidator = metaModelValidator;
    }

    // ///////////////////////////////////////////////////////
    // State management
    // ///////////////////////////////////////////////////////

    private State ensureNotInitialized() {
        return ensureThatState(state, is(State.NOT_INITIALIZED));
    }

    private State ensureInitialized() {
        return ensureThatState(state, is(State.INITIALIZED));
    }

}
