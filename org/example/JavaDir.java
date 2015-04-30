package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.ContentAccessor;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.extensions.surf.util.Content;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.WrappingWebScriptRequest;
import org.springframework.extensions.webscripts.servlet.FormData;
import org.springframework.extensions.webscripts.servlet.FormData.FormField;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRequest;

public class JavaDir extends DeclarativeWebScript
{	 protected ServiceRegistry serviceRegistry;
protected ContentService contentService;
protected NodeService nodeService;
protected FileFolderService fileFolderService;
protected NodeRef parentNode;
	private Repository repository;
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
	       this.serviceRegistry = serviceRegistry;
	 }
	 
	public void setRepository(Repository repository)
	{
		this.repository = repository;
	}

	protected Map<String, Object> executeImpl(WebScriptRequest req,
			Status status, Cache cache)
			{
		 this.contentService = serviceRegistry.getContentService();
		  this.nodeService = serviceRegistry.getNodeService();
		  this.fileFolderService = serviceRegistry.getFileFolderService();
		  List<String> nodes = new ArrayList<String>();
		  nodes.add("test");
		  NodeRef companyhome=repository.getCompanyHome();
		  NodeRef parentNode;
		try {
			parentNode = serviceRegistry.getFileFolderService().resolveNamePath(companyhome, nodes).getNodeRef();
			 if (parentNode != null) {
				  FormData formData = (FormData)req.parseContent();
				        FormData.FormField[] fields = formData.getFields();
				        
				        for(FormData.FormField field : fields) {
				            if(field.getName().equals("file") && field.getIsFile()) {
				                writeContent(field, parentNode);
				            }
				        } 
				  
		
		
		
		}} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 
		return null;
		 }
			
		 protected NodeRef getNodeRef (String request) {
		  try {
		   return new NodeRef(request);
		  } catch (Exception e) {
		   System.out.println(e);
		   
		   return null;
		  }
		 }
		 

		 protected boolean writeContent (FormData.FormField field, NodeRef parentNode) {
		  boolean success = false;
		  
		  String fileName = field.getFilename();
		        Content content =  field.getContent();
		        String mimetype = field.getMimetype();
		        NodeRef newNode = createNewNode(parentNode, fileName);
		        
		  try {
		   ContentWriter writer = contentService.getWriter(newNode, ContentModel.PROP_CONTENT, true);
		   writer.setMimetype(mimetype);
		            writer.putContent(content.getInputStream());
		            
		            success = true;
		  } catch (Exception e) {
		   System.out.println(e);
		  }
		  
		  return success;
		 }
		 

		 protected NodeRef createNewNode (NodeRef parentNode, String fileName) {
		  try {
		   QName contentQName = QName.createQName("{http://www.alfresco.org/model/content/1.0}content");
		   FileInfo newNodeRef = fileFolderService.create(parentNode, fileName, contentQName);
		   
		   return newNodeRef.getNodeRef();
		  } catch (Exception e) {
		   System.err.println(e);
		   
		   return null;
		  }
		  
		 }
		 
}