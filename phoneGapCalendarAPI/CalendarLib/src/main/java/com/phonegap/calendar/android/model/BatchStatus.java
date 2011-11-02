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


/*
 *  Modified by Sergio Martinez. Copyright 2011 Vodafone Group Services Ltd.
 * 
 */

package com.phonegap.calendar.android.model;

import com.google.api.client.util.Key;

/**
 * This class is not being used to perform any function in
 * this library at the moment.
 * @author Yaniv Inbar
 * @author Sergio Martinez Rodriguez
 */
public class BatchStatus {

	/**
	 * BatchStatus tag code field
	 */
  @Key("@code")
  public int code;

  /**
   * BatchStatus tag text field
   */
  @Key("text()")
  public String content;
  
  /**
   * BatchStatus tag content-type field 
   */
  @Key("@content-type")
  public String contentType;

  /**
   * BatchStatus tag reason field
   */
  @Key("@reason")
  public String reason;
  
  /**
   * Costructor
   */
  public BatchStatus() {
  }
}
