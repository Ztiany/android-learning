package com.smartdengg.plugin.internal

class ColoredLogger {

    private static final String ANSI_RESET = "\033[0m"
    private static final String ANSI_RED = "\033[1;31m"
    private static final String ANSI_GREEN = "\033[1;32m"
    private static final String ANSI_YELLOW = "\033[1;33m"
    private static final String ANSI_BLUE = "\033[1;34m"

    static void logRed(String text) {
        println "${ANSI_RED}${text}${ANSI_RESET}"
    }

    static void logGreen(String text) {
        println "${ANSI_GREEN}${text}${ANSI_RESET}"
    }

    static void logYellow(String text) {
        println "${ANSI_YELLOW}${text}${ANSI_RESET}"
    }

    static void logBlue(String text) {
        println "${ANSI_BLUE}${text}${ANSI_RESET}"
    }

    static void log(String text) {
        println text
    }

}