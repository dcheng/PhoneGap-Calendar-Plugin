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
 * Performs multiples CRUD operations 
 * - This class is not being used to perform any function in
 * this library at the moment -
 * @author Yaniv Inbar
 * @author Sergio Martinez Rodriguez
 */
public class BatchOperation {
	
	/**
	   * INSERT BatchOperation Constant
	   */
  public static final BatchOperation INSERT = BatchOperation.of("insert");
  /**
   * QUERY BatchOperation Constant
   */
  public static final BatchOperation QUERY = BatchOperation.of("query");
  /**
   * UPDATE BatchOperation Constant
   */
  public static final BatchOperation UPDATE = BatchOperation.of("update");
  
  /**
   * DELETE BatchOperation Constant
   */
  public static final BatchOperation DELETE = BatchOperation.of("delete");

  /**
   * Kind of operation to be performed
   */
  @Key("@type")
  public String type;

  /**
   * Constructor
   */
  public BatchOperation() {
  }

  /**
   * Returns a BatchOperation instance with the specified operation in type param 
   * @param type String with the operation type
   * @return BatchOperation object with the selected operation
   */
  public static BatchOperation of(String type) {
    BatchOperation result = new BatchOperation();
    result.type = type;
    return result;
  }
}
