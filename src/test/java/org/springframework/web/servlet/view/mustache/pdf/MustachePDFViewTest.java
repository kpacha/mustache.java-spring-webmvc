/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.web.servlet.view.mustache.pdf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.jmock.lib.script.ScriptedAction.perform;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXParseException;

import com.github.mustachejava.Mustache;

/**
 * @author Eric D. White <eric@ericwhite.ca>
 * @author kpacha <kpacha666@gmail.com>
 */
@RunWith(JMock.class)
public class MustachePDFViewTest {

    private final Mockery context = new Mockery() {
	{
	    setImposteriser(ClassImposteriser.INSTANCE);
	}
    };

    @Test
    public void rendersAModelUsingItsTemplate() throws Exception {
	final Map<String, Object> model = Collections
		.<String, Object> emptyMap();

	HttpServletRequest UNUSED_REQUEST = null;
	final HttpServletResponse response = context
		.mock(HttpServletResponse.class);
	final ServletOutputStream outputStream = context
		.mock(ServletOutputStream.class);
	final Mustache template = context.mock(Mustache.class);

	final byte[] rawOutput = new byte[1];
	context.checking(new Expectations() {
	    {
		oneOf(template).execute(with(any(StringWriter.class)),
			with(model));
		will(perform("$0.write(content)").where("content",
			getTestContent()));
		oneOf(response).getOutputStream();
		will(returnValue(outputStream));
		oneOf(outputStream).write(with(any(rawOutput.getClass())),
			with(any(Integer.class)), with(any(Integer.class)));
		allowing(outputStream).flush();
		oneOf(outputStream).close();
	    }
	});

	MustachePDFView view = new MustachePDFView();
	view.setTemplate(template);
	view.renderMergedTemplateModel(model, UNUSED_REQUEST, response);

	assertThat(view.getTemplate(), equalTo(template));
    }

    @Test
    public void renderThrowsAnException() throws Exception {
	final Map<String, Object> model = Collections
		.<String, Object> emptyMap();

	HttpServletRequest UNUSED_REQUEST = null;
	final HttpServletResponse response = context
		.mock(HttpServletResponse.class);
	final ServletOutputStream outputStream = context
		.mock(ServletOutputStream.class);
	final Mustache template = context.mock(Mustache.class);

	context.checking(new Expectations() {
	    {
		oneOf(template).execute(with(any(StringWriter.class)),
			with(model));
		oneOf(response).getOutputStream();
		will(returnValue(outputStream));
		oneOf(outputStream).flush();
	    }
	});

	MustachePDFView view = new MustachePDFView();
	view.setTemplate(template);
	try {
	    view.renderMergedTemplateModel(model, UNUSED_REQUEST, response);
	    assertTrue(false);
	} catch (SAXParseException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void theResponseIsDownloable() {
	MustachePDFView view = new MustachePDFView();
	assertTrue(view.generatesDownloadContent());
    }

    @Test
    public void checkTheResponseContentType() {
	MustachePDFView view = new MustachePDFView();
	assertThat(view.getContentType(), equalTo("application/pdf"));
    }

    private String getTestContent() throws FileNotFoundException, IOException {
	final StringWriter writer = new StringWriter();
	final BufferedReader reader = new BufferedReader(new FileReader(
		new ClassPathResource("WEB-INF/views/test-cjk.html").getFile()));

	String line = null;
	while ((line = reader.readLine()) != null) {
	    writer.write(line);
	}
	reader.close();
	return writer.toString();
    }
}
