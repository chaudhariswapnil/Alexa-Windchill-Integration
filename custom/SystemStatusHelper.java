package com.custom;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import wt.access.AccessControlException;
import wt.intersvrcom.SiteMonitorMBean;
import wt.jmx.core.MBeanRegistry;
import wt.jmx.core.MBeanUtilities;
import wt.jmx.core.SelfAwareMBean;
import wt.jmx.core.mbeans.Dumper;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.AttributeList;
import javax.management.Attribute;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.lang.management.ManagementFactory;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URLEncoder;

import wt.log4j.LogR;
import wt.manager.RemoteServerManager;
import wt.manager.jmx.MethodServerMProxyMBean;
import wt.method.jmx.JmxConnectInfo;
import wt.method.jmx.MethodServer;
import wt.util.WTAppServerPropertyHelper;
import wt.util.WTContext;
import wt.util.WTProperties;
import wt.util.jmx.WDSJMXConnector;
import wt.util.jmx.AccessUtil;
import wt.util.jmx.JmxConnectUtil;
import wt.util.jmx.serverStatusResource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;

import com.infoengine.object.factory.Att;
import com.infoengine.object.factory.Element;
import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.meta.common.DisplayOperationIdentifier;
import com.ptc.core.rest.AbstractResource;
import com.ptc.windchill.enterprise.change2.commands.RelatedChangesQueryCommands;
import com.ptc.windchill.enterprise.queryBuilder.service.QueryBuilderRestService;
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
import wt.org.OrganizationServicesHelper;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.ownership.Ownership;
import wt.part.WTPart;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.workflow.engine.WfActivity;
import wt.workflow.work.WorkItem;
import wt.workflow.work.WorkflowHelper;

public class SystemStatusHelper extends AbstractResource {
	private static final Logger LOGGER = LogR.getLogger(ActivityHelper.class.getName());
	private static final Logger  logger = LogR.getLogger(ActivityHelper.class.getName()); 
	private QueryBuilderRestService service = null;
	private static final String  WHC_PREFIX = "Whc";
	private static final String  SOLR_PREFIX = "Solr";

	private static final String  windchillWebAppPath;
	private static final String  whcWebAppPath;
	private static final String  solrWebAppPath;
	private static final String  helpUrl;
	private static final boolean  restrictToSiteAdministrators;
	static
	{
		final Properties  wtProps = MBeanUtilities.getProperties();
		windchillWebAppPath = "/" + wtProps.getProperty( "wt.webapp.name" );
		whcWebAppPath = windchillWebAppPath + "-WHC";
		solrWebAppPath = "/" + wtProps.getProperty( "wt.solr.webapp.name" );
		helpUrl = wt.help.HelpLinkHelper.createHelpHREF( "ServerStatusAbout" );
		restrictToSiteAdministrators = !"false".equals( wtProps.getProperty( "wt.serverStatus.restrictToSiteAdministrators" ) );
	}

	private static final String  thisJvmName = ManagementFactory.getRuntimeMXBean().getName();
	private static final String  thisMethodServerName = MethodServer.getStaticDisplayName();

	private static final ObjectName  dumperMBeanName = newObjectName( "com.ptc:wt.subsystem=Dumper" );
	private static final ObjectName  runtimeMBeanName = newObjectName( ManagementFactory.RUNTIME_MXBEAN_NAME );
	private static final ObjectName  gcMonitorMBeanName = newObjectName( "com.ptc:wt.subsystem=Monitors,wt.monitorType=GarbageCollection" );
	private static final ObjectName  memMonitorMBeanName = newObjectName( "com.ptc:wt.subsystem=Monitors,wt.monitorType=Memory" );
	private static final ObjectName  cpuMonitorMBeanName = newObjectName( "com.ptc:wt.subsystem=Monitors,wt.monitorType=ProcessCpuTime" );
	private static final ObjectName  osMBeanName = newObjectName( ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME );
	private static final ObjectName  methodServerMProxyMBeanName = newObjectName( "com.ptc:wt.processGroup=MethodServers" );
	private static final ObjectName  methodServerMBeanName = newObjectName( "com.ptc:wt.type=MethodServer" );
	private static final ObjectName  methodContextsMBeanName = newObjectName( "com.ptc:wt.subsystem=Monitors,wt.monitorType=MethodContexts" );
	private static final ObjectName  seRequestMonitorMBeanName = newObjectName( "com.ptc:wt.servlet.system=WebAppContexts,wt.webAppContext=" + windchillWebAppPath +
			",wt.subsystem=Monitors,wt.servlet.subsystem=ServletRequests" );
	private static final ObjectName  sessionMonitorMBeanName = newObjectName( "com.ptc:wt.servlet.system=WebAppContexts,wt.webAppContext=" + windchillWebAppPath +
			",wt.subsystem=Monitors,wt.servlet.subsystem=ServletSessions" );
	private static final ObjectName  whcRequestMonitorMBeanName = newObjectName( "com.ptc:wt.servlet.system=WebAppContexts,wt.webAppContext=" + whcWebAppPath +
			",wt.subsystem=Monitors,wt.servlet.subsystem=ServletRequests" );
	private static final ObjectName  solrRequestMonitorMBeanName = newObjectName( "com.ptc:wt.servlet.system=WebAppContexts,wt.webAppContext=" + solrWebAppPath +
			",wt.subsystem=Monitors,wt.servlet.subsystem=ServletRequests" );
	private static final ObjectName  activeUsersMBeanName = newObjectName( "com.ptc:wt.subsystem=Monitors,Name=ActiveUsers" );
	private static final ObjectName  serverManagerMBeanName = newObjectName( "com.ptc:wt.type=ServerManager" );
	private static final ObjectName  vaultSitesMBeanName = newObjectName( "com.ptc:wt.subsystem=Monitors,wt.monitorType=VaultSites" );

