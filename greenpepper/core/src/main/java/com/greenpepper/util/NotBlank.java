package com.greenpepper.util;

/**
 * <p>NotBlank class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class NotBlank implements CollectionUtil.Predicate<String>
{
    /**
     * <p>isVerifiedBy.</p>
     *
     * @param element a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isVerifiedBy(String element)
    {
        return !StringUtil.isBlank( element );
    }
}
