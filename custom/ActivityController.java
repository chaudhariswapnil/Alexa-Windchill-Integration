package com.custom;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import wt.doc.WTDocument;
import wt.enterprise.EnterpriseHelper;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistentReference;
import wt.fc.QueryResult;
import wt.meeting.actionitem.ActionItem;
import wt.meeting.actionitem.ActionItemHealthStatus;
import wt.meeting.actionitem.ActionItemPriority;
import wt.meeting.actionitem.ActionItemServiceHelper;
import wt.meeting.actionitem.ActionItemStatus;
import wt.meeting.actionitem.ActionItemSubjectLink;
import wt.meeting.actionitem.DiscreteActionItem;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipal;
import wt.ownership.Ownership;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

import org.apache.http.HttpRequest;
import org.apache.log4j.Logger;
import org.eclipse.core.internal.dtree.ObjectNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ptc.core.rest.AbstractResource;

import wt.log4j.LogR;

@Path("/action")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json"})
public class ActivityController  extends AbstractResource implements RemoteAccess,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogR.getLogger(ActivityController.class.getName());
	private static GsonBuilder builder = null;
	private static Gson gson = null;
	static{
		if(builder==null){
			builder = new GsonBuilder();
			builder.serializeNulls();
			gson = builder.create();
		}
	}

	@PUT
	@Path("/createActionItems")
	public static int createActionItems(@FormParam("actionItem") org.json.JSONObject jsonObject) throws JSONException, WTException, ParseException{

		Persistable pboObject = null;
		String pboUfid = (String)jsonObject.get("pboUfid");
		try{
			pboObject = ActivityHelper.getObjectByUfid(pboUfid);

		}catch(WTException e){
			LOGGER.error("PBOF not found for the : "+pboUfid+e.getLocalizedMessage(), e);
			throw new ObjectNotFoundException("PBOF not found for the : "+pboUfid);
		}

		JSONArray jsonArray = null;
		org.json.JSONObject explrObject = null;
		try {
			jsonArray = (JSONArray)jsonObject.get("activities");
			LOGGER.info(jsonArray.length()+" ActionItem is going to create for the : "+pboObject);
			for (int i = 0; i < jsonArray.length(); i++) {
				explrObject = jsonArray.getJSONObject(i);
				ActivityHelper.createActivity(explrObject,pboObject);
			}
		} catch (JSONException e) {
			LOGGER.error(explrObject+"= got error while create action item : "+e.getLocalizedMessage(),e);
		}

		return 0;

	}


	@GET
	@Path("/getActionItems/{ufid}")
	public static Response getActionItems(@PathParam("ufid") String workItemUfid) throws JSONException{
		Persistable pboObject = null;
		JSONArray jsonArray = null;
		try{
			pboObject = ActivityHelper.getObjectByUfid(workItemUfid);

		}catch(WTException e){
			LOGGER.error("PBO not found for the : "+workItemUfid+e.getLocalizedMessage(), e);
			throw new ObjectNotFoundException("PBOF not found for the : "+workItemUfid);
		}


		if(pboObject instanceof wt.workflow.work.WorkItem){
			wt.workflow.work.WorkItem workitem = null;
			workitem = (wt.workflow.work.WorkItem) pboObject;
			PersistentReference pbo = workitem.getPrimaryBusinessObject();
			ActionItem[] associatedActionItems = ActivityHelper.getActivitiesAssociatedToPbo(pbo.getObject());

			if(null!= associatedActionItems){
				jsonArray = new JSONArray();
				org.json.JSONObject jsonObject = null;
				for(int i=0;i<associatedActionItems.length;i++){
					DiscreteActionItem actionItem = (DiscreteActionItem)associatedActionItems[i];
					jsonObject = new org.json.JSONObject();
					jsonObject.put(ActivityConstant.ACTION_OWNER, actionItem.getOwnership().getOwner().getName());
					jsonObject.put(ActivityConstant.ACTION_ESTIMATED_START_DATE, actionItem.getEstimatedStart());
					jsonObject.put(ActivityConstant.ACTION_ESTIMATED_END_DATE, actionItem.getEstimatedFinish());
					jsonObject.put(ActivityConstant.ACTION_NAME, actionItem.getItemName());
					jsonObject.put(ActivityConstant.ACTION_PRIORITY, actionItem.getPriority());
					jsonObject.put(ActivityConstant.ACTION_STATUS, actionItem.getStatus());
					jsonObject.put(ActivityConstant.ACTION_OBID, actionItem.getIdentity());
					jsonArray.put(jsonObject);

				}
			}
		}

		return Response.status(Response.Status.OK).entity(jsonArray).build();
	}


	@GET
	@Path("/updateActionItems")
	public static Response updateActionItems(@FormParam("actionItems") org.json.JSONObject jsonObject) throws JSONException{
		JSONArray jsonArray = null;
		org.json.JSONObject explrObject = null;
		try {
			jsonArray = (JSONArray)jsonObject.get("activities");
			LOGGER.info(jsonArray.length()+" ActionItem is going to update for the : ");
			for (int i = 0; i < jsonArray.length(); i++) {
				explrObject = jsonArray.getJSONObject(i);
				ActivityHelper.updateActivity(explrObject);
			}
		} catch (JSONException e) {
			LOGGER.error(explrObject+"= got error while create action item : "+e.getLocalizedMessage(),e);
		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.OK).entity("").build();
	}


	@GET
	@Path("/welcomenew")
	public Response getFormData(HttpRequest req) throws Exception{

		System.out.println("Welcome ...: "+ req);
		return Response.status(Response.Status.OK).entity("Success : "+req).build();
	}



	@GET
	@Path("/welcome")
	//	@ApiOperation(position=0, response=Entity.class, responseContainer="List", value="Fetch the representation of an document object by specifying the object's OID String.")
	//	@ApiResponses({@io.swagger.annotations.ApiResponse(code=400, message="If one of the URL or query parameters is not in the correct format."), @io.swagger.annotations.ApiResponse(code=404, message="If the specified objects does not exist."), @io.swagger.annotations.ApiResponse(code=500, message="If an unexpected error occurs.")})
	public Response getWelcomeMsg(@HeaderParam("username") String paramString1) throws Exception{

		System.out.println("Welcome ...: "+ paramString1);
		return Response.status(Response.Status.OK).entity("Success : "+paramString1).build();
	}
	
	@PUT
	@Path("/put")
	//	@ApiOperation(position=0, response=Entity.class, responseContainer="List", value="Fetch the representation of an document object by specifying the object's OID String.")
	//	@ApiResponses({@io.swagger.annotations.ApiResponse(code=400, message="If one of the URL or query parameters is not in the correct format."), @io.swagger.annotations.ApiResponse(code=404, message="If the specified objects does not exist."), @io.swagger.annotations.ApiResponse(code=500, message="If an unexpected error occurs.")})
	public Response getPutData(@HeaderParam("username") String paramString1) throws Exception{

		System.out.println("Welcome ...: "+ paramString1);
		return Response.status(Response.Status.OK).entity("Success : "+paramString1).build();
	}


	public static String getActionItem() throws WTException, WTPropertyVetoException, IllegalAccessException{

		QuerySpec baselineQS = new QuerySpec(DiscreteActionItem.class);
		final SearchCondition searchCondition1 = new SearchCondition(DiscreteActionItem.class, DiscreteActionItem.ITEM_NUMBER, SearchCondition.EQUAL,"1", false);
		baselineQS.appendWhere(searchCondition1, new int[] { 0 });

		final QueryResult result = PersistenceHelper.manager
				.find((StatementSpec) baselineQS);
		System.out.println("Result :"+ result.size());
		while(result.hasMoreElements()){
			DiscreteActionItem epm = (DiscreteActionItem)result.nextElement();
			System.out.println(epm.getSourceReference());
			System.out.println(epm.getSource());
			System.out.println(epm.getItemNumber());
			System.out.println(epm.getHealthStatus());
			System.out.println(getActionItemAssociatedDoc(epm));
		}		

		return null;

	}


	private static WTDocument getActionItemAssociatedDoc(DiscreteActionItem actionItem) throws WTException, WTPropertyVetoException, IllegalAccessException{

		List listOfRelatedObject = ActionItemServiceHelper.service.getSubjectsOfActionItem(actionItem);
		for(Object object : listOfRelatedObject){
			if(object instanceof WTDocument){
				WTDocument doc = (WTDocument)object;
				System.out.println("From API : "+doc.getDisplayIdentifier());


				if(doc instanceof wt.doc.WTDocument){
					wt.doc.WTDocument pboDoc = (wt.doc.WTDocument)doc;
					WTPrincipal activityOwner = ActivityHelper.getWTUserByName("wcadmin");
					Ownership activtyOwnerShip = Ownership.newOwnership(activityOwner);

					Date EstimatedStartDate    = new Date();//new SimpleDateFormat("dd/MM/yyyy").parse(startDate); 
					Date EstimatedEndDate    = new Date();//new SimpleDateFormat("dd/MM/yyyy").parse(startDate); 
					Timestamp fromES1 = new Timestamp(EstimatedStartDate.getTime());
					Timestamp fromES2 = new Timestamp(EstimatedEndDate.getTime());

					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					DiscreteActionItem actionTask = DiscreteActionItem.newDiscreteActionItem("ItemName", "ItemDescription", SessionHelper.getPrincipal(), timestamp);
					actionTask.setOwnership(activtyOwnerShip);
					actionTask.setContainer(pboDoc.getContainer());
					actionTask.setDomainRef(pboDoc.getDomainRef());
					actionTask.setEstimatedStart(fromES1);
					actionTask.setEstimatedFinish(fromES2);
					actionTask.setHealthStatus(ActionItemHealthStatus.GREEN);
					actionTask.setPriority(ActionItemPriority.MEDIUM);
					actionTask.setStatus(ActionItemStatus.OPEN);
					String itemNumber = EnterpriseHelper.getNumber(new Object[]{"{GEN:wt.enterprise.SequenceGenerator:trwpartnumber:10:0}"});
					actionTask.setItemNumber(itemNumber);

					try{
						actionTask = (DiscreteActionItem)PersistenceHelper.manager.save(actionTask);
						actionTask = (DiscreteActionItem)PersistenceHelper.manager.refresh(actionTask);

						System.out.println("Custome Name : "+actionTask.getItemName());
						System.out.println("Custome Number : "+actionTask.getItemNumber());
						System.out.println("Custome Item : "+actionTask);

						ActionItemSubjectLink actionItemLink = ActionItemSubjectLink.newActionItemSubjectLink(actionTask, pboDoc);
						actionItemLink = (ActionItemSubjectLink)PersistenceHelper.manager.save(actionItemLink);
						actionItemLink = (ActionItemSubjectLink)PersistenceHelper.manager.refresh(actionItemLink);

						System.out.println("actionItemLink : "+actionItemLink.getDisplayIdentifier());

					}catch(WTException e){
						System.out.println(e.getLocalizedMessage().toString());
					}
				}
			}
		}


		/*	WTDocument document = null;
		wt.fc.QueryResult qr1 = wt.fc.PersistenceHelper.manager.navigate(actionItem,wt.meeting.actionitem.ActionItemSubject.SUBJECT_ROLE,wt.meeting.actionitem.ActionItemSubject.class);
		while(qr1.hasMoreElements())
		{
			java.lang.Object o1 = qr1.nextElement();
			if( o1 instanceof wt.doc.WTDocument)
			{

				document = (wt.doc.WTDocument)o1;
				ActionItem[] result = ActionItemServiceHelper.service.getActionItems(document);
				for(int i=0;i<result.length;i++){
					ActionItem actionItemnew = result[i];
					System.out.println("Name : "+actionItemnew.getItemName());
					System.out.println("Number : "+actionItemnew.getItemNumber());
				}
			}
		}*/


		return null;
	}
	
	
	
	
	
	
	
	@GET
	@Path("/status")
	public Response systemStatus() throws Exception{
		JSONObject json = com.custom.ActivityHelper.getSystemStatus();
		return Response.status(Response.Status.OK).entity(json).build();
	} 
	
	@GET
	@Path("/userTask")
	public Response getuserRelatedTask(@HeaderParam("username") String userName) throws Exception{

		System.out.println("Welcome ...: "+ userName);
		JSONObject userTask = ActivityHelper.getUserRelatedTask(userName);
		return Response.status(Response.Status.OK).entity(gson.toJson(userTask)).build();
	} 
	

	@GET
	@Path("/search/{number}/{type}")
	public static Response searchObject(@PathParam("number") String number,@PathParam("type") String type){
		
		JSONObject JSONObject = ActivityHelper.searchObject(number, type);
	
		return Response.status(Response.Status.OK).entity(JSONObject).build();
	}
	
	
	@GET
	@Path("/notification")
	public static Response sendNotification(@PathParam("notificationType") String notificationType,@PathParam("userType") String userType,@PathParam("principal") String principal){
		
		if(userType.equals("USER")){
			// principal is userName 
		}else{
			// principal is Group Name
			// read grp from site and get all participant from grp
		}
		
		return Response.status(Response.Status.OK).entity("").build();
	}
	
	
	
	
	
	

	public static void main(String[] args) throws WTException, PropertyVetoException, IOException, InvocationTargetException {

		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");

		rms.invoke("getActionItem", "com.custom.ActivityController", null, null, null);
	}
}
