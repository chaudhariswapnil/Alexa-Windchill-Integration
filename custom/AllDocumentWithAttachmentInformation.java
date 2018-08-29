/**
 * 
 */
package com.custom;

import java.beans.PropertyVetoException;
import java.lang.ExceptionInInitializerError;
import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.meta.common.DisplayOperationIdentifier;
import com.ptc.windchill.enterprise.change2.commands.RelatedChangesQueryCommands;
import com.ptc.windchill.enterprise.requirement.util.RequirementsVersionHelper;

import wt.change2.ChangeHelper2;
import wt.change2.Changeable2;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.meeting.actionitem.DiscreteActionItem;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipalReference;
import wt.pds.StatementSpec;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.vc.VersionControlHelper;
import wt.vc.Versioned;
import wt.inf.team.ContainerTeamManaged;

/**
 * @author Shirish Morkhade
 *
 */
public class AllDocumentWithAttachmentInformation implements RemoteAccess {

	/**
	 * @param args
	 * @throws WTException 
	 * @throws IOException 
	 * @throws PropertyVetoException 
	 * @throws InvocationTargetException 
	 */
	public static void main(String[] args) throws WTException, PropertyVetoException, IOException, InvocationTargetException {

		// we have set the Username and password for Method Server so it will not ask for credentials again and again.  
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");

		rms.invoke("callMethod", "com.custom.AllDocumentWithAttachmentInformation", null, null, null);
	}
	
	public static void callMethod() throws WTException, UnsupportedEncodingException{
		//	com.custom.ActivityHelper.getUserRelatedTask("wcadmin");
		//com.custom.ActivityHelper.getCurrentSystemStatus();
		JSONObject JSONObject = ActivityHelper.searchObject("*A000*", "WTPart");
		System.out.println(JSONObject);
	}
	
	
	
	public static void updateActivity() throws WTException{
		String obid = "OR:wt.meeting.actionitem.DiscreteActionItem:2751250";
		try {
			Persistable object = ActivityHelper.getObjectByUfid(obid);
			if(object instanceof DiscreteActionItem){
				DiscreteActionItem actionItem = (DiscreteActionItem)object;
				//System.out.println(actionItem.getItemName());
				/*PersistableAdapter obj = new PersistableAdapter(actionItem, null, SessionHelper.getLocale(), new DisplayOperationIdentifier());
				obj.load("itemName");
				System.out.println(obj.get("itemName"));
				obj.set("itemName", "newitemName");
				//obj.set("comments"	, "commentssasas");
				obj.persist();*/
				List<WTPrincipalReference> teamList = wt.inf.team.ContainerTeamHelper.service.getContainerTeam( (ContainerTeamManaged)actionItem.getContainer()).getMembers();
				for(WTPrincipalReference team : teamList){
					if(team.getObject() instanceof wt.org.WTGroup) {
						wt.org.WTGroup group = (wt.org.WTGroup)team.getObject();
						
					}
					System.out.println("Name :"+team.getDisplayName());
				}
				teamList.add(SessionHelper.manager.getPrincipalReference());
				System.out.println("Final Team : "+teamList);
				//System.out.println(teamList);
			}


		} catch (WTException e) {
			e.printStackTrace();
		}

	}
	public static void docInfo() throws WTException, PropertyVetoException, IOException, IllegalAccessException, JSONException{
		ActivityController.getActionItems("OR:wt.workflow.work.WorkItem:1920922:047709483-1490604181239-2035806106-69-32-220-10@DIN56002302.corp.capgemini.com");
			String currfileName = null;
		WTDocument doc = null;

		QuerySpec qs = new QuerySpec(WTDocument.class);

		final SearchCondition searchCondition1 = new SearchCondition(WTDocument.class, WTDocument.NUMBER, SearchCondition.EQUAL,"A0000A1050", false);
		qs.appendWhere(searchCondition1, new int[] { 0 });

		QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);

		System.out.println("Total Number of Objects retrieved -> "+ qr.size());
		while (qr.hasMoreElements()) {
			doc = (WTDocument)qr.nextElement();
			Object authReleasedInfo = com.ptc.windchill.enterprise.history.timeline.TimelineHistoryHelper.getAuthReleasedInfo(doc);
			System.out.println("authReleasedInfo ...:"+authReleasedInfo);
			String number = doc.getNumber();
			System.out.println("Doc Identifier : "+doc.getDisplayIdentifier());
			QueryResult qr1 = ContentHelper.service.getContentsByRole(doc,ContentRoleType.PRIMARY);
			while (qr1.hasMoreElements()) {
				ApplicationData appData = (ApplicationData) qr1.nextElement();
				if (appData != null) {
					String fileName = appData.getFileName();
					appData.setDistributable(true);
				}
				 ContentHelper.service.updateAppData( ContentHelper.service.getContents((ContentHolder) doc), appData);
			}
		}
	}
	
	public static WTDocument getlatestVersionOfDocument(String documentNumber) throws WTException{
		WTDocument document = null;
		QuerySpec qs = new QuerySpec(WTDocument.class);
		final SearchCondition searchCondition1 = new SearchCondition(WTDocument.class, WTDocument.NUMBER, SearchCondition.EQUAL,documentNumber, false);
		qs.appendWhere(searchCondition1, new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
		
		if(qr.hasMoreElements()) {
			document = (WTDocument)RequirementsVersionHelper.getLatestVersion((WTDocument)qr.nextElement());
		}
		return document;
		
	}
}
