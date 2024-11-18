package com.demo;

public class DemoApp {
    public String getMessage() {
        return "Hello from Demo App!";
    }

    public static void main(String[] args) {
        DemoApp app = new DemoApp();
        System.out.println(app.getMessage());
    }
}
