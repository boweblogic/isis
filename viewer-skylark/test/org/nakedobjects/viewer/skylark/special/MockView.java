package org.nakedobjects.viewer.skylark.special;

import org.nakedobjects.object.Naked;
import org.nakedobjects.viewer.skylark.Bounds;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Click;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.ContentDrag;
import org.nakedobjects.viewer.skylark.Drag;
import org.nakedobjects.viewer.skylark.DragStart;
import org.nakedobjects.viewer.skylark.FocusManager;
import org.nakedobjects.viewer.skylark.InteractionSpy;
import org.nakedobjects.viewer.skylark.InternalDrag;
import org.nakedobjects.viewer.skylark.KeyboardAction;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.UserActionSet;
import org.nakedobjects.viewer.skylark.Padding;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAreaType;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewDrag;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.ViewState;
import org.nakedobjects.viewer.skylark.Viewer;
import org.nakedobjects.viewer.skylark.Workspace;

import java.util.Vector;


public class MockView implements View {
    public static int next = 0;
    private int id = next++;

    private Vector expected = new Vector();
    private int addNo = 0;

    private View[] subviews;
    private Content content;

    public boolean invalidateContentCalled;
    private Size size;
    private Size requiredSize;

    public void addView(View view) {
        addActual("add " + view);
    }

    protected void addActual(Object actual) {
        if (addNo >= expected.size()) {
            throw new AssertionError("No more expected objects, but got <" + actual + ">");
        }
        Object e = expected.elementAt(addNo);
        if (!e.equals(actual)) {
            throw new AssertionError("Expected <" + e + ">, but got <" + actual + ">");
        }
        addNo++;
    }

    public boolean canChangeValue() {
        return false;
    }

    public boolean canFocus() {
        return false;
    }

    public String debugDetails() {
        return null;
    }

    public void dispose() {}

    public void drag(InternalDrag drag) {}

    public void dragCancel(InternalDrag drag) {}

    public View dragFrom(Location location) {
        return null;
    }

    public void dragIn(ContentDrag drag) {}

    public void dragOut(ContentDrag drag) {}

    public void dragTo(InternalDrag drag) {}

    public void draw(Canvas canvas) {}

    public void drop(ContentDrag drag) {}

    public void drop(ViewDrag drag) {}

    public void editComplete() {}

    public void entered() {}

    public void enteredSubview() {}

    public void exited() {}

    public void exitedSubview() {}

    public void firstClick(Click click) {}

    public void focusLost() {}

    public void focusReceived() {}

    public int getBaseline() {
        return 0;
    }

    public Bounds getBounds() {
        return null;
    }

    public Content getContent() {
        return content;
    }

    public void setupContent(Content content) {
        this.content = content;
    }

    public int getId() {
        return 0;
    }

    public Location getLocation() {
        return null;
    }

    public Padding getPadding() {
        return new Padding();
    }

    public View getParent() {
        return null;
    }

    public Size getRequiredSize() {
        return requiredSize;
    }

    public Size getSize() {
        return size;
    }

    public ViewSpecification getSpecification() {
        return null;
    }

    public ViewState getState() {
        return null;
    }

    public View[] getSubviews() {
        return subviews;
    }

    public void setupSubviews(View[] subviews) {
        this.subviews = subviews;
    }

    public View getView() {
        return this;
    }

    public ViewAxis getViewAxis() {
        return null;
    }

    public Viewer getViewManager() {
        return new Viewer() {
            public InteractionSpy getSpy() {
                return new InteractionSpy();
            }
        };
    }

    public Workspace getWorkspace() {
        return null;
    }

    public boolean hasFocus() {
        return false;
    }

    public void invalidateContent() {
        invalidateContentCalled = true;
    }

    public void invalidateLayout() {}

    public void keyPressed(KeyboardAction key) {}

    public void keyReleased(int keyCode, int modifiers) {}

    public void keyTyped(char keyCode) {}

    public void layout() {}

    public void markDamaged() {}

    public void markDamaged(Bounds bounds) {}

    public void contentMenuOptions(UserActionSet menuOptions) {}

    public void mouseMoved(Location at) {}

    public void objectActionResult(Naked result, Location at) {}

    public View pickupContent(Location location) {
        return null;
    }

    public View pickupView(Location location) {
        return null;
    }

    public void print(Canvas canvas) {}

    public void refresh() {}

    public void removeView(View view) {}

    public void replaceView(View toReplace, View replacement) {
        addActual("replace " + toReplace + " with " + replacement);
    }

    public void secondClick(Click click) {}

    public void setBounds(Bounds bounds) {}

    public void setLocation(Location point) {}

    public void setParent(View view) {}

    public void setSize(Size size) {
        this.size = size;}

    public void setView(View view) {}

    public void thirdClick(Click click) {}

    public void update(Naked object) {}

    public ViewAreaType viewAreaType(Location mouseLocation) {
        return null;
    }

    public void setRequiredSize(Size size) {
        requiredSize = size;}

    public View subviewFor(Location location) {
        return null;
    }

    public View identify(Location location) {
        return null;
    }

    public Location getAbsoluteLocation() {
        return null;
    }

    public boolean contains(View view) {
        return false;
    }

    public Drag dragStart(DragStart drag) {
        return null;
    }

    public void limitBoundsWithin(Bounds bounds) {}

    public void updateView() {}

    public void viewMenuOptions(UserActionSet menuOptions) {}

    public void verify() {
        if (expected.size() != addNo) {
            throw new AssertionError("Expected " + expected.size() + " objects, but got " + addNo);
        }
    }

    public void addAction(Object expected) {
        this.expected.addElement(expected);
    }

    public String toString() {
        return "MockView" + id;
    }

    public void mouseDown(Click click) {}

    public void mouseUp(Click click) {}

    public boolean containsFocus() {
        return false;
    }

    public FocusManager getFocusManager() {
        return null;
    }

    public void setFocusManager(FocusManager focusManager) {}
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2005 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */