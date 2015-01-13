<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
 <dependencies>
   <#assign jsonArray=jsonObject?eval />
    <#list jsonArray as jsonItem>
        <dependency>
          				    	<version>${jsonItem.version}</version>
     			  	    	  <artifactId>${jsonItem.artifactId}<artifactId>
        				     <groupId>${jsonItem.groupId}</groupId>
        </dependency>
    </#list>
 </dependencies>	
</project>
