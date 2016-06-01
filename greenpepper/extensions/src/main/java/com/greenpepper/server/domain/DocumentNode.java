package com.greenpepper.server.domain;

import com.google.common.collect.TreeTraverser;

import java.util.*;


/**
 * <p>DocumentNode class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class DocumentNode implements Comparable, Marshalizable
{

    /**
     * Allows retrieval of an Iterator using DocumentNode hierarchy.
     *
     * @see TreeTraverser#preOrderTraversal(Object)
     * @see TreeTraverser#postOrderTraversal(Object)
     */
    public static final TreeTraverser<DocumentNode>  traverser = new TreeTraverser<DocumentNode>() {
        @Override
        public Iterable<DocumentNode> children(DocumentNode root) {
            return root.getChildren();
        }
    };

    /** Constant <code>NODE_TITLE_INDEX=0</code> */
    private final static int NODE_TITLE_INDEX = 0;
    /** Constant <code>NODE_EXECUTABLE_INDEX=1</code> */
    private final static int NODE_EXECUTABLE_INDEX = 1;
    /** Constant <code>NODE_CAN_BE_IMPLEMENTED_INDEX=2</code> */
    private final static int NODE_CAN_BE_IMPLEMENTED_INDEX = 2;
    /** Constant <code>NODE_CHILDREN_INDEX=3</code> */
    private final static int NODE_CHILDREN_INDEX = 3;
    /** Constant <code>NODE_REPOSITORY_UID_INDEX=4</code> */
    final static int NODE_REPOSITORY_UID_INDEX = 4;
    /** Constant <code>NODE_SUT_NAME_INDEX=5</code> */
    final static int NODE_SUT_NAME_INDEX = 5;
    /** Constant <code>NODE_SECTION_INDEX=6</code> */
    final static int NODE_SECTION_INDEX = 6;

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

    /**
     * Rebuilds a DocumentNode based on the given vector.
     * </p>
     *
     * @param documentNodeParams a {@link Vector} object.
     * @return a DocumentNode based on the given vector.
     */
    public static DocumentNode toDocumentNode(List<Object> documentNodeParams)
    {
        DocumentNode node = new DocumentNode((String) documentNodeParams.get(NODE_TITLE_INDEX));
        node.setIsExecutable((Boolean) documentNodeParams.get(NODE_EXECUTABLE_INDEX));
        node.setCanBeImplemented((Boolean) documentNodeParams.get(NODE_CAN_BE_IMPLEMENTED_INDEX));

        Hashtable children = (Hashtable) documentNodeParams.get(NODE_CHILDREN_INDEX);
        Collection<Vector<Object>> values = children.values();
        for (Vector<Object> nodeParams : values) {

            if(nodeParams.size() > 4)
            {
                node.addChildren(toReferenceNode(nodeParams));
            }
            else
            {
                node.addChildren(toDocumentNode(nodeParams));
            }
        }

        return node;
    }

    private static ReferenceNode toReferenceNode(List<Object> referenceNodeParams)
    {
        ReferenceNode node = new ReferenceNode((String) referenceNodeParams.get(NODE_TITLE_INDEX),
                (String) referenceNodeParams.get(NODE_REPOSITORY_UID_INDEX),
                (String) referenceNodeParams.get(NODE_SUT_NAME_INDEX),
                (String) referenceNodeParams.get(NODE_SECTION_INDEX));

        node.setIsExecutable((Boolean) referenceNodeParams.get(NODE_EXECUTABLE_INDEX));
        node.setCanBeImplemented((Boolean) referenceNodeParams.get(NODE_CAN_BE_IMPLEMENTED_INDEX));

        return node;
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
