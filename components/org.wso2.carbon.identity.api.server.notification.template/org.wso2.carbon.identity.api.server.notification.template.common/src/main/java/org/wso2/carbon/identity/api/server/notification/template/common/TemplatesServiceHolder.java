package org.wso2.carbon.identity.api.server.notification.template.common;


import org.wso2.carbon.identity.governance.service.notification.NotificationTemplateManager;

/**
 * This class is used to hold the TemplateManager service.
 */
public class TemplatesServiceHolder {

    private static NotificationTemplateManager notificationTemplateManager;

    /**
     * Get TemplateManager osgi service.
     * @return TemplateManager
     */
    public static NotificationTemplateManager getNotificationTemplateManager() {
        return notificationTemplateManager;
    }

    /**
     * Set TemplateManager osgi service.
     * @param notificationTemplateManager TemplateManager
     */
    public static void setNotificationTemplateManager(NotificationTemplateManager notificationTemplateManager) {
        TemplatesServiceHolder.notificationTemplateManager = notificationTemplateManager;
    }
}
