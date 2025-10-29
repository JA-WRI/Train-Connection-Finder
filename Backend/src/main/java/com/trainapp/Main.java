package com.trainapp;

import com.trainapp.model.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Connection> foundConnections = new ArrayList<>();
        console mainConsole = new console();
        mainConsole.startProgram();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("-------------------Welcome Connection Finder-------------------");
            System.out.println("1. Search Connections");
            System.out.println("2. Create a Trip");
            System.out.println("3. View Trip");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    List<Connection> connections = mainConsole.searchConnections();;
                    //mainConsole.displayConnections(connections);
                    break;
                case 2:
                    System.out.println("Creating the trip");
                    break;
                case 3:
                    System.out.println("View trip");
                case 4:
                    System.out.println("Thank you for using connection finder, have a nice day");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println(); // Adds a blank line for readability
        } while (choice != 4);

        scanner.close();


    }


}