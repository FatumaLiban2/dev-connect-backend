//package org.devconnect.devconnectbackend.listener;
//
//import org.devconnect.devconnectbackend.model.User;
//import org.devconnect.devconnectbackend.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@Component
//public class WebSocketEventListener {
//
//    @Autowired
//    private UserService userService;
//
//    /**
//     * Handle WebSocket connection event
//     */
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        // Check if session attributes exist before accessing
//        if (headerAccessor.getSessionAttributes() != null) {
//            // Get user ID from session attributes (should be set during authentication)
//            String userId = (String) headerAccessor.getSessionAttributes().get("userId");
//
//            if (userId != null) {
//                try {
//                    // Update user status to online
//                    userService.updateUserStatus(Long.parseLong(userId), User.UserStatus.ONLINE);
//                    System.out.println("User connected: " + userId);
//                } catch (Exception e) {
//                    System.err.println("Error updating user status on connect: " + e.getMessage());
//                }
//            }
//        }
//    }
//
//    /**
//     * Handle WebSocket disconnection event
//     */
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        // Check if session attributes exist before accessing
//        if (headerAccessor.getSessionAttributes() != null) {
//            // Get user ID from session attributes
//            String userId = (String) headerAccessor.getSessionAttributes().get("userId");
//
//            if (userId != null) {
//                try {
//                    // Update user status to offline
//                    userService.updateUserStatus(Long.parseLong(userId), User.UserStatus.OFFLINE);
//                    System.out.println("User disconnected: " + userId);
//                } catch (Exception e) {
//                    System.err.println("Error updating user status on disconnect: " + e.getMessage());
//                }
//            }
//        }
//    }
//}