	private static final String  dumperMBeanAttrs[] = new String[] { "DeadlockedThreadIds" };
	private static final String  runtimeMBeanAttrs[] = new String[] { "StartTime", "Uptime" };
	private static final String  msRuntimeMBeanAttrs[] = new String[] { "StartTime", "Name", "Uptime" };
	private static final String  dsRuntimeMBeanAttrs[] = new String[] { "Name", "Uptime" };
	private static final String  gcMonitorMBeanAttrs[] = new String[] { "PercentTimeSpentInGCThreshold", "RecentPercentTimeSpentInGC", "OverallPercentTimeSpentInGC" };
	private static final String  memMonitorMBeanAttrs[] = new String[] { "HeapPercentUsageThreshold", "HeapPercentUsage", "PermGenPercentUsageThreshold", "PermGenPercentUsage" };
	private static final String  cpuMonitorMBeanAttrs[] = new String[] { "ProcessPercentCpuThreshold", "RecentCpuData", "AverageProcessPercentCpu" };
	private static final String  osMBeanAttrs[] = new String[] { "FreePhysicalMemorySize", "TotalPhysicalMemorySize", "FreeSwapSpaceSize", "TotalSwapSpaceSize", "SystemLoadAverage" };
	private static final String  methodServerMBeanAttrs[] = new String[] { "JmxServiceURL" };
	private static final String  methodContextsMBeanAttrs[] = new String[] { "MaxAverageActiveContextsThreshold", "RecentStatistics", "BaselineStatistics" };
	private static final String  seRequestMonitorMBeanAttrs[] = new String[] { "MaxAverageActiveRequestsThreshold", "RequestTimeWarnThreshold", "RecentStatistics", "BaselineStatistics" };
	private static final String  sessionMonitorMBeanAttrs[] = new String[] { "MaxAverageActiveSessionsThreshold", "ActiveSessions", "BaselineStatistics" };
	private static final String  serverManagerMBeanAttrs[] = new String[] { "CacheMaster", "JmxServiceURL" };

	private static final Object  windchillDsGetAttrsArgs[] =
		{
				new ObjectName[]
						{
								runtimeMBeanName,
								gcMonitorMBeanName,
								memMonitorMBeanName,
								cpuMonitorMBeanName,
								dumperMBeanName,
								osMBeanName
						},
						null,
						new String[][]
								{
					dsRuntimeMBeanAttrs,
					gcMonitorMBeanAttrs,
					memMonitorMBeanAttrs,
					cpuMonitorMBeanAttrs,
					dumperMBeanAttrs,
					osMBeanAttrs
								}
		};

	private static final Object  serverManagerGetAttrsArgs[] =
		{
				new ObjectName[]
						{
								runtimeMBeanName,
								gcMonitorMBeanName,
								memMonitorMBeanName,
								cpuMonitorMBeanName,
								dumperMBeanName,
								osMBeanName,
								serverManagerMBeanName
						},
						null,
						new String[][]
								{
					runtimeMBeanAttrs,
					gcMonitorMBeanAttrs,
					memMonitorMBeanAttrs,
					cpuMonitorMBeanAttrs,
					dumperMBeanAttrs,
					osMBeanAttrs,
					serverManagerMBeanAttrs
								}
		};

