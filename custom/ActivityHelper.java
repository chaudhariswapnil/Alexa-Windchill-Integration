package com.custom;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Enumeration;
import java.util.Iterator;

import wt.log4j.LogR;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infoengine.object.factory.Att;
import com.infoengine.object.factory.Element;
import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.meta.common.DisplayOperationIdentifier;
import com.ptc.core.rest.AbstractResource;
import com.ptc.windchill.enterprise.change2.commands.RelatedChangesQueryCommands;
import com.sun.xml.ws.rx.rm.runtime.sequence.persistent.PersistenceException;

import wt.adapter.BasicWebjectDelegate;
import wt.change2.ChangeOrder2;
import wt.change2.ChangeRequest2;
import wt.change2.Changeable2;
import wt.change2.WTChangeIssue;
import wt.change2.WTChangeOrder2;
import wt.change2.WTChangeRequest2;
import wt.doc.WTDocument;
import wt.enterprise.EnterpriseHelper;
import wt.epm.EPMDocument;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.meeting.actionitem.ActionItem;
import wt.meeting.actionitem.ActionItemHealthStatus;
import wt.meeting.actionitem.ActionItemPriority;
import wt.meeting.actionitem.ActionItemServiceHelper;
import wt.meeting.actionitem.ActionItemStatus;
import wt.meeting.actionitem.ActionItemSubjectLink;
import wt.meeting.actionitem.DiscreteActionItem;
import wt.org.DirectoryContextProvider;
import wt.org.OrganizationServicesHelper;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.org.WTGroup;
import wt.org.WTPrincipalReference;
import wt.ownership.Ownership;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.workflow.engine.WfActivity;
import wt.workflow.work.WorkItem;
import wt.workflow.work.WorkflowHelper;

import wt.mail.EMailMessage;
import wt.method.RemoteAccess;

public class ActivityHelper extends AbstractResource {
	private static final Logger LOGGER = LogR.getLogger(ActivityHelper.class.getName());



	public static WTPrincipal getWTUserByName(String userName) throws WTException {
		try {

			boolean enforce = wt.session.SessionServerHelper.manager.setAccessEnforced(false);
			WTUser user = null;
			try{
				if (!StringUtils.isEmpty(userName)) {
					user = OrganizationServicesHelper.manager.getUser(userName);        
				}
			} catch (WTException e) {
			} finally {
				SessionServerHelper.manager.setAccessEnforced(enforce);
			}
			return user;
		}catch(Exception e){
			return SessionHelper.manager.getPrincipal();
		}
	}


	public static Persistable getObjectByUfid(String pboUfid) throws WTException{
		return BasicWebjectDelegate.getObjectByUfid(pboUfid);
	}


