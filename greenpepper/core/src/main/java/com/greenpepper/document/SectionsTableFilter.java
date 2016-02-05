package com.greenpepper.document;

import com.greenpepper.Example;
import static com.greenpepper.util.ExampleUtil.content;
import static com.greenpepper.util.ExampleUtil.contentOf;

import java.util.Collection;
import java.util.HashSet;

/**
 * <p>SectionsTableFilter class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SectionsTableFilter extends AbstractTableFilter
{
    private final Sections filter;

    /**
     * <p>Constructor for SectionsTableFilter.</p>
     *
     * @param sections a {@link java.lang.String} object.
     */
    public SectionsTableFilter( String... sections )
    {
        super( "section" );
        this.filter = new Sections( sections );
    }

    /** {@inheritDoc} */
    public Example doFilter( Example example )
    {
        if (filter.areIncluded( content( example.at( 0, 1, 0 ) ) )) return example.nextSibling();
        return goToNextSection( example.nextSibling() );
    }

    /**
     * <p>goToNextSection.</p>
     *
     * @param example a {@link com.greenpepper.Example} object.
     * @return a {@link com.greenpepper.Example} object.
     */
    protected Example goToNextSection( Example example )
    {
        for (Example table = example; table != null; table = table.nextSibling())
        {
            if ("section".equalsIgnoreCase( contentOf( table.at( 0, 0, 0 ) ) )) return table;
        }
        return null;
    }

    /**
     * <p>includeSections.</p>
     *
     * @param tags a {@link java.lang.String} object.
     */
    public void includeSections( String... tags )
    {
        filter.allowSections( tags );
    }

    public static class Sections
    {
        private Collection<String> includedSections;

        public Sections( String... sections )
        {
            this.includedSections = new HashSet<String>();
            allowSections( sections );
        }

        public boolean areIncluded( String... sections )
        {
            if (includedSections.isEmpty()) return true;

            for (String section : sections)
            {
                if (!includedSections.contains( format( section ) )) return false;
            }

            return true;
        }

        public void allowSections( String... tags )
        {
            for (String tag : tags)
            {
                allowTag( tag );
            }
        }

        private String format( String tag )
        {
            return tag.trim().toUpperCase();
        }

        private void allowTag( String tag )
        {
            includedSections.add( format( tag ) );
        }
    }
}
