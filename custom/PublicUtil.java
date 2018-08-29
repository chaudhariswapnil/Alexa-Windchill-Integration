package com.custom;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.xssf.usermodel.XSSFCell;

import wt.auth.SimpleAuthenticator;
import wt.change2.WTChangeActivity2;
import wt.change2.WTChangeIssue;
import wt.change2.WTChangeOrder2;
import wt.change2.WTChangeRequest2;
import wt.doc.WTDocument;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.ObjectIdentifier;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.fc.WTReference;
import wt.folder.Folder;
import wt.folder.FolderHelper;
import wt.httpgw.URLFactory;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleException;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.State;
import wt.method.MethodContext;
import wt.method.MethodServerException;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.part.WTPartHelper;
import wt.part.WTPartUsageLink;
import wt.pds.StatementSpec;
import wt.pom.UnsupportedPDSException;
import wt.pom.WTConnection;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.team.TeamException;
import wt.type.TypedUtilityServiceHelper;
import wt.util.WTException;
import wt.util.WTInvalidParameterException;
import wt.util.WTPropertyVetoException;
import wt.util.WTRuntimeException;
import wt.vc.VersionControlHelper;
import wt.vc.baseline.ManagedBaseline;
import wt.vc.config.LatestConfigSpec;
import wt.vc.wip.CheckoutLink;
import wt.vc.wip.WorkInProgressHelper;

import com.ptc.core.lwc.client.commands.LWCCommands;
import com.ptc.core.lwc.common.view.AttributeDefinitionReadView;
import com.ptc.core.lwc.common.view.ConstraintDefinitionReadView;
import com.ptc.core.lwc.common.view.EnumerationDefinitionReadView;
import com.ptc.core.lwc.common.view.EnumerationEntryReadView;
import com.ptc.core.lwc.common.view.EnumerationMembershipReadView;
import com.ptc.core.lwc.common.view.PropertyHolderHelper;
import com.ptc.core.lwc.server.LWCAbstractAttributeTemplate;
import com.ptc.core.lwc.server.TypeDefinitionServiceHelper;
import com.ptc.core.lwc.server.cache.db.TypeDefinitionDBService;
import com.ptc.core.meta.common.DiscreteSet;
import com.ptc.core.meta.container.common.impl.DiscreteSetConstraint;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.misc.NmActionServiceHelper;

public class PublicUtil implements RemoteAccess {

	/**
	 * Deal with the special characters of fuzzy query value.(SQL Skills)
	 * 
	 * @param sqlStr
	 * @return
	 */
	public static String sqlLikeValueEncode(String value) {
		if (value == null || "".equals(value)) {
			return value;
		}

		// Add "*" or "%" before and after processing
		if (value.startsWith("*") || value.startsWith("%")) {
			value = "%" + value.substring(1);
		}
		if (value.endsWith("*") || value.endsWith("%")) {
			value = value.substring(0, value.length() - 1) + "%";
		}
		if (value.endsWith("*") || value.endsWith("%")) {
			value = value.substring(0, value.length() - 1) + "%";
		}

		// Deal with the special characters
		if (value.contains("[")) {

			value = value.replace("[", "\\[");
			value = "'" + value + "'" + " escape '\\'";
		} else if (value.contains("_")) {

			value = value.replace("_", "\\_");
			value = "'" + value + "'" + " escape '\\'";
		} else {
			value = "'" + value + "'";
		}

		return value;
	}

	/**
	 * Deal with the special characters of fuzzy query value. (Using the
	 * advanced query in Windchill)
	 * 
	 * @param sqlStr
	 * @return
	 */
	public static String queryLikeValueFormat(String value) {
		if (value == null || "".equals(value)) {
			return value;
		}

		// Only add "*" or "%" to begin or end.
		if (value.startsWith("*") || value.startsWith("%")) {
			value = "%" + value.substring(1);
		}
		if (value.endsWith("*") || value.endsWith("%")) {
			value = value.substring(0, value.length() - 1) + "%";
		}

		return value;
	}
	