	private static final Object  methodServersGetAttrsArgs[] =
		{
				new ObjectName[]
						{
								runtimeMBeanName,
								gcMonitorMBeanName,
								memMonitorMBeanName,
								cpuMonitorMBeanName,
								dumperMBeanName,
								methodServerMBeanName,
								methodContextsMBeanName,
								sessionMonitorMBeanName,
								seRequestMonitorMBeanName,
								whcRequestMonitorMBeanName,
								solrRequestMonitorMBeanName
						},
						null,
						new String[][]
								{
					msRuntimeMBeanAttrs,
					gcMonitorMBeanAttrs,
					memMonitorMBeanAttrs,
					cpuMonitorMBeanAttrs,
					dumperMBeanAttrs,
					methodServerMBeanAttrs,
					methodContextsMBeanAttrs,
					sessionMonitorMBeanAttrs,
					seRequestMonitorMBeanAttrs,
					seRequestMonitorMBeanAttrs,
					seRequestMonitorMBeanAttrs
								}
		};
	private static final ObjectName  thisMethodServerGetAttrsBeans[] =
		{
				runtimeMBeanName,
				gcMonitorMBeanName,
				memMonitorMBeanName,
				cpuMonitorMBeanName,
				dumperMBeanName,
				methodServerMBeanName,
				methodContextsMBeanName,
				sessionMonitorMBeanName,
				seRequestMonitorMBeanName,
				whcRequestMonitorMBeanName,
				solrRequestMonitorMBeanName,
				osMBeanName
		};
	private static final String  thisMethodServerGetAttrsAttrs[][] =
		{
				runtimeMBeanAttrs,
				gcMonitorMBeanAttrs,
				memMonitorMBeanAttrs,
				cpuMonitorMBeanAttrs,
				dumperMBeanAttrs,
				methodServerMBeanAttrs,
				methodContextsMBeanAttrs,
				sessionMonitorMBeanAttrs,
				seRequestMonitorMBeanAttrs,
				seRequestMonitorMBeanAttrs,
				seRequestMonitorMBeanAttrs,
				osMBeanAttrs
		};

	private static final String  getAttributesSignature[] = { ObjectName[].class.getName(), QueryExp[].class.getName(), String[][].class.getName() };










