package CoffeeMachine;

import java.util.Scanner;

public class CoffeeMachine {

    private int water = 400;
    private int milk = 540;
    private int coffeeGrounds = 120;
    private int cups = 9;
    private int money = 550;

    private Scanner scanner = new Scanner(System.in);

    public void fill() {
        System.out.println("Write how many cups of coffee you will need: ");
        System.out.print(">");
        int input = Integer.valueOf(scanner.nextLine());
        this.water = input * 200;
        this.milk = input * 50;
        this.coffeeGrounds = input * 15;
        System.out.println(water + " ml of water \n" + milk +" ml of milk \n" + coffeeGrounds + " g of coffee beans");
    }

    public void howMany() {
        int amountOfCoffee = 0;
        System.out.println("Write how many ml of water the coffee machine has:");
        int waterIn = Integer.valueOf(scanner.nextLine());
        System.out.println("Write how many ml of milk the coffee machine has:");
        int milkIn = Integer.valueOf(scanner.nextLine());
        System.out.println("Write how many grams of coffee beans the coffee machine has:");
        int coffeeIn = Integer.valueOf(scanner.nextLine());
        System.out.println("Write how many cups of coffee you will need:");
        int amount = Integer.valueOf(scanner.nextLine());

        for(int i = 0; i <= amountOfCoffee; i ++) {
            if(waterIn >= 200 && milkIn >= 50 && coffeeIn >= 15) {
                waterIn -= 200;
                milkIn -= 50;
                coffeeIn -= 15;
                amountOfCoffee++;
            }
        }

        if(amount == amountOfCoffee) {
            System.out.println("Yes, I can make that amount of coffee");
        } else if(amount < amountOfCoffee) {
            System.out.println("Yes, I can make that amount of coffee (and even " + (amountOfCoffee - amount) + " more than that)");
        } else {
            System.out.println("No, I can make only " + amountOfCoffee + " cup(s) of coffee");
        }
    }

    public void printState() {
        System.out.println("This coffee machine has: \n" + this.water + " of water \n" + this.milk + " of milk \n" +
                this.coffeeGrounds + " of coffee beans \n" + this.cups + " of disposable cups \n" + this.money + " of money \n");
    }


    public void ui() {
        while(true) {
            System.out.println("Write action (buy, fill, take, remaining, exit): ");
            String input = scanner.nextLine();
            if(input.equals("fill")) {
                System.out.println();
                System.out.println("Write how many ml of water do you want to add: ");
                this.water += Integer.valueOf(scanner.nextLine());
                System.out.println("Write how many ml of milk do you want to add: ");
                this.milk += Integer.valueOf(scanner.nextLine());
                System.out.println("Write how many grams of coffee beans do you want to add: ");
                this.coffeeGrounds += Integer.valueOf(scanner.nextLine());
                System.out.println("Write how many disposable cups of coffee do you want to add: ");
                this.cups += Integer.valueOf(scanner.nextLine());
            }
            if(input.equals("buy")) {
                System.out.println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
                String type = scanner.nextLine();
                if(type.equals("1")) {
                    sellEspresso();
                } else if(type.equals("2")) {
                    sellLatte();
                } else if(type.equals("3")) {
                    sellCappuccino();
                } else if(type.equals("back")) {
                    continue;
                }
            }
            if(input.equals("take")) {
                System.out.println("\nI gave you $" + this.money);
                this.money = 0;
            }
            if(input.equals("exit")) {
                break;
            }
            if(input.equals("remaining")) {
                printState();
            }
        }
    }

    public Boolean checkResources(int water, int milk, int coffee, int cups) {
        if(this.water - water <= 0) {
            System.out.println("\nSorry, not enough water!");
            return false;
        }
        if(this.milk - milk <= 0) {
            System.out.println("\nSorry, not enough milk!");
            return false;
        }
        if(this.coffeeGrounds - coffee <= 0) {
            System.out.println("\nSorry, not enough coffee beans!");
            return false;
        }
        if(this.cups - cups <= 0) {
            System.out.println("\nSorry, not enough disposable cups!");
            return false;
        }
        System.out.println("I have enough resources, making you a coffee!");
        return true;
    }

    public void sellEspresso() {
        if(!checkResources(250, 0, 16, 1)) {
            return;
        }
        this.water -= 250;
        this.coffeeGrounds -= 16;
        this.cups -= 1;
        this.money += 4;
    }

    public void sellLatte() {
        if(!checkResources(350, 75, 20, 1)) {
            return;
        }
        this.water -= 350;
        this.milk -= 75;
        this.cups -= 1;
        this.coffeeGrounds -= 20;
        this.money += 7;
    }

    public void sellCappuccino() {
        if(!checkResources(200, 100, 12, 1)) {
            return;
        }
        this.water -= 200;
        this.milk -= 100;
        this.coffeeGrounds -= 12;
        this.cups -= 1;
        this.money += 6;
    }

    public static void main(String[] args) {
        CoffeeMachine machine = new CoffeeMachine();
        machine.ui();
    }
}
