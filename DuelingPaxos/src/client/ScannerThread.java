package client;

import java.util.Scanner;

public class ScannerThread implements Runnable {

    private Scanner scanner = new Scanner(System.in);
    private final int limit = 5;
    private int count = 0;
    @Override
    public void run() {
        System.out.println("Please wait to start...");
        //System.out.println("Enter the input");
        while(count<limit){
            int input = scanner.nextInt();
            Client.setValue(input);
            count++;
        }
    }
}