	public static void createActivity(org.json.JSONObject explrObject,Persistable pboObject) throws ParseException, JSONException{
		try{
			WTPrincipal activityOwner = null;
			String ownerName  = (String) explrObject.get("Owner");

			if(null!=ownerName || !ownerName.isEmpty()){
				activityOwner = ActivityHelper.getWTUserByName(ownerName);
			}else{
				activityOwner = SessionHelper.getPrincipal();
			}

			if(pboObject instanceof wt.doc.WTDocument){

				wt.doc.WTDocument pboDoc = (wt.doc.WTDocument)pboObject;
				Ownership activtyOwnerShip = Ownership.newOwnership(activityOwner);
				Date EstimatedStartDate    = new SimpleDateFormat("dd/MM/yyyy").parse((String) explrObject.get(ActivityConstant.ACTION_ESTIMATED_START_DATE)); 
				Date EstimatedEndDate    = new SimpleDateFormat("dd/MM/yyyy").parse((String) explrObject.get(ActivityConstant.ACTION_ESTIMATED_END_DATE)); 
				Timestamp eSDate = new Timestamp(EstimatedStartDate.getTime());
				Timestamp eEDate = new Timestamp(EstimatedEndDate.getTime());
				Timestamp dueDateTimestamp = new Timestamp(EstimatedEndDate.getTime());
				String itemName = (String) explrObject.get(ActivityConstant.ACTION_NAME);
				String itemNameDescription = null;
				try{
					itemNameDescription = (String) explrObject.get(ActivityConstant.ACTION_DESCRIPTION);
				}catch(NullPointerException e){
					itemNameDescription = "";
				}
				String status = (String) explrObject.get(ActivityConstant.ACTION_STATUS);
				String priority = (String) explrObject.get(ActivityConstant.ACTION_PRIORITY);

				//Creating Action Item with name , description , owner and due date
				DiscreteActionItem actionTask = DiscreteActionItem.newDiscreteActionItem(itemName, itemNameDescription, activityOwner, dueDateTimestamp);

				actionTask.setOwnership(activtyOwnerShip);
				actionTask.setContainer(pboDoc.getContainer());
				actionTask.setDomainRef(pboDoc.getDomainRef());
				actionTask.setEstimatedStart(eSDate);
				actionTask.setEstimatedFinish(eEDate);
				actionTask.setHealthStatus(ActionItemHealthStatus.GREEN);
				actionTask.setPriority(priority!=null ? ActionItemPriority.toActionItemPriority(priority) : ActionItemPriority.LOW);
				actionTask.setStatus(status!=null ? ActionItemStatus.toActionItemStatus(status) : ActionItemStatus.OPEN);
				String itemNumber = EnterpriseHelper.getNumber(new Object[]{"{GEN:wt.enterprise.SequenceGenerator:trwpartnumber:10:0}"});
				actionTask.setItemNumber(itemNumber);


				try{
					actionTask = (DiscreteActionItem)PersistenceHelper.manager.save(actionTask);
					//actionTask = (DiscreteActionItem)PersistenceHelper.manager.refresh(actionTask);

					ActionItemSubjectLink actionItemLink = ActionItemSubjectLink.newActionItemSubjectLink(actionTask, pboDoc);
					actionItemLink = (ActionItemSubjectLink)PersistenceHelper.manager.save(actionItemLink);

				}catch(PersistenceException e){
					LOGGER.error(e.getLocalizedMessage(),e);
				}
			}
		}catch (WTPropertyVetoException e1) {
			e1.printStackTrace();
		}catch(WTException e){
		}
	}

