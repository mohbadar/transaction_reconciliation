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
package com.tutuka.lib.lang.validation.date;

import javax.annotation.Nonnull;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public interface DateConverter {
  @Nonnull
  static Long toEpochMillis(@Nonnull final LocalDateTime localDateTime) {
    return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  @Nonnull
  static Long toEpochDay(@Nonnull final LocalDate localDate) {
    return localDate.toEpochDay();
  }

  @Nonnull
  static LocalDateTime fromEpochMillis(@Nonnull final Long epochMillis) {
    return LocalDateTime.from(Instant.ofEpochMilli(epochMillis).atZone(ZoneOffset.UTC));
  }

  @Nonnull
  static String toIsoString(@Nonnull final Date date) {
    return toIsoString(LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC")));
  }

  @Nonnull
  static String toIsoString(@Nonnull final LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME) + "Z";
  }

  @Nonnull
  static LocalDateTime fromIsoString(@Nonnull final String isoDateTimeString) {
    return LocalDateTime.from(Instant.parse(isoDateTimeString).atZone(ZoneOffset.UTC));
  }

  @Nonnull
  static LocalDate dateFromIsoString(@Nonnull final String isoDateString) {
    final int zIndex = isoDateString.indexOf("Z");
    final String shortenedString = isoDateString.substring(0, zIndex);
    return LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(shortenedString));
  }

  @Nonnull
  static String toIsoString(@Nonnull final LocalDate localDate) {
    return localDate.format(DateTimeFormatter.ISO_DATE) + "Z";
  }

  @Nonnull
  static LocalDate toLocalDate(@Nonnull final LocalDateTime localDateTime) {
    return localDateTime.toLocalDate();
  }
}
