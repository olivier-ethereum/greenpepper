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

package com.greenpepper.runner;

import java.lang.reflect.Constructor;

import com.greenpepper.Statistics;
import com.greenpepper.document.CommentTableFilter;
import com.greenpepper.document.Document;
import com.greenpepper.document.GreenPepperTableFilter;
import com.greenpepper.document.InterpreterSelector;
import com.greenpepper.document.SectionsTableFilter;
import com.greenpepper.report.Report;
import com.greenpepper.report.ReportGenerator;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.Timer;
import com.greenpepper.util.log.GreenPepperLogger;
import org.slf4j.Logger;

/**
 * <p>DocumentRunner class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class DocumentRunner implements SpecificationRunner
{
    private static final Logger LOGGER = GreenPepperLogger.getLogger(DocumentRunner.class);

    private ReportGenerator reportGenerator;
    private SystemUnderDevelopment systemUnderDevelopment;
    private DocumentRepository documentRepository;
    private String[] sections;
    private SpecificationRunnerMonitor monitor;
    private Class<? extends InterpreterSelector> interpreterSelectorClass;

    private boolean lazy;

    /**
     * <p>Constructor for DocumentRunner.</p>
     */
    public DocumentRunner()
    {
        monitor = new NullSpecificationRunnerMonitor();
    }

    /**
     * <p>Setter for the field <code>monitor</code>.</p>
     *
     * @param monitor a {@link com.greenpepper.runner.SpecificationRunnerMonitor} object.
     */
    public void setMonitor( SpecificationRunnerMonitor monitor )
    {
        this.monitor = monitor;
    }

    /**
     * <p>Setter for the field <code>systemUnderDevelopment</code>.</p>
     *
     * @param systemUnderDevelopment a {@link com.greenpepper.systemunderdevelopment.SystemUnderDevelopment} object.
     */
    public void setSystemUnderDevelopment( SystemUnderDevelopment systemUnderDevelopment )
    {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    /**
     * <p>setInterpreterSelector.</p>
     *
     * @param interpreterSelectorClass a {@link java.lang.Class} object.
     */
    public void setInterpreterSelector( Class<? extends InterpreterSelector> interpreterSelectorClass )
    {
        this.interpreterSelectorClass = interpreterSelectorClass;
    }

    /**
     * <p>setRepository.</p>
     *
     * @param documentRepository a {@link com.greenpepper.repository.DocumentRepository} object.
     */
    public void setRepository( DocumentRepository documentRepository )
    {
        this.documentRepository = documentRepository;
    }

    /**
     * <p>Setter for the field <code>sections</code>.</p>
     *
     * @param sections a {@link java.lang.String} object.
     */
    public void setSections( String... sections )
    {
        this.sections = sections;
    }

    /**
     * <p>Setter for the field <code>reportGenerator</code>.</p>
     *
     * @param generator a {@link com.greenpepper.report.ReportGenerator} object.
     */
    public void setReportGenerator( ReportGenerator generator )
    {
        this.reportGenerator = generator;
    }

    /**
     * <p>Setter for the field <code>lazy</code>.</p>
     *
     * @param lazy a boolean.
     */
    public void setLazy(boolean lazy)
    {
        this.lazy = lazy;
    }

    /** {@inheritDoc} */
    public void run( String input, String output )
    {
        Report report = null;

        try
        {
            report = reportGenerator.openReport( output );
            monitor.testRunning( input );

			final Timer total = new Timer().start();

            Document document = documentRepository.loadDocument( input );
			document.setSections(sections);
            document.addFilter( new CommentTableFilter() );
            document.addFilter( new SectionsTableFilter( sections ) );
            document.addFilter( new GreenPepperTableFilter( lazy ) );

			final Timer execution = new Timer().start();

            systemUnderDevelopment.onStartDocument(document);
            document.execute( newInterpreterSelector(systemUnderDevelopment) );
            systemUnderDevelopment.onEndDocument(document);

			execution.stop();
            document.done();
			total.stop();

			document.getTimeStatistics().tally( total.elapse(), execution.elapse() );

            report.generate( document );
            Statistics stats = document.getStatistics();
            monitor.testDone( stats.rightCount(), stats.wrongCount(), stats.exceptionCount(), stats.ignoredCount() );
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to run the specification " + input, e);
            if (report != null) report.renderException( e );
            monitor.exceptionOccured( e );
        }
        finally
        {
            closeReport( report );
        }

    }

    private void closeReport( Report report )
    {
        if (report == null) return;
        try
        {
            reportGenerator.closeReport( report );
        }
        catch (Exception e)
        {
            monitor.exceptionOccured( e );
        }
    }

    private InterpreterSelector newInterpreterSelector(SystemUnderDevelopment systemUnderDevelopment) throws Exception
    {
		Constructor<? extends InterpreterSelector>  constructor = interpreterSelectorClass.getConstructor(SystemUnderDevelopment.class);
		return constructor.newInstance( systemUnderDevelopment );
	}
}
