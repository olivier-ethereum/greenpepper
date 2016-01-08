/*
 * Copyright (c) 2007 Pyxis Technologies inc.
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */

package com.greenpepper.runner;

import com.greenpepper.util.cli.ParseException;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        int exitcode = launch(args);
        System.exit(exitcode);
    }

    public static int launch(String[] args) {
        CommandLineRunner runner = new CommandLineRunner();
        LoggingMonitor loggingMonitor = new LoggingMonitor(System.out, System.err);
        SystemExitCodesMonitor exitCodesMonitor = new SystemExitCodesMonitor();
        CompositeSpecificationRunnerMonitor monitor = new CompositeSpecificationRunnerMonitor(loggingMonitor, exitCodesMonitor);
        runner.setMonitor(monitor);
        try {
            runner.run(args);
            return exitCodesMonitor.getExitcode();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.err.println("Try '--help' for more information.");

            if (e.getCause() != null) {
                System.err.println("Caused by:");
                e.getCause().printStackTrace(System.err);
            }

            return 1;
        } catch (Throwable t) {
            monitor.exceptionOccured(t);
            return 1;
        }
    }
}
