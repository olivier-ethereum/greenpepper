/*
 * Decompiled with CFR 0_100.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.events.ControlAdapter
 *  org.eclipse.swt.events.ControlEvent
 *  org.eclipse.swt.events.ControlListener
 *  org.eclipse.swt.events.DisposeEvent
 *  org.eclipse.swt.events.DisposeListener
 *  org.eclipse.swt.events.PaintEvent
 *  org.eclipse.swt.events.PaintListener
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Drawable
 *  org.eclipse.swt.graphics.GC
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.widgets.Canvas
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 */
package com.greenpepper.eclipse.util;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class GreenPepperProgressBar
extends Canvas {
    private static final int DEFAULT_WIDTH = 160;
    private static final int DEFAULT_HEIGHT = 18;
    private int currentTickCount = 0;
    private int maxTickCount = 0;
    private int colorBarWidth = 0;
    private Color oKColor;
    private Color failureColor;
    private Color stoppedColor;
    private boolean error;
    private boolean interrupted = false;

    public GreenPepperProgressBar(Composite parent) {
        super(parent, 0);
        this.addControlListener((ControlListener)new ControlAdapter(){

            public void controlResized(ControlEvent e) {
                GreenPepperProgressBar.access$2(GreenPepperProgressBar.this, GreenPepperProgressBar.this.scale(GreenPepperProgressBar.this.currentTickCount));
                GreenPepperProgressBar.this.redraw();
            }
        });
        this.addPaintListener((PaintListener)new PaintListener(){

            public void paintControl(PaintEvent e) {
                GreenPepperProgressBar.this.paint(e);
            }
        });
        this.addDisposeListener((DisposeListener)new DisposeListener(){

            public void widgetDisposed(DisposeEvent e) {
                GreenPepperProgressBar.this.failureColor.dispose();
                GreenPepperProgressBar.this.oKColor.dispose();
                GreenPepperProgressBar.this.stoppedColor.dispose();
            }
        });
        Display display = parent.getDisplay();
        this.failureColor = new Color((Device)display, 159, 63, 63);
        this.oKColor = new Color((Device)display, 95, 191, 95);
        this.stoppedColor = new Color((Device)display, 120, 120, 120);
    }

    public void setMaximum(int max) {
        this.maxTickCount = max;
    }

    public void reset() {
        this.error = false;
        this.interrupted = false;
        this.currentTickCount = 0;
        this.maxTickCount = 0;
        this.colorBarWidth = 0;
        this.redraw();
    }

    public void reset(boolean hasErrors, boolean stopped, int ticksDone, int maximum) {
        boolean noChange = this.error == hasErrors && this.interrupted == stopped && this.currentTickCount == ticksDone && this.maxTickCount == maximum;
        this.error = hasErrors;
        this.interrupted = stopped;
        this.currentTickCount = ticksDone;
        this.maxTickCount = maximum;
        this.colorBarWidth = this.scale(ticksDone);
        if (!noChange) {
            this.redraw();
        }
    }

    private void paintStep(int startX, int endX) {
        GC gc = new GC((Drawable)this);
        this.setStatusColor(gc);
        Rectangle rect = this.getClientArea();
        startX = Math.max(1, startX);
        gc.fillRectangle(startX, 1, endX - startX, rect.height - 2);
        gc.dispose();
    }

    private void setStatusColor(GC gc) {
        if (this.interrupted) {
            gc.setBackground(this.stoppedColor);
        } else if (this.error) {
            gc.setBackground(this.failureColor);
        } else {
            gc.setBackground(this.oKColor);
        }
    }

    public void stopped() {
        this.interrupted = true;
        this.redraw();
    }

    private int scale(int value) {
        if (this.maxTickCount > 0) {
            Rectangle r = this.getClientArea();
            if (r.width != 0) {
                return Math.max(0, value * (r.width - 2) / this.maxTickCount);
            }
        }
        return value;
    }

    private void drawBevelRect(GC gc, int x, int y, int w, int h, Color topleft, Color bottomright) {
        gc.setForeground(topleft);
        gc.drawLine(x, y, x + w - 1, y);
        gc.drawLine(x, y, x, y + h - 1);
        gc.setForeground(bottomright);
        gc.drawLine(x + w, y, x + w, y + h);
        gc.drawLine(x, y + h, x + w, y + h);
    }

    private void paint(PaintEvent event) {
        GC gc = event.gc;
        Display disp = this.getDisplay();
        Rectangle rect = this.getClientArea();
        gc.fillRectangle(rect);
        this.drawBevelRect(gc, rect.x, rect.y, rect.width - 1, rect.height - 1, disp.getSystemColor(18), disp.getSystemColor(20));
        this.setStatusColor(gc);
        this.colorBarWidth = Math.min(rect.width - 2, this.colorBarWidth);
        gc.fillRectangle(1, 1, this.colorBarWidth, rect.height - 2);
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
        this.checkWidget();
        Point size = new Point(160, 18);
        if (wHint != -1) {
            size.x = wHint;
        }
        if (hHint != -1) {
            size.y = hHint;
        }
        return size;
    }

    public void step(int failures) {
        ++this.currentTickCount;
        int x = this.colorBarWidth;
        this.colorBarWidth = this.scale(this.currentTickCount);
        if (!(this.error || failures <= 0)) {
            this.error = true;
            x = 1;
        }
        if (this.currentTickCount == this.maxTickCount) {
            this.colorBarWidth = this.getClientArea().width - 1;
        }
        this.paintStep(x, this.colorBarWidth);
    }

    public void refresh(boolean hasErrors) {
        this.error = hasErrors;
        this.redraw();
    }

    static /* synthetic */ void access$2(GreenPepperProgressBar greenPepperProgressBar, int n) {
        greenPepperProgressBar.colorBarWidth = n;
    }

}

