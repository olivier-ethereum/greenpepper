package com.greenpepper.server.rpc.xmlrpc;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by oaouattara on 21/01/16.
 */
public class CollectionUtil {
    public static <T> Vector<T> toVector( T... objects )
    {
        return new Vector<T>( Arrays.asList( objects ) );
    }
}