	public static org.json.JSONObject getCurrentSystemStatus() throws UnsupportedEncodingException{
		// collect data for WindchillDS
		org.json.JSONObject jsonSystemObject = new org.json.JSONObject();
		Map<ObjectName,AttributeList>  windchillDSResults[] = null;
		Object  dsJmxUrlString = null;
		Throwable  wcDsResultRetrievalException = null;
		try
		{
			try ( JMXConnector wcDsJmxConnection = WDSJMXConnector.getWDSJMXConnector() )
			{
				dsJmxUrlString = WDSJMXConnector.getWDSJMXServiceURL();
				final MBeanServerConnection  mbeanServer = wcDsJmxConnection.getMBeanServerConnection();
				windchillDSResults = (Map<ObjectName,AttributeList>[]) mbeanServer.invoke(
						dumperMBeanName, "getAttributes", windchillDsGetAttrsArgs, getAttributesSignature );
			}
		}
		catch ( VirtualMachineError e )
		{
			throw e;
		}
		catch ( Throwable t )
		{
			logger.error( "Failed to retrieve results from WindchillDS", t );
			wcDsResultRetrievalException = t;  // remember but otherwise eat exception
		}

		// fetch VaultSite status information
		final CompositeData  vaultSiteStatusInfo = ( WTAppServerPropertyHelper.isAppServerDeployment() ? null : getVaultSiteStatusInfo() );  // don't bother collecting vaultSiteStatusInfo in SC Ohio deployments



		// collect data on method servers and server managers
		Integer  activeUsers = null;
		Map<String,Object>  methodServerResults = null;
		Map<String,Object>  serverManagerResults = null;
		Throwable  resultRetrievalException = null;
		try
		{
			// establish JMX connection to default server manager to collect JMX data from server managers and method servers
			try ( JMXConnector smConnection = JmxConnectUtil.getDefaultServerManagerLocalConnector() )
			{
				final MBeanServerConnection  mbeanServer = smConnection.getMBeanServerConnection();

				// produce dynamic proxy for the "MethodServers" MBean in default server manager
				final MethodServerMProxyMBean  methodServersMBeanProxy =
						MBeanServerInvocationHandler.newProxyInstance( mbeanServer, methodServerMProxyMBeanName,
								MethodServerMProxyMBean.class, false );

				// grab data from all method servers (yes, this is a bit ugly, but it is all done in one request -- no chatter)
				methodServerResults = methodServersMBeanProxy.invokeInfoOpInAllClusterMethodServers(
						dumperMBeanName, "getAttributes", methodServersGetAttrsArgs, getAttributesSignature );

				// fetch active user count (done separately as we only need the result from one server manager, not all)
				try
				{
					activeUsers = (Integer) mbeanServer.getAttribute( activeUsersMBeanName, "TotalActiveUserCount" );
					jsonSystemObject.put("TotalActiveUserCount", activeUsers);
				}
				catch ( InstanceNotFoundException e )
				{
					LOGGER.debug( "Active users MBean not found", e );
					activeUsers = 0;  // must not have been any active users yet and thus this MBean has not yet been registered
				}
				catch ( VirtualMachineError e )
				{
					throw e;
				}
				catch ( Throwable t )
				{
					// don't let a problem here prevent us from acquiring serverManagerResults where possible
					// activeUsers is still null, clearly signaling lack of data to page rendering below
					LOGGER.error( "Problem acquiring total active user count", t );
				}

				// grab data from all server managers (yes, this is a bit ugly, but it is all done in one request -- no chatter)
				serverManagerResults = methodServersMBeanProxy.invokeInfoOpInAllServerManagers(
						dumperMBeanName, "getAttributes", serverManagerGetAttrsArgs, getAttributesSignature );
			}
		}
		catch ( VirtualMachineError e )
		{
			throw e;
		}
		catch ( Throwable t )
		{
			LOGGER.error( "Failed to retrieve results from server manager", t );
			resultRetrievalException = t;  // remember but otherwise eat exception
		}


		long  dataCollectionEnd = System.currentTimeMillis();
		final String  rbClassname = serverStatusResource.class.getName();
		final ResourceBundle  RB = ResourceBundle.getBundle( rbClassname, Locale.ENGLISH );
		final DecimalFormatSymbols  decimalSymbols = new DecimalFormatSymbols( Locale.ENGLISH );
		final DecimalFormat  decimalFormat = new DecimalFormat( "0.###", decimalSymbols );
		final String  percentString = xmlEscape( decimalSymbols.getPercent() );  // pre-escaped localized % symbol
		final SimpleDateFormat  dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS Z" );
		dateFormat.setTimeZone( WTContext.getContext().getTimeZone() );
		final String  webAppContextPaths[] = { windchillWebAppPath, whcWebAppPath, solrWebAppPath };
		final String  webAppPrefixes[] = { "", WHC_PREFIX, SOLR_PREFIX };
		final String  webAppLabelKeys[] = { serverStatusResource.GENERAL_SERVLET_REQUESTS_LABEL,
				serverStatusResource.HELP_SERVLET_REQUESTS_LABEL,
				serverStatusResource.SOLR_SERVLET_REQUESTS_LABEL };


		// collate server manager, method server, and servlet engine results into somewhat easier to use format
		final Map<String,Map<String,Object>>  smToAttrMap = new TreeMap<>();
		final Map<String,Throwable>  smToExceptionMap = new TreeMap<>();
		final Map<String,Map<String,Map<String,Object>>>  smToMsToAttrMap = new HashMap<>();
		final Map<String,Map<String,Throwable>>  smToMsToExceptionMap = new HashMap<>();
		if ( serverManagerResults != null )
			for ( Map.Entry<String,Object> serverManagerResultEntry : serverManagerResults.entrySet() )
			{
				final String  serverManagerName = serverManagerResultEntry.getKey();
				final Map<String,Map<String,Object>>  msToAttrMap = new TreeMap<>();
				smToMsToAttrMap.put( serverManagerName, msToAttrMap );
				final Map<String,Throwable>  msToExceptionMap = new TreeMap<>();
				smToMsToExceptionMap.put( serverManagerName, msToExceptionMap );
				final Object  smResult = serverManagerResultEntry.getValue();
				if ( smResult instanceof Map[] )
				{
					smToAttrMap.put( serverManagerName, collate( (Map<ObjectName,AttributeList>[]) smResult ) );
					collateResultsFromServerManager( serverManagerName, methodServerResults, msToAttrMap, msToExceptionMap );
				}
				else if ( smResult instanceof Throwable )
					smToExceptionMap.put( serverManagerName, (Throwable) smResult );
			}


		// grab all data of interest from this method server *if* it's not already covered by the data we have so far (which it should be)
		Map<String,Object>  thisMsAttrMap = null;
		Exception  localMsException = null;
		if ( !containsResultsForMs( smToMsToAttrMap, thisMethodServerName ) )
		{
			logger.error( "Could not obtain data for local method server from server manager!" );
			try
			{
				// could obtain this via a variety of direct API calls (and used to), but this is more consistent with the rest of the code here
				final Dumper  dumper = Dumper.getInstance( null, true );
				final Map<ObjectName,AttributeList>  msResults[] = dumper.getAttributes( thisMethodServerGetAttrsBeans, null,
						thisMethodServerGetAttrsAttrs );
				dataCollectionEnd = System.currentTimeMillis();  // reset end of data collection, since we just collected more data
				thisMsAttrMap = collate( msResults );
			}
			catch ( Exception e )
			{
				logger.error( "Failed to retrieve results from local method server", e );
				localMsException = e;
			}
		}

		// collate Windchill DS results into handy map
		final Map<String,Object>  wcDsAttrMap = ( ( windchillDSResults != null ) ? collate( windchillDSResults ) : null );

		// get siteStatusData
		final TabularData  siteStatusData = ( ( vaultSiteStatusInfo != null ) ? (TabularData) vaultSiteStatusInfo.get( "siteStatusData" ) : null );

		final double  msPercTimeInGCLimit = (Double) thisMsAttrMap.get( "PercentTimeSpentInGCThreshold" );
		final double  msPercTimeInGCRecent = (Double) thisMsAttrMap.get( "RecentPercentTimeSpentInGC" );
		final double  msPercTimeInGCOverall = (Double) thisMsAttrMap.get( "OverallPercentTimeSpentInGC" );
		final double  msPercCpuLimit = (Double) thisMsAttrMap.get( "ProcessPercentCpuThreshold" );
		final double  msPercCpuUsedOverall = (Double) thisMsAttrMap.get( "AverageProcessPercentCpu" );  // using overall rather than baseline...
		final CompositeData  recentCpuData = (CompositeData) thisMsAttrMap.get( "RecentCpuData" );
		final double  smPercCpuUsedRecent = ( ( recentCpuData != null ) ? (Double) recentCpuData.get( "processPercentCpu" ) : msPercCpuUsedOverall );
		final boolean  msDeadlocked = ( thisMsAttrMap.get( "DeadlockedThreadIds" ) != null );
		final String  thisMsChartQueryString = "jvmId=" + URLEncoder.encode( thisJvmName, "UTF-8" ) + "&amp;jvmStartTime=" + thisMsAttrMap.get( "StartTime" ) +
				"&amp;msName=" + URLEncoder.encode( thisMethodServerName, "UTF-8" );

		final String  msPhysMemInfo = renderSysMemInfo( thisMsAttrMap, "FreePhysicalMemorySize", "TotalPhysicalMemorySize", decimalFormat, RB );
		final String  msSwapMemInfo = renderSysMemInfo( thisMsAttrMap, "FreeSwapSpaceSize", "TotalSwapSpaceSize", decimalFormat, RB );
		final double  msPercPermLimit = (Double) thisMsAttrMap.get( "PermGenPercentUsageThreshold" );
		final double  msPercPermUsed = (Double) thisMsAttrMap.get( "PermGenPercentUsage" );
		final double  msPercHeapLimit = (Double) thisMsAttrMap.get( "HeapPercentUsageThreshold" );
		final double  msPercHeapUsed = (Double) thisMsAttrMap.get( "HeapPercentUsage" );
		final int  contextLimit = (Integer) thisMsAttrMap.get( "MaxAverageActiveContextsThreshold" );

		final String  defaultSmJvmName = getDefaultServerManagerJvmName();

		try{
			for ( int webAppIdx = 0; webAppIdx < webAppPrefixes.length; ++webAppIdx )
			{
				final String  webAppContextPath = webAppContextPaths[webAppIdx];
				final String  encodedWebAppContextPath = URLEncoder.encode( webAppContextPath, "UTF-8" );
				final String  webAppPrefix = webAppPrefixes[webAppIdx];

				final CompositeData  seRequestDataBaseline = (CompositeData) thisMsAttrMap.get( webAppPrefix + "RequestRecentStatistics" );
				if ( seRequestDataBaseline == null )
					continue;
				final String  webAppLabelKey = webAppLabelKeys[webAppIdx];
				int  rowCount = 0;

				final int  seSessionLimit = (Integer) thisMsAttrMap.get( webAppPrefix + "MaxAverageActiveSessionsThreshold" );
				final int  activeSessions = (Integer) thisMsAttrMap.get( webAppPrefix + "ActiveSessions" );  // do Integer to int conversion only once
				final double  activeSessionsBaseline = (Double) ((CompositeData)thisMsAttrMap.get( webAppPrefix + "SessionBaselineStatistics" )).get( "activeSessionsAverage" );

				CompositeData  seRequestDataRecent = (CompositeData) thisMsAttrMap.get( webAppPrefix + "RequestBaselineStatistics" );
				if ( seRequestDataRecent == null )
					seRequestDataRecent = seRequestDataBaseline;
				final long  seCompletedRequestsRecent = (Long) seRequestDataRecent.get( "completedRequests" );
				final long  seCompletedRequestsBaseline = (Long) seRequestDataBaseline.get( "completedRequests" );
				final double  seRespLimit = (Double) thisMsAttrMap.get( webAppPrefix + "RequestTimeWarnThreshold" );
				final double  seAvgRespSecsRecent = (Double) seRequestDataRecent.get( "requestSecondsAverage" );
				final double  seAvgRespSecsBaseline = (Double) seRequestDataBaseline.get( "requestSecondsAverage" );
				final int  seRequestLimit = (Integer) thisMsAttrMap.get( webAppPrefix + "MaxAverageActiveRequestsThreshold" );
				final int  seMaxRequestsRecent = (Integer) seRequestDataRecent.get( "activeRequestsMax" );
				final int  seMaxRequestsBaseline = (Integer) seRequestDataBaseline.get( "activeRequestsMax" );
				final double  seAvgRequestsRecent = (Double) seRequestDataRecent.get( "activeRequestsAverage" );
				final double  seAvgRequestsBaseline = (Double) seRequestDataBaseline.get( "activeRequestsAverage" );

			}
		}catch(NullPointerException e){
			LOGGER.error("Null pointer",e);
		}catch(Exception e){
			LOGGER.error("Null pointer",e);
		}

		CompositeData  recentMcData = (CompositeData) thisMsAttrMap.get( "RecentStatistics" );
		final CompositeData  baselineMcData = (CompositeData) thisMsAttrMap.get( "BaselineStatistics" );
		if ( recentMcData == null )
			recentMcData = baselineMcData;

		final long  completedContextsRecent = (Long) recentMcData.get( "completedContexts" );
		final long  completedContextsBaseline = (Long) baselineMcData.get( "completedContexts" );

		final int  maxContextsRecent = (Integer) recentMcData.get( "activeContextsMax" );
		final int  maxContextsBaseline = (Integer) baselineMcData.get( "activeContextsMax" );
		final double  avgContextsRecent = (Double) recentMcData.get( "activeContextsAverage" );
		final double  avgContextsBaseline = (Double) baselineMcData.get( "activeContextsAverage" );
		final Double  msSystemLoadAverage = (Double) thisMsAttrMap.get( "SystemLoadAverage" );


		for ( Map.Entry<String,Map<String,Object>> smToAttrsEntry : smToAttrMap.entrySet() )
		{
			final String  serverManagerName = smToAttrsEntry.getKey();
			final String  encodedServerManagerName = URLEncoder.encode( serverManagerName, "UTF-8" );
			final boolean  isDefaultSm = ( ( defaultSmJvmName != null ) && defaultSmJvmName.equals( serverManagerName ) );
			final Map<String,Object>  smAttrMap = smToAttrsEntry.getValue();
			final Map<String,Map<String,Object>>  msToAttrMap = smToMsToAttrMap.get( serverManagerName );
			final Map<String,Throwable>  msToExceptionMap = smToMsToExceptionMap.get( serverManagerName );
			final String  smChartQueryString = "jvmId=" + encodedServerManagerName + "&amp;jvmStartTime=" + smAttrMap.get( "StartTime" );
			final Map<String,String>  msChartQueryStrings = MBeanUtilities.newHashMap( msToAttrMap.size(), 0.75f );
			for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
			{
				final String  methodServerName = msToAttrMapEntry.getKey();
				final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
				final String  msChartQueryString = "jvmId=" + URLEncoder.encode( (String) msAttrs.get( "Name" ), "UTF-8" ) +
						"&amp;jvmStartTime=" + msAttrs.get( "StartTime" ) + "&amp;msName=" + URLEncoder.encode( methodServerName, "UTF-8" ) +
						"&amp;smName=" + encodedServerManagerName;
				msChartQueryStrings.put( methodServerName, msChartQueryString );
			}

			final boolean  cacheMaster = Boolean.TRUE.equals( smAttrMap.get( "CacheMaster" ) );
			final boolean  smDeadlocked = ( smAttrMap.get( "Deadlocked" ) != null );
			final double  smPercTimeInGCLimit = (Double) smAttrMap.get( "PercentTimeSpentInGCThreshold" );
			final double  smPercTimeInGCRecent = (Double) smAttrMap.get( "RecentPercentTimeSpentInGC" );
			final double  smPercTimeInGCBaseline = (Double) smAttrMap.get( "OverallPercentTimeSpentInGC" );
			final double  smPercCpuLimit = (Double) smAttrMap.get( "ProcessPercentCpuThreshold" );
			final double  smPercCpuUsedOverall = (Double) smAttrMap.get( "AverageProcessPercentCpu" );  // using overall rather than baseline...
			final CompositeData  smRecentCpuData = (CompositeData) smAttrMap.get( "RecentCpuData" );
			final double  smPercHeapLimit = (Double) smAttrMap.get( "HeapPercentUsageThreshold" ), smPercHeapUsed = (Double) smAttrMap.get( "HeapPercentUsage" );
			final double  smPercPermLimit = (Double) smAttrMap.get( "PermGenPercentUsageThreshold" ), smPercPermUsed = (Double) smAttrMap.get( "PermGenPercentUsage" );
			final String  smPhysMemInfo = renderSysMemInfo( smAttrMap, "FreePhysicalMemorySize", "TotalPhysicalMemorySize", decimalFormat, RB );
			final String  smSwapMemInfo = renderSysMemInfo( smAttrMap, "FreeSwapSpaceSize", "TotalSwapSpaceSize", decimalFormat, RB );
			final Double  smSystemLoadAverage = (Double) smAttrMap.get( "SystemLoadAverage" );

			for ( Map<String,Object> msAttrs : msToAttrMap.values() ) {
				xmlEscape( renderMillis( (Long) msAttrs.get( "Uptime" ), RB, Locale.ENGLISH ) );
			}

			for ( Map<String,Object> msAttrs : msToAttrMap.values() )
			{
				final boolean  msDeadlocked1 = ( msAttrs.get( "DeadlockedThreadIds" ) != null );
				getXmlEscapedString( RB, msDeadlocked1 ? serverStatusResource.YES : serverStatusResource.NO );
			}


			System.out.println(wcDsAttrMap);
			System.out.println(siteStatusData);
			System.out.println(getDefaultServerManagerJvmName());


		}
		return jsonSystemObject;
	}