	public static ActionItem[] getActivitiesAssociatedToPbo(Persistable persistable){
		ActionItem[] result = null;
		if(persistable instanceof wt.doc.WTDocument){

			try {
				result = ActionItemServiceHelper.service.getActionItems((wt.doc.WTDocument)persistable);
			} catch (WTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(persistable instanceof wt.part.WTPart){
			try {
				result = ActionItemServiceHelper.service.getActionItems((wt.part.WTPart)persistable);
			} catch (WTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void updateActivity(org.json.JSONObject explrObject) throws WTException{

		try {
			String obid = (String)explrObject.get(ActivityConstant.ACTION_OBID);
			Persistable object = getObjectByUfid(obid);
			if(object instanceof DiscreteActionItem){
				DiscreteActionItem actionItem = (DiscreteActionItem)object;

				PersistableAdapter obj = new PersistableAdapter(object, null, SessionHelper.getLocale(), new DisplayOperationIdentifier());
				obj.set("location"	, "location");
				obj.persist();

				//actionItem.setActualStart(paramTimestamp);
				//actionItem.setResolutionDate(paramTimestamp);
				//	actionItem.getac
				//actionItem.setActualStart(paramTimestamp);
				//actionItem.setA(paramTimestamp);
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static org.json.JSONObject getUserRelatedTask(final String userName) throws JSONException{
		org.json.JSONObject jsonObject = new org.json.JSONObject();

		WTPrincipal principal= null;
		try{
			if(null!=userName){
				principal = SessionHelper.manager.setPrincipal(userName);
			}else{
				principal = SessionHelper.getPrincipal();
			}
		}catch(WTException e){
			LOGGER.error("User not found : "+userName,e);
			return jsonObject;
		}
		try{
			QueryResult uncomletedWorkItem = WorkflowHelper.service.getUncompletedWorkItems(principal);
			jsonObject.put("WTTaskCount", uncomletedWorkItem.size());
			jsonObject.put("WTDeadline", 0);
			jsonObject.put("WTChangeActivity2", new JSONArray());
			jsonObject.put("WTChangeIssue", new JSONArray());
			jsonObject.put("WTChangeOrder2", new JSONArray());
			jsonObject.put("WTChangeRequest2", new JSONArray());
			jsonObject.put("WTOther", new JSONArray());


			while(uncomletedWorkItem.hasMoreElements()){
				WorkItem workItem = (WorkItem)uncomletedWorkItem.nextElement();
				workItemInfo(workItem,jsonObject);
			}
		}catch(WTException e){

		}catch(ParseException e){

		}
		System.out.println(jsonObject);
		return jsonObject;
	}

	public static org.json.JSONObject searchObject(final String partNumber,final String type){
		org.json.JSONObject searchResultJson = new org.json.JSONObject();

		List<Persistable> objectList = null;
		if(com.custom.StringUtil.equals(type, "WTPart"))
			objectList = PublicUtil.getLatestPersistableByNumber(partNumber,wt.part.WTPart.class);
		else if(com.custom.StringUtil.equals(type, "WTDocument"))
			objectList = PublicUtil.getLatestPersistableByNumber(partNumber,wt.doc.WTDocument.class);
		else
			return searchResultJson;


		if(objectList.isEmpty()){
			return searchResultJson;
		}


		JSONArray jsonArray = null;

		try {

			searchResultJson.put("count", objectList.size());
			jsonArray = new JSONArray();
			for(Persistable persistable : objectList){

				org.json.JSONObject jsonObject = new org.json.JSONObject();

				if(persistable instanceof WTPart){

					WTPart part = (WTPart)persistable;
					jsonObject.put("name", part.getName());
					jsonObject.put("createdBy", part.getCreatorName());
					jsonObject.put("createdByDate", part.getCreateTimestamp());
					jsonObject.put("createdByDate", part.getState());
					jsonObject.put("associatedChanges", getAsscociatedChanges(part));

				}else if(persistable instanceof WTDocument){

					WTDocument doc = (WTDocument)persistable;
					jsonObject.put("name", doc.getName());
					jsonObject.put("createdBy", doc.getCreatorName());
					jsonObject.put("createdByDate", doc.getCreateTimestamp());
					jsonObject.put("createdByDate", doc.getState());
					jsonObject.put("associatedChanges", getAsscociatedChanges(doc));
				}

				jsonArray.put(jsonObject);
			}
			if(null!= jsonArray)
				searchResultJson.put("partList", jsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchResultJson;
	}

	public static org.json.JSONObject getSystemStatus(){
		try {
			com.custom.SystemStatusHelper.getCurrentSystemStatus();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static org.json.JSONObject getAsscociatedChanges(final Persistable persistable){
		QueryResult queryResult= null;
		org.json.JSONObject jsonObject = new org.json.JSONObject();
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray();
			queryResult = RelatedChangesQueryCommands.getRelatedAffectingChangeNotices((Changeable2)persistable);
			while(queryResult.hasMoreElements()){
				WTChangeOrder2 changeNotice = (WTChangeOrder2)queryResult.nextElement();
				org.json.JSONObject changeNoticeJson = new org.json.JSONObject();
				changeNoticeJson.put("name",changeNotice.getName());
				changeNoticeJson.put("number", changeNotice.getNumber());
				changeNoticeJson.put("state", changeNotice.getState());
				jsonArray.put(changeNoticeJson);
			}
			jsonObject.put("changeNotice",jsonArray);
			jsonObject.put("changeNoticeCount", queryResult.size());

		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			jsonArray =  new JSONArray();
			queryResult = RelatedChangesQueryCommands.getRelatedChangeRequests((Changeable2)persistable);
			while(queryResult.hasMoreElements()){
				WTChangeRequest2 changeRequest = (WTChangeRequest2)queryResult.nextElement();
				org.json.JSONObject changeRequestJson = new org.json.JSONObject();
				changeRequestJson.put("name",changeRequest.getName());
				changeRequestJson.put("number", changeRequest.getNumber());
				changeRequestJson.put("state", changeRequest.getState());
				jsonArray.put(changeRequestJson);
			}
			jsonObject.put("changeRequest",jsonArray);
			jsonObject.put("changeRequestCount", queryResult.size());

		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			jsonArray =  new JSONArray();
			queryResult = RelatedChangesQueryCommands.getRelatedProblemReports((Changeable2)persistable);
			while(queryResult.hasMoreElements()){
				WTChangeIssue problemReport = (WTChangeIssue)queryResult.nextElement();
				org.json.JSONObject probelmReportJson = new org.json.JSONObject();
				probelmReportJson.put("name",problemReport.getName());
				probelmReportJson.put("number", problemReport.getNumber());
				probelmReportJson.put("state", problemReport.getState());
				jsonArray.put(probelmReportJson);
			}
			jsonObject.put("problemReport",jsonArray);
			jsonObject.put("problemReportCount", queryResult.size());

		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

	private static JSONObject workItemInfo(final wt.workflow.work.WorkItem workitem, JSONObject jsonObject) throws ParseException{

		Persistable pbo = workitem.getPrimaryBusinessObject().getObject();
		WfActivity activity = (WfActivity)(workitem.getSource().getObject());
		JSONObject activityInfo = new JSONObject();
		int deadlineCount = jsonObject.optInt("WTDeadline");
		JSONArray activityArray = null;
		Timestamp systemTimestamp = new Timestamp(System.currentTimeMillis());
		try {
			activityInfo.put("name", activity.getName());

			//activityInfo.put("CreateTimeStamp", activity.getCreateTimestamp().parse("dd/MMM/yy")+"");
			activityInfo.put("Description", activity.getDescription());
			activityInfo.put("Priority", workitem.getPriority());
			activityInfo.put("Status", workitem.getStatus().toString());
			activityInfo.put("Role", workitem.getRole().toString());
			activityInfo.put("Owner", workitem.getOwnership().getOwner().getName());
			Timestamp deadline = activity.getDeadline();
			if(null!=deadline && systemTimestamp.getTime()>=deadline.getTime()){
				deadlineCount++;
			}
			activityInfo.put("Deadline", deadline+"");
			jsonObject.put("WTDeadline", deadlineCount);


			if(pbo instanceof wt.change2.WTChangeActivity2){
				activityArray = jsonObject.getJSONArray("WTChangeActivity2");
				activityArray.put(activityInfo);
				jsonObject.put("WTChangeActivity2",activityArray);

			}else if(pbo instanceof wt.change2.WTChangeIssue){
				activityArray = jsonObject.getJSONArray("WTChangeIssue");
				activityArray.put(activityInfo);
				jsonObject.put("WTChangeIssue",activityArray);

			}else if(pbo instanceof WTChangeOrder2){
				activityArray = jsonObject.getJSONArray("WTChangeOrder2");
				activityArray.put(activityInfo);
				jsonObject.put("WTChangeOrder2",activityArray);

			}else if(pbo instanceof WTChangeRequest2){
				activityArray = jsonObject.getJSONArray("WTChangeRequest2");
				activityArray.put(activityInfo);
				jsonObject.put("WTChangeRequest2",activityArray);
			}else{
				activityArray = jsonObject.getJSONArray("WTOther");
				activityArray.put(activityInfo);
				jsonObject.put("WTOther", activityArray);
			}

		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static  void sendNotification( String notificationType,
			String userType,String principal)
					throws WTException
	{
		// No need to provide user type. Only name is needed
		DirectoryContextProvider localDirectoryContextProvider = OrganizationServicesHelper.manager
				.newDirectoryContextProvider((String[]) null, (String[]) null);
		Enumeration principalReferences = OrganizationServicesHelper.manager.getPrincipalReference(principal,
				localDirectoryContextProvider);

		// Method Number 1
		EMailMessage msg = EMailMessage.newEMailMessage();
		while (principalReferences.hasMoreElements())
		{
			WTPrincipalReference principalReference = (WTPrincipalReference) principalReferences.nextElement();
			msg.addRecipient(principalReference);
		}
		msg.setSubject("This is subject of mail");
		msg.addPart("This is body. Use HTML directly <b>This is Bold</b>", "text/html");
		// Set the sender
		msg.setOriginator(SessionHelper.getPrincipal());
		// This will send the mail
		msg.send(true);

		// Method Number two
		List<String> users = getUsersFromPrincipalName(principalReferences);
		if (!users.isEmpty())
		{
			EMailMessage msg2 = EMailMessage.newEMailMessage();
			msg2.addEmailAddress(users.toArray(new String[0]));
			msg2.setSubject("This is subject of mail");
			msg2.addPart("This is body. Use HTML directly <b>This is Bold</b>", "text/html");
			// Set the sender
			msg2.setOriginator(SessionHelper.getPrincipal());
			// This will send the mail
			msg2.send(true);
		}
	}

	// This method will return the list of users mail ID
	private static List<String> getUsersFromPrincipalName(Enumeration principalReferences) throws WTException
	{
		List<String> users = new ArrayList<String>();
		while (principalReferences.hasMoreElements())
		{
			WTPrincipalReference principalReference = (WTPrincipalReference) principalReferences.nextElement();
			if (principalReference.getPrincipal() instanceof WTGroup)
			{
				Enumeration groupUsers = OrganizationServicesHelper.manager
						.members((WTGroup) principalReference.getPrincipal(), true);
				while (groupUsers.hasMoreElements())
				{
					WTUser user = (WTUser) groupUsers.nextElement();
					users.add(user.getEMail());
				}
			} else
			{
				WTUser user = (WTUser) principalReference.getPrincipal();
				users.add(user.getEMail());
			}
		}
		return users;
	}
	
	
	private static void getAffectedObjects(wt.workflow.work.WorkItem workitem,Element element){
		//
		try {
			Persistable pboReff = workitem.getPrimaryBusinessObject().getObject();
			if(pboReff instanceof WTChangeIssue){
				WTChangeIssue pr = (WTChangeIssue) pboReff;
				QueryResult result = wt.change2.ChangeHelper2.service.getChangeables(pr);
				while(result.hasMoreElements()){
					Object object = result.nextElement();
					if(object instanceof wt.part.WTPart) {
						wt.part.WTPart part = (wt.part.WTPart) object;
						element.addAtt(new Att("Part",part.getDisplayIdentifier().toString()));
					}else{
						wt.doc.WTDocument doc = (wt.doc.WTDocument) object;
						element.addAtt(new Att("Doc",doc.getDisplayIdentifier().toString()));
					}

				}
				element.addAtt(new Att("Subject",pr.getDisplayIdentifier().toString()));
			}else if(pboReff instanceof ChangeOrder2){

				ChangeOrder2 co = (ChangeOrder2) pboReff;
				QueryResult result = wt.change2.ChangeHelper2.service.getChangeablesBefore(co);
				while(result.hasMoreElements()){
					Object object = result.nextElement();
					if(object instanceof wt.part.WTPart) {
						wt.part.WTPart part = (wt.part.WTPart) object;
						element.addAtt(new Att("Part",part.getDisplayIdentifier().toString()));
					}else if(object instanceof EPMDocument){
						EPMDocument epm = (EPMDocument) object;
						element.addAtt(new Att("Part",epm.getDisplayIdentifier().toString()));
					}else if(object instanceof wt.doc.WTDocument){
						wt.doc.WTDocument doc = (wt.doc.WTDocument) object;
						element.addAtt(new Att("Doc",doc.getDisplayIdentifier().toString()));
					}

				}
				element.addAtt(new Att("Subject",co.getDisplayIdentifier().toString()));

			}else if (pboReff instanceof ChangeRequest2){
				ChangeRequest2 cq = (ChangeRequest2) pboReff;
				QueryResult result = wt.change2.ChangeHelper2.service.getChangeables(cq);
				while(result.hasMoreElements()){
					Object object = result.nextElement();
					if(object instanceof wt.part.WTPart) {
						wt.part.WTPart part = (wt.part.WTPart) object;
						element.addAtt(new Att("Part",part.getDisplayIdentifier().toString()));
					}else{
						wt.doc.WTDocument doc = (wt.doc.WTDocument) object;
						element.addAtt(new Att("Doc",doc.getDisplayIdentifier().toString()));
					}
				}
				element.addAtt(new Att("Subject",cq.getDisplayIdentifier().toString()));
			}
		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
