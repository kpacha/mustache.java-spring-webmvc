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

import static junit.framework.Assert.assertEquals;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.servlet.view.mustache.MustacheTemplateLoader;

/**
 * @author Eric D. White <eric@ericwhite.ca>
 * @author kpacha <kpacha666@gmail.com>
 */
@RunWith(JMock.class)
public class MustachePDFViewResolverTest {

    private Mockery context = new Mockery() {
	{
	    setImposteriser(ClassImposteriser.INSTANCE);
	}
    };

    /**
     * Check the required view class
     */
    @Test
    public void checkTheRequiredViewClass() throws Exception {
	final MustacheTemplateLoader templateLoader = context
		.mock(MustacheTemplateLoader.class);

	context.checking(new Expectations() {
	    {
		oneOf(templateLoader).setPrefix(with(any(String.class)));
	    }
	});

	MustachePDFViewResolver r = new MustachePDFViewResolver();
	r.setTemplateLoader(templateLoader);
	r.afterPropertiesSet();

	Class<?> className = r.requiredViewClass();
	assertEquals(MustachePDFView.class, className);
    }

    /**
     * Check the required view class
     */
    @Test
    public void returnUrlWithoutExtension() throws Exception {
	final MustacheTemplateLoader templateLoader = context
		.mock(MustacheTemplateLoader.class);

	context.checking(new Expectations() {
	    {
		oneOf(templateLoader).setPrefix(with(any(String.class)));
	    }
	});

	MustachePDFViewResolver r = new MustachePDFViewResolver();
	r.setTemplateLoader(templateLoader);
	r.afterPropertiesSet();

	final String viewName = "top-level.mustache";
	final String realViewName = r.getRealUrl(viewName + ".pdf");
	assertEquals(viewName, realViewName);
    }
}
