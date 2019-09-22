package androidx.sframe.ui.controller;

import androidx.sframe.ui.controller.impl.AppToolbarMethod;
import androidx.sframe.ui.controller.impl.UIToolbarMethod;

/**
 * Author create by ok on 2019-06-13
 * Email : ok@163.com.
 */
public interface UIToolbarController {

	UIViewController getViewController();

	UILayoutController getLayoutController();

	AppToolbarMethod getToolbarMethod();

	<Method extends UIToolbarMethod> Method get(Class<Method> method);
}
