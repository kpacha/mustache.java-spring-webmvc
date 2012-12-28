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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.mustache.AbstractMustacheViewResolver;

/**
 * This resolves views that are returned from the @Controller in Spring MVC.
 * 
 * In the controller you need to return a path to the mustache template. This
 * path is relative to the 'prefix' defined. See:
 * UrlBasedViewResolver::buildView
 * 
 * <pre>
 * For example:
 *       @Controller
 *       public class HelloWorldController {
 * 
 *          @RequestMapping(value="/hello")
 *          public String hello(Model m) {
 *              m.addAttribute("token", new java.util.Date());
 *              return "parent";
 *          }
 *       }
 * </pre>
 * 
 * In this example "parent" is the name of the mustache template.
 * 
 * @author kpacha <kpacha666@gmail.com>
 */
public class MustachePDFViewResolver extends AbstractMustacheViewResolver
	implements ViewResolver, InitializingBean {

    public MustachePDFViewResolver() {
	setViewClass(MustachePDFView.class);
    }

    /**
     * This is verified when the view class is set.
     */
    @Override
    protected Class<?> requiredViewClass() {
	return MustachePDFView.class;
    }

    @Override
    protected String getRealUrl(String url) {
	return url.replaceAll(".pdf", "");
    }

}
