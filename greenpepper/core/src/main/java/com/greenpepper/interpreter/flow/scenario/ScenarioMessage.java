
/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 *
 * @author oaouattara
 * @version $Id: $Id
 */
package com.greenpepper.interpreter.flow.scenario;

import com.greenpepper.TypeConversion;
import com.greenpepper.call.ResultMatcher;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.SystemUnderDevelopmentException;
import com.greenpepper.util.DuckType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ScenarioMessage extends Message
{
    private final Object target;
    private Method method;
    private Annotation annotation;
    private MatchResult matchResult;

    private Object[] arguments;

    /**
     * <p>Constructor for ScenarioMessage.</p>
     *
     * @param target a {@link java.lang.Object} object.
     * @param example a {@link java.lang.String} object.
     */
    public ScenarioMessage(Object target, String example)
    {
        this.target = target;

        init( example );
    }

    /**
     * <p>getArity.</p>
     *
     * @return a int.
     */
    public int getArity()
    {
        return 0; //not used
    }

    /**
     * <p>send.</p>
     *
     * @param args a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Exception if any.
     */
    public Object send(String... args) throws Exception
    {

        arguments = buildArguments();

        try
        {
            return method.invoke( target, arguments );
        }
        catch (InvocationTargetException ex)
        {
            throw new SystemUnderDevelopmentException( ex.getTargetException() );
        }
        catch (Exception ex)
        {
            throw new SystemUnderDevelopmentException( ex );
        }
    }

    /**
     * <p>annotationIs.</p>
     *
     * @param annotationType a {@link java.lang.Class} object.
     * @return a {@link com.greenpepper.call.ResultMatcher} object.
     */
    public ResultMatcher annotationIs(Class<? extends Annotation> annotationType)
    {
        return new AnnotationResultMatcher( annotation, annotationType );
    }

    /**
     * <p>arguments.</p>
     *
     * @return an array of {@link java.lang.Object} objects.
     */
    public Object[] arguments()
    {
        return arguments;
    }

    /**
     * <p>matchResult.</p>
     *
     * @return a {@link java.util.regex.MatchResult} object.
     */
    public MatchResult matchResult()
    {
        return matchResult;
    }

    /**
     * <p>getIgnoredExceptions.</p>
     *
     * @return an array of {@link java.lang.Class} objects.
     */
    public Class<? extends Throwable>[] getIgnoredExceptions()
    {
        IgnoredException ignoredException = method.getAnnotation( IgnoredException.class );
        return ignoredException == null ? null : ignoredException.value();
    }

    private void init(String example)
    {

        Method[] methods = target.getClass().getMethods();

        for (Method aMethod : methods)
        {
            if (match( aMethod, example ))
            {
                return;
            }
        }

        if (method == null)
        {
            throw new IllegalArgumentException(
                    String.format( "Cannot find method matching '%s' in class '%s'", example, target.getClass() ) );
        }
    }

    private boolean match(Method method, String scenario)
    {

        for (Annotation anAnnotation : method.getAnnotations())
        {
            String regex = regularExpressionOf( anAnnotation );

            if (regex != null)
            {
                MatchResult match = match( scenario, regex );

                if (match != null)
                {
                    this.method = method;
                    this.annotation = anAnnotation;
                    this.matchResult = match;
                    return true;
                }
            }
        }

        return false;
    }

    private String regularExpressionOf(Annotation annotation)
    {

        if (DuckType.instanceOf( RegEx.class, annotation ))
        {
            try
            {
                RegEx regEx = DuckType.implement( RegEx.class, annotation );
                return regEx.value();
            }
            catch (Exception e)
            {
                // Not a valid RegEx annotation
            }
        }

        return null;
    }

    private MatchResult match(String scenario, String regex)
    {

        Pattern pattern = Pattern.compile( regex );
        Matcher match = pattern.matcher( scenario );

        return match.find() ? match.toMatchResult() : null;
    }

    private Object[] buildArguments()
    {

        String[] args = new String[matchResult.groupCount()];

        for (int i = 0; i < matchResult.groupCount(); i++)
        {
            args[i] = matchResult.group( i + 1 );
        }

        if (method.getParameterTypes().length != args.length)
        {
            throw new IllegalArgumentException(
                    String.format( "Wrong number of arguments: expected %d but got %d",
                            method.getParameterTypes().length, args.length ) );
        }

        return TypeConversion.convert( args, method.getParameterTypes() );
    }
}
