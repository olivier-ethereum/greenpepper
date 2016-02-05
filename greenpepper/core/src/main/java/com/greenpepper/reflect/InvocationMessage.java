package com.greenpepper.reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>InvocationMessage class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class InvocationMessage extends Message
{
    private final List<Message> messages = new ArrayList<Message>();

    /**
     * <p>addMessage.</p>
     *
     * @param message a {@link com.greenpepper.reflect.Message} object.
     */
    public void addMessage(Message message)
    {
        messages.add(message);
    }

    /** {@inheritDoc} */
    @Override
    public int getArity()
    {
        if (messages.isEmpty()) return 0;

        return messages.get(0).getArity();
    }

    /**
     * <p>isEmpty.</p>
     *
     * @return a boolean.
     */
    public boolean isEmpty()
    {
        return messages.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public Object send(String ...args) throws Exception
    {
        for(Message message : messages)
        {
            if (message.getArity() == args.length)
            {
               return message.send(args);   
            }
        }
        throw new IllegalArgumentException( String.format("No such method with %d arguments", args.length) );
    }

}
