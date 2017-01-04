package com.questionbank.faces.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name="qdownloadMB")
@ViewScoped
public class QuestionDownloadManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private StreamedContent downloadTemplateFile;

	public void downloadTemplateFile() { 
		try {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();        
			File result = new File(extContext.getRealPath("//resources//mcq_template.xls"));        
			InputStream stream = new FileInputStream(result.getAbsolutePath());

			//InputStream stream = this.getClass().getResourceAsStream("mcq_template.ods");
			downloadTemplateFile = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "QuestionPaper_Template.xls");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public StreamedContent getTemplateFile() {
		return downloadTemplateFile;
	}

	public void setTemplateFile(StreamedContent mcqFile) {
		this.downloadTemplateFile = mcqFile;
	}

}
