package com.greenpepper.confluence.rpc.xmlrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.atlassian.confluence.content.render.xhtml.DefaultConversionContext;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.rpc.InvalidSessionException;
import com.atlassian.confluence.rpc.NotPermittedException;
import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.security.SpacePermission;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.util.HtmlEntityEscapeUtil;
import com.atlassian.user.User;
import com.greenpepper.confluence.utils.stylesheet.StyleSheetExtractorFactory;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.report.XmlReport;
import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.DocumentNode;
import com.greenpepper.server.rpc.GreenPepperRpcHelper;
import com.greenpepper.util.StringUtil;

public class ConfluenceXmlRpcGreenPepperServiceImpl implements GreenPepperRpcHelper
{
	public static final String SPACE_NOT_FOUND = "greenpepper.rpc.spacenotfound";
	public static final String PAGE_NOT_FOUND = "greenpepper.rpc.pagenotfound";
	public static final String INVALID_SESSION = "greenpepper.rpc.invalidsession";
	public static final String PERMISSION_DENIED = "greenpepper.rpc.permissiondenied";
	public static final String GENERAL_EXCEPTION = "greenpepper.server.generalexeerror";

	private final Logger log = Logger.getLogger(ConfluenceXmlRpcGreenPepperServiceImpl.class);

	private ConfluenceGreenPepper gpUtil = new ConfluenceGreenPepper();

	public String getRenderedSpecification(final String username, final String password, final Vector<?> args)
    {
    	if(args.size() < 3) return error("Parameters Missing, expecting:[SpaceKey, PageTitle] !");
    	final boolean implementedVersion = args.size() < 4 || (Boolean)args.get(3);

        TransactionTemplate txTemplate = new TransactionTemplate(gpUtil.getPlatformTransactionManager());
        return (String)txTemplate.execute(new TransactionCallback()
        {
            public Object doInTransaction(TransactionStatus transactionStatus)
            {
				String token = null;

            	try
            	{
            		token = login(username, password);

					Page page = gpUtil.getPageManager().getPage((String)args.get(0), (String)args.get(1));
					if (page == null) return error(PAGE_NOT_FOUND);

					checkPermissions(page.getSpace(), token);
					return getRenderedSpecification(page, implementedVersion, (Boolean)args.get(2));
				}
            	catch (NotPermittedException e)
            	{
            		return error(PERMISSION_DENIED);
				}
            	catch (RemoteException e)
            	{
            		return error(INVALID_SESSION);
				}
            	finally
            	{
            		logout(token);
            	}
            }
        });
    }

    public Vector getSpecificationHierarchy(final String username, final String password, final Vector<?> args)
    {
    	if(args.isEmpty()) return new DocumentNode("Parameters Missing, expecting:[SpaceKey] !").marshallize();

        TransactionTemplate txTemplate = new TransactionTemplate(gpUtil.getPlatformTransactionManager());
        return (Vector)txTemplate.execute(new TransactionCallback()
        {
            public Object doInTransaction(TransactionStatus transactionStatus)
            {
				String token = null;
				
            	try
            	{
    				token = login(username, password);
                    Space space = gpUtil.getSpaceManager().getSpace((String)args.get(0));
					if (space == null) return new DocumentNode(gpUtil.getText(SPACE_NOT_FOUND)).marshallize();

					checkPermissions(space, token);
					return getSpecificationHierarchy(space);
				}
            	catch (NotPermittedException e)
            	{
            		return new DocumentNode(gpUtil.getText(PERMISSION_DENIED)).marshallize();
				}
            	catch (RemoteException e)
            	{
            		return new DocumentNode(gpUtil.getText(INVALID_SESSION)).marshallize();
				}
            	finally
            	{
            		logout(token);
            	}
            }
        });
    }
    
    public String setSpecificationAsImplemented(final String username, final String password, final Vector<?> args)
    {
    	if(args.size() < 3) return error("Parameters Missing, expecting:[SpaceKey, PageTitle] !");

        TransactionTemplate txTemplate = new TransactionTemplate(gpUtil.getPlatformTransactionManager());
        return (String)txTemplate.execute(new TransactionCallback()
        {
            public Object doInTransaction(TransactionStatus transactionStatus)
            {
        		String token = null;
        		
            	try
            	{
    				token = login(username, password);
					Page page = gpUtil.getPageManager().getPage((String)args.get(0), (String)args.get(1));
					if (page == null) return error(PAGE_NOT_FOUND);

					checkPermissions(page.getSpace(), token);
					gpUtil.saveImplementedVersion(page, page.getVersion());
					return GreenPepperServerErrorKey.SUCCESS;
				}
            	catch (NotPermittedException e)
            	{
            		return error(PERMISSION_DENIED);
				}
            	catch (RemoteException e)
            	{
            		return error(INVALID_SESSION);
				}
            	finally
            	{
            		logout(token);
            	}
            }
        });
    }