	//Impo Utilties
	private static String  getDefaultServerManagerJvmName()
	{
		try
		{
			final JmxConnectInfo  jmxConnectInfo = (JmxConnectInfo) RemoteServerManager.getDefault().getInfo().jmxConnectInfo;
			return ( jmxConnectInfo.getJvmName() );
		}
		catch ( VirtualMachineError e )
		{
			throw e;
		}
		catch ( Throwable t )
		{
			logger.error( "Failed to retrieve JVM name of default server manager", t );
			return ( null );
		}
	}

	private static CompositeData  getVaultSiteStatusInfo()
	{
		try
		{
			// Use SelfAwareMBean.getSelfAwareMBean() and caste as this will be faster than going through MBeanServer
			return ( ((SiteMonitorMBean)SelfAwareMBean.getSelfAwareMBean( vaultSitesMBeanName )).getSiteStatusInfo() );
		}
		catch ( VirtualMachineError e )
		{
			throw e;
		}
		catch ( Throwable t )
		{
			logger.error( "Failed to retrieve vault site status info", t );
			return ( null );
		}
	}


	private static Map<String,Object>  collate( final Map<ObjectName,AttributeList> mbeanAttrMaps[] )
	{
		final Map<String,Object>  collationMap = new HashMap<>();
		for ( Map<ObjectName,AttributeList> mbeanAttrMap : mbeanAttrMaps )
			for ( Map.Entry<ObjectName,AttributeList> mbeanAttrMapEntry : mbeanAttrMap.entrySet() )
				collate( mbeanAttrMapEntry.getKey(), collationMap, mbeanAttrMapEntry.getValue() );
		return ( collationMap );
	}

