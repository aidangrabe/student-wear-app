package com.aidangrabe.common;

/**
 * Created by aidan on 10/01/15.
 *
 */
public class SharedConstants {

    public static final class Wearable {
        public static final String MESSAGE_CREATE_TODO_ITEM = "/todo/create";
        public static final String MESSAGE_REQUEST_TODO_ITEMS = "/request-todo-items";
        public static final String MESSAGE_FIND_MY_PHONE = "/find-my-phone";

        // map
        public static final String REQUEST_MAP = "/map/request";
        public static final String DATA_PATH_MODULES = "/modules/get";

        // game controller
        public static final String MESSAGE_GAME_CONTROLLER = "/game-controller";
        public static final String MESSAGE_GAME_CONTROLLER_LEFT = "left";
        public static final String MESSAGE_GAME_CONTROLLER_RIGHT = "right";
        public static final String MESSAGE_GAME_CONTROLLER_UP = "up";
        public static final String MESSAGE_GAME_CONTROLLER_DOWN = "down";
    }

    public static final class Day {
        public static final int MONDAY = 0;
        public static final int TUESDAY = 1;
        public static final int WEDNESDAY = 2;
        public static final int THURSDAY = 3;
        public static final int FRIDAY = 4;
        public static final int SATURDAY = 5;
        public static final int SUNDAY = 6;
    }

}