	public String saveExecutionResult(final String username, final String password, final Vector<?> args)
	{
		if(args.size() < 4) return error("Parameters Missing, expecting:[SpaceKey, PageTitle, SUT, Xml Report Data] !");

		TransactionTemplate txTemplate = new TransactionTemplate(gpUtil.getPlatformTransactionManager());
		return (String)txTemplate.execute(new TransactionCallback()
		{
			public Object doInTransaction(TransactionStatus transactionStatus)
			{
				String token = null;

				try
				{
					token = login(username, password);

					Page page = gpUtil.getPageManager().getPage((String)args.get(0), (String)args.get(1));
					if (page == null) return error(PAGE_NOT_FOUND);

					checkPermissions(page.getSpace(), token);

					gpUtil.saveExecutionResult(page, (String)args.get(2), XmlReport.parse((String)args.get(3)));

					return GreenPepperServerErrorKey.SUCCESS;
				}
				catch (NotPermittedException e)
				{
					return error(PERMISSION_DENIED);
				}
				catch (RemoteException e)
				{
					return error(INVALID_SESSION);
				}
				catch (GreenPepperServerException e)
				{
					log.error(gpUtil.getText(e.getId()), e);
					return error(e.getId());
				}
				catch (Exception e)
				{
					log.error(gpUtil.getText(GENERAL_EXCEPTION), e);
					return error(GENERAL_EXCEPTION);
				}
				finally
				{
					logout(token);
				}
			}
		});
	}

	private void checkPermissions(Space space, String token) throws InvalidSessionException, NotPermittedException
    {
    	User user = getUser(token);
        List<String> permTypes = new ArrayList<String>();
        permTypes.add(SpacePermission.VIEWSPACE_PERMISSION);
        if(!gpUtil.getSpacePermissionManager().hasPermissionForSpace(user, permTypes, space))
    	{
        	throw new NotPermittedException();
    	}
    }

    private User getUser(String token) throws InvalidSessionException, NotPermittedException
    {
    	if(StringUtil.isEmpty(token)) return gpUtil.getTokenAuthenticationManager().makeAnonymousUser();
		return gpUtil.getTokenAuthenticationManager().makeNonAnonymousUserFromToken(token);
    }

    private String login(String username, String password) throws RemoteException
    {
    	if(StringUtil.isEmpty(username)) return "";
    	String token = gpUtil.getTokenAuthenticationManager().login(username, password);
		AuthenticatedUserThreadLocal.setUser(getUser(token));
		return token;
    }

    private void logout(String token)
    {
    	try { if(StringUtil.isEmpty(token)) gpUtil.getTokenAuthenticationManager().logout(token);}
    	catch (Exception e) { /* Do Nothing */}
		AuthenticatedUserThreadLocal.setUser(null);
    }