	// collect attribute list into a more handy map form, ensuring uniqueness amongst keys
	//   1) places a prefix on attribute names from WHC and Solr web apps
	//   2) places a prefix on BaselineStatistics and RecentStatistics attribute names from ServletSessions' and ServletRequests'
	private static void  collate( final ObjectName objectName, final Map<String,Object> dataMap, final AttributeList attrs )
	{
		final String  webAppContextPath = objectName.getKeyPropertyList().get( MBeanRegistry.WEB_APP_CONTEXT_PROP_KEY );
		if ( webAppContextPath != null )
		{
			final boolean  isSolrWebApp = solrWebAppPath.equals( webAppContextPath );
			final boolean  isWhcWebApp = whcWebAppPath.equals( webAppContextPath );
			final String  webAppPrefix = ( isSolrWebApp ? SOLR_PREFIX : ( isWhcWebApp ? WHC_PREFIX : "" ) );
			final boolean  isSessionAttr = "ServletSessions".equals( objectName.getKeyPropertyList().get( MBeanRegistry.SERVLET_ENGINE_SUBSYSTEM_PROP_KEY ) );
			for ( Object attrObj : attrs )
			{
				final Attribute  attr = (Attribute) attrObj;
				String  attrName = attr.getName();
				if ( "BaselineStatistics".equals( attrName ) || "RecentStatistics".equals( attrName ) )
					attrName = ( isSessionAttr ? "Session" : "Request" ) + attrName;  // currently assume these attributes are either from ServletSessions or ServletRequests mbeans
				dataMap.put( webAppPrefix + attrName, attr.getValue() );
			}
		}
		else
			for ( Object attrObj : attrs )
			{
				final Attribute  attr = (Attribute) attrObj;
				dataMap.put( attr.getName(), attr.getValue() );
			}
	}

