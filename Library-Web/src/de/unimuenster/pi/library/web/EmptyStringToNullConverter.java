package de.unimuenster.pi.library.web;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Workaround, because Tomcat does not interpret a context parameter
 * <code>javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL = true</code>
 * correctly.
 * 
 * @author Henning Heitkoetter
 */
public class EmptyStringToNullConverter implements Converter {
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value != null && value.isEmpty()) {
            if (component instanceof EditableValueHolder) {
                ((EditableValueHolder) component).setSubmittedValue(null);
            }
            value = null;
        }
        return value;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        return (value != null) ? value.toString() : null;
    }
}
