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
package org.apache.isis.tooling.cli.doclet;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.TypeDeclaration;

import org.apache.isis.commons.collections.Can;
import org.apache.isis.tooling.javamodel.CompilationUnits;
import org.apache.isis.tooling.javamodel.Javadocs;
import org.apache.isis.tooling.javamodel.TypeDeclarations;
import org.apache.isis.tooling.model4adoc.AsciiDocFactory;
import org.apache.isis.tooling.model4adoc.AsciiDocWriter;

import lombok.NonNull;
import lombok.Value;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Value
@Log4j2
public class Doclet {

    private final TypeDeclaration<?> td;

    public static Optional<Doclet> parse(final @NonNull File sourceFile) {

        try {
            val cu = StaticJavaParser.parse(sourceFile);

            return Stream.of(cu)
            .flatMap(CompilationUnits::streamPublicTypeDeclarations)
            .filter(Doclets::isApacheIsisDoclet)
            .map(Doclet::new)
            .findFirst();

        } catch (Exception e) {
            log.error("failed to parse java source file {}", sourceFile, e);
            return Optional.empty();
        }

    }

    public String toAsciiDoc() {
        
        val doc = AsciiDocFactory.doc();
        
        val introBlock = AsciiDocFactory.block(doc);
        val javaSourceBlock = AsciiDocFactory.block(doc);
        val footNoteBlock = AsciiDocFactory.block(doc);
        
        val mds = TypeDeclarations.streamPublicMethodDeclarations(td)
                .filter(Javadocs::presentAndNotHidden)
                .collect(Can.toCan());

        // -- intro
        
        td.getJavadoc().ifPresent(javadoc->{
            introBlock.setSource(Doclets.toAsciiDoc(javadoc));    
        });
        
        // -- java content
        
        val java = new StringBuilder();
        
        java.append(String.format("type %s {\n", td.getName().asString()));
        
        mds.forEach(md->{

            java.append(String.format("\n  %s // <.>\n", Doclets.toNormalizedMethodDeclaration(md)));
            
        });

        java.append("}\n");
        
        javaSourceBlock.setSource(AsciiDocFactory.SourceFactory.java(java.toString(), td.getName().asString()));
        
        // -- foot notes
        
        val footNotes = new StringBuilder();
        
        mds.forEach(md->{
            
            md.getJavadoc()
            .ifPresent(javadoc->{
            
                footNotes.append(String.format("\n<.> `%s` %s\n",
                        Doclets.toNormalizedMethodDeclaration(md),
                        Doclets.toAsciiDoc(javadoc)));
                
            });
            
        });
        
        footNoteBlock.setSource(footNotes.toString());
        
        try {
            doc.setTitle(td.getName().asString());
            return AsciiDocWriter.toString(doc);
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
        
    }


    // -- HELPER





}
