package com.p3.export.utility.json;

/**
 * The JSONException is thrown by the JSON.org classes when things are amiss.
 *
 * @author JSON.org
 * @version 2010-12-24
 */
public class JSONException extends Exception {
  private static final long serialVersionUID = 0;
  private Throwable cause;

  /**
   * Constructs a JSONException with an explanatory message.
   *
   * @param message Detail about the reason for the exception.
   */
  public JSONException(final String message) {
    super(message);
  }

  public JSONException(final Throwable cause) {
    super(cause.getMessage());
    this.cause = cause;
  }

  @Override
  public Throwable getCause() {
    return cause;
  }
}
