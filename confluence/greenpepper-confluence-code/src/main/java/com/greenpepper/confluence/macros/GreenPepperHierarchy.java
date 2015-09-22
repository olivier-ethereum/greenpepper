package com.greenpepper.confluence.macros;

import java.util.Map;

import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.MacroException;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.util.StringUtil;

public class GreenPepperHierarchy extends AbstractHttpRetrievalMacro
{
	@Override
	@SuppressWarnings("unchecked")
	public String execute(Map parameters, String body, RenderContext context) throws MacroException 
	{
		try {
			validateParams(parameters);
			String gpUrl = getParameter(parameters, "gpUrl");
			String gpService = getParameter(parameters, "gpService");
			String jiraUid = getParameter(parameters, "jiraUid");

			Repository repo = getRepository(gpUrl, gpService, jiraUid);
			String os_username = getParameter(parameters, "user");
			String os_password = getParameter(parameters, "pwd");

			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(repo.getBaseRepositoryUrl())
				  .append("/secure/VersionHierarchy.jspa?decorator=none&projectName=")
				  .append(repo.getProject().getName())
				  .append("&versionName=")
				  .append((String)parameters.get("versionName"))
				  .append("&themeName=GreenPepper")
				  .append("&linkTypeName=")
				  .append(getParameter(parameters, "linkName"));

			if(os_username != null && os_password != null)
			{
				urlBuilder.append("&os_username=")
						  .append(os_username)
						  .append("&os_password=")
						  .append(os_password);
			}

			parameters.put("url", urlBuilder.toString());

			return super.execute(parameters, body, context);
		}
		catch (GreenPepperServerException e) {
			return AbstractGreenPepperMacro.getErrorView("greenpepper.hierarchy.macroid", e.getId());
		}
	}

    public String getName()
    {
        return "greenpepper-hierarchy";
    }

    private void validateParams(Map parameters) throws GreenPepperServerException
    {
        if(StringUtil.isBlank((String)parameters.get("gpUrl"))){throw new GreenPepperServerException("greenpepper.hierarchy.missingurl", "");}
        if(StringUtil.isBlank((String)parameters.get("gpService"))){throw new GreenPepperServerException("greenpepper.hierarchy.missingservice", "");}
        if(StringUtil.isBlank((String)parameters.get("versionName"))){throw new GreenPepperServerException("greenpepper.hierarchy.missingversion", "");}
        if(StringUtil.isBlank((String)parameters.get("linkName"))){throw new GreenPepperServerException("greenpepper.hierarchy.missinglink", "");}
        if(StringUtil.isBlank((String)parameters.get("jiraUid"))){throw new GreenPepperServerException("greenpepper.hierarchy.missinguid", "");}
    }
}