package com.custom;




import java.util.Enumeration;
import java.util.Vector;

import com.infoengine.object.factory.Att;
import com.infoengine.object.factory.Element;
import com.infoengine.object.factory.Group;
import com.ptc.cat.entity.server.HTTPUtil;

import ext.thingworx.helper.ThingworxHelper;
import ext.trw.isfa.ptc.windchill.workflow.processhelper.TRWCNProcessHelper;
import wt.adapter.BasicWebjectDelegate;
import wt.change2.ChangeOrder2;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.doc.WTDocument;
import wt.epm.EPMDocument;
import wt.epm.workspaces.EPMAsStoredConfigSpec;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.load.LoadServerHelper;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.vc.wip.WorkInProgressHelper;
import wt.workflow.definer.UserEventVector;
import wt.workflow.engine.WfActivity;

public class CodeTest implements RemoteAccess{



	public static void main(String[] args) {

		try{

			//LoadServerHelper.setCacheValue("", paramObject);
			RemoteMethodServer rm = RemoteMethodServer.getDefault();
			rm.setUserName("wcadmin");//DeshmukhA	//PyzikM   //ZajacL	   //LintnerS   //plmmaster
			rm.setPassword("wcadmin");//tester
			
		
			Class argTypes [];
			Object svrArgs [];
			argTypes = (new Class[] {String.class}); 
			svrArgs = (new Object []{""}); 
			Object returnObj = rm.invoke("getActionItem", "com.custom.ActivityController", null, null, null);
			
			//new ActivityController().getActionItem("", "");
			//Group group = ThingworxHelper.getRoutingEvent("OR:wt.workflow.work.WorkItem:1920922:047709483-1490604181239-2035806106-69-32-220-10@DIN56002302.corp.capgemini.com");
			//System.out.println(getRoutingEvent("OR:wt.workflow.work.WorkItem:2342107:047709483-1490604181239-2035806106-69-32-220-10@DIN56002302.corp.capgemini.com"));
			
			/*QuerySpec baselineQS = new QuerySpec(EPMDocument.class);	//A0008L8436		A0009H8136   A0009H8136 	A0010D4654
			final SearchCondition searchCondition1 = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER, SearchCondition.EQUAL,"A0009H8136", false);
			baselineQS.appendWhere(searchCondition1, new int[] { 0 });

			final QueryResult result = PersistenceHelper.manager
					.find((StatementSpec) baselineQS);
			System.out.println("Result :"+ result.size());
			while(result.hasMoreElements()){
				EPMDocument epm = (EPMDocument)result.nextElement();
				//epm = new TRWCNProcessHelper().getLatestIterationOfEPM(epm);
				if(WorkInProgressHelper.isWorkingCopy(epm)){
					System.out.println("Working Copy......");
					System.out.println("Private   "+WorkInProgressHelper.isPrivateWorkingCopy(epm));
					//System.out.println("Orginal Copy Identifire : "+((EPMDocument) WorkInProgressHelper.service.originalCopyOf(epm)).getDisplayIdentifier());
				}
				if(WorkInProgressHelper.isCheckedOut(epm)){
					System.out.println("Checked out .....");
				}
			System.out.println("EPM DOc :"+epm.getDisplayIdentifier());
				EPMAsStoredConfigSpec asStored= EPMAsStoredConfigSpec.newEPMAsStoredConfigSpec(epm); //getting As Stored Config Spec
				if(asStored!=null){
					System.out.println("As Stored Found...");
				}else{
					System.out.println("As Stored Not found..");
				}
			}*/
		}catch(Exception e){
		}

	}
	public static Group getRoutingEvent(String workitemUFID){
		Group grp = new Group();

		wt.workflow.work.WorkItem workitem = null;
		try {
			
			workitem = (wt.workflow.work.WorkItem) BasicWebjectDelegate.getObjectByUfid(workitemUFID);
		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(workitem!=null){
			grp.addElement(getRoutingOption(workitem));
		}
		return grp;

	}
	public static Element getRoutingOption(wt.workflow.work.WorkItem workitem){
		String eventOption =""; 
		boolean flag =true;
		Element element = new Element();
		WfActivity activity = (WfActivity)(workitem.getSource().getObject());
		UserEventVector eventList = activity.getUserEventList();
		element.addAtt(new Att("Name", getActivityName(activity)));
		element.addAtt(new Att("Deadline", activity.getDeadline()+""));
		element.addAtt(new Att("Description", activity.getDescription()));
		element.addAtt(new Att("Priority", workitem.getPriority()));
		element.addAtt(new Att("Status", workitem.getStatus().toString()));
		element.addAtt(new Att("Role", workitem.getRole().toString()));
		element.addAtt(new Att("Owner", workitem.getOwnership().getOwner().getName()));
		Enumeration enumeration = eventList.elements();
		if(enumeration.hasMoreElements()){
			while(enumeration.hasMoreElements()){
				if(flag)
					eventOption = eventOption +	enumeration.nextElement();
				flag =false;
				eventOption = eventOption +":"+	enumeration.nextElement();
			}
		}else{
			eventOption = "CompleteTask";
		}

		element.addAtt(new Att("RoutingOption", eventOption));
		return element;
	}

	private static String getActivityName(WfActivity activity){
		return activity.getName();
	}
	public static void getlogger(){
		throw new NullPointerException("Null Pointer");
	}
	

	
}
