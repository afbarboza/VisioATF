/*
 * Copyright (C) 2014 Google Inc.
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

package com.google.android.apps.common.testing.accessibility.framework;

import static com.google.common.base.Preconditions.checkNotNull;

import android.view.View;
import com.google.android.apps.common.testing.accessibility.framework.checks.ClassNameCheck;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

/** Provides deprecated methods for {@link AccessibilityCheckPreset}. */
@SuppressWarnings("deprecation") // Need to support AccessibilityViewHierarchyCheck.
public final class AccessibilityCheckPresetAndroid {

  /**
   * Retrieve checks for {@code View}s based on a desired preset.
   *
   * @param preset The preset of interest
   * @return A set of all checks for {@code View}s with scopes for the preset
   * @deprecated ATF integrations should now use {@link
   *     AccessibilityCheckPreset#getAccessibilityHierarchyChecksForPreset(AccessibilityCheckPreset)}
   *     for the most up to date set of accessibility checks.
   */
  @Deprecated
  public static ImmutableSet<AccessibilityViewHierarchyCheck> getViewChecksForPreset(
      AccessibilityCheckPreset preset) {

    ImmutableSet<AccessibilityHierarchyCheck> checks =
        AccessibilityCheckPreset.getAccessibilityHierarchyChecksForPreset(AccessibilityCheckPreset.VERSION_4_0_CHECKS);

    // ClassNameCheck was available in
    // AccessibilityCheckPreset.getAccessibilityHierarchyChecksForPreset from version 3.0, but not
    // available here until 4.0 because the accessibility class names that the check requires were
    // not being obtained from Views until 4.0.
    if (preset == AccessibilityCheckPreset.VERSION_3_1_CHECKS) {
      checks =
          Sets.difference(
                  checks,
                  ImmutableSet.<AccessibilityHierarchyCheck>of(
                      AccessibilityCheckPreset.getHierarchyCheckForClass(ClassNameCheck.class)))
              .immutableCopy();
    }

    ImmutableSet.Builder<AccessibilityViewHierarchyCheck> viewChecks = ImmutableSet.builder();
    for (AccessibilityHierarchyCheck check : checks) {
      viewChecks.add(getDelegatedCheck(check.getClass()));
    }
    return viewChecks.build();
  }

  private static AccessibilityViewHierarchyCheck getDelegatedCheck(
      Class<? extends AccessibilityHierarchyCheck> clazz) {
    return new DelegatedViewHierarchyCheck(
        checkNotNull(AccessibilityCheckPreset.getHierarchyCheckForClass(clazz)));
  }

  /** An adapter to present an AccessibilityHierarchyCheck as an AccessibilityViewHierarchyCheck */
  /*package*/ static class DelegatedViewHierarchyCheck extends AccessibilityViewHierarchyCheck {

    private final AccessibilityHierarchyCheck toCheck;

    DelegatedViewHierarchyCheck(AccessibilityHierarchyCheck toCheck) {
      this.toCheck = toCheck;
    }

    AccessibilityHierarchyCheck getAccessibilityHierarchyCheck() {
      return toCheck;
    }

    @Override
    public List<AccessibilityViewCheckResult> runCheckOnViewHierarchy(
        View root, @Nullable Parameters parameters) {
      return new ViewChecker().runCheckOnView(toCheck, root, parameters);
    }

    @Override
    public boolean equals(@Nullable Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof DelegatedViewHierarchyCheck)) {
        return false;
      }
      DelegatedViewHierarchyCheck other = (DelegatedViewHierarchyCheck) o;
      return toCheck.equals(other.getAccessibilityHierarchyCheck());
    }

    @Override
    public int hashCode() {
      return toCheck.hashCode();
    }
  }

  private AccessibilityCheckPresetAndroid() {}
}
