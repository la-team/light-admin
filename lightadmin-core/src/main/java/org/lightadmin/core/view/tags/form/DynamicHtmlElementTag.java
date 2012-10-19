package org.lightadmin.core.view.tags.form;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.web.util.WebContextUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.form.AbstractHtmlInputElementTag;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.SelectTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.jsp.JspException;
import java.io.Serializable;

public class DynamicHtmlElementTag extends AbstractHtmlInputElementTag {

	private DomainTypeAttributeMetadata attributeMetadata;

	@Override
	protected int writeTagContent( final TagWriter tagWriter ) throws JspException {
		if ( attributeMetadata.isMapLike() ) {
			return SKIP_BODY;
		}
		if ( attributeMetadata.isCollectionLike() || attributeMetadata.isSetLike() ) {
			return selectTag().doStartTag();
		}

		return inputTag().doStartTag();
	}

	private SelectTag selectTag() {
		SelectTag selectTag = new SelectTag() {
			@Override
			protected Object getItems() {
				try {
					return getBindStatus().getValue();
				} catch ( JspException e ) {
					// nop
				}
				return null;
			}
		};
		selectTag.setMultiple( true );
		selectTag.setCssClass( getCssClass() );
		selectTag.setPageContext( pageContext );
		selectTag.setParent( getParent() );
		selectTag.setPath( attributeMetadata.getName() );

		final Class<?> optionElementClass = attributeMetadata.getElementType();

		final String idAttributeName = entityInformation( optionElementClass ).getIdAttribute().getName();

		selectTag.setItemLabel( idAttributeName );
		selectTag.setItemValue( idAttributeName );
		return selectTag;
	}

	private InputTag inputTag() {
		InputTag inputTag = new InputTag();
		inputTag.setCssClass( getCssClass() );
		inputTag.setPath( attributeMetadata.getName() );
		inputTag.setPageContext( pageContext );
		inputTag.setParent( getParent() );
		return inputTag;
	}

	public DomainTypeAttributeMetadata getAttributeMetadata() {
		return attributeMetadata;
	}

	public void setAttributeMetadata( final DomainTypeAttributeMetadata attributeMetadata ) {
		this.attributeMetadata = attributeMetadata;
	}

	private Class<? extends Serializable> idType( final Class<?> typeClass ) {
		return entityInformation( typeClass ).getIdType();
	}

	private JpaEntityInformation<?, ?> entityInformation( final Class<?> typeClass ) {
		return JpaEntityInformationSupport.getMetadata( typeClass, entityManager() );
	}

	private WebApplicationContext webApplicationContext() {
		return WebContextUtils.getWebApplicationContext( pageContext.getServletContext() );
	}

	private EntityManager entityManager() {
		return webApplicationContext().getBean( EntityManagerFactory.class ).createEntityManager();
	}
}