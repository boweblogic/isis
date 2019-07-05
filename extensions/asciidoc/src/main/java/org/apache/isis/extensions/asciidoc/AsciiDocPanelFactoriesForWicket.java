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

package org.apache.isis.extensions.asciidoc;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.model.models.ValueModel;
import org.apache.isis.viewer.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.isis.viewer.wicket.ui.components.scalars.markup.MarkupComponent;
import org.apache.isis.viewer.wicket.ui.components.scalars.markup.MarkupPanel;
import org.apache.isis.viewer.wicket.ui.components.scalars.markup.MarkupPanelFactories;
import org.apache.isis.viewer.wicket.ui.components.scalars.markup.StandaloneMarkupPanel;

import lombok.val;

/**
 * @implNote Almost a copy of {@link MarkupPanelFactories}, but specific to 
 * the {@link AsciiDoc} value-type which requires client-side java-script to be
 * executed to enable syntax highlighting
 */
public class AsciiDocPanelFactoriesForWicket {

    // -- PARENTED

    public static class Parented extends ComponentFactoryAbstract {
        private static final long serialVersionUID = 1L;

        public Parented() {
            super(ComponentType.SCALAR_NAME_AND_VALUE, MarkupPanel.class);
        }

        @Override
        public ApplicationAdvice appliesTo(final IModel<?> model) {
            if (!(model instanceof ScalarModel))
                return ApplicationAdvice.DOES_NOT_APPLY;

            final ScalarModel scalarModel = (ScalarModel) model;

            if(!scalarModel.isScalarTypeAnyOf(AsciiDoc.class))
                return ApplicationAdvice.DOES_NOT_APPLY;

            return appliesIf( !scalarModel.hasChoices() );
        }

        @Override
        public final Component createComponent(final String id, final IModel<?> model) {
            return new MarkupPanel(id, (ScalarModel) model) {
                private static final long serialVersionUID = 1L;
                
                @Override
                protected MarkupComponent createMarkupComponent(String id) {
                    val markupComponent = new AsciiDocComponent(id, getModel());
                    markupComponent.setEnabled(false);
                    return markupComponent;
                }

            };
        }
    }

    // -- STANDALONE

    public static class Standalone extends ComponentFactoryAbstract {
        private static final long serialVersionUID = 1L;

        public Standalone() {
            super(ComponentType.VALUE, StandaloneMarkupPanel.class);
        }

        @Override
        public ApplicationAdvice appliesTo(final IModel<?> model) {
            if (!(model instanceof ValueModel))
                return ApplicationAdvice.DOES_NOT_APPLY;
            final ValueModel valueModel = (ValueModel) model;
            final ObjectAdapter adapter = valueModel.getObject();
            if(adapter==null || adapter.getPojo()==null)
                return ApplicationAdvice.DOES_NOT_APPLY;

            return appliesIf( adapter.getPojo() instanceof AsciiDoc );
        }

        @Override
        public final Component createComponent(final String id, final IModel<?> model) {
            return new StandaloneMarkupPanel(id, (ValueModel) model) {
                private static final long serialVersionUID = 1L;
                
                @Override
                protected MarkupComponent createMarkupComponent(String id) {
                    val markupComponent = new AsciiDocComponent(id, getModel());
                    return markupComponent;
                }
            };
        }
    }


}
