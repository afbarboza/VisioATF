/*
 * Copyright (C) 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.android.apps.common.testing.accessibility.framework;

import android.view.View;
import com.google.android.apps.common.testing.accessibility.framework.replacements.Rect;
import com.google.android.apps.common.testing.accessibility.framework.replacements.TextUtils;
import com.google.android.apps.common.testing.accessibility.framework.uielement.ViewHierarchyElement;
import java.util.Locale;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An object that describes an {@link AccessibilityCheckResult}. This can be extended to provide
 * descriptions of the result and their contents in a form that is localized to the environment in
 * which checks are being run.
 */

public class AccessibilityCheckResultDescriptor {

  /**
   * Returns a String description of the given {@link AccessibilityCheckResult}.
   *
   * @param result the {@link AccessibilityCheckResult} to describe
   * @return a String description of the result
   */
  @SuppressWarnings("deprecation") // Needed to support AccessibilityViewCheckResult.
  public String describeResult(AccessibilityCheckResult result) {
    StringBuilder message = new StringBuilder();
    if (result instanceof AccessibilityViewCheckResult) {
      message.append(describeView(((AccessibilityViewCheckResult) result).getView()));
      message.append(": ");
    } else if (result instanceof AccessibilityHierarchyCheckResult) {
      message.append(describeElement(((AccessibilityHierarchyCheckResult) result).getElement()));
      message.append(": ");
    }
    message.append(result.getMessage(Locale.ENGLISH));
    return message.toString();
  }

  /**
   * Returns a String description of the given {@link View}. The default is to return the view's
   * resource entry name.
   *
   * @param view the {@link View} to describe
   * @return a String description of the given {@link View}
   */
  public String describeView(@Nullable View view) {
    StringBuilder message = new StringBuilder();
    if ((view != null
        && view.getId() != View.NO_ID
        && view.getResources() != null
        && !ViewAccessibilityUtils.isViewIdGenerated(view.getId()))) {
      message.append("View ");
      try {
        message.append(view.getResources().getResourceEntryName(view.getId()));
      } catch (Exception e) {
        /* In some integrations (seen in Robolectric), the resources may behave inconsistently */
        message.append("with no valid resource name");
      }
    } else {
      if (view != null) {
        message.append(handleUndefinedViewDescriptor(view));
      } else {
        message.append("with no valid resource name");
      }
    }
    return message.toString();
  }

  /**
   * Returns a String description of the given {@link ViewHierarchyElement}. The default is to
   * return the view's resource entry name. If the view has no valid resource entry name, then
   * returns the view's bounds.
   *
   * @param element the {@link ViewHierarchyElement} to describe
   * @return a String description of the given {@link ViewHierarchyElement}
   */
  public String describeElement(@Nullable ViewHierarchyElement element) {
    if (element == null) {
      return "<null>";
    }
    StringBuilder message = new StringBuilder();
    message.append("View ");
    if (!TextUtils.isEmpty(element.getResourceName())) {
      message.append(element.getResourceName());
    } else {
      Rect bounds = element.getBoundsInScreen();
      if (!bounds.isEmpty()) {
        message.append("with bounds: ");
        message.append(bounds.toShortString());
      } else {
        message.append("with no valid resource name or bounds");
      }
    }
    return message.toString();
  }

  /**
   *  When the resource descriptor is null, the default fallback is to
   *  return the descriptor as "View with no valid resource name" even when
   *  the View is not null. To report accessibility violations , this is
   *  misleading since may be confused with other identified violations
   *  that also do not have an appropriate descriptor.
   *
   *  As fallback, we are using the coordinates in (X0, Y0)(X1, Y1) as
   *  a descriptor, inspired by what is currently done by Accessibility Scanner.
   *  X0 represents the x-coordinate of top left
   *  Y0 represents the y-coordinate of top left
   *  X1 represents the x-coordinate of bottom right
   *  Y1 represents the y-coordinate of bottom right
   *
   * @param view the {@link View} to describe
   * @return a String description of format (X0, Y0)(X1, Y1) of the given {@link View}
   * using its position on screen
   */
  private String handleUndefinedViewDescriptor(View view) {
    int[] viewCoordinates = new int[2];
    view.getLocationOnScreen(viewCoordinates);

    int viewCoordinatesX0 = viewCoordinates[0];
    int viewCoordinatesY0 = viewCoordinates[1];

    int viewCoordinatesX1 = viewCoordinatesX0 + view.getWidth();
    int viewCoordinatesY1 = viewCoordinatesY0 + view.getHeight();

    StringBuilder pseudoIdentifier = new StringBuilder();

    pseudoIdentifier.append("[");
    pseudoIdentifier.append(viewCoordinatesX0);
    pseudoIdentifier.append(", ");
    pseudoIdentifier.append(viewCoordinatesY0);
    pseudoIdentifier.append("][");
    pseudoIdentifier.append(viewCoordinatesX1);
    pseudoIdentifier.append(", ");
    pseudoIdentifier.append(viewCoordinatesY1);
    pseudoIdentifier.append("]");

    return pseudoIdentifier.toString();
  }
}