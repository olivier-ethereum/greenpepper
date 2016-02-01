/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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

package com.greenpepper.report;

import com.greenpepper.util.Factory;
import com.greenpepper.util.IOUtil;
import com.greenpepper.util.URIUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class FileReportGenerator implements ReportGenerator
{
    private final File reportsDirectory;

    private Class<? extends Report> reportClass;
    private boolean automaticExtension;

    public FileReportGenerator( File outputDir )
    {
        this.reportsDirectory = outputDir;
        this.reportClass = PlainReport.class;
    }

    public void setReportClass( Class<? extends Report> reportClass )
    {
        this.reportClass = reportClass;
    }

    public void adjustReportFilesExtensions( boolean enable )
    {
        this.automaticExtension = enable;
    }

    public Report openReport( String name )
    {
        Factory<Report> factory = new Factory<Report>( reportClass );
        return factory.newInstance( name );
    }

    public void closeReport( Report report ) throws IOException, URISyntaxException {
        FileWriter out = null;
        try
        {
            File reportFile = new File( reportsDirectory, outputNameOf( report ) );
            String documentUri = report.getDocumentUri();
            if (documentUri != null) {
                if (reportFile.equals(new File(new URI(documentUri)))) {
                    reportFile = new File(reportFile.getAbsolutePath() + ".out");
                }
            }
            IOUtil.createDirectoryTree( reportFile.getParentFile() );
            out = new FileWriter( reportFile );
            report.printTo( out );
            out.flush();
        }
        finally
        {
            IOUtil.closeQuietly( out );
        }
    }

    /**
     * Utility method to guess the output name generated for this output parameter.
     * @param output the specified output.
     * @return the real generated filename.
     */
    public String outputNameFor(String output) {
        Report report = openReport(output);
        return outputNameOf(report);
    }

    private String outputNameOf( Report report )
    {
        String name = report.getName();
        if (automaticExtension && !name.endsWith( extensionOf( report ) ))
            name += extensionOf( report );
        return URIUtil.escapeFileSystemForbiddenCharacters(name);
    }

    private String extensionOf( Report report )
    {
        return "." + report.getType();
    }
}
