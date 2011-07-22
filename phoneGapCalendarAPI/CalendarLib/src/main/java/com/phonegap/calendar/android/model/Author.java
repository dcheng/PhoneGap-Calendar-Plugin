package com.phonegap.calendar.android.model;

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

import com.google.api.client.util.Key;

/**
 * Represents the author of the entry
* @author Yaniv Inbar
* @author Nicolas Garnier
* @author Sergio Martinez
*/
public class Author {

	/**
	 * Name of author
	 */
 @Key
 public String name;

 /**
  * E-mail address of author
  */
 @Key
 public String email;
}