	@SuppressWarnings( "unchecked" )
	private static void  collateResultsFromServerManager( final String serverManagerName, final Map<String,Object> resultsForAllSms,
			final Map<String,Map<String,Object>> jvmNameToAttrMap,
			final Map<String,Throwable> jvmNameToExceptionMap )
	{
		if ( resultsForAllSms != null )
		{
			final Object smResultsObj = resultsForAllSms.get( serverManagerName );
			if ( smResultsObj instanceof Map )
			{
				final Map<String,Object>  smResults = (Map<String,Object>) smResultsObj;
				for ( Map.Entry<String,Object> smResultsEntry : smResults.entrySet() )
				{
					final String  jvmName = smResultsEntry.getKey();
					final Object  result = smResultsEntry.getValue();
					if ( result instanceof Map[] )
						jvmNameToAttrMap.put( jvmName, collate( (Map<ObjectName,AttributeList>[]) result ) );
					else if ( result instanceof Throwable )
						jvmNameToExceptionMap.put( jvmName, (Throwable) result );
				}
			}
		}
	}

	private static boolean  containsResultsForMs( final Map<String,Map<String,Map<String,Object>>> smToMsAttrMap, final String methodServerName )
	{
		if ( smToMsAttrMap != null )
			for ( Map<String,Map<String,Object>> msToAttrMap : smToMsAttrMap.values() )
				if ( msToAttrMap.containsKey( methodServerName ) )
					return ( false );
		return ( false );
	}

