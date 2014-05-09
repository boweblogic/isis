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
package webapp.prototyping;

import java.math.BigDecimal;

import dom.todo.ToDoItem;
import dom.todo.ToDoItem.Category;
import dom.todo.ToDoItem.Subcategory;
import dom.todo.ToDoItems;

import org.joda.time.LocalDate;

import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.fixturescripts.FixtureResultList;
import org.apache.isis.applib.fixturescripts.SimpleFixtureScript;

public class ToDoItemsCreateForUser extends SimpleFixtureScript {

    // //////////////////////////////////////
    // Constructor
    // //////////////////////////////////////

    private final String user;
    
    public ToDoItemsCreateForUser(String user) {
        super("Create ToDoItems for '" + user + "'", user);
        this.user = user;
    }
    
    @Override
    protected void doRun(final FixtureResultList resultList) {
        installFor(user, resultList);
        getContainer().flush();
    }

    private void installFor(final String user, final FixtureResultList resultList) {

        createToDoItemForUser("Buy milk", Category.Domestic, Subcategory.Shopping, user, daysFromToday(0), new BigDecimal("0.75"), resultList);
        createToDoItemForUser("Buy bread", Category.Domestic, Subcategory.Shopping, user, daysFromToday(0), new BigDecimal("1.75"), resultList);
        createToDoItemForUser("Buy stamps", Category.Domestic, Subcategory.Shopping, user, daysFromToday(0), new BigDecimal("10.00"), resultList).setComplete(true);
        createToDoItemForUser("Pick up laundry", Category.Domestic, Subcategory.Chores, user, daysFromToday(6), new BigDecimal("7.50"), resultList);
        createToDoItemForUser("Mow lawn", Category.Domestic, Subcategory.Garden, user, daysFromToday(6), null, resultList);
        createToDoItemForUser("Vacuum house", Category.Domestic, Subcategory.Housework, user, daysFromToday(3), null, resultList);
        createToDoItemForUser("Sharpen knives", Category.Domestic, Subcategory.Chores, user, daysFromToday(14), null, resultList);

        createToDoItemForUser("Write to penpal", Category.Other, Subcategory.Other, user, null, null, resultList);

        createToDoItemForUser("Write blog post", Category.Professional, Subcategory.Marketing, user, daysFromToday(7), null, resultList).setComplete(true);
        createToDoItemForUser("Organize brown bag", Category.Professional, Subcategory.Consulting, user, daysFromToday(14), null, resultList);
        createToDoItemForUser("Submit conference session", Category.Professional, Subcategory.Education, user, daysFromToday(21), null, resultList);
        createToDoItemForUser("Stage Isis release", Category.Professional, Subcategory.OpenSource, user, null, null, resultList);

        getContainer().flush();
    }

    // //////////////////////////////////////

    private ToDoItem createToDoItemForUser(
            final String description, 
            final Category category, Subcategory subcategory, 
            final String user, 
            final LocalDate dueBy, final BigDecimal cost, FixtureResultList resultList) {
        ToDoItem newToDo = toDoItems.newToDo(description, category, subcategory, user, dueBy, cost);
        return resultList.add(this, newToDo);
    }

    private static LocalDate daysFromToday(final int i) {
        final LocalDate date = new LocalDate(Clock.getTimeAsDateTime());
        return date.plusDays(i);
    }


    // //////////////////////////////////////
    // Injected services
    // //////////////////////////////////////

    @javax.inject.Inject
    private ToDoItems toDoItems;
}