    private String getRenderedSpecification(Page page, boolean implementedVersion, boolean includeStyle)
    {
    	try 
    	{
			String baseUrl = gpUtil.getBaseUrl();

			StringBuffer basicRenderedPage = new StringBuffer("<html>\n");
			basicRenderedPage.append("<head>\n<title>").append(page.getTitle()).append("</title>\n");
			basicRenderedPage.append("<meta http-equiv=\"content-type\" content=\"text/html;charset=")
					.append(gpUtil.getEncoding()).append("\"/>\n");
			basicRenderedPage.append("<meta name=\"title\" content=\"").append(page.getTitle()).append("\"/>\n");
			basicRenderedPage.append("<meta name=\"external-link\" content=\"").append(baseUrl).append(page.getUrlPath()).append("\"/>\n");
        	
    	    if(includeStyle)
    	    {
    	        basicRenderedPage.append("<style>\n");
				basicRenderedPage.append(StyleSheetExtractorFactory.getInstance().renderStyleSheet(page.getSpace()));
				basicRenderedPage.append("</style>\n");
    	        basicRenderedPage.append("<base href=\"").append(baseUrl).append("\"/>\n");
			}

			basicRenderedPage.append("</head>\n<body>\n");

			if(includeStyle)
			{
				basicRenderedPage.append("<div id=\"Content\" style=\"text-align:left; padding: 5px;\">\n");
			}
			
			
			String content = gpUtil.getPageContent(page, implementedVersion);
    		if (content == null) throw new GreenPepperServerException();
    	    
    	    // To prevent loops caused by these macro rendering
    	    content = content.replaceAll("greenpepper-manage", "greenpepper-manage-not-rendered");
    	    content = content.replaceAll("greenpepper-hierarchy", "greenpepper-hierarchy-not-rendered");
    	    content = content.replaceAll("greenpepper-children", "greenpepper-children-not-rendered");
    	    content = content.replaceAll("greenpepper-labels", "greenpepper-labels-not-rendered");
    	    content = content.replaceAll("greenpepper-group", "greenpepper-group-not-rendered");

			// This macro breaks the labels/children macro with Javascript error "treeRequests not defined" 
			// http://www.greenpeppersoftware.com/jira/browse/GP-747
			content = content.replaceAll("\\{pagetree", "{pagetree-not-rendered");

//    	    basicRenderedPage.append(gpUtil.getWikiStyleRenderer().convertWikiToXHtml(page.toPageContext(), content));
    	    basicRenderedPage.append(gpUtil.getViewRenderer().render(content, new DefaultConversionContext(page.toPageContext())));
    	    
    	    if (includeStyle)
			{
				basicRenderedPage.append("\n</div>");
			}
    	    
			basicRenderedPage.append("\n</body>\n</html>");
    	    
    	    HtmlEntityEscapeUtil.unEscapeHtmlEntities(basicRenderedPage);

			return basicRenderedPage.toString();
    	}
    	catch(GreenPepperServerException e)
    	{
    		return e.getId().equals(ConfluenceGreenPepper.NEVER_IMPLEMENTED) ? warning(e.getId()) : error(e.getId());
    	}
    }

    @SuppressWarnings("unchecked")
    private Vector getSpecificationHierarchy(Space space)
    {
    	DocumentNode hierarchy = new DocumentNode(space.getName());
        List<Page> pages = gpUtil.getPageManager().getPages(space, true);
        for (Page page : pages)
        {
            if (page.isRootLevel())
            {
                DocumentNode node = buildNodeHierarchy(page, gpUtil);
                hierarchy.addChildren(node);
            }
        }

        return hierarchy.marshallize();
    }

    @SuppressWarnings("unchecked")
    private DocumentNode buildNodeHierarchy(Page page, ConfluenceGreenPepper gpUtil)
    {
        DocumentNode node = new DocumentNode(page.getTitle());
        node.setCanBeImplemented(gpUtil.canBeImplemented(page));

        List<Page> children = page.getChildren();
        for (Page child : children)
            node.addChildren(buildNodeHierarchy(child, gpUtil));

        return  node;
    }

    private String error(String errorId)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<html>");
    	sb.append("  <table style=\"text-align:center; border:1px solid #cc0000; border-spacing:0px; background-color:#ffcccc; padding:0px; margin:5px; width:100%;\">");
    	sb.append("    <tr style=\"display:none\"><td>Comment</td></tr>");
    	sb.append("    <tr><td id=\"conf_actionError_Msg\" style=\"color:#cc0000; font-size:12px; font-family:Arial, sans-serif; text-align:center; font-weight:bold;\">").append(gpUtil.getText(errorId)).append("</td></tr>");
    	sb.append("  </table>");
    	sb.append("</html>");

    	return sb.toString();
    }

    private String warning(String errorId)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<html>");
    	sb.append("  <table style=\"text-align:center; border:1px solid #FFD700; border-spacing:0px; background-color:#FFFF66; padding:0px; margin:5px; width:100%;\">");
    	sb.append("    <tr style=\"display:none\"><td>Comment</td></tr>");
    	sb.append("    <tr><td id=\"conf_actionWarn_Msg\" style=\"font-size:12px; font-family:Arial, sans-serif; text-align:center; font-weight:bold;\">").append(gpUtil.getText(errorId)).append("</td></tr>");
    	sb.append("  </table>");
    	sb.append("</html>");

    	return sb.toString();
    }
}