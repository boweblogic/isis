package org.nakedobjects.object;

import org.nakedobjects.object.persistence.Oid;
import org.nakedobjects.object.reflect.DummyNakedObject;

import java.util.Enumeration;


public class DummyObjectLoader implements NakedObjectLoader {
    private NakedObject adapter;

    public NakedObject createAdapterForTransient(Object object) {
        return adapter;
    }

    public NakedValue createAdapterForValue(Object value) {
        return null;
    }

    public NakedCollection createAdapterForCollection(Object collection, NakedObjectSpecification specification) {
        return null;
    }

    public NakedObject createTransientInstance(NakedObjectSpecification specification) {
        return adapter;
    }

    public NakedValue createValueInstance(NakedObjectSpecification specification) {
        return null;
    }

    public void end(NakedReference object) {}

    public NakedObject getAdapterFor(Object object) {
        return null;
    }

    public NakedObject getAdapterFor(Oid oid) {
        return null;
    }

    public NakedObject getAdapterForElseCreateAdapterForTransient(Object object) {
        return null;
    }

    public String getDebugData() {
        return null;
    }

    public String getDebugTitle() {
        return null;
    }

    public Enumeration getIdentifiedObjects() {
        return null;
    }

    public void init() {}

    public boolean isIdentityKnown(Oid oid) {
        return false;
    }

    public void madePersistent(NakedReference object, Oid oid) {}

    public NakedObject recreateAdapterForPersistent(Oid oid, NakedObjectSpecification spec) {
        return null;
    }
    
    public NakedCollection recreateCollection(NakedObjectSpecification specification) {
        return null;
    }

    public NakedObject recreateAdapterForPersistent(Oid oid, Object object) {
        return adapter;
    }

  	public NakedObject recreateTransientInstance(NakedObjectSpecification specification) {
        return null;
    }
    
    public void reset() {}

    public void setupAdapter(NakedObject adapter) {
        this.adapter = adapter;
    }

    public void shutdown() {}

    public void start(NakedReference object, ResolveState targetState) {
        ((DummyNakedObject) object).setupResolveState(targetState);
    }

    public void unloaded(NakedObject object) {}

  public NakedCollection getAdapterForElseCreateAdapterForCollection(
            NakedObject parent,
            String fieldName,
            NakedObjectSpecification elementSpecification,
            Object collection) {
        return null;
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business objects directly to the user.
 * Copyright (C) 2000 - 2005 Naked Objects Group Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address of Naked Objects Group is
 * Kingsway House, 123 Goldworth Road, Woking GU21 1NR, UK).
 */
