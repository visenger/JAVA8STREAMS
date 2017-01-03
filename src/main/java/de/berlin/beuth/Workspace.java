package de.berlin.beuth;

import java.util.Arrays;
import java.util.List;

public class Workspace {

    public static void main(String[] args) {


        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        //todo: 1. Find all transactions in the year 2011 and sort them by value (small to high).

        //todo: 2. What are all the unique cities where the traders work?

        //todo: 3.Find all traders from Cambridge and sort them by name.

        //todo: 4. Return a string of all traders’ names sorted alphabetically.

        //todo: 5.Are any traders based in Milan?

        //todo: 6.Print all transactions’ values from the traders living in Cambridge.

        //todo: 7.What’s the highest value of all the transactions?

        //todo: 8.Find the transaction with the smallest value.

        // Well done! :)
    }
}
