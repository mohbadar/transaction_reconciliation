/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.tutuka.lib.lang.exception.service;

import com.tutuka.lib.lang.error.service.ServiceError;

import java.text.MessageFormat;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class ServiceException extends RuntimeException {

  private final ServiceError serviceError;

  public ServiceException(final ServiceError serviceError) {
    super(serviceError.getMessage());
    this.serviceError = serviceError;
  }

  public static ServiceException badRequest(final String message, final Object... args) {
    return new ServiceException(ServiceError
        .create(400)
        .message(MessageFormat.format(message, args))
        .build());
  }

  public static ServiceException notFound(final String message, final Object... args) {
    return new ServiceException(ServiceError
        .create(404)
        .message(MessageFormat.format(message, args))
        .build());
  }

  public static ServiceException conflict(final String message, final Object... args) {

    return new ServiceException(ServiceError
        .create(409)
        .message(MessageFormat.format(message, args))
        .build());
  }

  public static ServiceException internalError(final String message, final Object... args) {
    return new ServiceException(ServiceError
        .create(500)
        .message(MessageFormat.format(message, args))
        .build());
  }

  public ServiceError serviceError() {
    return this.serviceError;
  }

  @Override
  public String toString() {
    return "ServiceException{" +
            "serviceError=" + serviceError +
            '}';
  }
}
