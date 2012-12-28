package org.springframework.web.servlet.view.mustache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import com.github.mustachejava.Mustache;

public abstract class AbstractMustacheViewResolver extends
	AbstractTemplateViewResolver implements ViewResolver, InitializingBean {

    private MustacheTemplateLoader templateLoader;

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {

	final MustacheView view = (MustacheView) super.buildView(viewName);

	Mustache template = templateLoader.compile(view.getUrl());
	view.setTemplate(template);

	return view;
    }

    /**
     * Forward the configuration onward so that the template loader knows the
     * prefix used by the view resolver to lookup templates in the classpath.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
	templateLoader.setPrefix(this.getPrefix());
    }

    @Required
    public void setTemplateLoader(MustacheTemplateLoader templateLoader) {
	this.templateLoader = templateLoader;
    }

}
