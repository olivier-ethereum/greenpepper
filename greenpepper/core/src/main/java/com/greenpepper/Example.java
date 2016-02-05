/*
 * Copyright (c) 2006 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper;

import java.io.PrintWriter;

/**
 * <p>Example interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface Example extends Annotatable, Iterable<Example>
{
    /**
     * Returns the example content.
     *
     * @return a {@link java.lang.String} object.
     */
    String getContent();

    /**
     * <p>hasChild.</p>
     *
     * @return a boolean.
     */
    boolean hasChild();

    /**
     * <p>firstChild.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    Example firstChild();

    /**
     * <p>hasSibling.</p>
     *
     * @return a boolean.
     */
    boolean hasSibling();

    /**
     * <p>nextSibling.</p>
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    Example nextSibling();

    /**
     * Returns the last sibling of this example or this example if it has
     * no sibling.
     *
     * @return a {@link com.greenpepper.Example} object.
     */
    Example lastSibling();

    /**
     * <p>remainings.</p>
     *
     * @return a int.
     */
    int remainings();

    /**
     * returns the i(th) sibling.
     * <p/>
     * Note: at(0) returns this.<br/>
     * at(1) returns nextSibling().<br/>
     * at(2) returns nextSibling().nextSibling().<br/>
     * at(n) returns nextSibling()...nextSibling()...nextSibling().<br/>
     * <p/>
     *
     * @param i the position
     * @return the i(th) sibling
     */
    Example at( int i );

    /**
     * Retrieves a descendent.
     * <p/>
     * Note: at(0, 0) returns firstChild().<br/>
     * at(0, 1) returns firstChild().nextSibling().<br/>
     * at(0, 2) returns firstChild().nextSibling().nextSibling().<br/>
     * at(0, n) returns firstChild().nextSibling()...nextSibling()...nextSibling().<br/>
     * at(1, 0) returns nextSibling().firstChild().<br/>
     * at(2, 0) returns nextSibling().nextSibling().firstChild().<br/>
     * at(n, 0) returns nextSibling()...nextSibling()...nextSibling().firstChild().<br/>
     * at(n, n) returns nextSibling()...nextSibling()...nextSibling().firstChild().nextSibling()...nextSibling()...nextSibling().<br/>
     * at(1, 1, 1) returns nextSibling().firstChild().nextSibling().firstChild().nextSibling().<br/>
     * at(2, 1, 1, 2) returns nextSibling().nextSibling().firstChild().nextSibling().firstChild().nextSibling().firstChild().nextSibling().nextSibling().<br/>
     * ...<br/>
     * <p/>
     *
     * @param i         the position
     * @param positions a int.
     * @return the descendent of the i(th) sibling
     */
    Example at( int i, int... positions );
    /**
     * Returns the remaining number of siblings (Self-included).
     */

    /**
     * Recursivly prints this example and its children.
     *
     * @param out a {@link java.io.PrintWriter} object.
     */
    void print( PrintWriter out );

    /**
     * <p>setContent.</p>
     *
     * @param content a {@link java.lang.String} object.
     */
    void setContent( String content );

    /**
     * Adds a new sibling at the end of the siblings.
     * <p/>
     *
     * @return the newly added sibling
     */
    Example addSibling();

    /**
     * Adds a new child at the end of the childs.
     * <p/>
     *
     * @return the newly added child
     */
    Example addChild();
}
