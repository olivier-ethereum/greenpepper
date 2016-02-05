package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_TITLE_INDEX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_EXECUTABLE_INDEX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_CAN_BE_IMPLEMENTED_INDEX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.NODE_CHILDREN_INDEX;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;


/**
 * <p>DocumentNode class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class DocumentNode implements Comparable, Marshalizable
{
    private String title;
    private boolean executable;
    private boolean canBeImplemented;

    private List<DocumentNode> children = new ArrayList<DocumentNode>();

    /**
     * <p>Constructor for DocumentNode.</p>
     *
     * @param title a {@link java.lang.String} object.
     */
    public DocumentNode(String title)
    {
        this.title = title;
    }

    /**
     * <p>Getter for the field <code>children</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<DocumentNode> getChildren()
    {
        return children;
    }

    /**
     * <p>Getter for the field <code>title</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * <p>isExecutable.</p>
     *
     * @return a boolean.
     */
    public boolean isExecutable()
    {
        return executable;
    }

    /**
     * <p>setIsExecutable.</p>
     *
     * @param executable a boolean.
     */
    public void setIsExecutable(boolean executable)
    {
        this.executable =  executable;
    }

    /**
     * <p>canBeImplemented.</p>
     *
     * @return a boolean.
     */
    public boolean canBeImplemented()
    {
        return canBeImplemented;
    }

    /**
     * <p>Setter for the field <code>canBeImplemented</code>.</p>
     *
     * @param canBeImplemented a boolean.
     */
    public void setCanBeImplemented(boolean canBeImplemented)
    {
        this.canBeImplemented =  canBeImplemented;
    }

    /**
     * <p>addChildren.</p>
     *
     * @param child a {@link com.greenpepper.server.domain.DocumentNode} object.
     */
    public void addChildren(DocumentNode child)
    {
        children.add(child);
    }

    /**
     * <p>hasChildren.</p>
     *
     * @return a boolean.
     */
    public boolean hasChildren()
    {
        return children.size() > 0;
    }

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> vector = new Vector<Object>();
        vector.add(NODE_TITLE_INDEX, title);
        vector.add(NODE_EXECUTABLE_INDEX, executable);
        vector.add(NODE_CAN_BE_IMPLEMENTED_INDEX, canBeImplemented);

        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        for (DocumentNode node : children)
            hashtable.put(node.getTitle(), node.marshallize());

        vector.add(NODE_CHILDREN_INDEX, hashtable);

        return vector;
    }

    /** {@inheritDoc} */
    public int compareTo(Object node)
    {
        return title.compareTo(((DocumentNode)node).getTitle());
    }
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof DocumentNode))
        {
            return false;
        }

        DocumentNode nodeCompared = (DocumentNode)o;
        if(getTitle().equals(nodeCompared.getTitle()))
        {
            return true;
        }

        return false;
    }

    /**
     * <p>hashCode.</p>
     *
     * @return a int.
     */
    public int hashCode()
    {
        return getTitle().hashCode();
    }
}
