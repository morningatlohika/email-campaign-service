package com.lohika.morning.ecs.vaadin;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

public class EcsLabel extends Label implements HasValue<String> {
  private boolean isLink = false;
  private static final Listener VOID_LISTENER = e -> {};

  public EcsLabel(String caption) {
    setCaptionAsHtml(true);
    setCaption(formatString("<b>{}</b>", caption));
  }

  public EcsLabel(String caption, boolean isLink) {
    this(caption);
    this.isLink = isLink;
  }

  @Override
  public void setValue(String value) {
    String linkValue = value;

    if (StringUtils.isNotBlank(value) && isLink) {
      setContentMode(ContentMode.HTML);
      linkValue = formatString("<a href=\"{}\">{}</a>", value, value);
    }

    super.setValue(linkValue);
  }

  @Override
  public Registration addValueChangeListener(ValueChangeListener<String> listener) {
    return addListener(VOID_LISTENER);
  }

  @Override
  public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
    throw new UnsupportedOperationException("Label doesn't support required indicators");
  }

  @Override
  public boolean isRequiredIndicatorVisible() {
    return false;
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    throw new UnsupportedOperationException("Label is read only by definition");
  }

  @Override
  public boolean isReadOnly() {
    return true;
  }
}
