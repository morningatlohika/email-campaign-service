package com.lohika.morning.ecs.ui;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class ExceptionHandler extends DefaultErrorHandler {
  @Override
  public void error(com.vaadin.server.ErrorEvent event) {
    // Find the final cause
    Throwable cause = event.getThrowable();
    while (cause.getCause() != null) {
      cause = cause.getCause();
    }

    new Notification("Exception",
        cause.getMessage(),
        Notification.Type.ERROR_MESSAGE, false)
        .show(Page.getCurrent());

    // Do the default errorMessage handling (optional)
    doDefault(event);
  }
}
