/*
 * Copyright (c) 2010 Google Inc.
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

package com.phonegap.calendar.android.model;

import com.google.api.client.util.Key;

/**
 * Represents a base GData Reminder Entry.
 * 
 * @author Alain Vongsouvanh
 * @author Sergio Martinez Rodriguez
 */
public class Reminder {

  /**
   * No reminder.
   */
  public static final String METHOD_NONE = "none";
  /**
   * Send an email as reminder.
   */
  public static final String METHOD_EMAIL = "email";
  /**
   * Window pop-up in the web browser.
   */
  public static final String METHOD_ALERT = "alert";
  /**
   * Send a SMS as reminder.
   */
  public static final String METHOD_SMS = "sms";

  /**
   * Number of minutes before the event from 5 minutes to 4 weeks.
   */
  @Key("@minutes")
  public int minutes;

  /**
   * The reminder method. Possible values are {@link #METHOD_ALERT},
   * {@link #METHOD_EMAIL}, {@link #METHOD_NONE} and {@link #METHOD_SMS}.
   */
  @Key("@method")
  public String method;
}