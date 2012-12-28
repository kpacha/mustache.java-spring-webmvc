/*
 * Copyright 2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.web.servlet.view.mustache.pdf;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.servlet.view.mustache.MustacheView;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;

import com.lowagie.text.DocumentException;

/**
 * This is the spring view use to generate the content based on a Mustache
 * template and then return it as a pdf file.
 * 
 * @author kpacha <kpacha666@gmail.com>
 */
public class MustachePDFView extends MustacheView {

    public MustachePDFView() {
	setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
	return true;
    }

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
	    HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	StringWriter stringWriter = new StringWriter();
	getTemplate().execute(stringWriter, model);

	try {
	    // parse the markup into an xml Document
	    final Document doc = DocumentBuilderFactory
		    .newInstance()
		    .newDocumentBuilder()
		    .parse(new InputSource(new StringReader(stringWriter
			    .toString())));

	    final ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocument(doc, null);
	    renderer.layout();
	    renderer.createPDF(response.getOutputStream());
	} catch (DocumentException e) {
	    e.printStackTrace();
	} finally {
	    response.getOutputStream().flush();
	}

    }
}
