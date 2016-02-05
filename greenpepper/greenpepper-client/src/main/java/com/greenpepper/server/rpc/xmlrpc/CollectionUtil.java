package com.greenpepper.server.rpc.xmlrpc;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by oaouattara on 21/01/16.
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CollectionUtil {
    /**
     * <p>toVector.</p>
     *
     * @param objects a T object.
     * @param <T> a T object.
     * @return a {@link java.util.Vector} object.
     */
    public static <T> Vector<T> toVector( T... objects )
    {
        return new Vector<T>( Arrays.asList( objects ) );
    }
}