	private static final int  MILLIS_PER_SECOND = 1000;
	private static final int  MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	private static final int  MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	private static final int  MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	// render millisecond duration into days, hours, minutes, and seconds
	private static String  renderMillis( final long millis, final ResourceBundle resourceBundle, final Locale locale )
	{
		final long  days = millis / MILLIS_PER_DAY;
		long  remainder = millis % MILLIS_PER_DAY;
		final long  hours = remainder / MILLIS_PER_HOUR;
		remainder %= MILLIS_PER_HOUR;
		final long  minutes = remainder / MILLIS_PER_MINUTE;
		remainder %= MILLIS_PER_MINUTE;
		final long  seconds = remainder / MILLIS_PER_SECOND;
		remainder %= MILLIS_PER_SECOND;
		final String  msgFormat = resourceBundle.getString( ( days > 0 ) ? serverStatusResource.TIME_DURATION_FORMAT_W_DAYS
				: serverStatusResource.TIME_DURATION_FORMAT_WO_DAYS );
		final DecimalFormat  twoDigitFormat = new DecimalFormat( "00" );
		return ( MBeanUtilities.formatMessage( msgFormat,
				NumberFormat.getNumberInstance( locale ).format( days ),  // in case # of days goes into thousands, use proper formatting...
				twoDigitFormat.format( hours ),
				twoDigitFormat.format( minutes ),
				twoDigitFormat.format( seconds ),
				remainder ) );
	}

	private static final double  BYTES_PER_MB_AS_DBL = 1024.0 * 1024.0;

	// render free vs. total physical or swap memory; these items may be null as they're Sun-specific features
	private static String  renderSysMemInfo( final Map<String,Object> dataMap,
			final String freeMemItemName, final String totalMemItemName,
			final DecimalFormat decimalFormat, final ResourceBundle resourceBundle )
	{
		final Long  freeMem = (Long) dataMap.get( freeMemItemName );
		if ( freeMem != null )
		{
			final StringBuilder  sb = new StringBuilder();
			sb.append( decimalFormat.format( freeMem / BYTES_PER_MB_AS_DBL ) );
			sb.append( resourceBundle.getString( serverStatusResource.MB ) );
			final Long  totalMem = (Long) dataMap.get( totalMemItemName );
			if ( totalMem != null )
			{
				sb.append( " (" );
				sb.append( decimalFormat.format( 100.0 * (double)freeMem / (double)totalMem ) );
				sb.append( decimalFormat.getDecimalFormatSymbols().getPercent() );
				sb.append( ')' );
			}
			return ( sb.toString() );
		}
		else
			return ( null );
	}

	private static ObjectName  newObjectName( final String objectNameString )
	{
		try
		{
			return ( new ObjectName( objectNameString ) );
		}
		catch ( Exception e )
		{
			logger.error( "Could not create ObjectName from " + objectNameString, e );
			if ( e instanceof RuntimeException )
				throw (RuntimeException) e;
			throw new RuntimeException( e );
		}
	}

	private static String   xmlEscape( final char ch )
	{
		final String  replacement = MBeanUtilities.getXmlReplacementString( ch );
		if ( replacement != null )
			return ( replacement );
		return ( new String( new char[] { ch } ) );
	}

	private static String  xmlEscape( final String string )
	{
		return ( MBeanUtilities.xmlEscape( string ) );
	}

	private static String  xmlEscape( Object object )
	{
		// catch-all location to catch this obnoxious exception type to provide a better toString() rendering here
		// Since this exception type passes null to its super class as the causal exception, it will never be represented in getMessage()!
		// This is to fix SPR #2158634 and replaces a previous, improper fix for this same issue in this location.
		if ( object instanceof UndeclaredThrowableException )
		{
			final UndeclaredThrowableException  undeclaredThrowableException = (UndeclaredThrowableException) object;
			object = object.toString() + "; undeclared throwable: " + undeclaredThrowableException.getUndeclaredThrowable();
		}

		return ( ( object != null ) ? xmlEscape( object.toString() ) : null );
	}

	private static String  getXmlEscapedString( final ResourceBundle bundle, final String key )
	{
		return ( xmlEscape( bundle.getString( key ) ) );
	}
}
