
package com.custom;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import org.apache.log4j.Logger;
import wt.access.AccessControlException;
import wt.intersvrcom.SiteMonitorMBean;
import wt.jmx.core.MBeanRegistry;
import wt.jmx.core.MBeanUtilities;
import wt.jmx.core.SelfAwareMBean;
import wt.jmx.core.mbeans.Dumper;
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

public final class SystemStatus  {/*


 private static final Logger  logger = LogR.getLogger( "wtcore.jsp.jmx.serverStatus" );  // must be assigned prior to newObjectName() calls

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

 private static final String  rbClassname = serverStatusResource.class.getName();


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

 // collect into a more handy map form
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
         return ( true );
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

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(1);
    _jspx_dependants.put("/WEB-INF/util.tld", Long.valueOf(1179423738000L));
  }


  private javax.el.ExpressionFactory _el_expressionfactory;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }




 final boolean  isPrivilegedUser = AccessUtil.isSiteAdministrator();

 // get JVM name of default server manager
 final String  defaultSmJvmName = getDefaultServerManagerJvmName();

 // get time right before we start result collection
 final long  dataCollectionStart = System.currentTimeMillis();

 // collect data for WindchillDS
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
     }
     catch ( InstanceNotFoundException e )
     {
       logger.debug( "Active users MBean not found", e );
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
       logger.error( "Problem acquiring total active user count", t );
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
   logger.error( "Failed to retrieve results from server manager", t );
   resultRetrievalException = t;  // remember but otherwise eat exception
 }

 // we're done collecting data now (usually, but not quite always, hence the lack of 'final' here)
 long  dataCollectionEnd = System.currentTimeMillis();

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



 // determine resource bundle, decimal and date format, and localized percent symbol for use throughout and set up various other data for re-use
 final DecimalFormatSymbols  decimalSymbols = new DecimalFormatSymbols( localeObj );
 final DecimalFormat  decimalFormat = new DecimalFormat( "0.###", decimalSymbols );
 final String  percentString = xmlEscape( decimalSymbols.getPercent() );  // pre-escaped localized % symbol
 final SimpleDateFormat  dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS Z" );
 dateFormat.setTimeZone( WTContext.getContext().getTimeZone() );
 final String  webAppContextPaths[] = { windchillWebAppPath, whcWebAppPath, solrWebAppPath };
 final String  webAppPrefixes[] = { "", WHC_PREFIX, SOLR_PREFIX };
 final String  webAppLabelKeys[] = { serverStatusResource.GENERAL_SERVLET_REQUESTS_LABEL,
                                     serverStatusResource.HELP_SERVLET_REQUESTS_LABEL,
                                     serverStatusResource.SOLR_SERVLET_REQUESTS_LABEL };
 final String  percContextTimeAttrNames[] =
 {
   "percentageOfContextTimeInJDBCCalls",
   "percentageOfContextTimeInJDBCConnWait",
   "percentageOfContextTimeInJNDICalls",
   "percentageOfContextTimeInRemoteCacheCalls"
 };
 final String  percContextTimeLabels[] =
 {
   RB.getString( serverStatusResource.IN_JDBC_CALLS ),
   RB.getString( serverStatusResource.IN_JDBC_CONN_WAIT ),
   RB.getString( serverStatusResource.IN_JNDI_CALLS ),
   RB.getString( serverStatusResource.IN_REMOTE_CACHE_CALLS )
 };
 final String  percContextTimeChartJsps[] =
 {
   "percTimeInJdbcChart.jsp",
   "percTimeInJdbcWaitChart.jsp",
   "percTimeInJndiChart.jsp",
   "percTimeInRemoteCacheChart.jsp",
 };

      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\r\n");
      out.write("<title>");
      out.print(getXmlEscapedString( RB, serverStatusResource.SERVER_STATUS ));
      out.write("</title>\r\n");
      out.write("<link rel=\"stylesheet\" href=\"serverStatusStyles.css\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("function launchWindow(url,title,options) { var x = window.open(url, title, options); x.focus(); }\r\n");
      out.write("</script>\r\n");
      out.write("</head>\r\n");
      out.write("<body class=\"wizbodybg\">\r\n");
      out.write("\r\n");
      out.write("<table class=\"wizHdr\" width=\"100%\">\r\n");
      out.write("<tbody>\r\n");
      out.write("<tr>\r\n");
      out.write("<td class=\"wizTitleTD\" align=\"left\">&#160;");
      out.print(getXmlEscapedString( RB, serverStatusResource.SERVER_STATUS ));
      out.write("</td>\r\n");
      out.write("<td class=\"wizTitle\" align=\"right\">\r\n");
      out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\r\n");
      out.write("<tr>\r\n");
      out.write("<td class=\"wizTitle\" width=\"1\"> &#160; </td>\r\n");
      out.write("<td class=\"wizTitle\" width=\"1\" align=\"right\">\r\n");
      out.write("<a onClick=\"launchWindow('");
      out.print(helpUrl);
      out.write("','HelpWindow','toolbar=yes,location=yes,directories=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes'); return false;\"\r\n");
      out.write("   href=\"");
      out.print(helpUrl);
      out.write("\" class=\"hlpTxt\" title=\"");
      out.print(getXmlEscapedString( RB, serverStatusResource.HELP ));
      out.write("\">\r\n");
      out.write("<img border=\"0\" class=\"hlpBg hlpBdr\" src=\"../../../netmarkets/images/help_tablebutton.gif\"\r\n");
      out.write("     alt=\"");
      out.print(getXmlEscapedString( RB, serverStatusResource.HELP ));
      out.write("\" title=\"");
      out.print(getXmlEscapedString( RB, serverStatusResource.HELP ));
      out.write("\"/>\r\n");
      out.write("</a>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("<table width=\"100%\">\r\n");
      out.write("\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<td>\r\n");
      out.write("<table>\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"row\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.CURRENT_ACTIVE_USERS ));
      out.write(": </th>\r\n");
      out.write("<td><a href='chartWrapper.jsp?chartPg=totalActiveUserChart.jsp'>");
      out.print(( activeUsers != null ) ? decimalFormat.format( activeUsers ) : "?");
      out.write("</a></td><td> </td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("<td>\r\n");
      out.write("<table>\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"row\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.SERVER_MANAGERS ));
      out.write(": </th>\r\n");
      out.write("<td>\r\n");

 if ( serverManagerResults != null )
   for ( String serverManagerName : serverManagerResults.keySet() )
   {
     final Map<String,Object>  smAttrMap = smToAttrMap.get( serverManagerName );
     final boolean  cacheMaster = ( ( smAttrMap != null ) && Boolean.TRUE.equals( smAttrMap.get( "CacheMaster" ) ) );
     final boolean  isDefaultSm = ( ( defaultSmJvmName != null ) && defaultSmJvmName.equals( serverManagerName ) );

      out.write("\r\n");
      out.write("<a href='#");
      out.print(serverManagerName);
      out.write('\'');
      out.write('>');
      out.print(serverManagerName);
      out.write("</a>");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(472,60) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f0.setTest(isDefaultSm);
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('*');
          int evalDoAfterBody = _jspx_th_c_005fif_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f1.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(472,98) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f1.setTest(cacheMaster);
      int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
      if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write(' ');
          out.write('(');
          out.print(getXmlEscapedString( RB, serverStatusResource.MASTER ));
          out.write(')');
          int evalDoAfterBody = _jspx_th_c_005fif_005f1.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
      out.write("<br />\r\n");

   }

      out.write("\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f2.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(480,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f2.setTest(isPrivilegedUser);
      int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
      if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<td align='right'>\r\n");
          wt.httpgw.URLFactory url_factory = null;
          url_factory = (wt.httpgw.URLFactory) _jspx_page_context.getAttribute("url_factory", javax.servlet.jsp.PageContext.REQUEST_SCOPE);
          if (url_factory == null){
            url_factory = new wt.httpgw.URLFactory();
            _jspx_page_context.setAttribute("url_factory", url_factory, javax.servlet.jsp.PageContext.REQUEST_SCOPE);
          }
          out.write('\r');
          out.write('\n');
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
          int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
          if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\r');
              out.write('\n');
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              // /wtcore/jsp/jmx/serverStatus.jsp(484,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f0.setTest(WTAppServerPropertyHelper.isAppServerDeployment());
              int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
              if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("<a title=\"");
                  out.print(getXmlEscapedString( RB, serverStatusResource.CONTACT_TECHNICAL_SUPPORT_TOOLTIP ));
                  out.write("\"\r\n");
                  out.write("   onClick=\"launchWindow('contactTechSupport.jsp','ContactTechSupport','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,height=200,width=450'); return false;\"\r\n");
                  out.write("   href=\"contactTechSupport.jsp\">\r\n");
                  out.print(getXmlEscapedString( RB, serverStatusResource.CONTACT_TECHNICAL_SUPPORT ));
                  out.write("\r\n");
                  out.write("</a>\r\n");
                  int evalDoAfterBody = _jspx_th_c_005fwhen_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
              out.write('\r');
              out.write('\n');
              //  c:otherwise
              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
              int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("<a title=\"");
                  out.print(getXmlEscapedString( RB, serverStatusResource.CONTACT_TECHNICAL_SUPPORT_TOOLTIP ));
                  out.write("\"\r\n");
                  out.write("   onClick=\"launchWindow('");
                  out.print(url_factory.getBaseURL().toString() );
                  out.write("app/#netmarkets/jsp/customersupport/customerSupportPage.jsp','System Configuration Collector','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,height=740,width=865'); return false;\"\r\n");
                  out.write("   href=\"");
                  out.print(url_factory.getBaseURL().toString() );
                  out.write("app/#netmarkets/jsp/customersupport/customerSupportPage.jsp\">\r\n");
                  out.print(getXmlEscapedString( RB, serverStatusResource.SITE_INFORMATION_LINK ));
                  out.write("\r\n");
                  out.write("</a>\r\n");
                  int evalDoAfterBody = _jspx_th_c_005fotherwise_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
              out.write('\r');
              out.write('\n');
              int evalDoAfterBody = _jspx_th_c_005fchoose_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
          out.write("\r\n");
          out.write("</td>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f2.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
      out.write("\r\n");
      out.write("</tr>\r\n");
      out.write("\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<td>\r\n");
      out.write("<table>\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"row\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.WINDCHILL_DS ));
      out.write(": </th>\r\n");
      out.write("<td><a href=\"#WindchillDS\">");
      out.print(getXmlEscapedString( RB, (wcDsResultRetrievalException == null ) ? serverStatusResource.UP : serverStatusResource.UNREACHABLE ));
      out.write("</a></td>\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f3.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(512,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f3.setTest(vaultSiteStatusInfo != null);
      int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
      if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<td>\r\n");

 int  reachableSites = 0;
 int  unreachableSites = 0;
 for ( Object siteStatusObject : siteStatusData.values() )
 {
   final CompositeData  siteStatus = (CompositeData) siteStatusObject;
   final String  lastStatus = (String) siteStatus.get( "lastStatus" );
   if ( SiteMonitorMBean.OK_STATUS.equals( lastStatus ) )
     ++reachableSites;
   else
     ++unreachableSites;
 }

          out.write("\r\n");
          out.write("<table>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.FILE_VAULT_SITES ));
          out.write(": </th>\r\n");
          out.write("<td>\r\n");
          out.write("<a href=\"#FileVaultSites\">\r\n");
          out.print(xmlEscape( ( unreachableSites == 0 ) ? RB.getString( serverStatusResource.REACHABLE )
                                        : MBeanUtilities.formatMessage( RB.getString( serverStatusResource.REACHABLE_SITES_MSG ),
                                                                        reachableSites, unreachableSites ) ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f3.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f4.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(541,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f4.setTest(isPrivilegedUser);
      int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
      if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<td align='right' ");
          out.print(( vaultSiteStatusInfo != null ) ? "" : "colspan='2'");
          out.write(">\r\n");
          out.write("<a href=\"index.jsp\" title=\"");
          out.print(getXmlEscapedString( RB, serverStatusResource.MONITORING_TOOLS_TOOLTIP ));
          out.write("\">\r\n");
          out.print(getXmlEscapedString( RB, serverStatusResource.MONITORING_TOOLS ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f4.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
      out.write("\r\n");
      out.write("</tr>\r\n");
      out.write("</table>\r\n");
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f5.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(551,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f5.setTest(thisMsAttrMap != null);
      int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
      if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\r');
          out.write('\n');
 { // explicitly limit scope of variables herein 
          out.write("\r\n");
          out.write("<table>\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<td>\r\n");
          out.write("<div class=\"frame, frame_outer\">\r\n");
          out.write("<span style=\"width: 100%\">  ");
          out.write('\r');
          out.write('\n');
          out.write("\r\n");
          out.write("<div class=\"frameTitle\">\r\n");
          out.write("<table class=\"pp\" width=\"100%\">\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr>\r\n");
          out.write("<td align=\"left\" valign=\"top\">\r\n");
          out.write("<table class=\"pp\">\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr>\r\n");
          out.write("<td class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.METHOD_SERVER ));
          out.write(": </td>\r\n");
          out.write("<td class=\"ppdata\">");
          out.print(xmlEscape( thisMethodServerName ));
          out.write('*');
          out.write(' ');
          out.write('(');
          out.print(xmlEscape( thisJvmName ));
          out.write(")</td>\r\n");
          out.write("</tr>\r\n");

 final Object  msJmxUrl = thisMsAttrMap.get( "JmxServiceURL" );

          out.write('\r');
          out.write('\n');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
          // /wtcore/jsp/jmx/serverStatus.jsp(574,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f6.setTest(msJmxUrl != null);
          int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
          if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<tr>\r\n");
              out.write("<td class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.JMX_URL ));
              out.write(": </td>\r\n");
              out.write("<td class=\"ppdata\">");
              out.print(xmlEscape( msJmxUrl ));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              int evalDoAfterBody = _jspx_th_c_005fif_005f6.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
          out.write("\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          out.write("<td align=\"right\" valign=\"top\">\r\n");
          out.write("<table class=\"pp\">\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr>\r\n");
          out.write("<td class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.UPTIME ));
          out.write(": </td>\r\n");
          out.write("<td class=\"ppdata\">");
          out.print(xmlEscape( renderMillis( (Long) thisMsAttrMap.get( "Uptime" ), RB, localeObj ) ));
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr>\r\n");
          out.write("<td class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.DEADLOCKED ));
          out.write(": </td>\r\n");

 final boolean  msDeadlocked = ( thisMsAttrMap.get( "DeadlockedThreadIds" ) != null );

          out.write("\r\n");
          out.write("<td class=\"ppdata\" ");
          out.print(msDeadlocked ? "style='color: red'" : "");
          out.write('>');
          out.print(getXmlEscapedString( RB, msDeadlocked ? serverStatusResource.YES : serverStatusResource.NO ));
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</div>\r\n");
          out.write("\r\n");
          out.write("<div class=\"frameContent\">\r\n");
          out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<td>\r\n");
          out.write("\r\n");
          out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
          out.write("<thead>\r\n");
          out.write("<tr>\r\n");
          out.write("<th/>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.RECENT ));
          out.write("&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.BASELINE ));
          out.write("&#160;</span></th>\r\n");
          out.write("</tr>\r\n");
          out.write("</thead>\r\n");
          out.write("<tbody class=\"tablebody\">\r\n");
          out.write("<tr class=\"o\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.TIME_IN_GC ));
          out.write("&#160;</th>\r\n");

 final double  msPercTimeInGCLimit = (Double) thisMsAttrMap.get( "PercentTimeSpentInGCThreshold" );
 final double  msPercTimeInGCRecent = (Double) thisMsAttrMap.get( "RecentPercentTimeSpentInGC" );
 final double  msPercTimeInGCOverall = (Double) thisMsAttrMap.get( "OverallPercentTimeSpentInGC" );  // using overall rather than baseline...
 final String  thisMsChartQueryString = "jvmId=" + URLEncoder.encode( thisJvmName, "UTF-8" ) + "&amp;jvmStartTime=" + thisMsAttrMap.get( "StartTime" ) +
                                        "&amp;msName=" + URLEncoder.encode( thisMethodServerName, "UTF-8" );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( msPercTimeInGCLimit > 0 ) && ( msPercTimeInGCRecent > msPercTimeInGCLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( msPercTimeInGCRecent ));
          out.print(percentString);
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=msGcChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercTimeInGCLimit > 0 ) && ( msPercTimeInGCOverall > msPercTimeInGCLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercTimeInGCOverall ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.CPU_USED_BY_PROCESS ));
          out.write("&#160;</th>\r\n");

 final double  msPercCpuLimit = (Double) thisMsAttrMap.get( "ProcessPercentCpuThreshold" );
 final double  msPercCpuUsedOverall = (Double) thisMsAttrMap.get( "AverageProcessPercentCpu" );  // using overall rather than baseline...
 final CompositeData  recentCpuData = (CompositeData) thisMsAttrMap.get( "RecentCpuData" );
 final double  smPercCpuUsedRecent = ( ( recentCpuData != null ) ? (Double) recentCpuData.get( "processPercentCpu" ) : msPercCpuUsedOverall );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( msPercCpuLimit > 0 ) && ( smPercCpuUsedRecent > msPercCpuLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( smPercCpuUsedRecent ));
          out.print(percentString);
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=msCpuChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercCpuLimit > 0 ) && ( msPercCpuUsedOverall > msPercCpuLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercCpuUsedOverall ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");

 for ( int webAppIdx = 0; webAppIdx < webAppPrefixes.length; ++webAppIdx )
 {
   final String  webAppContextPath = webAppContextPaths[webAppIdx];
   final String  encodedWebAppContextPath = URLEncoder.encode( webAppContextPath, "UTF-8" );
   final String  webAppPrefix = webAppPrefixes[webAppIdx];
   // if we do not have any data for this web app in any method server for this server manager, then don't generate empty table lines
   final CompositeData  seRequestDataBaseline = (CompositeData) thisMsAttrMap.get( webAppPrefix + "RequestRecentStatistics" );
   if ( seRequestDataBaseline == null )
     continue;
   final String  webAppLabelKey = webAppLabelKeys[webAppIdx];
   int  rowCount = 0;

          out.write("\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, webAppLabelKey ));
          out.write("</span>\r\n");
          out.write("</th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">&#160;</span></th>\r\n");
          out.write("</tr>\r\n");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
          // /wtcore/jsp/jmx/serverStatus.jsp(673,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f7.setTest("".equals( webAppPrefix ));
          int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
          if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write(' ');
              out.write(' ');
              out.write("\r\n");
              out.write("<tr class='");
              out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
              out.write("'>\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.ACTIVE_SESSIONS ));
              out.write("</th>\r\n");

   final int  seSessionLimit = (Integer) thisMsAttrMap.get( webAppPrefix + "MaxAverageActiveSessionsThreshold" );
   final int  activeSessions = (Integer) thisMsAttrMap.get( webAppPrefix + "ActiveSessions" );  // do Integer to int conversion only once
   final double  activeSessionsBaseline = (Double) ((CompositeData)thisMsAttrMap.get( webAppPrefix + "SessionBaselineStatistics" )).get( "activeSessionsAverage" );

              out.write("\r\n");
              out.write("<td class=\"c\" ");
              out.print(( ( seSessionLimit > 0 ) && ( activeSessions > seSessionLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.print(decimalFormat.format( activeSessions ));
              out.write("</td>\r\n");
              out.write("<td class=\"c\">\r\n");
              out.write("<a href='chartWrapper.jsp?chartPg=sessionsChart.jsp&amp;");
              out.print(thisMsChartQueryString);
              out.write("&amp;contextPath=");
              out.print(encodedWebAppContextPath);
              out.write("'\r\n");
              out.write("   ");
              out.print(( ( seSessionLimit > 0 ) && ( activeSessionsBaseline > seSessionLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.write('\r');
              out.write('\n');
              out.print(decimalFormat.format( activeSessionsBaseline ));
              out.write("\r\n");
              out.write("</a>\r\n");
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              int evalDoAfterBody = _jspx_th_c_005fif_005f7.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
          out.write('\r');
          out.write('\n');

   CompositeData  seRequestDataRecent = (CompositeData) thisMsAttrMap.get( webAppPrefix + "RequestBaselineStatistics" );
   if ( seRequestDataRecent == null )
     seRequestDataRecent = seRequestDataBaseline;

          out.write("\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.COMPLETED_REQUESTS ));
          out.write("</th>\r\n");

   final long  seCompletedRequestsRecent = (Long) seRequestDataRecent.get( "completedRequests" );
   final long  seCompletedRequestsBaseline = (Long) seRequestDataBaseline.get( "completedRequests" );

          out.write("\r\n");
          out.write("<td class=\"c\">");
          out.print(decimalFormat.format( seCompletedRequestsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=requestsPerMinuteChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("&amp;contextPath=");
          out.print(encodedWebAppContextPath);
          out.write("'>\r\n");
          out.print(decimalFormat.format( seCompletedRequestsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.AVERAGE_RESPONSE_TIME ));
          out.write("</th>\r\n");

   final double  seRespLimit = (Double) thisMsAttrMap.get( webAppPrefix + "RequestTimeWarnThreshold" );
   final double  seAvgRespSecsRecent = (Double) seRequestDataRecent.get( "requestSecondsAverage" );
   final double  seAvgRespSecsBaseline = (Double) seRequestDataBaseline.get( "requestSecondsAverage" );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( seRespLimit > 0 ) && ( seAvgRespSecsRecent > seRespLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( seAvgRespSecsRecent ));
          out.write("&#160;");
          out.print(getXmlEscapedString( RB, serverStatusResource.SECONDS ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=responseTimeChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("&amp;contextPath=");
          out.print(encodedWebAppContextPath);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( seRespLimit > 0 ) && ( seAvgRespSecsBaseline > seRespLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( seAvgRespSecsBaseline ));
          out.write("&#160;");
          out.print(getXmlEscapedString( RB, serverStatusResource.SECONDS ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.MAXIMUM_CONCURRENCY ));
          out.write("</th>\r\n");

   final int  seRequestLimit = (Integer) thisMsAttrMap.get( webAppPrefix + "MaxAverageActiveRequestsThreshold" );
   final int  seMaxRequestsRecent = (Integer) seRequestDataRecent.get( "activeRequestsMax" );
   final int  seMaxRequestsBaseline = (Integer) seRequestDataBaseline.get( "activeRequestsMax" );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( seRequestLimit > 0 ) && ( seMaxRequestsRecent > seRequestLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( seMaxRequestsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=maxRequestConcChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("&amp;contextPath=");
          out.print(encodedWebAppContextPath);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( seRequestLimit > 0 ) && ( seMaxRequestsBaseline > seRequestLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( seMaxRequestsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.AVERAGE_CONCURRENCY ));
          out.write("</th>\r\n");

   final double  seAvgRequestsRecent = (Double) seRequestDataRecent.get( "activeRequestsAverage" );
   final double  seAvgRequestsBaseline = (Double) seRequestDataBaseline.get( "activeRequestsAverage" );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( seRequestLimit > 0 ) && ( seAvgRequestsRecent > seRequestLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( seAvgRequestsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=avgRequestConcChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("&amp;contextPath=");
          out.print(encodedWebAppContextPath);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( seRequestLimit > 0 ) && ( seAvgRequestsBaseline > seRequestLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( seAvgRequestsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");

 }

          out.write("\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.METHOD_CONTEXTS ));
          out.write("</span>\r\n");
          out.write("</th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">&#160;</span></th>\r\n");
          out.write("</tr>\r\n");

 CompositeData  recentMcData = (CompositeData) thisMsAttrMap.get( "RecentStatistics" );
 final CompositeData  baselineMcData = (CompositeData) thisMsAttrMap.get( "BaselineStatistics" );
 if ( recentMcData == null )
   recentMcData = baselineMcData;

          out.write("\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.COMPLETED_CONTEXTS ));
          out.write("</th>\r\n");

 final long  completedContextsRecent = (Long) recentMcData.get( "completedContexts" );
 final long  completedContextsBaseline = (Long) baselineMcData.get( "completedContexts" );

          out.write("\r\n");
          out.write("<td class=\"c\">");
          out.print(decimalFormat.format( completedContextsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=contextsPerMinuteChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("'>\r\n");
          out.print(decimalFormat.format( completedContextsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.MAXIMUM_CONCURRENCY ));
          out.write("</th>\r\n");

 final int  contextLimit = (Integer) thisMsAttrMap.get( "MaxAverageActiveContextsThreshold" );
 final int  maxContextsRecent = (Integer) recentMcData.get( "activeContextsMax" );
 final int  maxContextsBaseline = (Integer) baselineMcData.get( "activeContextsMax" );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( contextLimit > 0 ) && ( maxContextsRecent > contextLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( maxContextsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=maxContextConcChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("'>\r\n");
          out.print(decimalFormat.format( maxContextsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"o\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.AVERAGE_CONCURRENCY ));
          out.write("</th>\r\n");

 final double  avgContextsRecent = (Double) recentMcData.get( "activeContextsAverage" );
 final double  avgContextsBaseline = (Double) baselineMcData.get( "activeContextsAverage" );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( contextLimit > 0 ) && ( avgContextsRecent > contextLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( avgContextsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=avgContextConcChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( contextLimit > 0 ) && ( avgContextsBaseline > contextLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( avgContextsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.METHOD_CONTEXT_TIME ));
          out.write("</span>\r\n");
          out.write("</th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">&#160;</span></th>\r\n");
          out.write("</tr>\r\n");

 for ( int ii = 0; ii < percContextTimeAttrNames.length; ++ii )
 {
   final String  attrName = percContextTimeAttrNames[ii];
   final String  chartJsp = percContextTimeChartJsps[ii];

          out.write("\r\n");
          out.write("<tr class='");
          out.print(( ( ii % 2 ) == 0 ) ? "o" : "e" );
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(xmlEscape( percContextTimeLabels[ii] ));
          out.write("</th>\r\n");

   final double  percTimeRecent = (Double) recentMcData.get( attrName );
   final double  percTimeBaseline = (Double) baselineMcData.get( attrName );

          out.write("\r\n");
          out.write("<td class=\"c\">");
          out.print(decimalFormat.format( percTimeRecent ));
          out.print(percentString);
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=");
          out.print(chartJsp);
          out.write("&amp;");
          out.print(thisMsChartQueryString);
          out.write("'>\r\n");
          out.print(decimalFormat.format( percTimeBaseline ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");

 }

          out.write("\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          out.write("<td>&#160;&#160;</td>\r\n");
          out.write("<td>\r\n");
          out.write("\r\n");
          out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
          out.write("<tbody class=\"tablebody\">\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.MEMORY_USED ));
          out.write("&#160;</span>\r\n");
          out.write("</th>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"o\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.HEAP ));
          out.write("&#160;</th>\r\n");

 final double  msPercHeapLimit = (Double) thisMsAttrMap.get( "HeapPercentUsageThreshold" );
 final double  msPercHeapUsed = (Double) thisMsAttrMap.get( "HeapPercentUsage" );

          out.write("\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=msHeapChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercHeapLimit > 0 ) && ( msPercHeapUsed > msPercHeapLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercHeapUsed ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.PERM_GEN ));
          out.write("</th>\r\n");

 final double  msPercPermLimit = (Double) thisMsAttrMap.get( "PermGenPercentUsageThreshold" );
 final double  msPercPermUsed = (Double) thisMsAttrMap.get( "PermGenPercentUsage" );

          out.write("\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=msPermGenChart.jsp&amp;");
          out.print(thisMsChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercPermLimit > 0 ) && ( msPercPermUsed > msPercPermLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercPermUsed ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");

 final String  msPhysMemInfo = renderSysMemInfo( thisMsAttrMap, "FreePhysicalMemorySize", "TotalPhysicalMemorySize", decimalFormat, RB );
 final String  msSwapMemInfo = renderSysMemInfo( thisMsAttrMap, "FreeSwapSpaceSize", "TotalSwapSpaceSize", decimalFormat, RB );

          out.write('\r');
          out.write('\n');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
          // /wtcore/jsp/jmx/serverStatus.jsp(881,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f8.setTest(( msPhysMemInfo != null ) || ( msSwapMemInfo != null ));
          int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
          if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<tr>\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
              out.write("<span class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.AVAILABLE_SYSTEM_MEMORY ));
              out.write("&#160;</span>\r\n");
              out.write("</th>\r\n");
              out.write("</tr>\r\n");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
              // /wtcore/jsp/jmx/serverStatus.jsp(887,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f9.setTest(msPhysMemInfo != null);
              int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
              if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("<tr class=\"o\">\r\n");
                  out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
                  out.print(getXmlEscapedString( RB, serverStatusResource.PHYSICAL ));
                  out.write("&#160;</th>\r\n");
                  out.write("<td class=\"c\" nowrap=\"nowrap\">");
                  out.print(xmlEscape( msPhysMemInfo ));
                  out.write("</td>\r\n");
                  out.write("</tr>\r\n");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f9.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
              out.write('\r');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
              // /wtcore/jsp/jmx/serverStatus.jsp(893,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f10.setTest(msSwapMemInfo != null);
              int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
              if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("<tr class=\"e\">\r\n");
                  out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
                  out.print(getXmlEscapedString( RB, serverStatusResource.SWAP ));
                  out.write("&#160;</th>\r\n");
                  out.write("<td class=\"c\" nowrap=\"nowrap\">");
                  out.print(xmlEscape( msSwapMemInfo ));
                  out.write("</td>\r\n");
                  out.write("</tr>\r\n");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f10.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
              out.write('\r');
              out.write('\n');
              int evalDoAfterBody = _jspx_th_c_005fif_005f8.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
          out.write('\r');
          out.write('\n');

 final Double  msSystemLoadAverage = (Double) thisMsAttrMap.get( "SystemLoadAverage" );

          out.write('\r');
          out.write('\n');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
          // /wtcore/jsp/jmx/serverStatus.jsp(903,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f11.setTest(( msSystemLoadAverage != null ) && ( msSystemLoadAverage >= 0 ));
          int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
          if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<tr>\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
              out.write("<span class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.OTHER_SYSTEM_INFO ));
              out.write("&#160;</span>\r\n");
              out.write("</th>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr class=\"o\">\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.LOAD_AVERAGE ));
              out.write("&#160;</th>\r\n");
              out.write("<td class=\"c\">");
              out.print(decimalFormat.format( msSystemLoadAverage ));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              int evalDoAfterBody = _jspx_th_c_005fif_005f11.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
          out.write("\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</div>\r\n");
          out.write("<a href=\"#\" class=\"ppdata\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.BACK_TO_TOP ));
          out.write("</a>\r\n");
          out.write("</span>\r\n");
          out.write("</div>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
 } 
          out.write('\r');
          out.write('\n');
          int evalDoAfterBody = _jspx_th_c_005fif_005f5.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f12.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(931,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f12.setTest(localMsException != null);
      int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
      if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<h3 style='color: red'>");
          out.print(getXmlEscapedString( RB, serverStatusResource.FAILED_TO_RETRIEVE_METHOD_SERVER_DATA ));
          out.write(": </h3>\r\n");
          out.write("<table>\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" align=\"left\" style='color: red'>");
          out.print(getXmlEscapedString( RB, serverStatusResource.ERROR_EMPHASIZED ));
          out.write(": </th>\r\n");
          out.write("<td>");
          out.print(xmlEscape( localMsException ));
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f12.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f13.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(943,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f13.setTest(resultRetrievalException != null);
      int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
      if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<h3 style='color: red'>");
          out.print(getXmlEscapedString( RB, serverStatusResource.FAILED_TO_RETRIEVE_SERVER_MANAGER_DATA ));
          out.write(": </h3>\r\n");
          out.write("<table>\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" align=\"left\" style='color: red'>");
          out.print(getXmlEscapedString( RB, serverStatusResource.ERROR_EMPHASIZED ));
          out.write(": </th>\r\n");
          out.write("<td>");
          out.print(xmlEscape( resultRetrievalException ));
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f13.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');

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

      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("<a name=\"");
      out.print(xmlEscape( serverManagerName ));
      out.write("\"/>\r\n");
      out.write("<table>\r\n");
      out.write("<tbody>\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<td>\r\n");
      out.write("<div class=\"frame, frame_outer\">\r\n");
      out.write("<span style=\"width: 100%\">  ");
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("<div class=\"frameTitle\">\r\n");
      out.write("<table class=\"pp\" width=\"100%\">\r\n");
      out.write("<tbody>\r\n");
      out.write("<tr>\r\n");
      out.write("<td align=\"left\" valign=\"top\">\r\n");
      out.write("<table class=\"pp\">\r\n");
      out.write("<tbody>\r\n");

   final boolean  cacheMaster = Boolean.TRUE.equals( smAttrMap.get( "CacheMaster" ) );

      out.write("\r\n");
      out.write("<tr>\r\n");
      out.write("<td class=\"pplabel\">");
      out.print(getXmlEscapedString( RB, ( cacheMaster ? serverStatusResource.MASTER_SERVER_MANAGER : serverStatusResource.SERVER_MANAGER ) ));
      out.write(": </td>\r\n");
      out.write("<td class=\"ppdata\">\r\n");
      //  c:choose
      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
      _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fchoose_005f1.setParent(null);
      int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
      if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\r');
          out.write('\n');
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
          // /wtcore/jsp/jmx/serverStatus.jsp(999,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f1.setTest(isPrivilegedUser);
          int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
          if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<a href='mbeanDump.jsp?sm=");
              out.print(encodedServerManagerName);
              out.write("'\r\n");
              out.write(">");
              out.print(xmlEscape( serverManagerName ));
              out.write("</a>");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f14 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f14.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
              // /wtcore/jsp/jmx/serverStatus.jsp(1001,40) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f14.setTest(isDefaultSm);
              int _jspx_eval_c_005fif_005f14 = _jspx_th_c_005fif_005f14.doStartTag();
              if (_jspx_eval_c_005fif_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('*');
                  int evalDoAfterBody = _jspx_th_c_005fif_005f14.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f14);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f14);
              out.write('\r');
              out.write('\n');
              int evalDoAfterBody = _jspx_th_c_005fwhen_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
          out.write('\r');
          out.write('\n');
          //  c:otherwise
          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
          _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
          int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
          if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\r');
              out.write('\n');
              out.print(xmlEscape( serverManagerName ));
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f15 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f15.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
              // /wtcore/jsp/jmx/serverStatus.jsp(1004,35) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f15.setTest(isDefaultSm);
              int _jspx_eval_c_005fif_005f15 = _jspx_th_c_005fif_005f15.doStartTag();
              if (_jspx_eval_c_005fif_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('*');
                  int evalDoAfterBody = _jspx_th_c_005fif_005f15.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f15);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f15);
              out.write('\r');
              out.write('\n');
              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fotherwise_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
          out.write('\r');
          out.write('\n');
          int evalDoAfterBody = _jspx_th_c_005fchoose_005f1.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fchoose_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
      out.write("\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");

   final Object  smJmxUrlString = smAttrMap.get( "JmxServiceURL" );

      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f16 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f16.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f16.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(1012,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f16.setTest(smJmxUrlString != null);
      int _jspx_eval_c_005fif_005f16 = _jspx_th_c_005fif_005f16.doStartTag();
      if (_jspx_eval_c_005fif_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<tr>\r\n");
          out.write("<td class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.JMX_URL ));
          out.write(": </td>\r\n");
          out.write("<td class=\"ppdata\">");
          out.print(xmlEscape( smJmxUrlString ));
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f16.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f16);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f16);
      out.write("\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("<td align=\"right\" valign=\"top\">\r\n");
      out.write("<table class=\"pp\">\r\n");
      out.write("<tbody>\r\n");
      out.write("<tr>\r\n");
      out.write("<td class=\"pplabel\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.UPTIME ));
      out.write(": </td>\r\n");
      out.write("<td class=\"ppdata\">");
      out.print(xmlEscape( renderMillis( (Long) smAttrMap.get( "Uptime" ), RB, localeObj ) ));
      out.write("</td>\r\n");
      out.write("</tr>\r\n");

   final boolean  smDeadlocked = ( smAttrMap.get( "Deadlocked" ) != null );

      out.write("\r\n");
      out.write("<tr>\r\n");
      out.write("<td class=\"pplabel\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.DEADLOCKED ));
      out.write(": </td>\r\n");
      out.write("<td class=\"ppdata\" ");
      out.print(smDeadlocked ? "style='color: red'" : "");
      out.write('>');
      out.print(getXmlEscapedString( RB, smDeadlocked ? serverStatusResource.YES : serverStatusResource.NO ));
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<div class=\"frameContent\">\r\n");
      out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
      out.write("<tbody>\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<td>\r\n");
      out.write("\r\n");
      out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
      out.write("<tbody>\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<td>\r\n");
      out.write("\r\n");
      out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
      out.write("<thead>\r\n");
      out.write("<tr>\r\n");
      out.write("<th/>\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.RECENT ));
      out.write("&#160;</span></th>\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.BASELINE ));
      out.write("&#160;</span></th>\r\n");
      out.write("</tr>\r\n");
      out.write("</thead>\r\n");
      out.write("<tbody class=\"tablebody\">\r\n");
      out.write("<tr class=\"o\">\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.TIME_IN_GC ));
      out.write("&#160;</th>\r\n");

   final double  smPercTimeInGCLimit = (Double) smAttrMap.get( "PercentTimeSpentInGCThreshold" );
   final double  smPercTimeInGCRecent = (Double) smAttrMap.get( "RecentPercentTimeSpentInGC" );
   final double  smPercTimeInGCBaseline = (Double) smAttrMap.get( "OverallPercentTimeSpentInGC" );

      out.write("\r\n");
      out.write("<td class=\"c\">\r\n");
      out.write("<a href='liveGcChart.jsp?");
      out.print(smChartQueryString);
      out.write("'\r\n");
      out.write("   ");
      out.print(( ( smPercTimeInGCLimit > 0 ) && ( smPercTimeInGCRecent > smPercTimeInGCLimit ) ? "style='color: red'" : "" ));
      out.write('>');
      out.write('\r');
      out.write('\n');
      out.print(decimalFormat.format( smPercTimeInGCRecent ));
      out.print(percentString);
      out.write("\r\n");
      out.write("</a>\r\n");
      out.write("</td>\r\n");
      out.write("<td class=\"c\">\r\n");
      out.write("<a href='chartWrapper.jsp?chartPg=smGcChart.jsp&amp;");
      out.print(smChartQueryString);
      out.write("'\r\n");
      out.write("   ");
      out.print(( ( smPercTimeInGCLimit > 0 ) && ( smPercTimeInGCBaseline > smPercTimeInGCLimit ) ? "style='color: red'" : "" ));
      out.write('>');
      out.write('\r');
      out.write('\n');
      out.print(decimalFormat.format( smPercTimeInGCBaseline ));
      out.print(percentString);
      out.write("\r\n");
      out.write("</a>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr class=\"e\">\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.CPU_USED_BY_PROCESS ));
      out.write("&#160;</th>\r\n");

   final double  smPercCpuLimit = (Double) smAttrMap.get( "ProcessPercentCpuThreshold" );
   final double  smPercCpuUsedOverall = (Double) smAttrMap.get( "AverageProcessPercentCpu" );  // using overall rather than baseline...
   final CompositeData  smRecentCpuData = (CompositeData) smAttrMap.get( "RecentCpuData" );
   final double  smPercCpuUsedRecent = ( ( smRecentCpuData != null ) ? (Double) smRecentCpuData.get( "processPercentCpu" ) : smPercCpuUsedOverall );

      out.write("\r\n");
      out.write("<td class=\"c\">\r\n");
      out.write("<a href='liveCpuChart.jsp?");
      out.print(smChartQueryString);
      out.write("'\r\n");
      out.write("   ");
      out.print(( ( smPercCpuLimit > 0 ) && ( smPercCpuUsedRecent > smPercCpuLimit ) ? "style='color: red'" : "" ));
      out.write('>');
      out.write('\r');
      out.write('\n');
      out.print(decimalFormat.format( smPercCpuUsedRecent ));
      out.print(percentString);
      out.write("\r\n");
      out.write("</a>\r\n");
      out.write("</td>\r\n");
      out.write("<td class=\"c\">\r\n");
      out.write("<a href='chartWrapper.jsp?chartPg=smCpuChart.jsp&amp;");
      out.print(smChartQueryString);
      out.write("'\r\n");
      out.write("   ");
      out.print(( ( smPercCpuLimit > 0 ) && ( smPercCpuUsedOverall > smPercCpuLimit ) ? "style='color: red'" : "" ));
      out.write('>');
      out.write('\r');
      out.write('\n');
      out.print(decimalFormat.format( smPercCpuUsedOverall ));
      out.print(percentString);
      out.write("\r\n");
      out.write("</a>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("<td>&#160;&#160;</td>\r\n");
      out.write("<td>\r\n");
      out.write("\r\n");
      out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
      out.write("<tbody class=\"tablebody\">\r\n");
      out.write("<tr>\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
      out.write("<span class=\"pplabel\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.MEMORY_USED ));
      out.write("&#160;</span>\r\n");
      out.write("</th>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr class=\"o\">\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">\r\n");
      out.write("<a href='liveMemUsageChart.jsp?");
      out.print(smChartQueryString);
      out.write('\'');
      out.write('>');
      out.print(getXmlEscapedString( RB, serverStatusResource.HEAP ));
      out.write("</a>&#160;\r\n");
      out.write("</th>\r\n");
 final double  smPercHeapLimit = (Double) smAttrMap.get( "HeapPercentUsageThreshold" ), smPercHeapUsed = (Double) smAttrMap.get( "HeapPercentUsage" ); 
      out.write("\r\n");
      out.write("<td class=\"c\">\r\n");
      out.write("<a href='chartWrapper.jsp?chartPg=smHeapChart.jsp&amp;");
      out.print(smChartQueryString);
      out.write("'\r\n");
      out.write("   ");
      out.print(( ( smPercHeapLimit > 0 ) && ( smPercHeapUsed > smPercHeapLimit ) ? "style='color: red'" : "" ));
      out.write('>');
      out.write('\r');
      out.write('\n');
      out.print(decimalFormat.format( smPercHeapUsed ));
      out.print(percentString);
      out.write("\r\n");
      out.write("</a>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("<tr class=\"e\">\r\n");
      out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">\r\n");
      out.write("<a href='liveMemUsageChart.jsp?");
      out.print(smChartQueryString);
      out.write("&amp;permGen=true'>");
      out.print(getXmlEscapedString( RB, serverStatusResource.PERM_GEN ));
      out.write("</a>&#160;\r\n");
      out.write("</th>\r\n");
 final double  smPercPermLimit = (Double) smAttrMap.get( "PermGenPercentUsageThreshold" ), smPercPermUsed = (Double) smAttrMap.get( "PermGenPercentUsage" ); 
      out.write("\r\n");
      out.write("<td class=\"c\">\r\n");
      out.write("<a href='chartWrapper.jsp?chartPg=smPermGenChart.jsp&amp;");
      out.print(smChartQueryString);
      out.write("'\r\n");
      out.write("   ");
      out.print(( ( smPercPermLimit > 0 ) && ( smPercPermUsed > smPercPermLimit ) ? "style='color: red'" : "" ));
      out.write('>');
      out.write('\r');
      out.write('\n');
      out.print(decimalFormat.format( smPercPermUsed ));
      out.print(percentString);
      out.write("\r\n");
      out.write("</a>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");

   final String  smPhysMemInfo = renderSysMemInfo( smAttrMap, "FreePhysicalMemorySize", "TotalPhysicalMemorySize", decimalFormat, RB );
   final String  smSwapMemInfo = renderSysMemInfo( smAttrMap, "FreeSwapSpaceSize", "TotalSwapSpaceSize", decimalFormat, RB );

      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f17 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f17.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f17.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(1149,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f17.setTest(( smPhysMemInfo != null ) || ( smSwapMemInfo != null ));
      int _jspx_eval_c_005fif_005f17 = _jspx_th_c_005fif_005f17.doStartTag();
      if (_jspx_eval_c_005fif_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<td>&#160;&#160;</td>\r\n");
          out.write("<td>\r\n");
          out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
          out.write("<tbody class=\"tablebody\">\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.AVAILABLE_SYSTEM_MEMORY ));
          out.write("&#160;</span>\r\n");
          out.write("</th>\r\n");
          out.write("</tr>\r\n");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f18 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f18.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f17);
          // /wtcore/jsp/jmx/serverStatus.jsp(1159,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f18.setTest(smPhysMemInfo != null);
          int _jspx_eval_c_005fif_005f18 = _jspx_th_c_005fif_005f18.doStartTag();
          if (_jspx_eval_c_005fif_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<tr class=\"o\">\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.PHYSICAL ));
              out.write("&#160;</th>\r\n");
              out.write("<td class=\"c\" nowrap=\"nowrap\">");
              out.print(xmlEscape( smPhysMemInfo ));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              int evalDoAfterBody = _jspx_th_c_005fif_005f18.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
          out.write('\r');
          out.write('\n');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f19 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f19.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f17);
          // /wtcore/jsp/jmx/serverStatus.jsp(1165,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f19.setTest(smSwapMemInfo != null);
          int _jspx_eval_c_005fif_005f19 = _jspx_th_c_005fif_005f19.doStartTag();
          if (_jspx_eval_c_005fif_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<tr class=\"e\">\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.SWAP ));
              out.write("&#160;</th>\r\n");
              out.write("<td class=\"c\" nowrap=\"nowrap\">");
              out.print(xmlEscape( smSwapMemInfo ));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              int evalDoAfterBody = _jspx_th_c_005fif_005f19.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
          out.write("\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f17.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
      out.write('\r');
      out.write('\n');

   final Double  smSystemLoadAverage = (Double) smAttrMap.get( "SystemLoadAverage" );

      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f20 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f20.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f20.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(1179,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f20.setTest(( smSystemLoadAverage != null ) && ( smSystemLoadAverage >= 0 ));
      int _jspx_eval_c_005fif_005f20 = _jspx_th_c_005fif_005f20.doStartTag();
      if (_jspx_eval_c_005fif_005f20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<td>&#160;&#160;</td>\r\n");
          out.write("<td>\r\n");
          out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
          out.write("<tbody class=\"tablebody\">\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.OTHER_SYSTEM_INFO ));
          out.write("&#160;</span>\r\n");
          out.write("</th>\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"o\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.LOAD_AVERAGE ));
          out.write("&#160;</th>\r\n");
          out.write("<td class=\"c\"><a href='liveLoadAverageChart.jsp?");
          out.print(smChartQueryString);
          out.write('\'');
          out.write('>');
          out.print(decimalFormat.format( smSystemLoadAverage ));
          out.write("</a></td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f20.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
      out.write("\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f21 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f21.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f21.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(1203,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f21.setTest(( ( msToAttrMap != null ) && !msToAttrMap.isEmpty() ));
      int _jspx_eval_c_005fif_005f21 = _jspx_th_c_005fif_005f21.doStartTag();
      if (_jspx_eval_c_005fif_005f21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\r');
          out.write('\n');
 { // explicitly limit scope of variables herein 
          out.write("\r\n");
          out.write("<tr><td height=\"14\"/></tr>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<td>\r\n");
          out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
          out.write("<thead>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\">\r\n");
          out.write("<div class=\"frameTitle\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.METHOD_SERVER_DATA ));
          out.write("</span></div>\r\n");
          out.write("</th>\r\n");
 for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
   {
     final String  methodServerName = msToAttrMapEntry.getKey();
     final boolean  isThisMethodServer = thisMethodServerName.equals( methodServerName );
     final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
     final Object  msJmxUrl = msAttrs.get( "JmxServiceURL" );

          out.write("\r\n");
          out.write("<th width=\"1\"/>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\">\r\n");
          out.write("<div class=\"frameTitle\">\r\n");
          out.write("<span class=\"pplabel\" ");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f22 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f22.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          // /wtcore/jsp/jmx/serverStatus.jsp(1224,22) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f22.setTest(msJmxUrl != null);
          int _jspx_eval_c_005fif_005f22 = _jspx_th_c_005fif_005f22.doStartTag();
          if (_jspx_eval_c_005fif_005f22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("title=\"");
              out.print(getXmlEscapedString( RB, serverStatusResource.JMX_URL ));
              out.write(':');
              out.write(' ');
              out.print(xmlEscape( msJmxUrl ));
              out.write('"');
              int evalDoAfterBody = _jspx_th_c_005fif_005f22.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f22);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f22);
          out.write('>');
          out.write('\r');
          out.write('\n');
          //  c:choose
          org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f2 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
          _jspx_th_c_005fchoose_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fchoose_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          int _jspx_eval_c_005fchoose_005f2 = _jspx_th_c_005fchoose_005f2.doStartTag();
          if (_jspx_eval_c_005fchoose_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\r');
              out.write('\n');
              //  c:when
              org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f2 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
              _jspx_th_c_005fwhen_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fwhen_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
              // /wtcore/jsp/jmx/serverStatus.jsp(1226,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fwhen_005f2.setTest(isPrivilegedUser);
              int _jspx_eval_c_005fwhen_005f2 = _jspx_th_c_005fwhen_005f2.doStartTag();
              if (_jspx_eval_c_005fwhen_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("<a href='mbeanDump.jsp?sm=");
                  out.print(URLEncoder.encode(serverManagerName,"UTF-8"));
                  out.write("&amp;ms=");
                  out.print(URLEncoder.encode(methodServerName,"UTF-8"));
                  out.write("'\r\n");
                  out.write(">");
                  out.print(xmlEscape( methodServerName ));
                  out.write("</a>");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f23 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f23.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);
                  // /wtcore/jsp/jmx/serverStatus.jsp(1228,39) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f23.setTest(isThisMethodServer);
                  int _jspx_eval_c_005fif_005f23 = _jspx_th_c_005fif_005f23.doStartTag();
                  if (_jspx_eval_c_005fif_005f23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('*');
                      int evalDoAfterBody = _jspx_th_c_005fif_005f23.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fif_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f23);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f23);
                  out.write('\r');
                  out.write('\n');
                  int evalDoAfterBody = _jspx_th_c_005fwhen_005f2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fwhen_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f2);
              out.write('\r');
              out.write('\n');
              //  c:otherwise
              org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f2 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
              _jspx_th_c_005fotherwise_005f2.setPageContext(_jspx_page_context);
              _jspx_th_c_005fotherwise_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f2);
              int _jspx_eval_c_005fotherwise_005f2 = _jspx_th_c_005fotherwise_005f2.doStartTag();
              if (_jspx_eval_c_005fotherwise_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\r');
                  out.write('\n');
                  out.print(xmlEscape( methodServerName ));
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f24 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f24.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f2);
                  // /wtcore/jsp/jmx/serverStatus.jsp(1231,34) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f24.setTest(isThisMethodServer);
                  int _jspx_eval_c_005fif_005f24 = _jspx_th_c_005fif_005f24.doStartTag();
                  if (_jspx_eval_c_005fif_005f24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('*');
                      int evalDoAfterBody = _jspx_th_c_005fif_005f24.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fif_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f24);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f24);
                  out.write('\r');
                  out.write('\n');
                  int evalDoAfterBody = _jspx_th_c_005fotherwise_005f2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fotherwise_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f2);
              out.write('\r');
              out.write('\n');
              int evalDoAfterBody = _jspx_th_c_005fchoose_005f2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fchoose_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f2);
          out.write("\r\n");
          out.write("</span>\r\n");
          out.write("</div>\r\n");
          out.write("</th>\r\n");

   }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("</thead>\r\n");
          out.write("<tbody class=\"tablebody\">\r\n");
          out.write("<tr class=\"o\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.UPTIME ));
          out.write("</th>\r\n");
 for ( Map<String,Object> msAttrs : msToAttrMap.values() ) { 
          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td colspan=\"2\" class=\"c\">");
          out.print(xmlEscape( renderMillis( (Long) msAttrs.get( "Uptime" ), RB, localeObj ) ));
          out.write("</td>\r\n");
 } 
          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.DEADLOCKED ));
          out.write("</th>\r\n");
 for ( Map<String,Object> msAttrs : msToAttrMap.values() )
   {
     final boolean  msDeadlocked = ( msAttrs.get( "DeadlockedThreadIds" ) != null );

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td colspan=\"2\" class=\"c\" ");
          out.print(msDeadlocked ? "style='color: red'" : "");
          out.write('>');
          out.print(getXmlEscapedString( RB, msDeadlocked ? serverStatusResource.YES : serverStatusResource.NO ));
          out.write("</td>\r\n");
 } 
          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.MEMORY_USED ));
          out.write("</span>\r\n");
          out.write("</th>\r\n");
          //  c:forEach
          org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f0 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
          _jspx_th_c_005fforEach_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fforEach_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          // /wtcore/jsp/jmx/serverStatus.jsp(1264,0) name = items type = javax.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
          _jspx_th_c_005fforEach_005f0.setItems(msToAttrMap.keySet());
          int[] _jspx_push_body_count_c_005fforEach_005f0 = new int[] { 0 };
          try {
            int _jspx_eval_c_005fforEach_005f0 = _jspx_th_c_005fforEach_005f0.doStartTag();
            if (_jspx_eval_c_005fforEach_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              do {
                out.write("\r\n");
                out.write("<th/>\r\n");
                out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">&#160;</span></th>\r\n");
                int evalDoAfterBody = _jspx_th_c_005fforEach_005f0.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                  break;
              } while (true);
            }
            if (_jspx_th_c_005fforEach_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              return;
            }
          } catch (java.lang.Throwable _jspx_exception) {
            while (_jspx_push_body_count_c_005fforEach_005f0[0]-- > 0)
              out = _jspx_page_context.popBody();
            _jspx_th_c_005fforEach_005f0.doCatch(_jspx_exception);
          } finally {
            _jspx_th_c_005fforEach_005f0.doFinally();
            _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.reuse(_jspx_th_c_005fforEach_005f0);
          }
          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"o\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">\r\n");
          out.write("<a href=\"liveMemUsageChart.jsp?smName=");
          out.print(encodedServerManagerName);
          out.write("&amp;msName=*\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.HEAP ));
          out.write("</a>&#160;\r\n");
          out.write("</th>\r\n");
 for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
   {
     final String  methodServerName = msToAttrMapEntry.getKey();
     final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
     final double  msPercHeapLimit = (Double) msAttrs.get( "HeapPercentUsageThreshold" ), msPercHeapUsed = (Double) msAttrs.get( "HeapPercentUsage" );

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td colspan=\"2\" class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=msHeapChart.jsp&amp;");
          out.print(msChartQueryStrings.get( methodServerName ));
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercHeapLimit > 0 ) && ( msPercHeapUsed > msPercHeapLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercHeapUsed ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

   }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">\r\n");
          out.write("<a href=\"liveMemUsageChart.jsp?smName=");
          out.print(encodedServerManagerName);
          out.write("&amp;msName=*&amp;permGen=true\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.PERM_GEN ));
          out.write("</a>&#160;\r\n");
          out.write("</th>\r\n");
 for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
   {
     final String  methodServerName = msToAttrMapEntry.getKey();
     final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
     final double  msPercPermLimit = (Double) msAttrs.get( "PermGenPercentUsageThreshold" ), msPercPermUsed = (Double) msAttrs.get( "PermGenPercentUsage" );

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td colspan=\"2\" class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=msPermGenChart.jsp&amp;");
          out.print(msChartQueryStrings.get( methodServerName ));
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercPermLimit > 0 ) && ( msPercPermUsed > msPercPermLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercPermUsed ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

   }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.OTHER_STATS ));
          out.write("&#160;</span></th>\r\n");
          //  c:forEach
          org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f1 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
          _jspx_th_c_005fforEach_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fforEach_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          // /wtcore/jsp/jmx/serverStatus.jsp(1313,0) name = items type = javax.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
          _jspx_th_c_005fforEach_005f1.setItems(msToAttrMap.keySet());
          int[] _jspx_push_body_count_c_005fforEach_005f1 = new int[] { 0 };
          try {
            int _jspx_eval_c_005fforEach_005f1 = _jspx_th_c_005fforEach_005f1.doStartTag();
            if (_jspx_eval_c_005fforEach_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              do {
                out.write("\r\n");
                out.write("<th/>\r\n");
                out.write("<th scope=\"col\" nowrap=\"nowrap\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
                out.print(getXmlEscapedString( RB, serverStatusResource.RECENT ));
                out.write("&#160;</span></th>\r\n");
                out.write("<th scope=\"col\" nowrap=\"nowrap\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
                out.print(getXmlEscapedString( RB, serverStatusResource.BASELINE ));
                out.write("&#160;</span></th>\r\n");
                int evalDoAfterBody = _jspx_th_c_005fforEach_005f1.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                  break;
              } while (true);
            }
            if (_jspx_th_c_005fforEach_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              return;
            }
          } catch (java.lang.Throwable _jspx_exception) {
            while (_jspx_push_body_count_c_005fforEach_005f1[0]-- > 0)
              out = _jspx_page_context.popBody();
            _jspx_th_c_005fforEach_005f1.doCatch(_jspx_exception);
          } finally {
            _jspx_th_c_005fforEach_005f1.doFinally();
            _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.reuse(_jspx_th_c_005fforEach_005f1);
          }
          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"o\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.TIME_IN_GC ));
          out.write("&#160;</th>\r\n");
 for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
   {
     final String  methodServerName = msToAttrMapEntry.getKey();
     final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
     final double  msPercGCLimit = (Double) msAttrs.get( "PercentTimeSpentInGCThreshold" );
     final double  msPercGC[] = { (Double) msAttrs.get( "RecentPercentTimeSpentInGC" ), (Double) msAttrs.get( "OverallPercentTimeSpentInGC" ) };
     final String  msChartQueryString = msChartQueryStrings.get( methodServerName );

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='liveGcChart.jsp?");
          out.print(msChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercGCLimit > 0 ) && ( msPercGC[0] > msPercGCLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercGC[0] ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=msGcChart.jsp&amp;");
          out.print(msChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercGCLimit > 0 ) && ( msPercGC[1] > msPercGCLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercGC[1] ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

   }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">\r\n");
          out.write("<a href=\"liveCpuChart.jsp?smName=");
          out.print(encodedServerManagerName);
          out.write("&amp;msName=*\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.CPU_USED_BY_PROCESS ));
          out.write("</a>&#160;\r\n");
          out.write("</th>\r\n");
 for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
   {
     final String  methodServerName = msToAttrMapEntry.getKey();
     final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
     final double  msPercCPULimit = (Double) msAttrs.get( "ProcessPercentCpuThreshold" );
     final double  msPercCPUUsed[] = { 0.0, (Double) msAttrs.get( "AverageProcessPercentCpu" ) };
     final CompositeData  msRecentCpuData = (CompositeData) msAttrs.get( "RecentCpuData" );
     final String  msChartQueryString = msChartQueryStrings.get( methodServerName );
     msPercCPUUsed[0] = ( ( msRecentCpuData != null ) ? (Double) msRecentCpuData.get( "processPercentCpu" ) : msPercCPUUsed[1] );

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='liveCpuChart.jsp?");
          out.print(msChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercCPULimit > 0 ) && ( msPercCPUUsed[0] > msPercCPULimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercCPUUsed[0] ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=msCpuChart.jsp&amp;");
          out.print(msChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( msPercCPULimit > 0 ) && ( msPercCPUUsed[1] > msPercCPULimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( msPercCPUUsed[1] ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

   }

          out.write("\r\n");
          out.write("</tr>\r\n");

   for ( int webAppIdx = 0; webAppIdx < webAppPrefixes.length; ++webAppIdx )
   {
     final String  webAppContextPath = webAppContextPaths[webAppIdx];
     final String  encodedWebAppContextPath = URLEncoder.encode( webAppContextPath, "UTF-8" );
     final String  webAppPrefix = webAppPrefixes[webAppIdx];
     {  // if we do not have any data for this web app in any method server for this server manager, then don't generate empty table lines
       boolean  hasDataForWebApp = false;
       for ( Map<String,Object> msAttrs : msToAttrMap.values() )
         if ( msAttrs.get( webAppPrefix + "RequestBaselineStatistics" ) != null )
         {
           hasDataForWebApp = true;
           break;
         }
       if ( !hasDataForWebApp )
         continue;
     }
     final String  webAppLabelKey = webAppLabelKeys[webAppIdx];
     int  rowCount = 0;

          out.write("\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, webAppLabelKey ));
          out.write("</span>\r\n");
          out.write("</th>\r\n");
          //  c:forEach
          org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f2 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
          _jspx_th_c_005fforEach_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fforEach_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          // /wtcore/jsp/jmx/serverStatus.jsp(1401,0) name = items type = javax.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
          _jspx_th_c_005fforEach_005f2.setItems(msToAttrMap.keySet());
          int[] _jspx_push_body_count_c_005fforEach_005f2 = new int[] { 0 };
          try {
            int _jspx_eval_c_005fforEach_005f2 = _jspx_th_c_005fforEach_005f2.doStartTag();
            if (_jspx_eval_c_005fforEach_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              do {
                out.write("\r\n");
                out.write("<th/>\r\n");
                out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
                out.print(getXmlEscapedString( RB, serverStatusResource.RECENT ));
                out.write("&#160;</span></th>\r\n");
                out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
                out.print(getXmlEscapedString( RB, serverStatusResource.BASELINE ));
                out.write("&#160;</span></th>\r\n");
                int evalDoAfterBody = _jspx_th_c_005fforEach_005f2.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                  break;
              } while (true);
            }
            if (_jspx_th_c_005fforEach_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              return;
            }
          } catch (java.lang.Throwable _jspx_exception) {
            while (_jspx_push_body_count_c_005fforEach_005f2[0]-- > 0)
              out = _jspx_page_context.popBody();
            _jspx_th_c_005fforEach_005f2.doCatch(_jspx_exception);
          } finally {
            _jspx_th_c_005fforEach_005f2.doFinally();
            _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.reuse(_jspx_th_c_005fforEach_005f2);
          }
          out.write("\r\n");
          out.write("</tr>\r\n");
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f25 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f25.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          // /wtcore/jsp/jmx/serverStatus.jsp(1407,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f25.setTest("".equals( webAppPrefix ));
          int _jspx_eval_c_005fif_005f25 = _jspx_th_c_005fif_005f25.doStartTag();
          if (_jspx_eval_c_005fif_005f25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write(' ');
              out.write(' ');
              out.write("\r\n");
              out.write("<tr class='");
              out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
              out.write("'>\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.ACTIVE_SESSIONS ));
              out.write("</th>\r\n");
   for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
     {
       final String  methodServerName = msToAttrMapEntry.getKey();
       final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();

              out.write("\r\n");
              out.write("<th/>\r\n");

       final Integer  activeSessionsObj = (Integer) msAttrs.get( webAppPrefix + "ActiveSessions" );  // using current rather than recent...
       if ( activeSessionsObj != null )  // the given method server may not have this web app and thus have no data for it
       {
         final int  seSessionLimit = (Integer) msAttrs.get( webAppPrefix + "MaxAverageActiveSessionsThreshold" );
         final int  activeSessions = activeSessionsObj;  // do Integer to int conversion only once
         final double  activeSessionsBaseline = (Double) ((CompositeData)msAttrs.get( webAppPrefix + "SessionBaselineStatistics" )).get( "activeSessionsAverage" );

              out.write("\r\n");
              out.write("<td class=\"c\" ");
              out.print(( ( seSessionLimit > 0 ) && ( activeSessions > seSessionLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.print(decimalFormat.format( activeSessions ));
              out.write("</td>\r\n");
              out.write("<td class=\"c\">\r\n");
              out.write("<a href='chartWrapper.jsp?chartPg=sessionsChart.jsp&amp;");
              out.print(msChartQueryStrings.get( methodServerName ));
              out.write("&amp;contextPath=");
              out.print(encodedWebAppContextPath);
              out.write("'\r\n");
              out.write("   ");
              out.print(( ( seSessionLimit > 0 ) && ( activeSessionsBaseline > seSessionLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.write('\r');
              out.write('\n');
              out.print(decimalFormat.format( activeSessionsBaseline ));
              out.write("\r\n");
              out.write("</a>\r\n");
              out.write("</td>\r\n");

       }
       else
       {

              out.write("\r\n");
              out.write("<td class=\"c\">&#160;</td>\r\n");
              out.write("<td class=\"c\">&#160;</td>\r\n");

       }
     }

              out.write("\r\n");
              out.write("</tr>\r\n");
              int evalDoAfterBody = _jspx_th_c_005fif_005f25.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f25);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f25);
          out.write('\r');
          out.write('\n');

     final CompositeData  seRequestDataRecent[] = new CompositeData[msToAttrMap.size()];
     final CompositeData  seRequestDataBaseline[] = new CompositeData[msToAttrMap.size()];
     {
       int  ii = 0;
       for ( Map<String,Object> msAttrs : msToAttrMap.values() )
       {
         seRequestDataBaseline[ii] = (CompositeData) msAttrs.get( webAppPrefix + "RequestBaselineStatistics" );
         final CompositeData  recentData = (CompositeData) msAttrs.get( webAppPrefix + "RequestRecentStatistics" );
         seRequestDataRecent[ii] = ( ( recentData != null ) ? recentData : seRequestDataBaseline[ii] );
         ++ii;
       }
     }

          out.write("\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.COMPLETED_REQUESTS ));
          out.write("</th>\r\n");

     {
       int  ii = 0;
       for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
       {
         final String  methodServerName = msToAttrMapEntry.getKey();
         final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();

          out.write("\r\n");
          out.write("<th/>\r\n");

         final CompositeData  requestBaselineData = seRequestDataBaseline[ii];
         if ( requestBaselineData != null )  // the given method server may not have this web app and thus have no data for it
         {
           final long  seCompletedRequestsRecent = (Long) seRequestDataRecent[ii].get( "completedRequests" );
           final long  seCompletedRequestsBaseline = (Long) requestBaselineData.get( "completedRequests" );

          out.write("\r\n");
          out.write("<td class=\"c\">");
          out.print(decimalFormat.format( seCompletedRequestsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=requestsPerMinuteChart.jsp&amp;");
          out.print(msChartQueryStrings.get( methodServerName ));
          out.write("&amp;contextPath=");
          out.print(encodedWebAppContextPath);
          out.write("'>\r\n");
          out.print(decimalFormat.format( seCompletedRequestsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

         }
         else
         {

          out.write("\r\n");
          out.write("<td class=\"c\">&#160;</td>\r\n");
          out.write("<td class=\"c\">&#160;</td>\r\n");

         }
         ++ii;
       }
     }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.AVERAGE_RESPONSE_TIME ));
          out.write("</th>\r\n");

     {
       int  ii = 0;
       for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
       {
         final String  methodServerName = msToAttrMapEntry.getKey();
         final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();

          out.write("\r\n");
          out.write("<th/>\r\n");

         final CompositeData  requestBaselineData = seRequestDataBaseline[ii];
         if ( requestBaselineData != null )  // the given method server may not have this web app and thus have no data for it
         {
           final double  seRespLimit = (Double) msAttrs.get( webAppPrefix + "RequestTimeWarnThreshold" );
           final double  seAvgRespSecsRecent = (Double) seRequestDataRecent[ii].get( "requestSecondsAverage" );
           final double  seAvgRespSecsBaseline = (Double) requestBaselineData.get( "requestSecondsAverage" );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( seRespLimit > 0 ) && ( seAvgRespSecsRecent > seRespLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( seAvgRespSecsRecent ));
          out.write("&#160;");
          out.print(getXmlEscapedString( RB, serverStatusResource.SECONDS ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=responseTimeChart.jsp&amp;");
          out.print(msChartQueryStrings.get( methodServerName ));
          out.write("&amp;contextPath=");
          out.print(encodedWebAppContextPath);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( seRespLimit > 0 ) && ( seAvgRespSecsBaseline > seRespLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( seAvgRespSecsBaseline ));
          out.write("&#160;");
          out.print(getXmlEscapedString( RB, serverStatusResource.SECONDS ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

         }
         else
         {

          out.write("\r\n");
          out.write("<td class=\"c\">&#160;</td>\r\n");
          out.write("<td class=\"c\">&#160;</td>\r\n");

         }
         ++ii;
       }
     }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.MAXIMUM_CONCURRENCY ));
          out.write("</th>\r\n");

     {
       int  ii = 0;
       for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
       {
         final String  methodServerName = msToAttrMapEntry.getKey();
         final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();

          out.write("\r\n");
          out.write("<th/>\r\n");

         final CompositeData  requestBaselineData = seRequestDataBaseline[ii];
         if ( requestBaselineData != null )  // the given method server may not have this web app and thus have no data for it
         {
           final int  seRequestLimit = (Integer) msAttrs.get( webAppPrefix + "MaxAverageActiveRequestsThreshold" );
           final int  seMaxRequestsRecent = (Integer) seRequestDataRecent[ii].get( "activeRequestsMax" );
           final int  seMaxRequestsBaseline = (Integer) requestBaselineData.get( "activeRequestsMax" );

          out.write("\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( seRequestLimit > 0 ) && ( seMaxRequestsRecent > seRequestLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( seMaxRequestsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=maxRequestConcChart.jsp&amp;");
          out.print(msChartQueryStrings.get( methodServerName ));
          out.write("&amp;contextPath=");
          out.print(encodedWebAppContextPath);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( seRequestLimit > 0 ) && ( seMaxRequestsBaseline > seRequestLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( seMaxRequestsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

         }
         else
         {

          out.write("\r\n");
          out.write("<td class=\"c\">&#160;</td>\r\n");
          out.write("<td class=\"c\">&#160;</td>\r\n");

         }
         ++ii;
       }
     }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">\r\n");
          out.write("<a href='liveAvgRequestConcChart.jsp?smName=");
          out.print(encodedServerManagerName);
          out.write("&amp;contextPath=");
          out.print(encodedWebAppContextPath);
          out.write("'>\r\n");
          out.print(getXmlEscapedString( RB, serverStatusResource.AVERAGE_CONCURRENCY ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</th>\r\n");

     {
       int  ii = 0;
       for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
       {
         final String  methodServerName = msToAttrMapEntry.getKey();
         final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();

          out.write("\r\n");
          out.write("<th/>\r\n");

         final CompositeData  requestBaselineData = seRequestDataBaseline[ii];
         if ( requestBaselineData != null )  // the given method server may not have this web app and thus have no data for it
         {
           final int  seRequestLimit = (Integer) msAttrs.get( webAppPrefix + "MaxAverageActiveRequestsThreshold" );
           final double  seAvgRequestsRecent = (Double) seRequestDataRecent[ii].get( "activeRequestsAverage" );
           final double  seAvgRequestsBaseline = (Double) requestBaselineData.get( "activeRequestsAverage" );
           final String  msChartQueryString = msChartQueryStrings.get( methodServerName ) + "&amp;contextPath=" + encodedWebAppContextPath;

          out.write("\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='liveAvgRequestConcChart.jsp?");
          out.print(msChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( seRequestLimit > 0 ) && ( seAvgRequestsRecent > seRequestLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( seAvgRequestsRecent ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=avgRequestConcChart.jsp&amp;");
          out.print(msChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( seRequestLimit > 0 ) && ( seAvgRequestsBaseline > seRequestLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( seAvgRequestsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

         }
         else
         {

          out.write("\r\n");
          out.write("<td class=\"c\">&#160;</td>\r\n");
          out.write("<td class=\"c\">&#160;</td>\r\n");

         }
         ++ii;
       }
     }

          out.write("\r\n");
          out.write("</tr>\r\n");

   }

          out.write("\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.METHOD_CONTEXTS ));
          out.write("</span>\r\n");
          out.write("</th>\r\n");
          //  c:forEach
          org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f3 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
          _jspx_th_c_005fforEach_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fforEach_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          // /wtcore/jsp/jmx/serverStatus.jsp(1633,0) name = items type = javax.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
          _jspx_th_c_005fforEach_005f3.setItems(msToAttrMap.keySet());
          int[] _jspx_push_body_count_c_005fforEach_005f3 = new int[] { 0 };
          try {
            int _jspx_eval_c_005fforEach_005f3 = _jspx_th_c_005fforEach_005f3.doStartTag();
            if (_jspx_eval_c_005fforEach_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              do {
                out.write("\r\n");
                out.write("<th/>\r\n");
                out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
                out.print(getXmlEscapedString( RB, serverStatusResource.RECENT ));
                out.write("&#160;</span></th>\r\n");
                out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
                out.print(getXmlEscapedString( RB, serverStatusResource.BASELINE ));
                out.write("&#160;</span></th>\r\n");
                int evalDoAfterBody = _jspx_th_c_005fforEach_005f3.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                  break;
              } while (true);
            }
            if (_jspx_th_c_005fforEach_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              return;
            }
          } catch (java.lang.Throwable _jspx_exception) {
            while (_jspx_push_body_count_c_005fforEach_005f3[0]-- > 0)
              out = _jspx_page_context.popBody();
            _jspx_th_c_005fforEach_005f3.doCatch(_jspx_exception);
          } finally {
            _jspx_th_c_005fforEach_005f3.doFinally();
            _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.reuse(_jspx_th_c_005fforEach_005f3);
          }
          out.write("\r\n");
          out.write("</tr>\r\n");

   final CompositeData  recentData[] = new CompositeData[msToAttrMap.size()];
   final CompositeData  baselineData[] = new CompositeData[msToAttrMap.size()];
   {
     int  ii = 0;
     for ( Map<String,Object> msAttrs : msToAttrMap.values() )
     {
       baselineData[ii] = (CompositeData) msAttrs.get( "BaselineStatistics" );
       final CompositeData  recentStats = (CompositeData) msAttrs.get( "RecentStatistics" );
       recentData[ii] = ( ( recentStats != null ) ? recentStats : baselineData[ii] );
       ++ii;
     }
   }

          out.write("\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.COMPLETED_CONTEXTS ));
          out.write("</th>\r\n");

   {
     int  ii = 0;
     for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
     {
       final String  methodServerName = msToAttrMapEntry.getKey();
       final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
       final long  completedContextsRecent = (Long) recentData[ii].get( "completedContexts" );
       final long  completedContextsBaseline = (Long) baselineData[ii].get( "completedContexts" );

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td class=\"c\">");
          out.print(decimalFormat.format( completedContextsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=contextsPerMinuteChart.jsp&amp;");
          out.print(msChartQueryStrings.get( methodServerName ));
          out.write("'>\r\n");
          out.print(decimalFormat.format( completedContextsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

       ++ii;
     }
   }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"e\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.MAXIMUM_CONCURRENCY ));
          out.write("</th>\r\n");

   {
     int  ii = 0;
     for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
     {
       final String  methodServerName = msToAttrMapEntry.getKey();
       final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
       final int  contextLimit = (Integer) msAttrs.get( "MaxAverageActiveContextsThreshold" );
       final int  maxContextsRecent = (Integer) recentData[ii].get( "activeContextsMax" );
       final int  maxContextsBaseline = (Integer) baselineData[ii].get( "activeContextsMax" );

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td class=\"c\" ");
          out.print(( ( contextLimit > 0 ) && ( maxContextsRecent > contextLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.print(decimalFormat.format( maxContextsRecent ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=maxContextConcChart.jsp&amp;");
          out.print(msChartQueryStrings.get( methodServerName ));
          out.write("'>\r\n");
          out.print(decimalFormat.format( maxContextsBaseline ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

       ++ii;
     }
   }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr class=\"o\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">\r\n");
          out.write("<a href='liveAvgContextConcChart.jsp?smName=");
          out.print(encodedServerManagerName);
          out.write('\'');
          out.write('>');
          out.print(getXmlEscapedString( RB, serverStatusResource.AVERAGE_CONCURRENCY ));
          out.write("</a>\r\n");
          out.write("</th>\r\n");

   {
     int  ii = 0;
     for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
     {
       final String  methodServerName = msToAttrMapEntry.getKey();
       final Map<String,Object>  msAttrs = msToAttrMapEntry.getValue();
       final int  contextLimit = (Integer) msAttrs.get( "MaxAverageActiveContextsThreshold" );
       final double  avgContexts[] = { (Double) recentData[ii].get( "activeContextsAverage" ), (Double) baselineData[ii].get( "activeContextsAverage" ) };
       final String msChartQueryString = msChartQueryStrings.get( methodServerName );

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='liveAvgContextConcChart.jsp?");
          out.print(msChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( contextLimit > 0 ) && ( avgContexts[0] > contextLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( avgContexts[0] ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=avgContextConcChart.jsp&amp;");
          out.print(msChartQueryString);
          out.write("'\r\n");
          out.write("   ");
          out.print(( ( contextLimit > 0 ) && ( avgContexts[1] > contextLimit ) ? "style='color: red'" : "" ));
          out.write('>');
          out.write('\r');
          out.write('\n');
          out.print(decimalFormat.format( avgContexts[1] ));
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

       ++ii;
     }
   }

          out.write("\r\n");
          out.write("</tr>\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\">\r\n");
          out.write("<span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.METHOD_CONTEXT_TIME ));
          out.write("</span>\r\n");
          out.write("</th>\r\n");
          //  c:forEach
          org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f4 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
          _jspx_th_c_005fforEach_005f4.setPageContext(_jspx_page_context);
          _jspx_th_c_005fforEach_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f21);
          // /wtcore/jsp/jmx/serverStatus.jsp(1742,0) name = items type = javax.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
          _jspx_th_c_005fforEach_005f4.setItems(msToAttrMap.keySet());
          int[] _jspx_push_body_count_c_005fforEach_005f4 = new int[] { 0 };
          try {
            int _jspx_eval_c_005fforEach_005f4 = _jspx_th_c_005fforEach_005f4.doStartTag();
            if (_jspx_eval_c_005fforEach_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
              do {
                out.write("\r\n");
                out.write("<th/>\r\n");
                out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
                out.print(getXmlEscapedString( RB, serverStatusResource.RECENT ));
                out.write("&#160;</span></th>\r\n");
                out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
                out.print(getXmlEscapedString( RB, serverStatusResource.BASELINE ));
                out.write("&#160;</span></th>\r\n");
                int evalDoAfterBody = _jspx_th_c_005fforEach_005f4.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                  break;
              } while (true);
            }
            if (_jspx_th_c_005fforEach_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
              return;
            }
          } catch (java.lang.Throwable _jspx_exception) {
            while (_jspx_push_body_count_c_005fforEach_005f4[0]-- > 0)
              out = _jspx_page_context.popBody();
            _jspx_th_c_005fforEach_005f4.doCatch(_jspx_exception);
          } finally {
            _jspx_th_c_005fforEach_005f4.doFinally();
            _005fjspx_005ftagPool_005fc_005fforEach_0026_005fitems.reuse(_jspx_th_c_005fforEach_005f4);
          }
          out.write("\r\n");
          out.write("</tr>\r\n");

   for ( int ii = 0; ii < percContextTimeAttrNames.length; ++ii )
   {
     final String  attrName = percContextTimeAttrNames[ii];
     final String  chartJsp = percContextTimeChartJsps[ii];

          out.write("\r\n");
          out.write("<tr class='");
          out.print(( ( ii % 2 ) == 0 ) ? "o" : "e" );
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(xmlEscape( percContextTimeLabels[ii] ));
          out.write("</th>\r\n");

     int  jj = 0;
     for ( Map.Entry<String,Map<String,Object>> msToAttrMapEntry : msToAttrMap.entrySet() )
     {
       final String  methodServerName = msToAttrMapEntry.getKey();
       final double  percTimes[] = { (Double) recentData[jj].get( attrName ), (Double) baselineData[jj].get( attrName ) };

          out.write("\r\n");
          out.write("<th/>\r\n");
          out.write("<td class=\"c\">");
          out.print(decimalFormat.format( percTimes[0] ));
          out.print(percentString);
          out.write("</td>\r\n");
          out.write("<td class=\"c\">\r\n");
          out.write("<a href='chartWrapper.jsp?chartPg=");
          out.print(chartJsp);
          out.write("&amp;");
          out.print(msChartQueryStrings.get( methodServerName ));
          out.write("'>\r\n");
          out.print(decimalFormat.format( percTimes[1] ));
          out.print(percentString);
          out.write("\r\n");
          out.write("</a>\r\n");
          out.write("</td>\r\n");

       ++jj;
     }

          out.write("\r\n");
          out.write("</tr>\r\n");

   }

          out.write("\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
 } 
          out.write('\r');
          out.write('\n');
          int evalDoAfterBody = _jspx_th_c_005fif_005f21.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f26 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f26.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f26.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(1785,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f26.setTest(!msToExceptionMap.isEmpty());
      int _jspx_eval_c_005fif_005f26 = _jspx_th_c_005fif_005f26.doStartTag();
      if (_jspx_eval_c_005fif_005f26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<tr><td height=\"14\"/></tr>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<td>\r\n");
          out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
          out.write("<thead>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\">\r\n");
          out.write("<div class=\"frameTitle\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.METHOD_SERVER ));
          out.write("</span></div>\r\n");
          out.write("</th>\r\n");
          out.write("<th width=\"1\"/>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\">\r\n");
          out.write("<div class=\"frameTitle\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.ERROR ));
          out.write("</span></div>\r\n");
          out.write("</th>\r\n");
          out.write("</tr>\r\n");
          out.write("</thead>\r\n");
          out.write("<tbody class=\"tablebody\">\r\n");

   {
     int  ii = 0;
     for ( Map.Entry<String,Throwable> msToExceptionEntry : msToExceptionMap.entrySet() )
     {
       ++ii;

          out.write("\r\n");
          out.write("<tr class='");
          out.print(( ( ii % 2 ) == 0 ) ? "e" : "o");
          out.write("'>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
          out.print(xmlEscape( msToExceptionEntry.getKey() ));
          out.write("</th>\r\n");
          out.write("<th width=\"1\"/>\r\n");
          out.write("<td class=\"c\" style=\"color: red\">");
          out.print(xmlEscape( msToExceptionEntry.getValue() ));
          out.write("</td>\r\n");
          out.write("</tr>\r\n");

     }
   }

          out.write("\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f26.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f26);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f26);
      out.write("\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("<a href=\"#\" class=\"ppdata\">");
      out.print(getXmlEscapedString( RB, serverStatusResource.BACK_TO_TOP ));
      out.write("</a>\r\n");
      out.write("</span>\r\n");
      out.write("</div>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");

 }

      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');

 for ( Map.Entry<String,Throwable> smToExceptionEntry : smToExceptionMap.entrySet() )
 {
   final String  serverManagerName = smToExceptionEntry.getKey();

      out.write("\r\n");
      out.write("<a name=\"");
      out.print(xmlEscape( serverManagerName ));
      out.write("\">\r\n");
      out.write("<h3 style='color: red'>");
      out.print(getXmlEscapedString( RB, serverStatusResource.SERVER_MANAGER ));
      out.write(':');
      out.write(' ');
      out.print(xmlEscape( serverManagerName ));
      out.write("</h3>\r\n");
      out.write("</a>\r\n");
      out.write("<table>\r\n");
      out.write("<tbody>\r\n");
      out.write("<tr valign=\"top\">\r\n");
      out.write("<th align=\"left\" style='color: red'>");
      out.print(getXmlEscapedString( RB, serverStatusResource.ERROR_EMPHASIZED ));
      out.write(": </th>\r\n");
      out.write("<td>");
      out.print(xmlEscape( smToExceptionEntry.getValue() ));
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");

 }

      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("<a name=\"WindchillDS\"/>\r\n");

{  // intentionally limit scope of variables herein

      out.write('\r');
      out.write('\n');
      //  c:choose
      org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f3 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
      _jspx_th_c_005fchoose_005f3.setPageContext(_jspx_page_context);
      _jspx_th_c_005fchoose_005f3.setParent(null);
      int _jspx_eval_c_005fchoose_005f3 = _jspx_th_c_005fchoose_005f3.doStartTag();
      if (_jspx_eval_c_005fchoose_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write('\r');
          out.write('\n');
          //  c:when
          org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f3 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
          _jspx_th_c_005fwhen_005f3.setPageContext(_jspx_page_context);
          _jspx_th_c_005fwhen_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
          // /wtcore/jsp/jmx/serverStatus.jsp(1863,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fwhen_005f3.setTest(wcDsAttrMap != null);
          int _jspx_eval_c_005fwhen_005f3 = _jspx_th_c_005fwhen_005f3.doStartTag();
          if (_jspx_eval_c_005fwhen_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<table>\r\n");
              out.write("<tbody>\r\n");
              out.write("<tr valign=\"top\">\r\n");
              out.write("<td>\r\n");
              out.write("<div class=\"frame, frame_outer\">\r\n");
              out.write("<span style=\"width: 100%\">  ");
              out.write('\r');
              out.write('\n');
              out.write("\r\n");
              out.write("<div class=\"frameTitle\">\r\n");
              out.write("<table class=\"pp\" width=\"100%\">\r\n");
              out.write("<tbody>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"left\" valign=\"top\">\r\n");
              out.write("<table class=\"pp\">\r\n");
              out.write("<tbody>\r\n");
              out.write("<tr>\r\n");
              out.write("<td class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.WINDCHILL_DS ));
              out.write(": </td>\r\n");
              out.write("<td class=\"ppdata\">\r\n");
              //  c:choose
              org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f4 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
              _jspx_th_c_005fchoose_005f4.setPageContext(_jspx_page_context);
              _jspx_th_c_005fchoose_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
              int _jspx_eval_c_005fchoose_005f4 = _jspx_th_c_005fchoose_005f4.doStartTag();
              if (_jspx_eval_c_005fchoose_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write('\r');
                  out.write('\n');
                  //  c:when
                  org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f4 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
                  _jspx_th_c_005fwhen_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fwhen_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                  // /wtcore/jsp/jmx/serverStatus.jsp(1882,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fwhen_005f4.setTest(isPrivilegedUser);
                  int _jspx_eval_c_005fwhen_005f4 = _jspx_th_c_005fwhen_005f4.doStartTag();
                  if (_jspx_eval_c_005fwhen_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\r\n");
                      out.write("<a href='mbeanDump.jsp?target=windchillDS'>");
                      out.print(xmlEscape( wcDsAttrMap.get("Name") ));
                      out.write("</a>\r\n");
                      int evalDoAfterBody = _jspx_th_c_005fwhen_005f4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fwhen_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f4);
                  out.write('\r');
                  out.write('\n');
                  //  c:otherwise
                  org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f3 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
                  _jspx_th_c_005fotherwise_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fotherwise_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f4);
                  int _jspx_eval_c_005fotherwise_005f3 = _jspx_th_c_005fotherwise_005f3.doStartTag();
                  if (_jspx_eval_c_005fotherwise_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write('\r');
                      out.write('\n');
                      out.print(xmlEscape( wcDsAttrMap.get("Name") ));
                      out.write('\r');
                      out.write('\n');
                      int evalDoAfterBody = _jspx_th_c_005fotherwise_005f3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fotherwise_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f3);
                  out.write('\r');
                  out.write('\n');
                  int evalDoAfterBody = _jspx_th_c_005fchoose_005f4.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fchoose_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f4);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f4);
              out.write("\r\n");
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f27 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f27.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
              // /wtcore/jsp/jmx/serverStatus.jsp(1891,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f27.setTest(dsJmxUrlString != null);
              int _jspx_eval_c_005fif_005f27 = _jspx_th_c_005fif_005f27.doStartTag();
              if (_jspx_eval_c_005fif_005f27 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("<tr>\r\n");
                  out.write("<td class=\"pplabel\">");
                  out.print(getXmlEscapedString( RB, serverStatusResource.JMX_URL ));
                  out.write(": </td>\r\n");
                  out.write("<td class=\"ppdata\">");
                  out.print(xmlEscape( dsJmxUrlString ));
                  out.write("</td>\r\n");
                  out.write("</tr>\r\n");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f27.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f27);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f27);
              out.write("\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              out.write("</td>\r\n");
              out.write("<td align=\"right\" valign=\"top\">\r\n");
              out.write("<table class=\"pp\">\r\n");
              out.write("<tbody>\r\n");
              out.write("<tr>\r\n");
              out.write("<td class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.UPTIME ));
              out.write(": </td>\r\n");
              out.write("<td class=\"ppdata\">");
              out.print(xmlEscape( renderMillis( (Long) wcDsAttrMap.get( "Uptime" ), RB, localeObj ) ));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");

 final boolean  dsDeadlocked = ( wcDsAttrMap.get( "Deadlocked" ) != null );

              out.write("\r\n");
              out.write("<tr>\r\n");
              out.write("<td class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.DEADLOCKED ));
              out.write(": </td>\r\n");
              out.write("<td class=\"ppdata\" ");
              out.print(dsDeadlocked ? "style='color: red'" : "");
              out.write('>');
              out.print(getXmlEscapedString( RB, dsDeadlocked ? serverStatusResource.YES : serverStatusResource.NO ));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              out.write("</div>\r\n");
              out.write("\r\n");
              out.write("<div class=\"frameContent\">\r\n");
              out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
              out.write("<tbody>\r\n");
              out.write("<tr valign=\"top\">\r\n");
              out.write("<td>\r\n");
              out.write("\r\n");
              out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
              out.write("<tbody>\r\n");
              out.write("<tr valign=\"top\">\r\n");
              out.write("<td>\r\n");
              out.write("\r\n");
              out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
              out.write("<thead>\r\n");
              out.write("<tr>\r\n");
              out.write("<th/>\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.RECENT ));
              out.write("&#160;</span></th>\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.BASELINE ));
              out.write("&#160;</span></th>\r\n");
              out.write("</tr>\r\n");
              out.write("</thead>\r\n");
              out.write("<tbody class=\"tablebody\">\r\n");
              out.write("<tr class=\"o\">\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.TIME_IN_GC ));
              out.write("&#160;</th>\r\n");

 final double  dsPercTimeInGCLimit = (Double) wcDsAttrMap.get( "PercentTimeSpentInGCThreshold" );
 final double  dsPercTimeInGCRecent = (Double) wcDsAttrMap.get( "RecentPercentTimeSpentInGC" );
 final double  dsPercTimeInGCBaseline = (Double) wcDsAttrMap.get( "OverallPercentTimeSpentInGC" );

              out.write("\r\n");
              out.write("<td class=\"c\">\r\n");
              out.write("<a href='liveDSGcChart.jsp' ");
              out.print(( ( dsPercTimeInGCLimit > 0 ) && ( dsPercTimeInGCRecent > dsPercTimeInGCLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.write('\r');
              out.write('\n');
              out.print(decimalFormat.format( dsPercTimeInGCRecent ));
              out.print(percentString);
              out.write("\r\n");
              out.write("</a>\r\n");
              out.write("</td>\r\n");
              out.write("<td class=\"c\" ");
              out.print(( ( dsPercTimeInGCLimit > 0 ) && ( dsPercTimeInGCBaseline > dsPercTimeInGCLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.print(decimalFormat.format( dsPercTimeInGCBaseline ));
              out.print(percentString);
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr class=\"e\">\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.CPU_USED_BY_PROCESS ));
              out.write("&#160;</th>\r\n");

 final double  dsPercCpuLimit = (Double) wcDsAttrMap.get( "ProcessPercentCpuThreshold" );
 final double  dsPercCpuUsedOverall = (Double) wcDsAttrMap.get( "AverageProcessPercentCpu" );  // using overall rather than baseline...
 final CompositeData  dsRecentCpuData = (CompositeData) wcDsAttrMap.get( "RecentCpuData" );
 final double  dsPercCpuUsedRecent = ( ( dsRecentCpuData != null ) ? (Double) dsRecentCpuData.get( "processPercentCpu" ) : dsPercCpuUsedOverall );

              out.write("\r\n");
              out.write("<td class=\"c\">\r\n");
              out.write("<a href=\"liveDSCpuChart.jsp\" ");
              out.print(( ( dsPercCpuLimit > 0 ) && ( dsPercCpuUsedRecent > dsPercCpuLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.write('\r');
              out.write('\n');
              out.print(decimalFormat.format( dsPercCpuUsedRecent ));
              out.print(percentString);
              out.write("\r\n");
              out.write("</a>\r\n");
              out.write("</td>\r\n");
              out.write("<td class=\"c\" ");
              out.print(( ( dsPercCpuLimit > 0 ) && ( dsPercCpuUsedOverall > dsPercCpuLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.print(decimalFormat.format( dsPercCpuUsedOverall ));
              out.print(percentString);
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              out.write("</td>\r\n");
              out.write("<td>&#160;&#160;</td>\r\n");
              out.write("<td>\r\n");
              out.write("\r\n");
              out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
              out.write("<tbody class=\"tablebody\">\r\n");
              out.write("<tr>\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
              out.write("<span class=\"pplabel\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.MEMORY_USED ));
              out.write("&#160;</span>\r\n");
              out.write("</th>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr class=\"o\">\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.HEAP ));
              out.write("&#160;</th>\r\n");
 final double  dsPercHeapLimit = (Double) wcDsAttrMap.get( "HeapPercentUsageThreshold" ), dsPercHeapUsed = (Double) wcDsAttrMap.get( "HeapPercentUsage" ); 
              out.write("\r\n");
              out.write("<td class=\"c\">\r\n");
              out.write("<a href=\"liveDSMemUsageChart.jsp\" ");
              out.print(( ( dsPercHeapLimit > 0 ) && ( dsPercHeapUsed > dsPercHeapLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.write('\r');
              out.write('\n');
              out.print(decimalFormat.format( dsPercHeapUsed ));
              out.print(percentString);
              out.write("\r\n");
              out.write("</a>\r\n");
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr class=\"e\">\r\n");
              out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.PERM_GEN ));
              out.write("&#160;</th>\r\n");
 final double  dsPercPermLimit = (Double) wcDsAttrMap.get( "PermGenPercentUsageThreshold" ), dsPercPermUsed = (Double) wcDsAttrMap.get( "PermGenPercentUsage" ); 
              out.write("\r\n");
              out.write("<td class=\"c\">\r\n");
              out.write("<a href=\"liveDSMemUsageChart.jsp?permGen=true\" ");
              out.print(( ( dsPercPermLimit > 0 ) && ( dsPercPermUsed > dsPercPermLimit ) ? "style='color: red'" : "" ));
              out.write('>');
              out.write('\r');
              out.write('\n');
              out.print(decimalFormat.format( dsPercPermUsed ));
              out.print(percentString);
              out.write("\r\n");
              out.write("</a>\r\n");
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              out.write("</td>\r\n");

 final String  dsPhysMemInfo = renderSysMemInfo( wcDsAttrMap, "FreePhysicalMemorySize", "TotalPhysicalMemorySize", decimalFormat, RB );
 final String  dsSwapMemInfo = renderSysMemInfo( wcDsAttrMap, "FreeSwapSpaceSize", "TotalSwapSpaceSize", decimalFormat, RB );

              out.write('\r');
              out.write('\n');
              out.write('\r');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f28 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f28.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
              // /wtcore/jsp/jmx/serverStatus.jsp(2010,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f28.setTest(( dsPhysMemInfo != null ) || ( dsSwapMemInfo != null ));
              int _jspx_eval_c_005fif_005f28 = _jspx_th_c_005fif_005f28.doStartTag();
              if (_jspx_eval_c_005fif_005f28 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("<td>&#160;&#160;</td>\r\n");
                  out.write("<td>\r\n");
                  out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
                  out.write("<tbody class=\"tablebody\">\r\n");
                  out.write("<tr>\r\n");
                  out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
                  out.write("<span class=\"pplabel\">");
                  out.print(getXmlEscapedString( RB, serverStatusResource.AVAILABLE_SYSTEM_MEMORY ));
                  out.write("&#160;</span>\r\n");
                  out.write("</th>\r\n");
                  out.write("</tr>\r\n");
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f29 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f29.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f28);
                  // /wtcore/jsp/jmx/serverStatus.jsp(2020,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f29.setTest(dsPhysMemInfo != null);
                  int _jspx_eval_c_005fif_005f29 = _jspx_th_c_005fif_005f29.doStartTag();
                  if (_jspx_eval_c_005fif_005f29 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\r\n");
                      out.write("<tr class=\"o\">\r\n");
                      out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
                      out.print(getXmlEscapedString( RB, serverStatusResource.PHYSICAL ));
                      out.write("&#160;</th>\r\n");
                      out.write("<td class=\"c\" nowrap=\"nowrap\">");
                      out.print(xmlEscape( dsPhysMemInfo ));
                      out.write("</td>\r\n");
                      out.write("</tr>\r\n");
                      int evalDoAfterBody = _jspx_th_c_005fif_005f29.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fif_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f29);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f29);
                  out.write('\r');
                  out.write('\n');
                  //  c:if
                  org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f30 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                  _jspx_th_c_005fif_005f30.setPageContext(_jspx_page_context);
                  _jspx_th_c_005fif_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f28);
                  // /wtcore/jsp/jmx/serverStatus.jsp(2026,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_c_005fif_005f30.setTest(dsSwapMemInfo != null);
                  int _jspx_eval_c_005fif_005f30 = _jspx_th_c_005fif_005f30.doStartTag();
                  if (_jspx_eval_c_005fif_005f30 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    do {
                      out.write("\r\n");
                      out.write("<tr class=\"e\">\r\n");
                      out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
                      out.print(getXmlEscapedString( RB, serverStatusResource.SWAP ));
                      out.write("&#160;</th>\r\n");
                      out.write("<td class=\"c\" nowrap=\"nowrap\">");
                      out.print(xmlEscape( dsSwapMemInfo ));
                      out.write("</td>\r\n");
                      out.write("</tr>\r\n");
                      int evalDoAfterBody = _jspx_th_c_005fif_005f30.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                  }
                  if (_jspx_th_c_005fif_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f30);
                    return;
                  }
                  _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f30);
                  out.write("\r\n");
                  out.write("</tbody>\r\n");
                  out.write("</table>\r\n");
                  out.write("</td>\r\n");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f28.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f28);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f28);
              out.write('\r');
              out.write('\n');

 final Double  dsSystemLoadAverage = (Double) wcDsAttrMap.get( "SystemLoadAverage" );

              out.write('\r');
              out.write('\n');
              out.write('\r');
              out.write('\n');
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f31 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f31.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f3);
              // /wtcore/jsp/jmx/serverStatus.jsp(2040,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f31.setTest(( dsSystemLoadAverage != null ) && ( dsSystemLoadAverage >= 0 ));
              int _jspx_eval_c_005fif_005f31 = _jspx_th_c_005fif_005f31.doStartTag();
              if (_jspx_eval_c_005fif_005f31 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\r\n");
                  out.write("<td>&#160;&#160;</td>\r\n");
                  out.write("<td>\r\n");
                  out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
                  out.write("<tbody class=\"tablebody\">\r\n");
                  out.write("<tr>\r\n");
                  out.write("<th nowrap=\"nowrap\" scope=\"col\" colspan=\"2\" class=\"tablecolumnheaderbg\">\r\n");
                  out.write("<span class=\"pplabel\">");
                  out.print(getXmlEscapedString( RB, serverStatusResource.OTHER_SYSTEM_INFO ));
                  out.write("&#160;</span>\r\n");
                  out.write("</th>\r\n");
                  out.write("</tr>\r\n");
                  out.write("<tr class=\"o\">\r\n");
                  out.write("<th nowrap=\"nowrap\" scope=\"row\" class=\"c\">");
                  out.print(getXmlEscapedString( RB, serverStatusResource.LOAD_AVERAGE ));
                  out.write("&#160;</th>\r\n");
                  out.write("<td class=\"c\">");
                  out.print(decimalFormat.format( dsSystemLoadAverage ));
                  out.write("</td>\r\n");
                  out.write("</tr>\r\n");
                  out.write("</tbody>\r\n");
                  out.write("</table>\r\n");
                  out.write("</td>\r\n");
                  int evalDoAfterBody = _jspx_th_c_005fif_005f31.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f31);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f31);
              out.write("\r\n");
              out.write("</tr>\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              out.write("</div>\r\n");
              out.write("\r\n");
              out.write("<a href=\"#\" class=\"ppdata\">");
              out.print(getXmlEscapedString( RB, serverStatusResource.BACK_TO_TOP ));
              out.write("</a>\r\n");
              out.write("</span>\r\n");
              out.write("</div>\r\n");
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              int evalDoAfterBody = _jspx_th_c_005fwhen_005f3.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fwhen_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f3);
          out.write('\r');
          out.write('\n');
          //  c:otherwise
          org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f4 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
          _jspx_th_c_005fotherwise_005f4.setPageContext(_jspx_page_context);
          _jspx_th_c_005fotherwise_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f3);
          int _jspx_eval_c_005fotherwise_005f4 = _jspx_th_c_005fotherwise_005f4.doStartTag();
          if (_jspx_eval_c_005fotherwise_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("<h3 style='color: red'>");
              out.print(getXmlEscapedString( RB, serverStatusResource.WINDCHILL_DS ));
              out.write("</h3>\r\n");
              out.write("<table>\r\n");
              out.write("<tbody>\r\n");
              out.write("<tr valign=\"top\">\r\n");
              out.write("<th align=\"left\" style='color: red'>");
              out.print(getXmlEscapedString( RB, serverStatusResource.ERROR_EMPHASIZED ));
              out.write(": </th>\r\n");
              out.write("<td>");
              out.print(xmlEscape( wcDsResultRetrievalException ));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("</tbody>\r\n");
              out.write("</table>\r\n");
              int evalDoAfterBody = _jspx_th_c_005fotherwise_005f4.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fotherwise_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f4);
          out.write('\r');
          out.write('\n');
          int evalDoAfterBody = _jspx_th_c_005fchoose_005f3.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fchoose_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f3);
      out.write('\r');
      out.write('\n');

}  // intentionally limit scope of variables herein

      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      //  c:if
      org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f32 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
      _jspx_th_c_005fif_005f32.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f32.setParent(null);
      // /wtcore/jsp/jmx/serverStatus.jsp(2091,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f32.setTest(siteStatusData != null );
      int _jspx_eval_c_005fif_005f32 = _jspx_th_c_005fif_005f32.doStartTag();
      if (_jspx_eval_c_005fif_005f32 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<a name=\"FileVaultSites\"/>\r\n");
          out.write("<table>\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr valign=\"top\">\r\n");
          out.write("<td>\r\n");
          out.write("<div class=\"frame, frame_outer\">\r\n");
          out.write("<span style=\"width: 100%\">  ");
          out.write('\r');
          out.write('\n');
          out.write("\r\n");
          out.write("<div class=\"frameTitle\">\r\n");
          out.write("<table class=\"pp\" width=\"100%\">\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr>\r\n");
          out.write("<td align=\"left\" valign=\"top\">\r\n");
          out.write("<table class=\"pp\">\r\n");
          out.write("<tbody>\r\n");
          out.write("<tr>\r\n");
          out.write("<td class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.FILE_VAULT_SITES ));
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</div>\r\n");
          out.write("\r\n");
          out.write("<div class=\"frameContent\">\r\n");
          out.write("\r\n");
          out.write("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"tablecellsepbg frameTable\">\r\n");
          out.write("<thead>\r\n");
          out.write("<tr>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.SITE_URL ));
          out.write("&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.NAME ));
          out.write("&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.STATUS ));
          out.write("&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.TIME_OF_LAST_PING ));
          out.write("&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.AVAILABILITY ));
          out.write("&#160;</span></th>\r\n");
          out.write("<th nowrap=\"nowrap\" scope=\"col\" class=\"tablecolumnheaderbg\"><span class=\"pplabel\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.AVERAGE_RESPONSE_TIME ));
          out.write("&#160;</span></th>\r\n");
          out.write("</tr>\r\n");
          out.write("</thead>\r\n");
          out.write("<tbody class=\"tablebody\">\r\n");

 int  rowCount = 0;
 for ( CompositeData siteStatus : MBeanUtilities.getSortedData( siteStatusData ) )
 {
   final String  names[] = (String[]) siteStatus.get( "names" );
   final Date  lastPingTime = (Date) siteStatus.get( "lastPing" );
   final String  lastStatus = (String) siteStatus.get( "lastStatus" );

          out.write("\r\n");
          out.write("<tr class='");
          out.print(( ( ++rowCount % 2 ) == 1 ? "o" : "e" ));
          out.write("'>\r\n");
          out.write("<td class=\"c\">");
          out.print(xmlEscape( siteStatus.get( "url" ) ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">");
          out.print(xmlEscape( ( names != null ) ? MBeanUtilities.getStringArrayAsString( names ) : null ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\" ");
          out.print(SiteMonitorMBean.OK_STATUS.equals( lastStatus ) ? "" : "style='color: red'" );
          out.write('>');
          out.print(xmlEscape( lastStatus ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">");
          out.print(xmlEscape( ( lastPingTime != null ) ? dateFormat.format( lastPingTime ) : null ));
          out.write("</td>\r\n");
          out.write("<td class=\"c\">");
          out.print(xmlEscape( decimalFormat.format( siteStatus.get( "percentageUptime" ) ) ));
          out.print(percentString);
          out.write("</td>\r\n");
          out.write("<td class=\"c\">");
          out.print(xmlEscape( decimalFormat.format( siteStatus.get( "averageResponseSeconds" ) ) ));
          out.write("</td>\r\n");
          out.write("</tr>\r\n");

   ++rowCount;
 }

          out.write("\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          out.write("</div>\r\n");
          out.write("\r\n");
          out.write("<a href=\"#\" class=\"ppdata\">");
          out.print(getXmlEscapedString( RB, serverStatusResource.BACK_TO_TOP ));
          out.write("</a>\r\n");
          out.write("</span>\r\n");
          out.write("</div>\r\n");
          out.write("</td>\r\n");
          out.write("</tr>\r\n");
          out.write("</tbody>\r\n");
          out.write("</table>\r\n");
          int evalDoAfterBody = _jspx_th_c_005fif_005f32.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f32);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f32);
      out.write("\r\n");
      out.write("<br />\r\n");
      out.write("\r\n");
      out.write("<table>\r\n");
      out.write("<tbody>\r\n");
      out.write("<tr>\r\n");
      out.write("<td width=\"60\"/>\r\n");
      out.write("<td class=\"ppdata\">\r\n");
      out.write("<i>\r\n");
      out.print( MBeanUtilities.formatMessage( RB.getString( serverStatusResource.DATA_COLLECTED_BETWEEN_MSG ),
                                  dateFormat.format( new Date( dataCollectionStart ) ),
                                  dateFormat.format( new Date( dataCollectionEnd ) ) ) );
      out.write("\r\n");
      out.write("</i>\r\n");
      out.write("</td>\r\n");
      out.write("</tr>\r\n");
      out.write("</tbody>\r\n");
      out.write("</table>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
    */} /*catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }*/