	/**
	 * @param args
	 * @throws WTException 
	 * @throws TeamException 
	 */
	public static void main(String[] args) throws TeamException, WTException {
		// TODO Auto-generated method stub
		 
//		RemoteMethodServer rms = RemoteMethodServer.getDefault();
//		rms.setUserName("wcadmin");
//		rms.setPassword("wcadmin");
//		
//		String oid="VR%3Awt.part.WTPart%3A168650".replaceAll("%3A", ":");
//		WTPart part=(WTPart) getObjectByOid(oid);
//		
//		String moid="VR%3Acom.ptc.windchill.suma.part.ManufacturerPart%3A289309".replaceAll("%3A", ":");
//		ManufacturerPart mpart=(ManufacturerPart) getObjectByOid(moid);
//		
//		String vmoid="VR%3Acom.ptc.windchill.suma.part.VendorPart%3A289389".replaceAll("%3A", ":");
//		VendorPart vpart=(VendorPart) getObjectByOid(vmoid);
//		
//		
//		Class[] classes = {VendorPart.class,WTPart.class};
//		Object[] objs = {vpart,part};
//
//		try {
// 
//			rms.invoke("createAVLLink", PublicUtil.class.getName(), null,classes, objs);
//			
//			 
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//		
		Map<String,String> map = new HashMap<String, String>();
		map.put("aaaa", "AAAAAA;;;qqqFFFFFFF;;;qqqpppp");
		map.put("bbbb", "BBBBBB;;;qqqfffffff;;;qqqpppp");
		map.put("cccc", "pppp;;;qqqfffffff;;;qqqCCCCCC");
		map.put("dddd", "DDDDDD;;;qqqpppp");
		Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();  
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next(); 
			String component = entry.getKey();
			String partStr = entry.getValue(); 
			String[] parNumbers = partStr.split(";;;qqq");
			for(String partNm : parNumbers){
				if("pppp".equals(partNm)){ //???????????bom??????????map??key
					entries.remove();
					break;  //??????
				}
			}
		}
		map.keySet();
		
	}
	


	
  /**
    * @author baijuanjuan
    * @directions     Get the internal name in English
    * @param 
    *
    */
 public static String getTypeByObject(WTObject obj) {
   String pType = null;
   try {
     if (obj instanceof WTPart) {
       WTPart part = (WTPart) obj;
       pType = TypedUtilityServiceHelper.service
           .getExternalTypeIdentifier(part);
     } else if (obj instanceof WTDocument) {
       WTDocument doc = (WTDocument) obj;
       pType = TypedUtilityServiceHelper.service
           .getExternalTypeIdentifier(doc);
     } else if (obj instanceof EPMDocument) {
       EPMDocument epm = (EPMDocument) obj;
       pType = TypedUtilityServiceHelper.service
           .getExternalTypeIdentifier(epm);
     } else if (obj instanceof ManagedBaseline) {
       ManagedBaseline baseline = (ManagedBaseline) obj;
       pType = TypedUtilityServiceHelper.service
           .getExternalTypeIdentifier(baseline);
     } else if (obj instanceof WTChangeIssue) {
       WTChangeIssue pr = (WTChangeIssue) obj;
       pType = TypedUtilityServiceHelper.service
           .getExternalTypeIdentifier(pr);
     } else if (obj instanceof WTChangeRequest2) {
       WTChangeRequest2 ecr = (WTChangeRequest2) obj;
       pType = TypedUtilityServiceHelper.service
           .getExternalTypeIdentifier(ecr);
     } else if (obj instanceof WTChangeOrder2) {
       WTChangeOrder2 ecn = (WTChangeOrder2) obj;
       pType = TypedUtilityServiceHelper.service
           .getExternalTypeIdentifier(ecn);
     } else if (obj instanceof WTChangeActivity2) {
       WTChangeActivity2 eca = (WTChangeActivity2) obj;
       pType = TypedUtilityServiceHelper.service
           .getExternalTypeIdentifier(eca);
     }
     int m = pType.lastIndexOf(".");
     pType = pType.substring(m + 1);
   } catch (RemoteException e) {
     e.printStackTrace();
   } catch (WTException e) {
     e.printStackTrace();
   }
   return pType;

 }


	
	/**
	 * ??????????
	 * @param name  ????
	 * @return WTContainer ????
	 * @throws WTException  ????
	 */
	public static WTContainer getWTContainerByName(String name) throws WTException{
	
		QuerySpec qs = new QuerySpec(WTContainer.class);
		SearchCondition sc = new SearchCondition(WTContainer.class, WTContainer.NAME, SearchCondition.EQUAL, name);
		qs.appendSearchCondition(sc);
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		if(qr!=null && qr.size() == 1) {
			return (WTContainer)qr.nextElement();
		} else {
			throw new WTException("WTContainer name:" + name  + " has more than one container or no container!");
		} 
	}
	
	
	public static Folder getFolder(WTContainer container, String path) {
		Folder folder = null;
		WTContainerRef containerRef = null;
		try {
			containerRef = WTContainerRef.newWTContainerRef(container);
		} catch (WTException e1) {
			e1.printStackTrace();
		}
		try {
			folder = FolderHelper.service.getFolder(path, containerRef);
		} catch (WTException e) {
			folder = null;
		}
		// ??????
		if (folder == null) {
			try {
//				folder = FolderHelper.service.createSubFolder(path,containerRef);
				folder=FolderHelper.service.saveFolderPath(path,containerRef);//???????????
			} catch (WTException e) {
				e.printStackTrace();
			}
		}
		
		return folder;
	}
	
	
	

    /**
     * Author: Freedom Rain
     * Description:               get persistable's oid by persistable
     * 2015-8-10??6:24:56
     */
	public static String getOidByPersistable(Persistable persistable)
			throws WTException {
		if (persistable != null) {
			ReferenceFactory referencefactory = new ReferenceFactory();
			return referencefactory.getReferenceString(persistable);
		}
		return null;
	}


	/**
	 * Author: Freedom Rain
	 * Description:           get persistable by oid
	 * 2015-8-10??6:24:02
	 */
	public static Persistable getPersistableByOid(String strOid)
			throws WTException {
		if (strOid != null && strOid.trim().length() > 0) {
			ReferenceFactory referencefactory = new ReferenceFactory();
			WTReference wtreference = referencefactory.getReference(strOid);
			Persistable persistable = wtreference != null ? wtreference
					.getObject() : null;
			return persistable;
		}
		return null;
	}
	
	
	
	/**
	 * @author baijuanjuan
	 * @directions         get object by oid
	 * @param
	 */
	public static Object getObjectByOid(String oid){
		Object obj = null;
			ReferenceFactory rf=new ReferenceFactory();
			 try {
				obj =rf.getReference(oid).getObject();	
			} catch (WTRuntimeException e) {
				e.printStackTrace();
			} catch (WTException e) {
				e.printStackTrace();
			}			
		return obj;
	}
	
	
	

	/**
	 * Author: Freedom Rain
	 * Description:               get persistable url ,not including containeroid
	 * @param persistable
	 * @return
	 * @throws WTException
	 */
	public static String getInfoPageURL(Persistable persistable) throws WTException{
		ReferenceFactory referencefactory = new ReferenceFactory();
		String oid = referencefactory.getReferenceString(persistable);
		URLFactory urlfactory = new URLFactory();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("oid", oid);
		String action = NmActionServiceHelper.service.getAction("object", "view").getUrl();
		System.out.println("action___________________________"+action);
		return urlfactory.getHREF(action, map, true);
	}
	
	
