package com.greenpepper.util.cmdline;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic stream manager for a process.
 * <p/>
 *
 * @author JCHUET
 * @version $Id: $Id
 */
public class StreamGobblerImpl implements StreamGobbler
{
    private String input = "";
    private OutputStream stdin;
    private InputStream stdout;
    private InputStream stderr;
    private StringBuffer outBuffer = new StringBuffer();
    private StringBuffer errBuffer = new StringBuffer();
    private List<Exception> exceptions = new ArrayList<Exception>();
    
    /**
     * <p>Constructor for StreamGobblerImpl.</p>
     *
     * @param process a {@link java.lang.Process} object.
     */
    public StreamGobblerImpl(Process process)
    {
        stdin = process.getOutputStream();
        stdout = process.getInputStream();
        stderr = process.getErrorStream();
    }

    /**
     * <p>run.</p>
     */
    public void run()
    {
        new Thread(new OuputReadingRunnable(stdout, outBuffer), "Process standard out").start();
        new Thread(new OuputReadingRunnable(stderr, errBuffer), "Process error").start();
        sendInput();
    }

    /**
     * <p>getOutput.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutput()
    {
        return outBuffer.toString();
    }

    /**
     * <p>getError.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getError()
    {
        return errBuffer.toString();
    }

    /**
     * <p>hasErrors.</p>
     *
     * @return a boolean.
     */
    public boolean hasErrors()
    {
        return !StringUtils.isEmpty(errBuffer.toString());
    }

    /**
     * <p>Getter for the field <code>exceptions</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Exception> getExceptions()
    {
        return exceptions;
    }

    /**
     * <p>hasExceptions.</p>
     *
     * @return a boolean.
     */
    public boolean hasExceptions()
    {
        return exceptions.size() > 0;
    }

    /** {@inheritDoc} */
    public void exceptionCaught(Exception e)
    {
        exceptions.add(e);
    }

    /**
     * <p>sendInput.</p>
     */
    protected void sendInput()
    {
        Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                    stdin.write(input.getBytes("UTF-8"));
                    stdin.flush();
                    stdin.close();
                }
                catch (Exception e)
                {
                    exceptionCaught(e);
                }
            }
        };

        try
        {
            thread.start();
            thread.join();
        }
        catch (Exception e)
        {
            exceptionCaught(e);
        }
    }

    private void readOutput(InputStream input, StringBuffer buffer)
    {
        try
        {
            int c;
            while ((c = input.read()) != -1)
            {
                buffer.append((char) c);
            }
        }
        catch (Exception e)
        {
            exceptionCaught(e);
        }
    }

    private class OuputReadingRunnable implements Runnable
    {
        public InputStream input;
        public StringBuffer buffer;
        public OuputReadingRunnable(InputStream input, StringBuffer buffer)
        {
            this.input = input;
            this.buffer = buffer;
        }

        public void run()
        {
            readOutput(input, buffer);
        }
    }
}
