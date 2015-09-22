package com.greenpepper.confluence.listeners;

import org.apache.log4j.Logger;

import com.atlassian.confluence.event.events.content.page.PageEvent;
import com.atlassian.confluence.event.events.content.page.PageRemoveEvent;
import com.atlassian.confluence.event.events.content.page.PageTrashedEvent;
import com.atlassian.confluence.event.events.content.page.PageUpdateEvent;
import com.atlassian.confluence.event.events.space.SpaceRemoveEvent;
import com.atlassian.confluence.pages.AbstractPage;
import com.atlassian.confluence.pages.Page;
import com.atlassian.event.Event;
import com.atlassian.event.EventListener;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.Specification;

public class GreenPepperPageListener implements EventListener
{
    private static Logger log = Logger.getLogger(GreenPepperPageListener.class);

	private final ConfluenceGreenPepper gp = new ConfluenceGreenPepper();

	public void handleEvent(Event evt)
	{
        try
        {
            if (evt instanceof PageUpdateEvent)
            {
				updateSpecification(evt);
            }
			else if (evt instanceof SpaceRemoveEvent)
			{
				removeRepository(evt);
			}
			else if(evt instanceof PageRemoveEvent || evt instanceof PageTrashedEvent)
			{
				removeSpecification(evt);
			}
		}
        catch (Exception e)
        {
            log.warn(e);
        }
    }

	private void removeSpecification(Event evt) throws GreenPepperServerException 
	{
		PageEvent pageEvt = (PageEvent) evt;
		Page page = pageEvt.getPage();

		Specification specification = Specification.newInstance(page.getTitle());
		specification.setRepository(gp.getHomeRepository(page.getSpaceKey()));

		gp.getGPServerService().removeSpecification(specification);

		gp.saveExecuteChildren(page, null);
		gp.saveImplementedVersion(page, null);
		gp.savePreviousImplementedVersion(page, null);
	}

	private void removeRepository(Event evt) throws GreenPepperServerException
	{
		SpaceRemoveEvent spaceEvt = (SpaceRemoveEvent)evt;

		gp.getGPServerService().removeRepository(gp.getHomeRepository(spaceEvt.getSpace().getKey()).getUid());
	}

	private void updateSpecification(Event evt) throws GreenPepperServerException
	{
		PageUpdateEvent pageEvt = (PageUpdateEvent) evt;
		AbstractPage oldPage = pageEvt.getOriginalPage();
		Page newPage = pageEvt.getPage();

		if(newPage != null && oldPage != null && !newPage.getTitle().equals(oldPage.getTitle()))
		{
			Specification oldSpecification = Specification.newInstance(oldPage.getTitle());
			oldSpecification.setRepository(gp.getHomeRepository(newPage.getSpace().getKey()));
			try
			{
				Specification newSpecification = Specification.newInstance(newPage.getTitle());
				newSpecification.setRepository(gp.getHomeRepository(newPage.getSpace().getKey()));
				gp.getGPServerService().updateSpecification(oldSpecification, newSpecification);
			}
			catch (GreenPepperServerException e)
			{
				gp.getGPServerService().removeSpecification(oldSpecification);
			}
		}
	}

	public Class[] getHandledEventClasses()
    {
        return new Class[] { PageUpdateEvent.class, PageTrashedEvent.class, PageRemoveEvent.class, SpaceRemoveEvent.class};
    }
}