/*	
	*//**
	 * Author: Freedom Rain
	 * Description:               get persistable url ,not including containeroid
	 * @param pbo
	 * @return
	 * @throws WTException
	 *//*
	public static String getPartsMailURL(WTObject pbo) throws WTException{
		String url=null;
			if(pbo instanceof WTPart){
				WTPart wtpart = (WTPart)pbo	;
				wtpart =  (WTPart) VersionUtil.getLatestRevision(wtpart);
				TypeIdentifier ti = TypeIdentifierUtility.getTypeIdentifier(wtpart);
				String partname = ti.getTypename();
				
				String[] tmp = partname.split("\\|");
				if(tmp.length > 1){
					partname = tmp[1];
						
						String _url = getInfoPageURL(wtpart);
//						logger.debug("partnum:"+ wtpart.getNumber()+ "++url++"+_url);
						url="<tr>"+
					   "<td style='text-align:center'>"
						+"<a href='"+ _url+ "' target='_blank'>"+ wtpart.getNumber()+ "("+wtpart.getName()+")"+  "</a>"+
						"</td>"+
						"</tr>";
				}
			}
			System.out.println("url*****************************:"+url);
		return url;
	}
	*/
	
	
	
	
	
	/**
	   * @author baijuanjuan
	   * @directions     ????????????????????
	   * @param 
	   *
	 */
	public static String getLocalTypeByObject(WTObject obj) {
		String pType = null;
		try {
			if (obj instanceof WTPart) {
				WTPart part = (WTPart) obj;
				pType = TypedUtilityServiceHelper.service
						.getLocalizedTypeName(part,Locale.getDefault());
			} else if (obj instanceof WTDocument) {
				WTDocument doc = (WTDocument) obj;
				pType =TypedUtilityServiceHelper.service
						.getLocalizedTypeName(doc, Locale.getDefault()) ;
			} else if (obj instanceof EPMDocument) {
				EPMDocument epm = (EPMDocument) obj;
				pType = TypedUtilityServiceHelper.service
						.getLocalizedTypeName(epm, Locale.getDefault()) ;
			} else if (obj instanceof ManagedBaseline) {
				ManagedBaseline baseline = (ManagedBaseline) obj;
				pType = TypedUtilityServiceHelper.service
						.getLocalizedTypeName(baseline, Locale.getDefault()) ;
			} else if (obj instanceof WTChangeIssue) {
				WTChangeIssue pr = (WTChangeIssue) obj;
				pType = TypedUtilityServiceHelper.service
						.getLocalizedTypeName(pr, Locale.getDefault()) ;
			} else if (obj instanceof WTChangeRequest2) {
				WTChangeRequest2 ecr = (WTChangeRequest2) obj;
				pType = TypedUtilityServiceHelper.service
						.getLocalizedTypeName(ecr, Locale.getDefault()) ;
			} else if (obj instanceof WTChangeOrder2) {
				WTChangeOrder2 ecn = (WTChangeOrder2) obj;
				pType = TypedUtilityServiceHelper.service
						.getLocalizedTypeName(ecn, Locale.getDefault()) ;
			} else if (obj instanceof WTChangeActivity2) {
				WTChangeActivity2 eca = (WTChangeActivity2) obj;
				pType = TypedUtilityServiceHelper.service
						.getLocalizedTypeName(eca, Locale.getDefault()) ;
			}
			 
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}
		return pType;

	}
	
	
	
	
	
	/**
	 * @author baijuanjuan
	 * @directions ????????
	 * @param part
	 * @return
	 */
	public static boolean isCheckOut(RevisionControlled rc) {
		try {
			if (WorkInProgressHelper.isCheckedOut(rc)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
    
    /**
     * ??????
     * @author baijuanjuan
     * @param revision
     * @return
     */
	public static RevisionControlled getWorkofCopy(RevisionControlled revision) {
		RevisionControlled tempRevision = null;
		try {
			if (WorkInProgressHelper.isCheckedOut(revision)) {
				if (WorkInProgressHelper.isWorkingCopy(revision)) {
					tempRevision = revision;
				} else {
					tempRevision = (RevisionControlled) WorkInProgressHelper.service
							.workingCopyOf(revision);
				}
			} else {
				Folder folder = WorkInProgressHelper.service
						.getCheckoutFolder();
				CheckoutLink link = WorkInProgressHelper.service.checkout(
						revision, folder, "");
				tempRevision = (RevisionControlled) link.getWorkingCopy();
			}
		} catch (WTException e) {

			e.printStackTrace();
		} catch (WTPropertyVetoException e) {

			e.printStackTrace();
		}
		return tempRevision;
	}
    
    
    

    /**
     * ????   RevisionControlled?????? ????????
     * @author ymj
     * @param revision
     * @return
     * @throws WTException
     */
    public static RevisionControlled checkInObject(RevisionControlled revision) throws WTException {
        if (!revision.isLatestIteration())
            revision = (RevisionControlled) VersionControlHelper.service.getLatestIteration(revision, false);
        try {
            if (WorkInProgressHelper.isWorkingCopy(revision)) {
                return (RevisionControlled) WorkInProgressHelper.service.checkin(revision, "");
            } else if (WorkInProgressHelper.isCheckedOut(revision)) {
                revision = (RevisionControlled) WorkInProgressHelper.service.workingCopyOf(revision);
                return (RevisionControlled) WorkInProgressHelper.service.checkin(revision, "");
            } else
                return revision;
        } catch (WTPropertyVetoException ex) {
            WTException e = new WTException(ex);
            throw e;
        }
    }
    
    /**
	 * ???????(??????)????key???value????????
	 * @directions
	 * @param
	 */
	public static  String readProperties(String propertyName,String key ){
		String value="";
		Locale locale = Locale.getDefault(); 
		ResourceBundle bundle = ResourceBundle.getBundle(propertyName,locale); 
		if (bundle.getString(key)!=null||!"".equals(key)) {
			value=bundle.getString(key);	
		}
		return value;
	}
	
	
	
	
	/**
	 * ?????????????????
	 * @param propertyName
	 * @return
	 */
	public static  ResourceBundle getResourceBundle(String propertyName){
//		String value="";
		Locale locale = Locale.getDefault(); 
		ResourceBundle bundle = ResourceBundle.getBundle(propertyName,locale); 
//		if (bundle.getString(key)!=null||!"".equals(key)) {
//			value=bundle.getString(key);	
//		}
		return bundle;
	}
	
	/**
	 * ???????????
	 * @param obj
	 * @param state
	 * @throws WTInvalidParameterException
	 * @throws LifeCycleException
	 * @throws WTException
	 */
	public static void setObjectState(WTObject obj, String state)
			throws WTInvalidParameterException, LifeCycleException, WTException {
		boolean flagAccess = wt.session.SessionServerHelper.manager.setAccessEnforced(false);
	 if (obj instanceof WTDocument) {
			WTDocument doc =(WTDocument) obj;
			LifeCycleManaged lcm = (LifeCycleManaged) doc;
			LifeCycleHelper.service.setLifeCycleState(lcm, State.toState(state));
		}else if (obj instanceof WTPart ) {
			WTPart part =(WTPart) obj;
			LifeCycleManaged lcm = (LifeCycleManaged) part;
			LifeCycleHelper.service.setLifeCycleState(lcm, State.toState(state));
		}
		SessionServerHelper.manager.setAccessEnforced(flagAccess);

	}

	
	/**
	 * Description:                ??????????????????????????????
	 * @param number
	 * @param thisClass
	 * @return
	 */
	public static List<Persistable> getLatestPersistableByNumber(String number,
			Class thisClass) {
		List<Persistable> list = new ArrayList<>();
		try {
			int[] index = { 0 };
			QuerySpec qs = new QuerySpec(thisClass);
			String attribute = (String) thisClass.getField("NUMBER").get(
					thisClass);
			qs.appendWhere(new SearchCondition(thisClass, attribute,
					SearchCondition.LIKE, queryLikeValueFormat(number)), index);
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
			LatestConfigSpec configSpec = new LatestConfigSpec();
			qr = configSpec.process(qr);
			while (qr != null && qr.hasMoreElements()) {
				list.add((Persistable) qr.nextElement());
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	

	
	
	/**
	 * ?????????????????
	 * @param enumName
	 * @param displayName
	 * @return
	 * @throws RemoteException
	 * @throws WTException
	 * @throws java.rmi.RemoteException 
	 */
	public static String getEnumListDisPlayValueByInternalName(String enumName,String displayName) throws RemoteException, WTException, java.rmi.RemoteException {
		String enumkey=displayName;
		List<Map<String, String>> list = getEnumListDisPlayValueByInternalName(enumName);
//		System.out.println("    ***************list**************  "+list.size());
		if(list != null && list.size() > 0 )	{
			for(Map<String,String> keyValue : list){
				Iterator<String> it = keyValue.keySet().iterator();
				while(it.hasNext()){
					String key = (String) it.next();
//					System.out.println("    ***************key**************  "+key);
					String value = keyValue.get(key);
//					System.out.println("    ***************value**************  "+value);
					if (displayName.equals(value)) {
						enumkey=key;
//						System.out.println("    ***************enumkey**************  "+enumkey);
						return enumkey;
					}
					 
				}
			}
		}
		return enumkey;
	}
	
	
	
    public static List<Map<String, String>> getEnumListByInternalName(String enumInternalName,Comparator<EnumerationMembershipReadView> compar) throws WTException, java.rmi.RemoteException {
    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                try {
                    EnumerationDefinitionReadView edr = TypeDefinitionServiceHelper.service.getEnumDefView(enumInternalName);
                    if (edr != null) {
                    	Collection<EnumerationMembershipReadView> views = edr.getAllMemberships();
                    	 List<EnumerationMembershipReadView> newViews = new ArrayList<EnumerationMembershipReadView>();
                    	 newViews.addAll(views);
                    	 Collections.sort(newViews, compar);//?????????
                    	 if(newViews != null && newViews.size() > 0){
                    		 for (EnumerationMembershipReadView view : newViews) {
                    			 if(view == null){
                    				 
                    				 continue;
                    			 }
                    			 EnumerationEntryReadView member =  view.getMember();
                    			 Map<String, String> result = new HashMap<String, String>();
                    				 String enumKey = member.getName();
                                     String enumName = PropertyHolderHelper.getDisplayName(member, SessionHelper.getLocale());
                                     result.put(enumKey, enumName);
                                     
                                     list.add(result);
                             }
                    	 }else{
                    		  
                    	 }
                    }
        } catch (Exception e) {
            throw new WTException(e, "Get Enumeration [" + enumInternalName + "] failed.");
        }
        return list;
    }
	
	
    public static List<Map<String, String>> getEnumListDisPlayValueByInternalName(String enumInternalName) throws WTException, java.rmi.RemoteException {
        try {
            if (!RemoteMethodServer.ServerFlag) {
                return (List<Map<String, String>>) RemoteMethodServer.getDefault().invoke("getEnumListDisPlayValueByInternalName", PublicUtil.class.getName(), null, new Class[] { String.class },
                        new Object[] { enumInternalName });
            } else {
            	
            	return getEnumListByInternalName(enumInternalName, new Comparator<EnumerationMembershipReadView>() {
					@Override
					public int compare(
							EnumerationMembershipReadView o1,
							EnumerationMembershipReadView o2) {
						Locale locale = Locale.CHINA;
						try {
							locale = SessionHelper.getLocale();
						} catch (WTException e) {
							e.printStackTrace();
						}
						String display1  =	PropertyHolderHelper.getDisplayName(o1.getMember(),locale );
						String display2  =	PropertyHolderHelper.getDisplayName(o2.getMember(),locale );
						
						if(display1==null || display2 == null){
							return 1;
						}
						return display1.compareTo(display2);
					}
				});
            }
        } catch (RemoteException e) {
            
        } catch (InvocationTargetException e) {
            
        }
        return null;
    }
	

	
	/**
	 * ?????????????????????
	 * @param typeName
	 * @param attrName
	 * @return
	 * @throws WTException
	 */
	public static List<String> getTypeLegalValues(String typeName, String attrName) throws WTException {
//		logger.debug("attrName=" + attrName);
		List<String> valueList = new ArrayList<String>();
		LWCAbstractAttributeTemplate td = TypeDefinitionDBService.newTypeDefinitionDBService().getTypeDefinition(typeName);
//		logger.debug("td=" + td);
		NmOid nmOid = NmOid.newNmOid(ObjectIdentifier.newObjectIdentifier(td.toString()));

		//????????????
		ArrayList<? extends Object> adrvs = LWCCommands.getTypeAttributes(nmOid.toString());
		for(Object object:adrvs) {
			if(object instanceof AttributeDefinitionReadView) {
				AttributeDefinitionReadView adrv = (AttributeDefinitionReadView)object;
//				logger.debug("adrv.getAttributeTypeIdentifier().getAttributeName()=" + adrv.getAttributeTypeIdentifier());
				//?????????
				if(adrv.getAttributeTypeIdentifier().getAttributeName().equals(attrName)) {
					//??????
					Collection<ConstraintDefinitionReadView> constraints = adrv.getAllConstraints();
//					logger.debug("constraints.size()=" + constraints.size());
					Iterator<ConstraintDefinitionReadView> iter = constraints.iterator();
					for(ConstraintDefinitionReadView constraint: constraints) {
						String defClassName = constraint.getRule().getRuleClassname();
//						logger.debug("defClassName=" + defClassName);
						//???????
						if(defClassName.equals(DiscreteSetConstraint.class.getName())) {
							Serializable ruleData = constraint.getRuleData();
//							logger.debug("ruleData=" + ruleData);
							if(ruleData!=null && ruleData instanceof DiscreteSet) {
								DiscreteSet set  = (DiscreteSet)ruleData;
								Object[] elements = set.getElements();
								for(Object element:elements) {
									if(element instanceof String) {
										String value = (String)element;
										valueList.add(value);
									}
								}
								break;
							}
						}
					}
					break;
				}
			}
		}
		return valueList;
	}
	
	
	
	/**
	 * Description:              ?????????????????
	 */
	public static WTPart removeUsageLink(WTPart part) {
		try {
			QueryResult qr = WTPartHelper.service.getUsesWTParts(part,WTPartHelper.service.findWTPartConfigSpec());
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				WTPartUsageLink usagelink = (WTPartUsageLink) obj[0];
				PersistenceServerHelper.manager.remove(usagelink);
			}
			PersistenceServerHelper.manager.update(part);
		} catch (WTException e) {
			e.printStackTrace();
		}
		
		return part;
	}
    
	
	/**
	 * Description:              ?????????????????
	 */
	public static WTPart removeSubParts(WTPart part) {
		try {
			QueryResult qr = WTPartHelper.service.getUsesWTParts(part,WTPartHelper.service.findWTPartConfigSpec());
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				WTPartUsageLink usagelink = (WTPartUsageLink) obj[0];
				if (usagelink!=null) {
					PersistenceServerHelper.manager.remove(usagelink);
				}
				WTPart subPart = (WTPart) obj[1];
				if (subPart!=null) {
					removeSubParts(subPart);
				}
			}
			PersistenceServerHelper.manager.update(part);
		} catch (WTException e) {
			e.printStackTrace();
		}
		
		return part;
	}
	
	
	/**
	 * ????????
	 * @param displayName
	 * @return
	 */
	public static String getRoleNamebyDisplayName(String displayName){
		String roleName="";
		String sql="select  role from wtroleprincipal where name='" +  displayName+"'";
		String temp=getRoleNameBySql(sql);
		if (temp!=null||!"".equals(temp)) {
			if ("????".equals(displayName)) {
				roleName="RD";
			}else{
				roleName=temp;
			}
		}
		return roleName;
	}
	
	
	
	
	
	
	/**
	 * ??sql ?????????????
	 * @param sql
	 * @return
	 */
	public static String getRoleNameBySql(String sql){
		boolean checkflag =SessionServerHelper.manager.setAccessEnforced(false);
		ResultSet rs=null ;
		WTConnection connection=null;
		String roleName="";
		try {
			connection = (WTConnection)getMethodContext().getConnection();
			if (connection!=null) {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					roleName = rs.getString("ROLE");
					if ("RD".equals(roleName)) {
						roleName="RD";
					}
					break;
				}		
			}
		} catch (UnsupportedPDSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.net.UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			SessionServerHelper.manager.setAccessEnforced(checkflag);
		}
		return roleName;
	}
	

	/**
	 * ??????
	 * @return
	 * @throws UnsupportedPDSException
	 * @throws UnknownHostException
	 */
	public static MethodContext getMethodContext()
	        throws UnsupportedPDSException, UnknownHostException {
	    MethodContext methodcontext = null;
	    try {
	        methodcontext = MethodContext.getContext();
	    } catch (MethodServerException methodserverexception) {
	        RemoteMethodServer.ServerFlag = true;
	        InetAddress inetaddress = InetAddress.getLocalHost();
	        String s = inetaddress.getHostName();
	        if (s == null) {
	            s = inetaddress.getHostAddress();
	        }
	        SimpleAuthenticator simpleauthenticator = new SimpleAuthenticator();
	        methodcontext = new MethodContext(s, simpleauthenticator);
	        methodcontext.setThread(Thread.currentThread());
	    }
	    return methodcontext;
	}
	
	
	
	@SuppressWarnings("static-access")
    private static String getValue(XSSFCell xssfRow) {
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }
	 


}