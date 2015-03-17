package com.greenpepper.util.cmdline;

import java.util.List;

public interface StreamGobbler extends Runnable
{
    public String getOutput();
    public String getError();

    public List<Exception> getExceptions();
    public boolean hasErrors();
    public boolean hasExceptions();
    public void exceptionCaught(Exception e);
}
