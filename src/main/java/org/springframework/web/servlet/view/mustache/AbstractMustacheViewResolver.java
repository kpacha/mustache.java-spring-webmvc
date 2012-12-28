package org.springframework.web.servlet.view.mustache;

import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import com.github.mustachejava.Mustache;

public abstract class AbstractMustacheViewResolver extends
	AbstractTemplateViewResolver implements ViewResolver, InitializingBean {

    private MustacheTemplateLoader templateLoader;

    private String[] excludedViewNames = null;

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {

	final MustacheView view = (MustacheView) super.buildView(viewName);

	Mustache template = templateLoader.compile(getRealUrl(view.getUrl()));
	view.setTemplate(template);

	return view;
    }

    protected String getRealUrl(String url) {
	return url;
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

    /**
     * Indicates whether or not this
     * {@link org.springframework.web.servlet.ViewResolver} can handle the
     * supplied view name. If not, {@link #createView(String, java.util.Locale)}
     * will return <code>null</code>.
     * 
     * This implementation also checks against the configured
     * {@link #setExcludedExtensions extensions to exclude from the view names}.
     * 
     * @param viewName
     *            the name of the view to retrieve
     * @param locale
     *            the Locale to retrieve the view for
     * @return whether this resolver applies to the specified view
     * @see org.springframework.util.PatternMatchUtils#simpleMatch(String,
     *      String)
     */
    protected boolean canHandle(String viewName, Locale locale) {
	String[] excludedViewNames = getExcludedViewNames();
	boolean isExcluded = (excludedViewNames != null && PatternMatchUtils
		.simpleMatch(excludedViewNames, viewName));
	return super.canHandle(viewName, locale) && !isExcluded;
    }

    /**
     * @return the excludedViewNames
     */
    protected String[] getExcludedViewNames() {
	return excludedViewNames;
    }

    /**
     * @param excludedViewNames
     *            the excludedExtensions to set
     */
    public void setExcludedViewNames(String[] excludedViewNames) {
	this.excludedViewNames = excludedViewNames;
    }

}
